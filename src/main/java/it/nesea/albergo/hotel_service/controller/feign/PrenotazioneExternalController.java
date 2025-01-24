package it.nesea.albergo.hotel_service.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${external-call.name.prenotazione}", url = "${external-call.prenotazione.util.url}")
public interface PrenotazioneExternalController {

}
