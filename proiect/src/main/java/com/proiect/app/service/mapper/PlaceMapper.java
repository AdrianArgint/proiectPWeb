package com.proiect.app.service.mapper;

import com.proiect.app.domain.Place;
import com.proiect.app.service.dto.PlaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Place} and its DTO {@link PlaceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlaceMapper extends EntityMapper<PlaceDTO, Place> {}
