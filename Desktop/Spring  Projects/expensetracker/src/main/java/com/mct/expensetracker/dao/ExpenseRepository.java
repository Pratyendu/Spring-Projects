package com.mct.expensetracker.dao;

import com.mct.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense , Integer> {
    @Query(value = "select * from tbl_expense where user_id = :userId" , nativeQuery = true)
    public List<Expense> getExpenseByUserId(Integer userId);
    @Query(value = "select * from tbl_expense where user_id = :userId and date between :startDate and :endDate" , nativeQuery = true)
    public List<Expense> findByDateBetween(Integer userId , Date startDate, Date endDate);
    @Query(value = "select * from tbl_expense where user_id = :userId and date = :date"  , nativeQuery = true)
    public List<Expense> findByDate(Integer userId, Date date);

    @Query(value = "select * from tbl_expense where user_id = :userId and date = :date and time = :time"  , nativeQuery = true)
    public List<Expense> findByDateAndTime(Integer userId, Date date, Time time);
    @Query(value = "select * from tbl_expense where user_id = :userId and MONTH(date) = :month" , nativeQuery = true)
    public List<Expense> findByMonth(Integer userId, Integer month);
}
