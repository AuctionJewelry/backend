package com.se.jewelryauction.scheduleds;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.AutoBiddingEntity;
import com.se.jewelryauction.models.BiddingEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IAutoBiddingRepository;
import com.se.jewelryauction.repositories.IBiddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
@Component
public class BiddingSchedule {

    @Autowired
    private IAuctionRepository auctionRepository;

    @Autowired
    private IAutoBiddingRepository autoBiddingRepository;

    @Autowired
    private IBiddingRepository biddingRepository;

    @Scheduled(fixedRate = 6000) // runs every 60 seconds
    public void checkAndPlaceAutoBids() {
        List<AuctionEntity> auctions = auctionRepository.findAll();
        for (AuctionEntity auction : auctions) {
            if (auction.getStatus() == AuctionStatus.InProgress) {
                List<AutoBiddingEntity> autoBiddings = autoBiddingRepository.findByAuctionId(auction.getId());
                for (AutoBiddingEntity autoBid : autoBiddings) {
                    if (auction.getCurrentPrice() < autoBid.getMaxBid() && !auction.getWinner().getId().equals(autoBid.getCustomer().getId())) {
                        placeBid(auction, autoBid);
                        break;
                    }
                }
            }
        }
    }


    private void placeBid(AuctionEntity auction, AutoBiddingEntity autoBid) {
        BiddingEntity newBid = new BiddingEntity();
        newBid.setAuction(auction);
        newBid.setCustomer(autoBid.getCustomer());
        newBid.setBidAmount(auction.getCurrentPrice() + auction.getStep());
        newBid.setBidTime(LocalDateTime.now());
        newBid.setAutoBid(true);
        biddingRepository.save(newBid);

        auction.setCurrentPrice(auction.getCurrentPrice() + auction.getStep());
        auction.setWinner(autoBid.getCustomer());
        auction.setTotalBids(auction.getTotalBids()+1);
        auctionRepository.save(auction);
    }
}
