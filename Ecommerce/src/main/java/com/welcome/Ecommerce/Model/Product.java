package com.welcome.Ecommerce.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Entity @Data @AllArgsConstructor @NoArgsConstructor @Component
public class Product
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal price;
    private int stockQuantity;
    private String name;
    private String description;
    private String brand;
    private String category;
    private boolean productAvailable;
    private Date releaseDate;
    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;
}
