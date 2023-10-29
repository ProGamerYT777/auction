package org.auction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LotDto {
    private Long id;
    private String title;
    private String description;
    @JsonProperty("start_price")
    private Integer startPrice;
    @JsonProperty("bid_price")
    private Integer bidPrice;
    private String status;
    private List<BidDto> bids;
}