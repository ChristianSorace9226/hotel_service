package it.nesea.albergo.hotel_service.mapper;

import it.nesea.albergo.hotel_service.dto.response.FasciaEtaDTO;
import it.nesea.albergo.hotel_service.dto.response.StatoCameraDTO;
import it.nesea.albergo.hotel_service.dto.response.TipoCameraDTO;
import it.nesea.albergo.hotel_service.model.FasciaEtaEntity;
import it.nesea.albergo.hotel_service.model.StatoCameraEntity;
import it.nesea.albergo.hotel_service.model.TipoCameraEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UtilMapper {

    public abstract StatoCameraEntity fromStatoCameraDTOToEntity(StatoCameraDTO statoCamera);

    public abstract StatoCameraDTO fromStatoCameraEntityToDTO(StatoCameraEntity statoCamera);

    public abstract TipoCameraEntity fromTipoCameraDTOToEntity(TipoCameraDTO tipoCamera);

    public abstract TipoCameraDTO fromTipoCameraEntityToDTO(TipoCameraEntity tipoCamera);

    public abstract FasciaEtaDTO fromEntityToDTO(FasciaEtaEntity fasciaEta);

    public abstract FasciaEtaEntity fromDTOToEntity(FasciaEtaDTO fasciaEta);

}