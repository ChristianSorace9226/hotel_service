package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.dto.PrezzoCameraDTO;
import it.nesea.albergo.common_lib.exception.BadRequestException;
import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.hotel_service.controller.feign.PrenotazioneExternalController;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.PrezzarioRequest;
import it.nesea.albergo.hotel_service.dto.response.CameraDTO;
import it.nesea.albergo.hotel_service.dto.response.DisponibilitaDTO;
import it.nesea.albergo.hotel_service.dto.response.OccupazioneDTO;
import it.nesea.albergo.hotel_service.mapper.CameraMapper;
import it.nesea.albergo.hotel_service.mapper.UtilMapper;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.FasciaEtaEntity;
import it.nesea.albergo.hotel_service.model.PrezzoCameraEntity;
import it.nesea.albergo.hotel_service.model.repository.CameraRepository;
import it.nesea.albergo.hotel_service.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
@Slf4j
public class CameraServiceImpl implements CameraService {

    private final EntityManager entityManager;
    private final CameraRepository cameraRepository;
    private final CameraMapper cameraMapper;
    private final UtilMapper utilMapper;
    private final UtilService utilService;
    private final Util util;
    private final PrenotazioneExternalController prenotazioneExternalController;

    public CameraServiceImpl(CameraMapper cameraMapper, CameraRepository cameraRepository,
                             EntityManager entityManager, Util util, UtilMapper utilMapper, @Lazy UtilService utilService, PrenotazioneExternalController prenotazioneExternalController) {
        this.cameraMapper = cameraMapper;
        this.cameraRepository = cameraRepository;
        this.entityManager = entityManager;
        this.util = util;
        this.utilMapper = utilMapper;
        this.utilService = utilService;
        this.prenotazioneExternalController = prenotazioneExternalController;
    }

    @Override
    @Transactional
    public CameraDTO aggiungiCamera(CreaCameraRequest request) throws InstanceAlreadyExistsException {
        log.info("Richiesta ricevuta per aggiungere una camera: [{}]", request);
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera().toLowerCase().trim());
        if (camera != null) {
            log.warn("Tentativo di creare una camera con il numeroCamera già esistente: {}", request.getNumeroCamera());
            throw new InstanceAlreadyExistsException("Numero camera già presente nel db");
        }
        if (request.getDataInizioDisponibilita().isBefore(LocalDate.now())) {
            log.warn("Tentativo di creare una camera con una data di inizio disponibilità antecedente alla data odierna: {}", request.getDataInizioDisponibilita());
            throw new BadRequestException("La data di inizio disponibilità non può essere antecedente alla data odierna");
        }
        if (utilService.getStatoCamera(request.getIdStato()) == null) {
            log.warn("Stato camera non trovato per la camera con numero {}: {}", request.getNumeroCamera(), request.getIdStato());
            throw new NotFoundException("Stato camera non valido");
        }
        if (request.getDataInizioDisponibilita().isEqual(LocalDate.now())) {
            LocalTime oraAttuale = LocalTime.now();
            LocalTime limiteOrario = LocalTime.of(14, 30);

            if (oraAttuale.isAfter(limiteOrario)) {
                log.warn("Tentativo di creare una camera con una data di inizio disponibilità per oggi oltre le 14:30: {}", request.getDataInizioDisponibilita());
                throw new BadRequestException("La data di inizio disponibilità per oggi non può essere oltre le 14:30");
            }
        }
        camera = cameraMapper.toCameraEntityFromCreaCameraRequest(request);
        camera.setNumeroCamera(request.getNumeroCamera().toLowerCase().trim());
        camera.setTipo(utilService.getTipoCamera(request.getIdTipo()));
        camera.setCapacita(camera.getTipo().getId());
        camera.setPrezzoPerNotte(utilService.getPrezzoCamera(camera, camera.getCapacita()).getPrezzoTotale());
        camera.setNumeroAlloggiati(0);
        cameraRepository.save(camera);
        log.info("Oggetto camera salvato sul database: [{}]", camera);

        return cameraMapper.toCameraDTOFromCameraEntity(camera);
    }


    @Override
    @Transactional
    public Void eliminaCamera(EliminaCameraRequest request) {
        log.info("Richiesta ricevuta per la rimozione della camera: [{}]", request);
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera().toLowerCase().trim());
        if (camera == null) {
            log.warn("Camera con numero {} non trovata per la rimozione", request.getNumeroCamera());
            throw new NotFoundException("Camera non trovata");
        }
        if (request.getRimozioneLogica()) {
            if (request.getMotivazione() == null || request.getMotivazione().toLowerCase().trim().isEmpty()) {
                log.warn("Motivazione mancante per la rimozione logica della camera {}", request.getNumeroCamera());
                throw new BadRequestException("La motivazione per la rimozione logica è obbligatoria");
            }
            camera.setDataRimozione(LocalDate.now());
            camera.setMotivazioneRimozione(request.getMotivazione().toLowerCase().trim());

            cameraRepository.save(camera);
            log.info("Camera con numero {} rimossa logicamente. Motivazione: {}", request.getNumeroCamera(), request.getMotivazione());
        } else {
            cameraRepository.delete(camera);
            log.info("Camera con numero {} rimossa fisicamente.", request.getNumeroCamera());
        }
        return null;
    }


    @Override
    public OccupazioneDTO calcolaOccupazioneHotel() {
        List<Camera> camere = cameraRepository.findAll();
        log.info("Richiesta ricevuta per il calcolo dell'occupazione dell'hotel: [{}]", camere);
        List<Map<String, BigDecimal>> listaOccupazioneCamere = new ArrayList<>();
        int totaleCamere = camere.size();
        int camereOccupate = 0;

        for (Camera camera : camere) {
            if (camera.getDataRimozione() != null) {
                continue;
            }
            Map<String, BigDecimal> occupazioneCamera = new HashMap<>();
            if (camera.getStato().getId() == 2) {
                camereOccupate++;
            }
            occupazioneCamera.put(camera.getNumeroCamera(), util.calcolaPercentuale(camera.getCapacita(), camera.getNumeroAlloggiati()).setScale(2, RoundingMode.HALF_UP));
            listaOccupazioneCamere.add(occupazioneCamera);
        }
        BigDecimal percentualeOccupazione = util.calcolaPercentuale(totaleCamere, camereOccupate).setScale(2, RoundingMode.HALF_UP);

        OccupazioneDTO occupazioneDTO = new OccupazioneDTO();
        occupazioneDTO.setPercentualeOccupazioneTotale(percentualeOccupazione);
        occupazioneDTO.setNumeroCamere(totaleCamere);
        occupazioneDTO.setPercentualeOccupazioneCamera(listaOccupazioneCamere);
        log.info("Calcolato percentuale di occupazione per l'hotel: [{}]", occupazioneDTO);
        return occupazioneDTO;
    }

    @Override
    public DisponibilitaDTO getDisponibilita() {
        log.info("Richiesta ricevuta per ottenere la disponibilità delle camere");
        Integer disponibilitaPotenziale = 0;
        Map<String, Map<Boolean, Integer>> cameraPostiDisponibili = new HashMap<>();
        Integer disponibilitaReale = 0;

        List<Camera> camere = cameraRepository.findAll();
        for (Camera camera : camere) {
            disponibilitaPotenziale += camera.getCapacita();
            Map<Boolean, Integer> disponibilita = new HashMap<>();
            if (camera.getDataRimozione() != null) {
                disponibilita.put(false, camera.getCapacita());
                cameraPostiDisponibili.put(camera.getNumeroCamera(), disponibilita);
                continue;
            }
            disponibilita.put(true, camera.getCapacita() - camera.getNumeroAlloggiati());
            disponibilitaReale += camera.getCapacita() - camera.getNumeroAlloggiati();
            if (camera.getStato().getId() == 2) {
                disponibilita.put(false, camera.getNumeroAlloggiati());
            }
            cameraPostiDisponibili.put(camera.getNumeroCamera(), disponibilita);
        }
        DisponibilitaDTO disponibilitaDto = new DisponibilitaDTO();
        disponibilitaDto.setDisponibilitaPotenziale(disponibilitaPotenziale);
        disponibilitaDto.setCameraPostiDisponibili(cameraPostiDisponibili);
        disponibilitaDto.setDisponibilitaReale(disponibilitaReale);
        log.info("Calcolato disponibilità delle camere: [{}]", disponibilitaDto);
        return disponibilitaDto;
    }

    @Override
    public List<CameraDTO> getAllCamere() {
        log.info("Richiesta ricevuta per ottenere tutte le camere");
        List<Camera> camere = cameraRepository.findAll();
        List<CameraDTO> camereDto = new ArrayList<>();
        for (Camera camera : camere ){
            camereDto.add(cameraMapper.toCameraDTOFromCameraEntity(camera));
        }
        return camereDto;
    }

    @Override
    public PrezzoCameraDTO getPrezzario(PrezzarioRequest request) {
        log.info("Richiesta ricevuta per ottenere il prezzario per camera");
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera().toLowerCase().trim());
        if (camera == null || camera.getDataRimozione() != null) {
            log.warn("Camera con numero {} non trovata per il prezzario", request.getNumeroCamera());
            throw new NotFoundException("Camera non trovata");
        }
        Integer numeroOccupanti = request.getEta().size();
        PrezzoCameraEntity prezzoCameraEntity = utilService.getPrezzoCamera(camera, numeroOccupanti);
        PrezzoCameraDTO prezzoCameraDto = utilMapper.toPrezzoCameraDTOFromPrezzoCameraEntity(prezzoCameraEntity);

        // Prezzo base per persona (senza sconto)
        BigDecimal prezzoTotale = prezzoCameraDto.getPrezzoTotale();
        BigDecimal prezzoAPersonaBase = prezzoTotale.divide(BigDecimal.valueOf(numeroOccupanti), 2, RoundingMode.HALF_UP);

        List<BigDecimal> prezziAPersonaList = new ArrayList<>();
        BigDecimal prezzoTotaleConSconto = BigDecimal.ZERO;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FasciaEtaEntity> criteriaQueryEta = criteriaBuilder.createQuery(FasciaEtaEntity.class);
        Root<FasciaEtaEntity> rootEta = criteriaQueryEta.from(FasciaEtaEntity.class);
        for (Integer fasciaEtaRequest : request.getEta()) {
            List<Predicate> predicatesEta = new ArrayList<>();
            predicatesEta.add(criteriaBuilder.between(
                    criteriaBuilder.literal(fasciaEtaRequest),
                    rootEta.get("etaMin"),
                    rootEta.get("etaMax")
            ));
            criteriaQueryEta.select(rootEta).where(criteriaBuilder.and(predicatesEta.toArray(new Predicate[0])));

            FasciaEtaEntity fasciaEta;
            try {
                fasciaEta = entityManager.createQuery(criteriaQueryEta).getSingleResult();
            } catch (NoResultException e) {
                log.warn("Fascia d'età {} non trovata per il prezzario", fasciaEtaRequest);
                throw new NotFoundException("Fascia d'età non trovata per il prezzario");
            }
            log.info("Fascia d'età trovata: [{}]", fasciaEta);

            // Calcolo lo sconto
            BigDecimal sconto = fasciaEta.getPercentualeSconto();
            log.info("Percentuale sconto: {}", sconto);

            // Applico lo sconto
            BigDecimal prezzoPerPersona = prezzoAPersonaBase.subtract(prezzoAPersonaBase.multiply(sconto)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

            log.info("Prezzo per persona (con o senza sconto): {}", prezzoPerPersona);

            // Aggiungo il prezzo per questa persona al totale con sconto
            prezzoTotaleConSconto = prezzoTotaleConSconto.add(prezzoPerPersona);
            prezziAPersonaList.add(prezzoPerPersona);
        }

        // Imposto i prezzi per persona con sconto
        prezzoCameraDto.setPrezziAPersona(prezziAPersonaList);

        prezzoCameraDto.setNumeroCamera(request.getNumeroCamera());

        // Imposto il prezzo totale con sconto
        prezzoCameraDto.setPrezzoTotale(prezzoTotaleConSconto.setScale(2, RoundingMode.HALF_UP));

        log.info("Ottenuto il prezzario: [{}]", prezzoCameraDto);
        return prezzoCameraDto;

    }
}