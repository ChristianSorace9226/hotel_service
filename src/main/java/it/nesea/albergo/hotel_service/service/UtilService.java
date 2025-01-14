package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.dto.PrezzoCameraDTO;
import it.nesea.albergo.common_lib.dto.request.CheckDateStart;
import it.nesea.albergo.hotel_service.dto.response.FasciaEtaDTO;
import it.nesea.albergo.hotel_service.dto.response.StatoCameraDTO;
import it.nesea.albergo.hotel_service.dto.response.TipoCameraDTO;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.PrezzoCameraEntity;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface UtilService {
    List<StatoCameraDTO> getAllStati();

    List<TipoCameraDTO> getAllTipi();

    StatoCameraEntity getStatoCamera(Integer idStato);

    TipoCameraEntity getTipoCamera(Integer idTipo);

    PrezzoCameraEntity getPrezzoCamera(Camera camera, Integer numeroOccupanti);

    List<FasciaEtaDTO> getListaFasciaEta();

    List<PrezzoCameraDTO> getListaPrezzario(List<Integer> listaEta);

    Boolean checkDataInizioDisponibilita(CheckDateStart request);
}
