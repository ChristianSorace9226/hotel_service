package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;

public interface CameraService {
    CameraDTO aggiungiCamera(CreaCameraRequest creaCameraRequest);
    Void eliminaCamera(EliminaCameraRequest eliminaCameraRequest);
}
