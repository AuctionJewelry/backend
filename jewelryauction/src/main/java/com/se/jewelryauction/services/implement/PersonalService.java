package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.constants.ImageContants;
import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.mappers.UserMapper;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.repositories.IUserRepository;
import com.se.jewelryauction.repositories.IWalletRepository;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import com.se.jewelryauction.responses.UserMeResponse;
import com.se.jewelryauction.services.IPersonalService;
import com.se.jewelryauction.utils.UploadImagesUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
@RequiredArgsConstructor
public class PersonalService implements IPersonalService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IWalletRepository walletRepository;
    @Override
    public UserMeResponse getPersonalInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        // Find the wallet information for the user
        WalletEntity wallet = walletRepository.findByUser(user);

        UserMeResponse response = new UserMeResponse();
        response.setUser(user);
        if (wallet != null) {
            response.setMoney(wallet.getMoney());
        } else {
            response.setMoney(0); // Default to 0 if no wallet found
        }

        return response;
    }

    @Override
    public UserEntity updatePersonalInformation(PersonalUpdateRequest update) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();


        if(update.getCurrentPassword() != null && user.getPassword() != null) {
            if (passwordEncoder.matches(update.getCurrentPassword(), user.getPassword())) {
                if (update.getPassword().equals(update.getConfirmedPassword())) {
                    if (update.getPassword() != null) {
                        update.setPassword(passwordEncoder.encode(update.getPassword()));
                    }
                } else {
                    throw new AppException(HttpStatus.BAD_REQUEST, "Confirmed password is wrong!!");
                }
            } else {
                throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password!!");
            }
        }
        UserMapper.INSTANCE.updatePersonalFromRequest(update, user);
        return userRepository.save(user);
    }

    @Override
    public UserEntity uploadAvatar(MultipartFile imageFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        user.setImageUrl(UploadImagesUtils.storeFile(imageFile, ImageContants.USER_IMAGE_PATH));
        return userRepository.save(user);
    }
}
