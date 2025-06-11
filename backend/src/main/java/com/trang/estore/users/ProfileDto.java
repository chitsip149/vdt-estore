package com.trang.estore.users;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDto {
    private Long id;
    private UserDto user;
    private String bio;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Integer loyaltyPoints;


}
