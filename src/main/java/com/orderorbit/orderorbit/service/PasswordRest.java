package com.orderorbit.orderorbit.service;

import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Restaurant;

public interface PasswordRest {
    void sendOTP(String email);
    ResponseStatus verifyOTPCustomer(String email, String otp);
    ResponseStatus verifyOTPRestaurant(String email, String otp);
    void resetPasswordCustomer(String token,Customer customer);
    void resetPasswordRestaurant(String token,Restaurant restaurant);
    String generateOTP();
}

