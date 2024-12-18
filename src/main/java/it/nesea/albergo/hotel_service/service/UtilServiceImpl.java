package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.hotel_service.dto.response.StatoCameraDTO;
import it.nesea.albergo.hotel_service.dto.response.TipoCameraDTO;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UtilServiceImpl implements UtilService {

    private final EntityManager entityManager;

    public UtilServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<StatoCameraDTO> getAllStati() {
        log.info("Ricevuta richiesta getAllStati");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StatoCameraEntity> query = cb.createQuery(StatoCameraEntity.class);
        Root<StatoCameraEntity> root = query.from(StatoCameraEntity.class);
        query.select(root);
        List<StatoCameraEntity> statiCamera = entityManager.createQuery(query).getResultList();
        List<StatoCameraDTO> statiCameraDTO = new ArrayList<>();
        for (StatoCameraEntity statoCamera : statiCamera) {
            StatoCameraDTO statoCameraDTO = new StatoCameraDTO();
            statoCameraDTO.setId(statoCamera.getId());
            statoCameraDTO.setStato(statoCamera.getStato());
            statiCameraDTO.add(statoCameraDTO);
        }
        return statiCameraDTO;
    }

    @Override
    public List<TipoCameraDTO> getAllTipi() {
        log.info("Ricevuta richiesta getAllTipi");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TipoCameraEntity> query = cb.createQuery(TipoCameraEntity.class);
        Root<TipoCameraEntity> root = query.from(TipoCameraEntity.class);
        query.select(root);
        List<TipoCameraEntity> tipoCameraEntityList = entityManager.createQuery(query).getResultList();
        List<TipoCameraDTO> tipiCameraDTO = new ArrayList<>();
        for (TipoCameraEntity tipoCamera : tipoCameraEntityList) {
            TipoCameraDTO tipoCameraDTO = new TipoCameraDTO();
            tipoCameraDTO.setId(tipoCamera.getId());
            tipoCameraDTO.setTipo(tipoCamera.getTipo());
            tipiCameraDTO.add(tipoCameraDTO);
        }
        return tipiCameraDTO;
    }

    @Override
    public StatoCameraEntity getStatoCamera(Integer idStato) {
        log.info("Ricevuta richiesta getStatoCamera con id: {}", idStato);
        try {
            return entityManager.find(StatoCameraEntity.class, idStato);
        } catch (NoResultException e) {
            throw new NotFoundException("StatoCamera non presente per l'id fornito");
        }
    }

    @Override
    public TipoCameraEntity getTipoCamera(Integer idTipo) {
        log.info("Ricevuta richiesta getTipoCamera con id: {}", idTipo);
        try {
            return entityManager.find(TipoCameraEntity.class, idTipo);
        } catch (NoResultException e) {
            throw new NotFoundException("TipoCamera non presente per l'id fornito");
        }
    }

}