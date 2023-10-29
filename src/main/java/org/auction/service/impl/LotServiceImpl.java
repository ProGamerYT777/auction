package org.auction.service.impl;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.auction.dto.LotDto;
import org.auction.mapper.LotMapper;
import org.auction.model.Lot;
import org.auction.model.LotStatus;
import org.auction.repository.BidRepository;
import org.auction.repository.LotRepository;
import org.auction.service.BidService;
import org.auction.service.LotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private final BidService bidService;
    private final LotMapper lotMapper;

    public LotServiceImpl(LotRepository lotRepository, BidRepository bidRepository, BidService bidService, LotMapper lotMapper) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
        this.bidService = bidService;
        this.lotMapper = lotMapper;
    }

    @Override
    public LotDto createLot(LotDto createLotDto) {
        Lot lot = lotMapper.toEntity(createLotDto);
        lot.setStatus(LotStatus.CREATED);
        return lotMapper.toDto(lotRepository.save(lot));
    }

    @Override
    public LotDto getLotById(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                return lotMapper.toDto(lotOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public void startAuction(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                Lot lot = lotOptional.get();
                lot.setStatus(LotStatus.STARTED);
                lotRepository.save(lot);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public void stopAuction(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                Lot lot = lotOptional.get();
                lot.setStatus(LotStatus.STOPPED);
                lotRepository.save(lot);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public Integer getCurrentPrice(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                Lot lot = lotOptional.get();
                Integer bidCount = bidRepository.countByLotId(lotId);
                return bidCount * lot.getBidPrice() + lot.getStartPrice();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public List<LotDto> getLotsByStatusAndPage(String status, int page) {
        Sort sort = Sort.by("id").ascending();
        PageRequest pageRequest = PageRequest.of(page, 10, sort);

        Page<Lot> lotPage = lotRepository.findByStatus(LotStatus.valueOf(status), pageRequest);
        return lotPage.getContent().stream()
                .map(lotMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void exportLotsToCSV(HttpServletResponse response) {
        List<Lot> lots = lotRepository.findAll();

        if (lots.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет лотов для экспорта");
        }

        try {

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=lots.csv");

            PrintWriter writer = response.getWriter();

            writer.println("id,title,status,lastBidder,currentPrice");

            for (Lot Lot : lots) {
                String currentPrice = String.valueOf(getCurrentPrice(Lot.getId()));
                String lastBidder = bidService.getLastBid(Lot.getId()).getBidderName();

                writer.println(
                        Lot.getId() + "," +
                                Lot.getTitle() + "," +
                                Lot.getStatus().name() + "," +
                                lastBidder + "," +
                                currentPrice
                );
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка экспорта csv файла", e);
        }
    }
}
