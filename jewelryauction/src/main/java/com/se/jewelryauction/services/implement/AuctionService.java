package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.requests.UpdateTimeAuctionRequest;
import com.se.jewelryauction.responses.AuctionResponse;
import com.se.jewelryauction.responses.ListBidForAuction;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final IAuctionRepository auctionRepository;
    private final IJewelryRepository jewelryRepository;
    private final IBiddingRepository  biddingRepository;
    private final IAutoBiddingRepository autoBiddingRepository;
    private final IValuatingRepository valuatingRepository;

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
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        List<AuctionEntity> activeAuctions = auctionRepository
                .findActiveAuctionsByJewelryId(existingJewelry.getId());
        if (!activeAuctions.isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Jewelry already has an active auction.");
        }
        existingJewelry.setStatus(JewelryStatus.AUCTIONING);
        auction.setCurrentPrice(existingJewelry.getStaringPrice());
        auction.setJewelry(existingJewelry);
        auction.setStatus(AuctionStatus.Waiting);

        return auctionRepository.save(auction);
    }


    @Override
    public AuctionEntity getAuctionById(long id) {

        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));

        return auction;
    }

    @Override
    public List<AuctionEntity> getAllAuctions() {
        return auctionRepository.findAll();
    }


    @Override
    public AuctionEntity updateTime(Long auctionId, UpdateTimeAuctionRequest request) {
        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));

        if (request.getStartTime() != null) {
            auction.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            auction.setEndTime(request.getEndTime());
        }
        return auctionRepository.save(auction);


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

    @Override
    public void cancelAuction(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));

        if (auction.getStatus() != AuctionStatus.Waiting && auction.getStatus() != AuctionStatus.WaitingConfirm) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Auctions are not waiting or waiting confirm.");
        }
        if (!auction.getJewelry().getSellerId().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You do not have access");
        }
        auction.setStatus(AuctionStatus.Cancel);

        auctionRepository.save(auction);
    }

    @Override
    public List<ListBidForAuction> getBidsByAuctionId(Long auctionId) {
        List<BiddingEntity> biddingEntities = biddingRepository.findByAuctionId(auctionId);
        List<AutoBiddingEntity> autoBiddingEntities = autoBiddingRepository.findByAuctionId(auctionId);

        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));

        List<ListBidForAuction> bids = new ArrayList<>();

        for (BiddingEntity bidding : biddingEntities) {
            ListBidForAuction dto = new ListBidForAuction();
            dto.setId(bidding.getId());
            dto.setAuctionId(bidding.getAuction().getId());
            dto.setUserName(bidding.getCustomer().getFull_name());
            dto.setBidAmount(bidding.getBidAmount());
            dto.setBidTime(bidding.getBidTime());
            dto.setStatus("BIDDING");
            bids.add(dto);
        }

        for (AutoBiddingEntity autoBidding : autoBiddingEntities) {
            if (autoBidding.getMaxBid() < auction.getCurrentPrice()) {
                ListBidForAuction dto = new ListBidForAuction();
                dto.setId(autoBidding.getId());
                dto.setAuctionId(autoBidding.getAuction().getId());
                dto.setUserName(autoBidding.getCustomer().getFull_name());
                dto.setBidAmount(autoBidding.getMaxBid());
                dto.setBidTime(autoBidding.getBidTime());
                dto.setStatus("AUTO_BIDDING");
                bids.add(dto);
            }
        }

        return bids.stream()
                .sorted((b1, b2) -> b1.getBidTime().compareTo(b2.getBidTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionResponse> getAuctionsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        List<BiddingEntity> biddingEntities = biddingRepository.findByCustomerId(user.getId());

        return biddingEntities.stream()
                .map(BiddingEntity::getAuction)
                .filter(auction -> auction.getStatus() == AuctionStatus.InProgress)
                .distinct()
                .map(auction -> {
                    AuctionResponse dto = new AuctionResponse();
                    dto.setId(auction.getId());
                    dto.setJewelryName(auction.getJewelry().getName());
                    dto.setStartTime(auction.getStartTime());
                    dto.setEndTime(auction.getEndTime());
                    dto.setCurrentPrice(auction.getCurrentPrice());
                    dto.setStatus(auction.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionEntity> getAuctionsWin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return auctionRepository.findByWinnerAndStatus(user, AuctionStatus.Completed);
    }

    private void validateAuctionDuration(Date startTime, Date endTime) {
        if (startTime.after(endTime)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time");
        }

        Instant startInstant = startTime.toInstant();
        Instant endInstant = endTime.toInstant();

        Duration duration = Duration.between(startInstant, endInstant);

        if (duration.toDays() < 1 || duration.toDays() > 7) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Auction duration must be between 1 and 7 days");
        }
    }

    private boolean isStartTimeValid(Date startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = startTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return start.isAfter(now.plusMinutes(2));
    }

    @Override
    public int countUniqueBidders(Long auctionId) {
        List<Long> bidderIds = biddingRepository.findDistinctBiddersByAuctionId(auctionId);
        return bidderIds.size();
    }

    @Override
    public void comfirmAuctionForSeller(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));

        if (auction.getStatus() != AuctionStatus.WaitingConfirm) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Auction is not waiting confirm.");
        }
        if (!auction.getJewelry().getSellerId().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You do not have access");
        }
        auction.setStatus(AuctionStatus.Completed);

        auctionRepository.save(auction);
    }

    @Override
    public AuctionEntity reAuction(long id, UpdateTimeAuctionRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        AuctionEntity auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This auction is not existed!"));
        if (!auction.getJewelry().getSellerId().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You do not have access");
        }
        JewelryEntity jewelry = auction.getJewelry();

        validateAuctionDuration(request.getStartTime(), request.getEndTime());

        if (!isStartTimeValid(request.getStartTime())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu phải sau thời gian tạo ít nhất 2 phút.");
        }

        auction.setCurrentPrice(jewelry.getStaringPrice());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setStatus(AuctionStatus.Waiting);

        return auctionRepository.save(auction);
    }


}
