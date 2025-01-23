package it.nesea.albergo.hotel_service.controller.feign;

import it.nesea.albergo.common_lib.dto.response.CustomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "${external-call.name.prenotazione}", url = "${external-call.prenotazione.util.url}")
public interface PrenotazioneExternalController {

}
