package it.nesea.albergo.hotel_service.controller;

import it.nesea.albergo.common_lib.dto.response.CustomResponse;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.service.UtilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/util")
public class UtilController {

    private final UtilService util;

    public UtilController(UtilService util) {
        this.util = util;
    }

    @GetMapping("/get-stati")
    public ResponseEntity<CustomResponse<List<StatoCameraEntity>>> getStati() {
        return ResponseEntity.ok(CustomResponse.success(util.getAllStati()));
    }

}
