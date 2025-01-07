package it.nesea.albergo.hotel_service.mapper;

import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.response.CameraDTO;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;
import jakarta.persistence.EntityManager;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CameraMapper {

    @Autowired
    private EntityManager entityManager;

    @Mapping(source = "idStato", target = "stato")
    @Mapping(source = "idTipo", target = "tipo")
    public abstract Camera toCameraEntityFromCameraDTO(CameraDTO cameraDTO);

    @Mapping(source = "stato", target = "idStato")
    @Mapping(source = "tipo", target = "idTipo")
    public abstract CameraDTO toCameraDTOFromCameraEntity(Camera camera);

    @Mapping(source = "idStato", target = "stato")
    @Mapping(source = "idTipo", target = "tipo")
    public abstract Camera toCameraEntityFromCreaCameraRequest(CreaCameraRequest request);

    Integer mapTipoToIdTipo(TipoCameraEntity tipo) {
        return tipo.getId();
    }

    TipoCameraEntity mapIdTipoToTipo(Integer idTipo) {
        return entityManager.find(TipoCameraEntity.class, idTipo);
    }

    Integer mapStatoToIdStato(StatoCameraEntity stato) {
        return stato.getId();
    }

    StatoCameraEntity mapIdStatoToStato(Integer idStato) {
        return entityManager.find(StatoCameraEntity.class, idStato);
    }
}
