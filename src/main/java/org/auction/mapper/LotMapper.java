package org.auction.mapper;

import org.auction.dto.LotDto;
import org.auction.model.Lot;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = BidMapper.class)
@Component
public interface LotMapper {
    Lot toEntity(LotDto lotDto);

    @AfterMapping
    default void linkBids(@MappingTarget Lot lot) {
        if (lot.getBids() != null) {
            lot.getBids().forEach(bid -> bid.setLot(lot));
        }
    }

    LotDto toDto(Lot lot);

}
