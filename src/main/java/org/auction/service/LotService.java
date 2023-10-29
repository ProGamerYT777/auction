package org.auction.service;

import jakarta.servlet.http.HttpServletResponse;
import org.auction.dto.LotDto;

import java.util.List;

public interface LotService {

    LotDto createLot(LotDto createLotDto);

    LotDto getLotById(Long lotId);

    void startAuction(Long lotId);

    void stopAuction(Long lotId);

    Integer getCurrentPrice(Long lotId);

    List<LotDto> getLotsByStatusAndPage(String status, int page);

    void exportLotsToCSV(HttpServletResponse response);
}
