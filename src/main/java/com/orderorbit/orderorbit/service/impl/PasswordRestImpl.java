package com.orderorbit.orderorbit.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.exception.AuthorizationException;
import com.orderorbit.orderorbit.exception.DuplicateResourceException;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.repository.CustomerRepository;
import com.orderorbit.orderorbit.repository.RestaurantRepository;
import com.orderorbit.orderorbit.service.PasswordRest;
import com.orderorbit.orderorbit.utility.JwtTokenUtil;
import com.orderorbit.orderorbit.utility.Role;

@Service
public class PasswordRestImpl implements PasswordRest
{
    
    @Autowired
    private EmailService emailService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
     @Autowired
    JwtTokenUtil tokenObj;
   
    
    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

private Map<String, String> otpMap = new HashMap<>(); 
@Override
public void sendOTP(String email) {
    String otp = generateOTP();
        otpMap.put(email, otp);
        String subject = "Password Reset OTP";
        String body = "Your OTP for password reset is: " + otp;
        emailService.sendEmail(email, subject, body);
}

@Override
public ResponseStatus verifyOTPCustomer(String email, String otp) {
    ResponseStatus response = new ResponseStatus();
    if (otp.equals(otpMap.get(email))) {
        String token = tokenObj.generateToken(email, Role.CUSTOMER);
        response.setToken(token);
        response.setMessage("OTP verified successfully");
    } else {
        response.setMessage("Invalid OTP");
    }
    return response;
}

@Override
public void resetPasswordCustomer(String token, Customer customer) {
    if(tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())){
        if (tokenObj.verifyToken(token)){

            Customer cus = customerRepository.findBycEmail(customer.getCEmail()).get();
            // cus.setCName(customer.getCName());
            cus.setCPassword(hashPassword(customer.getCPassword()));
            customerRepository.save(cus);
        }
          else{
                throw new AuthorizationException("Invalid token,");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Customers");
        }
   
    otpMap.remove(customer.getCEmail());
   
}

@Override
public String generateOTP() {
      Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
}

@Override
public ResponseStatus verifyOTPRestaurant(String email, String otp) {
   
    ResponseStatus response = new ResponseStatus();
    if (otp.equals(otpMap.get(email))) {
        String token = tokenObj.generateToken(email, Role.RESTAURANT);
        response.setToken(token);
        response.setMessage("OTP verified successfully");
    } else {
        response.setMessage("Invalid OTP");
    }
    return response;
}

@Override
public void resetPasswordRestaurant(String token, Restaurant restaurant) {
    if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
        if (tokenObj.verifyToken(token)){

           Restaurant res = restaurantRepository.findByrEmail(restaurant.getREmail()).get();
            res.setRPassword(hashPassword(restaurant.getRPassword()));
            restaurantRepository.save(res);
        }
          else{
                throw new AuthorizationException("Invalid token,");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Restaurent ");
        }
   
    

    otpMap.remove(restaurant.getREmail());
}

}