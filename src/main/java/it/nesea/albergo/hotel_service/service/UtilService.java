package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;

import java.util.List;

public interface UtilService {
    List<StatoCameraEntity> getAllStati();

    List<TipoCameraEntity> getAllTipi();

    StatoCameraEntity getStatoCamera(Integer idStato);

    TipoCameraEntity getTipoCamera(Integer idTipo);
}
