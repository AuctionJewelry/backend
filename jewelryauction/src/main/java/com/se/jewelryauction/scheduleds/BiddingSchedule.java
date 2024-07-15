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

    @Scheduled(fixedRate = 1000) // runs every 1 second
    public void checkAndPlaceAutoBids() {
        List<AuctionEntity> auctions = auctionRepository.findAll();
        for (AuctionEntity auction : auctions) {
            if (auction.getStatus() == AuctionStatus.InProgress) {
                List<AutoBiddingEntity> autoBiddings = autoBiddingRepository.findByAuctionId(auction.getId());

                if (autoBiddings.isEmpty()) {
                    continue;
                }

                AutoBiddingEntity highestBidder = null;
                float highestBid = 0;
                float secondHighestBid = 0;

                // Determine the highest and second highest bids
                for (AutoBiddingEntity autoBid : autoBiddings) {
                    if (autoBid.getMaxBid() > highestBid) {
                        secondHighestBid = highestBid; // Update second highest
                        highestBid = autoBid.getMaxBid();
                        highestBidder = autoBid;
                    } else if (autoBid.getMaxBid() > secondHighestBid) {
                        secondHighestBid = autoBid.getMaxBid();
                    }
                }

                // Calculate the next valid bid amount
                float nextBidAmount = auction.getCurrentPrice() + auction.getStep();

                // Determine the bid amount for the highest bidder
                if (highestBidder != null) {
                    float finalBidAmount = Math.min(highestBid, secondHighestBid + auction.getStep());
                    if (finalBidAmount > auction.getCurrentPrice()) {
                        placeBid(auction, highestBidder, finalBidAmount);
                    }
                }
            }
        }
    }

    private void placeBid(AuctionEntity auction, AutoBiddingEntity autoBid, float bidAmount) {
        BiddingEntity newBid = new BiddingEntity();
        newBid.setAuction(auction);
        newBid.setCustomer(autoBid.getCustomer());
        newBid.setBidAmount(bidAmount);
        newBid.setBidTime(LocalDateTime.now());
        newBid.setAutoBid(true);
        biddingRepository.save(newBid);

        // Update the current price and winner
        auction.setCurrentPrice(bidAmount);
        auction.setWinner(autoBid.getCustomer());
        auction.setTotalBids(auction.getTotalBids() + 1);
        auctionRepository.save(auction);
    }
}
