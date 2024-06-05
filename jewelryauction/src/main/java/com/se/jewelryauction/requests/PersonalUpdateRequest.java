package com.se.jewelryauction.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalUpdateRequest {
    private String fullName;

    @Email(message = "Invalid email format")
    private String phoneNumber;

    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String currentPassword;

    private String password;

    private String confirmedPassword;
}
