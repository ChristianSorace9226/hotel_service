package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
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

    public List<StatoCameraEntity> getAllStati() {
        log.info("Ricevuta richiesta ottenimento informazioni stati");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<StatoCameraEntity> query = cb.createQuery(StatoCameraEntity.class);

        Root<StatoCameraEntity> root = query.from(StatoCameraEntity.class);

        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }

    public StatoCameraEntity getStatoCameraEntity(Integer idStato){
        log.info("Ricevuta richiesta ottenimento stato camera con id: {}", idStato);
        return entityManager.find(StatoCameraEntity.class, idStato);
    }

}