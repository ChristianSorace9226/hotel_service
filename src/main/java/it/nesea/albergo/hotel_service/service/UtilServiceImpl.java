package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UtilServiceImpl implements UtilService {

    private final EntityManager entityManager;

    public UtilServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<StatoCameraEntity> getAllStati() {
        log.info("Ricevuta richiesta getAllStati");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StatoCameraEntity> query = cb.createQuery(StatoCameraEntity.class);
        Root<StatoCameraEntity> root = query.from(StatoCameraEntity.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<TipoCameraEntity> getAllTipi() {
        log.info("Ricevuta richiesta getAllTipi");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TipoCameraEntity> query = cb.createQuery(TipoCameraEntity.class);
        Root<TipoCameraEntity> root = query.from(TipoCameraEntity.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public StatoCameraEntity getStatoCamera(Integer idStato) {
        log.info("Ricevuta richiesta getStatoCamera con id: {}", idStato);
        return entityManager.find(StatoCameraEntity.class, idStato);
    }

    @Override
    public TipoCameraEntity getTipoCamera(Integer idTipo) {
        log.info("Ricerca tipoCamera");
        return entityManager.find(TipoCameraEntity.class, idTipo);
    }

}