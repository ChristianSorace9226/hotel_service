package it.nesea.albergo.hotel_service.controller;

import it.nesea.albergo.common_lib.dto.PrezzoCameraDTO;
import it.nesea.albergo.common_lib.dto.request.CheckDateStart;
import it.nesea.albergo.common_lib.dto.response.CustomResponse;
import it.nesea.albergo.hotel_service.dto.response.FasciaEtaDTO;
import it.nesea.albergo.hotel_service.dto.response.StatoCameraDTO;
import it.nesea.albergo.hotel_service.dto.response.TipoCameraDTO;
import it.nesea.albergo.hotel_service.service.UtilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/util")
public class UtilController {

    private final UtilService util;

    public UtilController(UtilService util) {
        this.util = util;
    }

    @GetMapping("/get-stati")
    public ResponseEntity<CustomResponse<List<StatoCameraDTO>>> getStati() {
        return ResponseEntity.ok(CustomResponse.success(util.getAllStati()));
    }

    @GetMapping("/get-tipi")
    public ResponseEntity<CustomResponse<List<TipoCameraDTO>>> getTipi() {
        return ResponseEntity.ok(CustomResponse.success(util.getAllTipi()));
    }

    @GetMapping("/get-fascia-eta")
    public ResponseEntity<CustomResponse<List<FasciaEtaDTO>>> getFasciaEta() {
        return ResponseEntity.ok(CustomResponse.success(util.getListaFasciaEta()));
    }

    @PostMapping("/get-prezzi-camere")
    public ResponseEntity<CustomResponse<List<PrezzoCameraDTO>>> getPrezziCamere(@RequestBody List<Integer> listaEta) {
        return ResponseEntity.ok(CustomResponse.success(util.getListaPrezzario(listaEta)));
    }

    @PostMapping("/check-disponibilita")
    public ResponseEntity<CustomResponse<Boolean>> checkDisponibilita(@RequestBody CheckDateStart request) {
        return ResponseEntity.ok(CustomResponse.success(util.checkDataInizioDisponibilita(request)));
    }

}