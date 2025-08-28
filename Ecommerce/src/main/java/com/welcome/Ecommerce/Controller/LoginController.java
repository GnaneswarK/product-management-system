package com.welcome.Ecommerce.Controller;

import com.welcome.Ecommerce.Model.*;
import com.welcome.Ecommerce.Service.LoginServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin
@RequestMapping("/api/ecommerce/")
public class LoginController
{
    @Autowired
    LoginServices loginServices;

    @PostMapping("login")
    public ResponseEntity<String> verifyLoginDetails(@RequestBody Login login){
        return loginServices.verifyLoginDetails(login);
    }

    @PostMapping("signup")
    public ResponseEntity<String> addCustomerDetails(@RequestBody SignUp signUp){
        return loginServices.addCustomerDetails(signUp);
    }

    @PostMapping("updatePassword")
    public ResponseEntity<Integer> updatePassword(@RequestBody UpdatePasscode updatePasscode){
        return loginServices.updatePassword(updatePasscode);
    }

    @PostMapping( "sendmail")
    public ResponseEntity<String> sendOtpViaMail(@RequestBody EmailId emailId){
        return loginServices.sendOtpViaMail(emailId);
    }

    @PostMapping("validate")
    public ResponseEntity<String> validateOtp(@RequestBody ValidateEmail validateEmail){
        return loginServices.validateOtp(validateEmail);
    }
    @GetMapping("getallemailids")
    public ResponseEntity<List<String>> getAllEmailIDs(){
        return loginServices.getAllEmailIDs();
    }
}
