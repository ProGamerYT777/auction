package org.auction.model;

public class CsvLot {

    private Long id;
    private String title;
    private LotStatus status;
    private String lastBidder;
    private int currentPrice;

    public CsvLot(Long id, String title, LotStatus status, String lastBidder, int currentPrice) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.lastBidder = lastBidder;
        this.currentPrice = currentPrice;
    }

    public CsvLot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LotStatus getStatus() {
        return status;
    }

    public void setStatus(LotStatus status) {
        this.status = status;
    }

    public String getLastBidder() {
        return lastBidder;
    }

    public void setLastBidder(String lastBidder) {
        this.lastBidder = lastBidder;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }
}
