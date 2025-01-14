package it.nesea.albergo.hotel_service.service;

import it.nesea.albergo.common_lib.dto.PrezzoCameraDTO;
import it.nesea.albergo.common_lib.dto.request.CheckDateStart;
import it.nesea.albergo.common_lib.exception.NotFoundException;
import it.nesea.albergo.hotel_service.dto.request.PrezzarioRequest;
import it.nesea.albergo.hotel_service.dto.response.FasciaEtaDTO;
import it.nesea.albergo.hotel_service.dto.response.StatoCameraDTO;
import it.nesea.albergo.hotel_service.dto.response.TipoCameraDTO;
import it.nesea.albergo.hotel_service.mapper.UtilMapper;
import it.nesea.albergo.hotel_service.model.*;
import it.nesea.albergo.hotel_service.model.repository.CameraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UtilServiceImpl implements UtilService {

    private final EntityManager entityManager;
    private final UtilMapper utilMapper;
    private final CameraService cameraService;
    private final CameraRepository cameraRepository;

    public UtilServiceImpl(EntityManager entityManager, UtilMapper utilMapper, @Lazy CameraService cameraService, CameraRepository cameraRepository) {
        this.entityManager = entityManager;
        this.utilMapper = utilMapper;
        this.cameraService = cameraService;
        this.cameraRepository = cameraRepository;
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
            statiCameraDTO.add(utilMapper.fromStatoCameraEntityToDTO(statoCamera));
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
            tipiCameraDTO.add(utilMapper.fromTipoCameraEntityToDTO(tipoCamera));
        }
        return tipiCameraDTO;
    }

    @Override
    public List<FasciaEtaDTO> getListaFasciaEta() {
        log.info("Ricevuta richiesta getAllFasciaEta");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FasciaEtaEntity> query = cb.createQuery(FasciaEtaEntity.class);
        Root<FasciaEtaEntity> root = query.from(FasciaEtaEntity.class);
        query.select(root);
        List<FasciaEtaEntity> fasceEta = entityManager.createQuery(query).getResultList();
        List<FasciaEtaDTO> fasceEtaDTO = new ArrayList<>();
        for (FasciaEtaEntity fasciaEta : fasceEta) {
            fasceEtaDTO.add(utilMapper.fromEntityToDTO(fasciaEta));
        }
        return fasceEtaDTO;
    }

    @Override
    public List<PrezzoCameraDTO> getListaPrezzario(List<Integer> listaEta) {
        log.info("Richiesta ricevuta per ottenere il prezzario per almeno {} persone", listaEta.size());

        // Costruzione del CriteriaBuilder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // Prima query: Ottenere camere con capacità >= numeroPersone
        CriteriaQuery<Camera> criteriaQueryCamera = criteriaBuilder.createQuery(Camera.class);
        Root<Camera> rootCamera = criteriaQueryCamera.from(Camera.class);
        Predicate filtroCapacita = criteriaBuilder.greaterThanOrEqualTo(rootCamera.get("capacita"), listaEta.size());
        criteriaQueryCamera.select(rootCamera).where(filtroCapacita);

        List<Camera> camere = entityManager.createQuery(criteriaQueryCamera).getResultList();

        // Iterazione sulle camere filtrate
        List<PrezzoCameraDTO> prezziCamera = new ArrayList<>();
        for (Camera camera : camere) {
            PrezzarioRequest prezzarioRequest = new PrezzarioRequest();
            prezzarioRequest.setEta(listaEta);
            prezzarioRequest.setNumeroCamera(camera.getNumeroCamera());
            prezziCamera.add(cameraService.getPrezzario(prezzarioRequest));
        }

        return prezziCamera;
    }

    @Override
    public Boolean checkDataInizioDisponibilita(CheckDateStart request) {
       Camera cameraRequest = cameraRepository.findByNumeroCamera(request.getNumeroCamera());
       return !request.getDataCheckIn().isBefore(cameraRequest.getDataInizioDisponibilita().atStartOfDay());
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