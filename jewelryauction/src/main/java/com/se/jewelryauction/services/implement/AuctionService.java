package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IJewelryRepository;
import com.se.jewelryauction.services.IAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final IAuctionRepository auctionRepository;
    private final IJewelryRepository jewelryRepository;


    @Override
    public AuctionEntity createAuction(AuctionEntity auction) {
        validateAuctionDuration(auction.getStartTime(), auction.getEndTime());
        JewelryEntity existingJewelry = jewelryRepository
                .findById(auction.getJewelry().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Jewelry", "id", auction.getJewelry().getId()));
        List<AuctionEntity> activeAuctions = auctionRepository
                .findActiveAuctionsByJewelryId(existingJewelry.getId());
        if (!activeAuctions.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Jewelry already has an active auction.");
        }
        auction.setJewelry(existingJewelry);
        auction.setStatus(AuctionStatus.Waiting);

        return auctionRepository.save(auction);
    }

    
    @Override
    public AuctionEntity getAuctionById(long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));    }

    @Override
    public List<AuctionEntity> getAllAuctions() {
        return auctionRepository.findAll();
    }

    @Override
    public AuctionEntity updateStatusAuction(long auctionId, AuctionStatus status) {
        AuctionEntity existingAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));
        existingAuction.setStatus(status);

        return auctionRepository.save(existingAuction);
    }

    @Override
    public List<AuctionEntity> getAuctionsByCategoryId(Long categoryId) {
        return auctionRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<AuctionEntity> getAuctionsByCollectionId(Long collectionId) {
        return auctionRepository.findByCollectionId(collectionId);
    }

    @Override
    public List<AuctionEntity> getMyAuctionsByStatus(AuctionStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return auctionRepository.findBySellerIdAndStatus(user.getId(), status);
    }

    @Override
    public List<AuctionEntity> searchAuctions(
            Long collectionId, Long categoryId, Float minPrice, Float maxPrice,
            Long brandId, JewelryCondition jewelryCondition, AuctionStatus status, Sex sex) {
        return auctionRepository.searchAuctions(
                collectionId, categoryId, minPrice, maxPrice, brandId, jewelryCondition, status, sex);
    }




    private void validateAuctionDuration(Date startTime, Date endTime) {
        if (startTime.after(endTime)) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Start time cannot be after end time");
        }

        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();

        Duration duration = Duration.between(startInstant, endInstant);

        if (duration.toDays() < 1 || duration.toDays() > 7) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Auction duration must be between 1 and 7 days");
        }
    }


}
