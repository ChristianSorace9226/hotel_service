package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.dto.response.DisponibilitaDto;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

public interface CameraService {
    CameraDTO aggiungiCamera(CreaCameraRequest creaCameraRequest) throws InstanceAlreadyExistsException;
    Void eliminaCamera(EliminaCameraRequest eliminaCameraRequest);
    OccupazioneDTO calcolaOccupazioneHotel();

    DisponibilitaDto getDisponibilita();

    List<CameraDTO> getAllCamere();
}
