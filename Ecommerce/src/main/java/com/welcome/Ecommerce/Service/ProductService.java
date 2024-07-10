package com.welcome.Ecommerce.Service;

import com.welcome.Ecommerce.Model.Product;
import com.welcome.Ecommerce.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService
{
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts()
    {
       return productRepo.findAll();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return productRepo.save(product);
    }

    public Product UpdateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
         if(product.getId()==id) {
             product.setImageType(imageFile.getContentType());
             product.setImageData(imageFile.getBytes());
             product.setImageName(imageFile.getOriginalFilename());
             return productRepo.save(product);
         }
         else{
             return product;
         }
    }

    public void DeleteProduct(int id)
    {
        productRepo.deleteById(id);
    }

    public List<Product> SearchKeyword(String keyword)
    {
        return productRepo.SearchKeyword(keyword);
    }
}
