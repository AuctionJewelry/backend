package com.se.jewelryauction.services;

import com.se.jewelryauction.requests.UserForgotPasswordRequest;
import com.se.jewelryauction.requests.UserSignInRequest;
import com.se.jewelryauction.requests.UserSignUpRequest;
import com.se.jewelryauction.responses.SignInResponse;

public interface IUserService {
    void signUp(UserSignUpRequest request);

    void verify(Long userId,String token);

    SignInResponse signIn(UserSignInRequest request);

    String refresh(String token);
    void setPassword(Long userId, String token, UserForgotPasswordRequest request);

    void sendMailForgotPassword(String email);
}
