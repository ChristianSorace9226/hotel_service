package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;

import javax.management.InstanceAlreadyExistsException;

public interface CameraService {
    CameraDTO aggiungiCamera(CreaCameraRequest creaCameraRequest) throws InstanceAlreadyExistsException;
    Void eliminaCamera(EliminaCameraRequest eliminaCameraRequest);
    OccupazioneDTO calcolaOccupazioneHotel();
}
