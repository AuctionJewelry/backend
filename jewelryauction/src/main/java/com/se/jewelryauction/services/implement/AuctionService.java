package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IBrandRepository;
import com.se.jewelryauction.repositories.IJewelryRepository;
import com.se.jewelryauction.services.IAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AuctionService implements IAuctionService {
    private final IAuctionRepository auctionRepository;
    private final IJewelryRepository jewelryRepository;


    @Override
    public AuctionEntity createAuction(AuctionEntity auction) {
        JewelryEntity existingJewelry = jewelryRepository
                .findById(auction.getJewelry().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Jewelry", "id", auction.getJewelry().getId()));
        auction.setJewelry(existingJewelry);

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
}
