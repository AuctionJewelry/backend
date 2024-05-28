package com.se.jewelryauction.requests;

import com.se.jewelryauction.components.validations.FieldMatch;
import com.se.jewelryauction.models.RoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "confirmPassword", second = "password", message = "Mật khẩu lần 2 không trùng khớp")
public class UserSignUpRequest {
    @NotEmpty(message = "Full Name is required")
    private String fullName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone must have 10 number")
    private String phone;

    @NotEmpty(message = "Address is required")
    private String address;

    private LocalDate dob;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;
}
