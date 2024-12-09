package it.nesea.albergo.hotel_service.mapper;

import it.nesea.albergo.hotel_service.dto.CameraDTO;
import it.nesea.albergo.hotel_service.dto.request.CreaCameraRequest;
import it.nesea.albergo.hotel_service.model.Camera;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CameraMapper {
    public abstract Camera toCameraEntityFromCameraDTO (CameraDTO cameraDTO);
    public abstract CameraDTO toCameraDTOFromCameraEntity (Camera camera);
    public abstract Camera toCameraEntityFromCreaCameraRequest (CreaCameraRequest request);
}
