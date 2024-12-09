package it.nesea.albergo.hotel_service.controller;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.response.CustomResponse;
import it.nesea.albergo.hotel_service.service.CameraService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/hotel")
@AllArgsConstructor
@Validated
public class CameraController {
    private final CameraService cameraService;

    @PostMapping(path = "/aggiungi-camera")
    public ResponseEntity<CustomResponse<CameraDTO>> aggiungiCamera(@Valid @RequestBody CreaCameraRequest request) {
        return ResponseEntity.ok(CustomResponse.success(cameraService.aggiungiCamera(request)));
    }

}
