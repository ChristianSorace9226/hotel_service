package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilServiceImpl implements UtilService {

    private final EntityManager entityManager;

    public UtilServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<StatoCameraEntity> getAllStati() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<StatoCameraEntity> query = cb.createQuery(StatoCameraEntity.class);

        Root<StatoCameraEntity> root = query.from(StatoCameraEntity.class);

        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }

    public StatoCameraEntity getStatoCameraEntity(Integer idStato){
        return entityManager.find(StatoCameraEntity.class, idStato);
    }

}