package it.nesea.albergo.hotel_service.controller;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.OccupazioneDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.request.EliminaCameraRequest;
import it.nesea.albergo.hotel_service.dto.response.CustomResponse;
import it.nesea.albergo.hotel_service.service.CameraService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;

@RestController
@RequestMapping(path = "/hotel")
@AllArgsConstructor
@Validated
public class CameraController {
    private final CameraService cameraService;

    @PostMapping(path = "/aggiungi-camera")
    public ResponseEntity<CustomResponse<CameraDTO>> aggiungiCamera(@Valid @RequestBody CreaCameraRequest request) throws InstanceAlreadyExistsException {
        return ResponseEntity.ok(CustomResponse.success(cameraService.aggiungiCamera(request)));
    }

    @DeleteMapping(path = "/rimuovi-camera")
    public ResponseEntity<CustomResponse<Void>> rimuoviCamera(@Valid @RequestBody EliminaCameraRequest request) {
        return ResponseEntity.ok(CustomResponse.success(cameraService.eliminaCamera(request)));
    }

    @GetMapping(path = "/occupazione-hotel")
    public ResponseEntity<CustomResponse<OccupazioneDTO>> occupazioneHotel() {
        return ResponseEntity.ok(CustomResponse.success(cameraService.calcolaOccupazioneHotel()));
    }

}