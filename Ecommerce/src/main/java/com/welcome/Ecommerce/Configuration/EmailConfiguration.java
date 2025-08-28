package com.welcome.Ecommerce.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration
{
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String userName;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(Integer.parseInt(port));
        sender.setUsername(userName);
        sender.setPassword(password);

        Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable","true");
        return sender;
    }
}
