package it.nesea.albergo.hotel_service.mapper;

import it.nesea.albergo.hotel_service.dto.response.FasciaEtaDTO;
import it.nesea.albergo.hotel_service.model.FasciaEtaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class FasciaEtaMapper {
    public abstract FasciaEtaDTO fromEntityToDTO(FasciaEtaEntity fasciaEta);
    public abstract FasciaEtaEntity fromDTOToEntity(FasciaEtaDTO fasciaEta);

}