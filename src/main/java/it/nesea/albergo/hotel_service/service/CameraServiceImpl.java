package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
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
}
