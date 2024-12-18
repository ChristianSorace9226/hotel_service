package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.exception.BadRequestException;
import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.PrezzarioRequest;
import it.nesea.albergo.hotel_service.dto.response.CameraDTO;
import it.nesea.albergo.hotel_service.dto.response.DisponibilitaDTO;
import it.nesea.albergo.hotel_service.dto.response.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.response.PrezzoCameraDTO;
import it.nesea.albergo.hotel_service.mapper.CameraMapper;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.FasciaEtaEntity;
import it.nesea.albergo.hotel_service.model.PrezzoCameraEntity;
import it.nesea.albergo.hotel_service.model.repository.CameraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@Slf4j
public class CameraServiceImpl implements CameraService {

    private final EntityManager entityManager;
    private final CameraRepository cameraRepository;
    private final CameraMapper cameraMapper;
    private final UtilService utilService;


    // todo-generico : implementare controlli di integrità (ex.: numero massimo di persone che possono accedere ad una stanza = capacità stanza)

    @Override
    @Transactional
    public CameraDTO aggiungiCamera(CreaCameraRequest request) throws InstanceAlreadyExistsException {
        log.info("Richiesta ricevuta per aggiungere una camera: [{}]", request);
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
        if (camera != null) {
            log.warn("Tentativo di creare una camera con il numeroCamera già esistente: {}", request.getNumeroCamera());
            throw new InstanceAlreadyExistsException("Numero camera già presente nel db");
        }
        if (request.getCapacita() <= 0) {
            log.warn("Tentativo di creare una camera con capacità pari a 0: {}", request.getCapacita());
            throw new BadRequestException("La capacità della camera deve essere maggiore di 0");
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

        camera.setTipo(utilService.getTipoCamera(request.getIdTipo()));
        camera.setNumeroAlloggiati(0);
        cameraRepository.save(camera);
        log.info("Oggetto camera salvato sul database: [{}]", camera);

        return cameraMapper.toCameraDTOFromCameraEntity(camera);
    }


    @Override
    @Transactional
    public Void eliminaCamera(EliminaCameraRequest request) {
        log.info("Richiesta ricevuta per la rimozione della camera: [{}]", request);
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
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
        log.info("Richiesta ricevuta per il calcolo dell'occupazione del hotel: {}", camere);
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
            occupazioneCamera.put(camera.getNumeroCamera(), calcolaPercentuale(camera.getCapacita(), camera.getNumeroAlloggiati()).setScale(2, RoundingMode.HALF_UP));
            listaOccupazioneCamere.add(occupazioneCamera);
        }
//        double percentualeOccupazione = calcolaPercentualeOccupazione(postiTotali, postiOccupatiTotali);
        BigDecimal percentualeOccupazione = calcolaPercentuale(totaleCamere, camereOccupate).setScale(2, RoundingMode.HALF_UP);

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
        for (Camera camera : camere) {
            camereDto.add(cameraMapper.toCameraDTOFromCameraEntity(camera));
        }
        log.info("Ottenute tutte le camere: {}", camereDto);
        return camereDto;
    }


    @Override
    public PrezzoCameraDTO getPrezzario(PrezzarioRequest request) {
        log.info("Richiesta ricevuta per ottenere il prezzario");

        // Recupera la camera dal DB
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
        if (camera == null || camera.getDataRimozione() != null) {
            log.warn("Camera con nome {} non trovata per il prezzario", request.getNumeroCamera());
            throw new NotFoundException("Camera non trovata");
        }

        // Calcola il numero di occupanti
        Integer numeroOccupanti = request.getEta().size();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PrezzoCameraEntity> criteriaQueryPrezzo = criteriaBuilder.createQuery(PrezzoCameraEntity.class);
        Root<PrezzoCameraEntity> rootPrezzo = criteriaQueryPrezzo.from(PrezzoCameraEntity.class);
        List<Predicate> predicatesPrezzo = new ArrayList<>();
        predicatesPrezzo.add(criteriaBuilder.equal(rootPrezzo.get("tipo").get("id"), camera.getTipo().getId()));
        predicatesPrezzo.add(criteriaBuilder.equal(rootPrezzo.get("numeroOccupanti"), numeroOccupanti));
        criteriaQueryPrezzo.select(rootPrezzo).where(criteriaBuilder.and(predicatesPrezzo.toArray(new Predicate[0])));

        PrezzoCameraEntity prezzoCameraEntity;
        try {
            prezzoCameraEntity = entityManager.createQuery(criteriaQueryPrezzo).getSingleResult();
        } catch (NoResultException e) {
            log.warn("Prezzario non trovato per numero occupanti {}", numeroOccupanti);
            throw new NotFoundException("Prezzario non trovato per il numero di persone fornito");
        }

        PrezzoCameraDTO prezzoCameraDto = cameraMapper.toPrezzoCameraDTOFromPrezzoCameraEntity(prezzoCameraEntity);

        // Prezzo base per persona (senza sconto)
        BigDecimal prezzoTotale = prezzoCameraDto.getPrezzoTotale();
        BigDecimal prezzoAPersonaBase = prezzoTotale.divide(BigDecimal.valueOf(numeroOccupanti), 2, RoundingMode.HALF_UP);

        List<BigDecimal> prezziAPersonaList = new ArrayList<>();
        BigDecimal prezzoTotaleConSconto = BigDecimal.ZERO;

        // Itera sulle età per determinare gli sconti applicabili
        for (Integer fasciaEtaRequest : request.getEta()) {
            // Recupera la fascia di età dal database per determinare lo sconto
            CriteriaQuery<FasciaEtaEntity> criteriaQueryEta = criteriaBuilder.createQuery(FasciaEtaEntity.class);
            Root<FasciaEtaEntity> rootEta = criteriaQueryEta.from(FasciaEtaEntity.class);
            List<Predicate> predicatesEta = new ArrayList<>();
            predicatesEta.add(criteriaBuilder.between(
                    criteriaBuilder.literal(fasciaEtaRequest), // Età richiesta
                    rootEta.get("etaMin"), // Limite inferiore
                    rootEta.get("etaMax")  // Limite superiore
            ));
            criteriaQueryEta.select(rootEta).where(criteriaBuilder.and(predicatesEta.toArray(new Predicate[0])));

            FasciaEtaEntity fasciaEta;
            try {
                fasciaEta = entityManager.createQuery(criteriaQueryEta).getSingleResult();
            } catch (NoResultException e) {
                log.warn("Fascia d'età {} non trovata per il prezzario", fasciaEtaRequest);
                throw new NotFoundException("Fascia d'età non trovata per il prezzario: " + e.getMessage());
            }

            log.info("Fascia d'età trovata: [{}]", fasciaEta);

            // Calcola lo sconto
            BigDecimal sconto = fasciaEta.getPercentualeSconto();
            log.info("Percentuale sconto: {}", sconto);

            // Se la persona ha diritto allo sconto, applica lo sconto
            BigDecimal prezzoPerPersona;
            if (sconto.compareTo(BigDecimal.ZERO) > 0) {
                prezzoPerPersona = prezzoAPersonaBase.subtract(prezzoAPersonaBase.multiply(sconto)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            } else {
                prezzoPerPersona = prezzoAPersonaBase;
            }

            log.info("Prezzo per persona (con o senza sconto): {}", prezzoPerPersona);

            // Aggiungi il prezzo per questa persona al totale con sconto
            prezzoTotaleConSconto = prezzoTotaleConSconto.add(prezzoPerPersona);
            prezziAPersonaList.add(prezzoPerPersona);
        }

        // Imposta i prezzi per persona con sconto
        prezzoCameraDto.setPrezziAPersona(prezziAPersonaList);

        // Imposta il prezzo totale con sconto
        prezzoCameraDto.setPrezzoTotale(prezzoTotaleConSconto.setScale(2, RoundingMode.HALF_UP));

        log.info("Ottenuto il prezzario: [{}]", prezzoCameraDto);
        return prezzoCameraDto;
    }


    private BigDecimal calcolaPercentuale(int totale, int parte) {
        if (totale == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(parte * 100.0 / totale);
    }

}