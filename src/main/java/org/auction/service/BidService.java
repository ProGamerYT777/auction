package org.auction.service;

import org.auction.dto.BidDto;

public interface BidService {

    BidDto createBid(Long lotId, BidDto bidDto);

    BidDto getFirstBidder(Long lotId);

    BidDto getMostFrequentBidder(Long lotId);

    BidDto getLastBid(Long lotId);
}