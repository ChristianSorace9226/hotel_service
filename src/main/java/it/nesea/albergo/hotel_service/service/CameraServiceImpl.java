package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.dto.response.DisponibilitaDto;
import it.nesea.albergo.hotel_service.exception.BadRequestException;
import it.nesea.albergo.hotel_service.exception.NotFoundException;
import it.nesea.albergo.hotel_service.mapper.CameraMapper;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.repository.CameraRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
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
    private final CameraRepository cameraRepository;
    private final CameraMapper cameraMapper;
    private final UtilService utilService;


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
        if (utilService.getStatoCameraEntity(request.getIdStato()) == null) {
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
//        List<Camera> camere = cameraRepository.findAll();
//
//        int totaleCamere = camere.size();
//        int camereOccupate = 0;
//        int postiLiberiTotali = 0;
//        int postiOccupatiTotali = 0;
//        int postiTotali = 0;
//
//        for (Camera camera : camere) {
//            postiTotali += camera.getCapacita();
//            // Controllo se la camera è occupata
//            if ("occupato".equalsIgnoreCase(camera.getStato())) {
//                camereOccupate++;
//                // Considero i posti occupati dalla camera
//                postiOccupatiTotali += camera.getCapacita();
//            } else if ("disponibile".equalsIgnoreCase(camera.getStato())) {
//                // Considero i posti liberi dalla camera disponibile
//                postiLiberiTotali += camera.getCapacita();
//            }
//        }
//
////        double percentualeOccupazione = calcolaPercentualeOccupazione(postiTotali, postiOccupatiTotali);
//        double percentualeOccupazione = calcolaPercentualeOccupazione(totaleCamere, camereOccupate);
//
//        OccupazioneDTO occupazioneDTO = new OccupazioneDTO();
//        occupazioneDTO.setTotaleCamere(totaleCamere);
//        occupazioneDTO.setCamereOccupate(camereOccupate);
//        occupazioneDTO.setPercentualeOccupazione(percentualeOccupazione);
//        occupazioneDTO.setPostiLiberiTotali(postiLiberiTotali);
//        occupazioneDTO.setPostiOccupatiTotali(postiOccupatiTotali);
//
//        log.info("Calcolato stato di occupazione per l'hotel: [{}]", occupazioneDTO);
//
//        return occupazioneDTO;
        return null;
    }

    public DisponibilitaDto getDisponibilita() {
        Integer disponibilitaTotale = 0;
        Map<String, Map<Boolean, Integer>> cameraPostiDisponibili = new HashMap<>();
        Integer disponibilitaReale = 0;

        List<Camera> camere = cameraRepository.findAll();
        for (Camera camera : camere) {
            disponibilitaTotale += camera.getCapacita();
            Map<Boolean, Integer> disponibilita = new HashMap<>();
            disponibilita.put(true, camera.getCapacita() - camera.getNumeroAlloggiati());
            disponibilitaReale += camera.getCapacita() - camera.getNumeroAlloggiati();
            if (camera.getIdStato() == 2) {
                disponibilita.put(false, camera.getNumeroAlloggiati());
            }
            cameraPostiDisponibili.put(camera.getNumeroCamera(), disponibilita);
        }
        DisponibilitaDto disponibilitaDto = new DisponibilitaDto();
        disponibilitaDto.setDisponibilitaTotale(disponibilitaTotale);
        disponibilitaDto.setCameraPostiDisponibili(cameraPostiDisponibili);
        disponibilitaDto.setDisponibilitaReale(disponibilitaReale);
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
        return camereDto;
    }

    private double calcolaPercentualeOccupazione(int totaleCamere, int camereOccupate) {
        if (totaleCamere == 0) {
            return 0.0;
        }
        return camereOccupate * 100.0 / totaleCamere;
    }
}
