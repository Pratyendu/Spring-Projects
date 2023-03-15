package com.mct.expensetracker.dao;

import com.mct.expensetracker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users , Integer> {
    @Query(value = "select * from tbl_user where username = :username" , nativeQuery = true)
    public Users findByUsername(String username);
    @Query(value = "select * from tbl_user where user_id = :userId" , nativeQuery = true)
    public List<Users> getUserByUserId(Integer userId);

    @Query(value = "select * from tbl_user" , countQuery = "Select count(*) from tbl_user" , nativeQuery = true)
    public List<Users> getAllUsers();
    @Modifying
    @Transactional
    @Query(value = "delete from tbl_user where user_id = :userId" ,
            countQuery = "Select count(*) from tbl_user" , nativeQuery = true)
    public void deleteUserByUserId(int userId);
}
