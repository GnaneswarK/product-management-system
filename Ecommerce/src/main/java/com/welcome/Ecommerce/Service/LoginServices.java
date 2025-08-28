package com.welcome.Ecommerce.Service;
import com.welcome.Ecommerce.Model.*;
import com.welcome.Ecommerce.Repo.LoginRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LoginServices {


    private static final Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,12}$");

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String,Integer> oneTimePassword= new HashMap<>();

    public ResponseEntity<String> verifyLoginDetails(Login login) {

        if (login != null) {

            if (login.getEmail() == null || login.getEmail().isEmpty())
                return new ResponseEntity<>("Email mandatory", HttpStatus.BAD_REQUEST);

            List<String> emailIds = loginRepo.findAllEmailIds();
            boolean isEmailIdAlreadyPresent = emailIds.contains(login.getEmail());

            if (!isEmailIdAlreadyPresent)
                return new ResponseEntity<>("user not found please signup", HttpStatus.BAD_REQUEST);

            String encodedPassword = loginRepo.findPasswordByEmailId(login.getEmail());

            boolean validatePassword = passwordEncoder.matches(login.getPassword(), encodedPassword);

            if (!validatePassword)
                return new ResponseEntity<>("password is not valid please check", HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>("Login Successful", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("null or empty details detected please fill valid details", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> addCustomerDetails(SignUp signUp) {

        if (signUp != null) {

            if (signUp.getEmail() == null || signUp.getEmail().isEmpty())
                return new ResponseEntity<>("Email mandatory", HttpStatus.BAD_REQUEST);

            List<String> emailIds = getAllEmailIDs().getBody();
            assert emailIds != null;
            boolean isEmailIdAlreadyPresent = emailIds.contains(signUp.getEmail());

            if (isEmailIdAlreadyPresent)
                return new ResponseEntity<>("user already exits please login", HttpStatus.BAD_REQUEST);

            if (signUp.getPassword() == null || signUp.getPassword().isEmpty())
                return new ResponseEntity<>("password is mandatory", HttpStatus.BAD_REQUEST);

            if (!isPasswordValid(signUp.getPassword()))
                return new ResponseEntity<>("password must contains one special character,number,capital and small letter minimum 6 charecters", HttpStatus.BAD_REQUEST);

            if ((signUp.getFirstName() == null || signUp.getLastName() == null) || (signUp.getFirstName().isEmpty() || signUp.getLastName().isEmpty()))
                return new ResponseEntity<>("first name or last name is mandatory", HttpStatus.BAD_REQUEST);

            SignUp addNewCustomer = new SignUp(
                    signUp.getFirstName(),
                    signUp.getLastName(),
                    signUp.getEmail(),
                    this.passwordEncoder.encode(signUp.getPassword())
            );
            loginRepo.save(addNewCustomer);
            return new ResponseEntity<>("successfully added customer field", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("All fields are mandatory", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean isPasswordValid(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public ResponseEntity<List<String>> getAllEmailIDs() {
        return new ResponseEntity<>(loginRepo.findAllEmailIds(),HttpStatus.OK);
    }

    //Need to add Email Validation send email and verify otp;
    @Transactional
    public ResponseEntity<Integer> updatePassword(UpdatePasscode updatePasscode) {
        if (updatePasscode != null) {

            if (updatePasscode.getEmail() == null || updatePasscode.getEmail().isEmpty())
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);

            List<String> emailIds = getAllEmailIDs().getBody();
            assert emailIds != null;
            boolean isEmailIdAlreadyPresent = emailIds.contains(updatePasscode.getEmail());


            if (!isEmailIdAlreadyPresent)
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);

            int numOfRowsAffected = loginRepo.updatePasswordByEmailId(updatePasscode.getEmail(), passwordEncoder.encode(updatePasscode.getUpdatedPassword()));

            if(numOfRowsAffected==1)
                return new ResponseEntity<>(numOfRowsAffected, HttpStatus.OK);
            else if(numOfRowsAffected==0)
                return new ResponseEntity<>(numOfRowsAffected, HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(numOfRowsAffected, HttpStatus.BAD_REQUEST);
        }else
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> sendOtpViaMail(EmailId email) {
        oneTimePassword.put(email.getEmail(),generateOtp());
        try {
            sendOtp(email.getEmail(), oneTimePassword.get(email.getEmail()));
            return new ResponseEntity<>("mail sent successfully",HttpStatus.OK);
        }catch (MessagingException me){
            return new ResponseEntity<>("mail not sent \n"+me,HttpStatus.BAD_REQUEST);
        }
    }

    private void sendOtp(String email, int oneTimePassword) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email.trim());
        mimeMessageHelper.setSubject("Otp ");
        mimeMessageHelper.setText("Your one time password "+oneTimePassword);
        javaMailSender.send(mimeMessage);
    }

    private int generateOtp() {
        SecureRandom random = new SecureRandom();
        return 100000+random.nextInt(900000);
    }

    public ResponseEntity<String> validateOtp(ValidateEmail validateEmail)
    {
        if(this.oneTimePassword.get(validateEmail.getEmail())==validateEmail.getOtp())
            return new ResponseEntity<>("otp valid",HttpStatus.OK);
        else
            return new ResponseEntity<>("invalid otp",HttpStatus.BAD_REQUEST);
    }
}
