package com.welcome.Ecommerce.Controller;

import com.welcome.Ecommerce.Model.Product;
import com.welcome.Ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController
{
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product product = productService.getProductById(id);
        if(product!=null)
            return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try {
            Product addProduct1 = productService.addProduct(product,imageFile);
            return new ResponseEntity<>(addProduct1,HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int productId){
        Product product = productService.getProductById(productId);
        byte[] image = product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(image);
    }
    @PutMapping("product/{id}")
    public ResponseEntity<String> UpdateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
        Product p = null;
        try {
            p = productService.UpdateProduct(id,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
        }
        if(p != null)
            return new  ResponseEntity<>("updated",HttpStatus.OK);
        else
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("product/{id}")
    public ResponseEntity<String> DeleteProduct(@PathVariable int id){
        Product p = productService.getProductById(id);
        if(p!=null) {
            productService.DeleteProduct(id);
            return new ResponseEntity<>("Deleted product", HttpStatus.OK);
        }else
            return new ResponseEntity<>("failed to delete the product",HttpStatus.OK);
    }
    @GetMapping("products/{keyword}")
    public ResponseEntity<List<Product>> SearchKeyword(@RequestParam String keyword){
        List<Product> products = productService.SearchKeyword(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
