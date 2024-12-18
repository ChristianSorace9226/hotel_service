package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.hotel_service.dto.response.StatoCameraDTO;
import it.nesea.albergo.hotel_service.dto.response.TipoCameraDTO;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.PrezzoCameraEntity;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
        StatoCameraEntity statoCamera = entityManager.find(StatoCameraEntity.class, idStato);
        if (statoCamera == null) {
            throw new NotFoundException("StatoCamera non presente per l'id fornito");
        }
        return statoCamera;
    }

    @Override
    public TipoCameraEntity getTipoCamera(Integer idTipo) {
        log.info("Ricevuta richiesta getTipoCamera con id: {}", idTipo);
        TipoCameraEntity tipoCamera = entityManager.find(TipoCameraEntity.class, idTipo);
        if (tipoCamera == null) {
            throw new NotFoundException("TipoCamera non presente per l'id fornito");
        }
        return tipoCamera;
    }

    @Override
    public PrezzoCameraEntity getPrezzoCamera(Camera camera, Integer numeroOccupanti) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PrezzoCameraEntity> criteriaQueryPrezzo = criteriaBuilder.createQuery(PrezzoCameraEntity.class);
        Root<PrezzoCameraEntity> rootPrezzo = criteriaQueryPrezzo.from(PrezzoCameraEntity.class);
        List<Predicate> predicatesPrezzo = new ArrayList<>();
        predicatesPrezzo.add(criteriaBuilder.equal(rootPrezzo.get("tipo").get("id"), camera.getTipo().getId()));
        predicatesPrezzo.add(criteriaBuilder.equal(rootPrezzo.get("numeroOccupanti"), numeroOccupanti));
        criteriaQueryPrezzo.select(rootPrezzo).where(criteriaBuilder.and(predicatesPrezzo.toArray(new Predicate[0])));
        PrezzoCameraEntity prezzoCameraEntity = entityManager.createQuery(criteriaQueryPrezzo).getSingleResult();
        if (prezzoCameraEntity == null) {
            log.warn("Prezzario non trovato per numero occupanti {}", numeroOccupanti);
            throw new NotFoundException("Prezzario non trovato per il numero di persone fornito");
        }
        return prezzoCameraEntity;
    }

}