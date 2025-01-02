package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.dto.PrezzoCameraDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.PrezzarioRequest;
import it.nesea.albergo.hotel_service.dto.response.CameraDTO;
import it.nesea.albergo.hotel_service.dto.response.DisponibilitaDTO;
import it.nesea.albergo.hotel_service.dto.response.OccupazioneDTO;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public interface CameraService {
    CameraDTO aggiungiCamera(CreaCameraRequest creaCameraRequest) throws InstanceAlreadyExistsException;

    Void eliminaCamera(EliminaCameraRequest eliminaCameraRequest);

    OccupazioneDTO calcolaOccupazioneHotel();

    DisponibilitaDTO getDisponibilita();

    List<CameraDTO> getAllCamere();

    PrezzoCameraDTO getPrezzario(PrezzarioRequest request);
}
