package org.auction.repository;

import org.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    Integer countByLotId(Long lotId);

    Optional<Bid> findFirstByLotIdOrderByDateAsc(Long lotId);

    Optional<Bid> findFirstByLotIdOrderByDateDesc(Long lotId);
    @Query("""
        SELECT new pro.sky.auction.dto.BidDto(b.bidderName, b.date) FROM Bid b WHERE b.bidderName = (
        SELECT b.bidderName FROM Bid b GROUP BY b.bidderName ORDER BY count(b.bidderName) DESC LIMIT 1
        ) ORDER BY b.date DESC LIMIT 1
        """)
    Optional<Bid> findTheMostFrequentBidder(Long lotId);

}
