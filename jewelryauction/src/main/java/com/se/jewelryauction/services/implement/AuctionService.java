package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IJewelryRepository;
import com.se.jewelryauction.services.IAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final IAuctionRepository auctionRepository;
    private final IJewelryRepository jewelryRepository;


    @Override
    public AuctionEntity createAuction(AuctionEntity auction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        validateAuctionDuration(auction.getStartTime(), auction.getEndTime());

        if (!isStartTimeValid(auction.getStartTime())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu phải sau thời gian tạo ít nhất 2 phút.");
        }

        JewelryEntity existingJewelry = jewelryRepository
                .findById(auction.getJewelry().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Jewelry", "id", auction.getJewelry().getId()));

        if (!existingJewelry.getSellerId().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED,"Bạn không có quyền truy cập");
        }
        List<AuctionEntity> activeAuctions = auctionRepository
                .findActiveAuctionsByJewelryId(existingJewelry.getId());
        if (!activeAuctions.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Jewelry already has an active auction.");
        }
        existingJewelry.setStatus(JewelryStatus.AUCTIONING);
        auction.setJewelry(existingJewelry);
        auction.setStatus(AuctionStatus.Waiting);

        return auctionRepository.save(auction);
    }

    
    @Override
    public AuctionEntity getAuctionById(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));

        if (auction.getJewelry().getSellerId().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED,"Bạn không có quyền truy cập");
        }

        return auction;
    }

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
    public Page<AuctionEntity> searchAuctions(
            Long collectionId, Long categoryId, Float minPrice, Float maxPrice,
            Long brandId, JewelryCondition jewelryCondition, AuctionStatus status, Sex sex, PageRequest pageRequest) {
        Page<AuctionEntity> auctionListPage;
        auctionListPage = auctionRepository.searchAuctions(
                collectionId, categoryId, minPrice, maxPrice, brandId, jewelryCondition, status, sex, pageRequest);
        return auctionListPage;
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

    private boolean isStartTimeValid(Date startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = startTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return start.isAfter(now.plusMinutes(2));
    }

}
