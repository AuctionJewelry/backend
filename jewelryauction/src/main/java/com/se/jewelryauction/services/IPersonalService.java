package com.se.jewelryauction.services;

import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import com.se.jewelryauction.responses.UserMeResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPersonalService {
    UserMeResponse getPersonalInformation();
    UserEntity updatePersonalInformation(PersonalUpdateRequest update);
    UserEntity uploadAvatar(MultipartFile imageFile) throws IOException;
}
