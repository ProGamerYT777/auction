package org.auction.repository;

import jakarta.persistence.Tuple;
import org.auction.model.CsvLot;
import org.auction.model.Lot;
import org.auction.model.LotStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    Page<Lot> findByStatus(LotStatus lotStatus, PageRequest pageRequest);

    @Query(value = """
     SELECT l.id AS id,
            l.title AS title,
            l.status AS status,
            (SELECT b.bidderName
            FROM bids b
            WHERE b.lot_id = l.id
            ORDER BY b.date DESC
            LIMIT 1) AS lastBidder,
            (SELECT l2.start_price + 
                    l2.bid_price * 
                    (SELECT count(b.id) FROM bids b WHERE b.lot_id = l2.id)
            FROM lots l2
            WHERE l2.id = l.id AS currentPrice
            FROM lots l;
            """, nativeQuery = true)
    List<Tuple> getLotsForExport();
}
