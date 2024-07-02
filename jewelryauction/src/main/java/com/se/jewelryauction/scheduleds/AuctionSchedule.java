package com.se.jewelryauction.scheduleds;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IJewelryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuctionSchedule {
    private final IAuctionRepository auctionRepository;
    private final IJewelryRepository jewelryRepository;

    @Scheduled(fixedRate = 30000)
    public void updateStatusAuction() {
        List<AuctionEntity> auctions = auctionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (AuctionEntity auction : auctions) {
            JewelryEntity jewelry = auction.getJewelry();

            LocalDateTime startTime = LocalDateTime.ofInstant(auction.getStartTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime endTime = LocalDateTime.ofInstant(auction.getEndTime().toInstant(), ZoneId.systemDefault());

            if (auction.getStatus() == AuctionStatus.Waiting && startTime.isBefore(now)) {
                auction.setStatus(AuctionStatus.InProgress);
                auctionRepository.save(auction);

            }

            if (auction.getStatus() == AuctionStatus.InProgress && endTime.isBefore(now)) {
                if (auction.getWinner() != null) {
                    auction.setStatus(AuctionStatus.Completed);
                    jewelry.setStatus(JewelryStatus.AUCTION_SUCCESS);
                } else {
                    auction.setStatus(AuctionStatus.Fail);
                    jewelry.setStatus(JewelryStatus.AUCTION_FAIL);
                }
                jewelryRepository.save(jewelry);
                auctionRepository.save(auction);
            }
        }
    }
}
