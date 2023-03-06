package com.project.chatApplication.dao;

import com.project.chatApplication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<Users , Integer> {

    @Query(value = "Select * from tbl_user where username = :username and status id = 1" , nativeQuery = true)
    public Users findByUsername(String username);

    @Query(value = "select * from tbl_user where user_id = :userId and status_id = 1" , nativeQuery = true)
    public List<Users> getUserByUserId(int userId);

    @Query(value = "select * from tbl_user where status_id = 1" , nativeQuery = true)
    public List<Users> getAllUsers();

    @Modifying
    @Transactional
    @Query(value = "update tbl_user set status_id = 2 where user_id = :userId" ,
           countQuery = "Select count(*) from tbl_user" , nativeQuery = true)
    public List<Users> deleteUserByUserId(int userId);
}
