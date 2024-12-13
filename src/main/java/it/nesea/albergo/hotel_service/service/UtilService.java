package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.model.StatoCameraEntity;

import java.util.List;

public interface UtilService {
    List<StatoCameraEntity> getAllStati();

    StatoCameraEntity getStatoCameraEntity(Integer idStato);
}
