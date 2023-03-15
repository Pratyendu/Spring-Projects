package com.mct.expensetracker.service;

import com.mct.expensetracker.dao.ExpenseRepository;
import com.mct.expensetracker.model.Expense;
import com.mct.expensetracker.model.Users;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;
    public int saveExpense(Expense expense) {
        expenseRepository.save(expense);
        return expense.getExpId();
    }

    public List<Expense> findByDateTime(Integer userId, Date date, Time time) {
        if (time == null){
            return expenseRepository.findByDate(userId,date);
        }
        return expenseRepository.findByDateAndTime(userId,date,time);
    }

    public JSONObject deleteExpense(Integer userId, Integer expId) {
        List<Expense> expenses = expenseRepository.getExpenseByUserId(userId);
        JSONObject error = new JSONObject();
        if(expenses.isEmpty()){
            error.put("userId" , "No records found!");
            return error;
        }
        expenseRepository.deleteById(expId);
        return error.put("expense found" ,"Expense Deleted ");
    }
}
