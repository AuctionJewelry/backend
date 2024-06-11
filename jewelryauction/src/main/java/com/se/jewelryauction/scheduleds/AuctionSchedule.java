package com.se.jewelryauction.scheduleds;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.repositories.IAuctionRepository;
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

    @Scheduled(fixedRate = 30000)
    public void updateStatusAuction() {
        List<AuctionEntity> auctions = auctionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (AuctionEntity auction : auctions) {
            LocalDateTime startTime = LocalDateTime.ofInstant(auction.getStartTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime endTime = LocalDateTime.ofInstant(auction.getEndTime().toInstant(), ZoneId.systemDefault());

            if (auction.getStatus() == AuctionStatus.Waiting && startTime.isBefore(now)) {
                auction.setStatus(AuctionStatus.InProgress);
                auctionRepository.save(auction);
            }

            if (auction.getStatus() == AuctionStatus.InProgress && endTime.isBefore(now)) {
                if (auction.getWinner() != null) {
                    auction.setStatus(AuctionStatus.Completed);
                } else {
                    auction.setStatus(AuctionStatus.Fail);
                }
                auctionRepository.save(auction);
            }
        }
    }
}
