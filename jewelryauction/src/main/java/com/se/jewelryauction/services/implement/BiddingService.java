package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IAutoBiddingRepository;
import com.se.jewelryauction.repositories.IBiddingRepository;
import com.se.jewelryauction.requests.BidRequest;
import com.se.jewelryauction.services.IBiddingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
@AllArgsConstructor
@Service
public class BiddingService implements IBiddingService {
    private final IBiddingRepository biddingRepository;
    private final IAuctionRepository auctionRepository;
    private final IAutoBiddingRepository autoBiddingRepository;


    @Override
    public void createBidding(BidRequest bidRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        AuctionEntity auction = auctionRepository.findById(bidRequest.getAuctionId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Auction not found with ID: " + bidRequest.getAuctionId()));

        AutoBiddingEntity highestAutoBid = autoBiddingRepository.findTop(auction);

        if (highestAutoBid != null && bidRequest.getBidAmount() == highestAutoBid.getMaxBid()) {
            createBidding(auction, highestAutoBid.getCustomer(), highestAutoBid.getMaxBid());
        }else {
            if (auction.getWinner() != null && auction.getWinner().getId().equals(user.getId())) {
                createAutoBidding(auction, user, bidRequest.getBidAmount());
            } else {
                float minBidAmount = auction.getCurrentPrice() + auction.getStep();

                if (bidRequest.getBidAmount() > minBidAmount) {
                    createBidding(auction, user, minBidAmount);

                    //float remainingAmount = bidRequest.getBidAmount() - minBidAmount;
                    createAutoBidding(auction, user, bidRequest.getBidAmount());
                } else {
                    createBidding(auction, user, bidRequest.getBidAmount());
                }
            }
        }
    }



    @Override
    public List<BiddingEntity> getBiddingByAuctionId(long id) {
        return biddingRepository.findByAuctionId(id);
    }

    private void createBidding(AuctionEntity auction, UserEntity user, float bidAmount) {
        BiddingEntity bidding = new BiddingEntity();
        bidding.setAuction(auction);
        bidding.setCustomer(user);
        bidding.setBidAmount(bidAmount);
        bidding.setBidTime(LocalDateTime.now());
        biddingRepository.save(bidding);

        auction.setCurrentPrice(bidAmount);
        auction.setTotalBids(auction.getTotalBids() + 1);
        auction.setWinner(user);
        auctionRepository.save(auction);
    }

    private void createAutoBidding(AuctionEntity auction, UserEntity user, float maxBid) {
        AutoBiddingEntity autoBidding = new AutoBiddingEntity();
        autoBidding.setAuction(auction);
        autoBidding.setCustomer(user);
        autoBidding.setMaxBid(maxBid);
        autoBidding.setBidTime(LocalDateTime.now());
        autoBiddingRepository.save(autoBidding);
    }
}
