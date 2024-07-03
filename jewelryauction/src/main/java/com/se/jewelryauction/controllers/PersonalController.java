package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import com.se.jewelryauction.responses.UserMeResponse;
import com.se.jewelryauction.services.IPersonalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("${app.api.version.v1}/user")
@PreAuthorize("isAuthenticated() && hasAnyRole('MANAGER','STAFF','ADMIN','USER')")


public class PersonalController {
    private final IPersonalService personalService;
    @GetMapping("/me")
    public CoreApiResponse<UserMeResponse> getCurrentUser() {
        return CoreApiResponse.success(personalService.getPersonalInformation());
    }

    @PutMapping("")
    public CoreApiResponse<UserEntity> updateUser(
            @RequestBody PersonalUpdateRequest personalUpdateRequest
    ) {
        UserEntity user = personalService.updatePersonalInformation(personalUpdateRequest);
        return CoreApiResponse.success(user,"Update information successfully");
    }

    @PostMapping("/uploadavatar")
    public CoreApiResponse<UserEntity> uploadThumbnail(
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException
    {
        UserEntity user = personalService.uploadAvatar(imageFile);
        return CoreApiResponse.success(user ,"Avatar uploaded successfully.");
    }
}
