package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class CameraServiceImpl implements CameraService {
    private final CameraRepository cameraRepository;
    private final CameraMapper cameraMapper;


    @Override
    @Transactional
    public CameraDTO aggiungiCamera(CreaCameraRequest request) {
        log.info("Richiesta ricevuta per aggiungere una camera: [{}]", request);
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
        if (camera != null) {
            log.warn("Tentativo di creare una camera con il numeroCamera già esistente: {}", request.getNumeroCamera());
            throw new NotFoundException("Numero camera già presente nel db");
        }
        if (request.getCapacita() <= 0) {
            log.warn("Tentativo di creare una camera con capacità pari a 0: {}", request.getCapacita());
            throw new BadRequestException("La capacità della camera deve essere maggiore di 0");
        }
        if (request.getDataInizioDisponibilita().isBefore(LocalDate.now())) {
            log.warn("Tentativo di creare una camera con una data di inizio disponibilità antecedente alla data odierna: {}", request.getDataInizioDisponibilita());
            throw new BadRequestException("La data di inizio disponibilità non può essere antecedente alla data odierna");
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

    private double calcolaPercentualeOccupazione(int totaleCamere, int camereOccupate) {
        if (totaleCamere == 0) {
            return 0.0;
        }
        return camereOccupate * 100.0 / totaleCamere;
    }
}
