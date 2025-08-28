package com.welcome.Ecommerce.Repo;

import com.welcome.Ecommerce.Model.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepo extends JpaRepository<SignUp, Integer> {

    @Query("SELECT l.email FROM SignUp l")
    List<String> findAllEmailIds();

    @Query("SELECT l.password FROM SignUp l WHERE l.email = :email")
    String findPasswordByEmailId(String email);

    @Modifying
    @Query("Update SignUp SET password = :updatedPassword where email = :email")
    int updatePasswordByEmailId(String email, String updatedPassword);
}
