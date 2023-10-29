package org.auction.service.impl;

import lombok.RequiredArgsConstructor;
import org.auction.dto.BidDto;
import org.auction.mapper.BidMapper;
import org.auction.model.Bid;
import org.auction.model.Lot;
import org.auction.model.LotStatus;
import org.auction.repository.BidRepository;
import org.auction.repository.LotRepository;
import org.auction.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    private final BidMapper bidMapper;

    public BidServiceImpl(BidRepository bidRepository, LotRepository lotRepository, BidMapper bidMapper) {
        this.bidRepository = bidRepository;
        this.lotRepository = lotRepository;
        this.bidMapper = bidMapper;
    }

    @Override
    public BidDto createBid(Long lotId, BidDto bidDto) {
        Optional<Lot> lotOptional = lotRepository.findById(lotId);

        if (lotOptional.isPresent()) {
            Lot lot = lotOptional.get();

            if (lot.getStatus() == LotStatus.STARTED) {
                Bid bid = bidMapper.toEntity(bidDto);
                bid.setLot(lot);
                return bidMapper.toDto(bidRepository.save(bid));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Лот в неверном статусе");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
        }
    }

    @Override
    public BidDto getFirstBidder(Long lotId) {
        Optional<Bid> firstBid = bidRepository.findFirstByLotIdOrderByDateAsc(lotId);

        if (firstBid.isPresent()) {
            return bidMapper.toDto(firstBid.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
        }
    }

    @Override
    public BidDto getMostFrequentBidder(Long lotId) {

        return null;
    }

    @Override
    public BidDto getLastBid(Long lotId) {
        Optional<Bid> lastBidOptional = bidRepository.findFirstByLotIdOrderByDateDesc(lotId);

        if (lastBidOptional.isPresent()) {
            return bidMapper.toDto(lastBidOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ставок ещё не было");
        }
    }
}
