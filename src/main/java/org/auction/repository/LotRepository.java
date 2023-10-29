package org.auction.repository;

import org.auction.model.Lot;
import org.auction.model.LotStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    Page<Lot> findByStatus(LotStatus lotStatus, PageRequest pageRequest);

}
