package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.exception.NotFoundException;
import it.nesea.albergo.hotel_service.mapper.CameraMapper;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.repository.CameraRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
        if (camera == null) {
            camera = cameraMapper.toCameraEntityFromCreaCameraRequest(request);
            camera = cameraRepository.save(camera);
            log.info("camera salvata correttamente sul db");
            return cameraMapper.toCameraDTOFromCameraEntity(camera);
        } else {
            log.warn("stai provando a creare una camera dal numeroCamera già esistente");
            throw new NotFoundException("Numero camera già presente nel db");
        }
    }

    @Override
    @Transactional
    public Void eliminaCamera(EliminaCameraRequest request) {
        Camera camera = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
        if (camera != null) {
            if (request.getRimozioneLogica()) {
                camera.setDataRimozione(new Date());
                camera.setMotivazioneRimozione(request.getMotivazione());
                cameraRepository.save(camera);
                log.info("Camera con numero {} rimossa logicamente. Motivazione: {}", request.getNumeroCamera(), request.getMotivazione());
            } else {
                cameraRepository.delete(camera);
                log.info("Camera con numero {} rimossa fisicamente.", request.getNumeroCamera());
            }
        } else {
            log.warn("Camera con numero {} non trovata per la rimozione", request.getNumeroCamera());
            throw new NotFoundException("Camera non trovata");
        }
        return null;
    }

    @Override
    public OccupazioneDTO calcolaOccupazioneHotel() {
        List<Camera> camere = cameraRepository.findAll();

        int totaleCamere = camere.size();
        int camereOccupate = 0;
        int postiLiberoTotali = 0;
        int postiOccupatiTotali = 0;

        for (Camera camera : camere) {
            // Controllo se la camera è occupata
            if ("occupato".equalsIgnoreCase(camera.getStato())) {
                camereOccupate++;
                // Considero i posti occupati dalla camera
                postiOccupatiTotali += camera.getCapacita();
            } else if ("disponibile".equalsIgnoreCase(camera.getStato())) {
                // Considero i posti liberi dalla camera disponibile
                postiLiberoTotali += camera.getCapacita();
            }
        }

        double percentualeOccupazione = calcolaPercentualeOccupazione(totaleCamere, camereOccupate);

        OccupazioneDTO occupazioneDTO = new OccupazioneDTO();
        occupazioneDTO.setTotaleCamere(totaleCamere);
        occupazioneDTO.setCamereOccupate(camereOccupate);
        occupazioneDTO.setPercentualeOccupazione(percentualeOccupazione);
        occupazioneDTO.setPostiLiberoTotali(postiLiberoTotali);
        occupazioneDTO.setPostiOccupatiTotali(postiOccupatiTotali);

        log.info("Calcolato stato di occupazione per l'hotel: {}", occupazioneDTO);

        return occupazioneDTO;
    }

    private double calcolaPercentualeOccupazione(int totaleCamere, int camereOccupate) {
        if (totaleCamere == 0) {
            return 0.0;
        }
        return camereOccupate * 100.0 / totaleCamere;
    }
}
