package com.welcome.Ecommerce.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasscode
{
    private String email;
    private String password;
    private String updatedPassword;
}
