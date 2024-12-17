package it.nesea.albergo.hotel_service.mapper;

import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.dto.response.CameraDTO;
import it.nesea.albergo.hotel_service.dto.response.PrezzoCameraDTO;
import it.nesea.albergo.hotel_service.model.Camera;
import it.nesea.albergo.hotel_service.model.PrezzoCameraEntity;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;
import it.nesea.albergo.hotel_service.service.UtilService;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CameraMapper {

    @Autowired
    private UtilService utilService;

    @Mapping(source = "idStato", target = "stato")
    @Mapping(source = "idTipo", target = "tipo")
    public abstract Camera toCameraEntityFromCameraDTO(CameraDTO cameraDTO);

    @Mapping(source = "stato", target = "idStato")
    @Mapping(source = "tipo", target = "idTipo")
    public abstract CameraDTO toCameraDTOFromCameraEntity(Camera camera);

    @Mapping(source = "idStato", target = "stato")
    @Mapping(source = "idTipo", target = "tipo")
    public abstract Camera toCameraEntityFromCreaCameraRequest(CreaCameraRequest request);

    @Mapping(source = "tipo", target = "idTipo")
    public abstract PrezzoCameraDTO toPrezzoCameraDTOFromPrezzoCameraEntity(PrezzoCameraEntity prezzoCameraEntity);

    Integer mapIdTipoToTipo(TipoCameraEntity tipo) {
        return tipo.getId();
    }

    TipoCameraEntity mapTipoToIdTipo(Integer idTipo) {
        return utilService.getTipoCamera(idTipo);
    }

    Integer mapIdStatoToStato(StatoCameraEntity stato) {
        return stato.getId();
    }

    StatoCameraEntity mapStatoToIdStato(Integer idStato) {
        return utilService.getStatoCamera(idStato);
    }
}
