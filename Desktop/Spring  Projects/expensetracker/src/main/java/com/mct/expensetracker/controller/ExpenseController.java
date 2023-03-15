package com.mct.expensetracker.controller;

import com.mct.expensetracker.dao.ExpenseRepository;
import com.mct.expensetracker.dao.UserRepository;
import com.mct.expensetracker.model.Expense;
import com.mct.expensetracker.model.Users;
import com.mct.expensetracker.service.ExpenseService;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v2/expenses")
public class ExpenseController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExpenseService expenseService;
    @Autowired
    ExpenseRepository expenseRepository;


    @PostMapping(value = "/create-expense")
    public ResponseEntity<String> createExpense(@RequestBody String expenseData){
        JSONObject errorList = validateRequest(expenseData);
        Expense expense = null;
        int expId = 0;
        if (errorList.isEmpty()){
             expense = setExpense(expenseData);
             expId = expenseService.saveExpense(expense);
        }else {
            return new ResponseEntity<String>(errorList.toString() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Expense saved with id: " + expId , HttpStatus.CREATED);
    }

    private Expense setExpense(String expenseData) {
        Expense expense = new Expense();
        JSONObject expenseObj = new JSONObject(expenseData);

        expense.setTitle(expenseObj.getString("title"));
        expense.setDescription(expenseObj.getString("description"));
        expense.setPrice(expenseObj.getInt("price"));

        int userId = expenseObj.getInt("user");
        Users user = userRepository.findById(userId).get();
        expense.setUser(user);

        Date date = new Date(System.currentTimeMillis());
        expense.setDate(date);
        Time time = new Time(System.currentTimeMillis());
        expense.setTime(time);

        return expense;

    }

    private JSONObject validateRequest(String expenseData) {
        JSONObject expenseObj = new JSONObject(expenseData);
        JSONObject errorObj = new JSONObject();

        if(!expenseObj.has("user")){
            errorObj.put("user" , "Missing Parameter");
        }

        if(!expenseObj.has("title")){
            errorObj.put("title" , "Missing Parameter");
        }

        if(!expenseObj.has("description")){
            errorObj.put("description" , "Missing Parameter");
        }

        if(!expenseObj.has("price")){
            errorObj.put("price" , "Missing Parameter");
        }
        return errorObj;
    }



    @GetMapping(value = "get-expenses-by-user-id")
    public List<Expense> getExpensesByUser(@RequestParam Integer userId){
        List<Expense> expenses = expenseRepository.getExpenseByUserId(userId);
        return expenses;
    }

    @GetMapping(value = "get-expenses-by-timespan")
    public List<Expense> getExpensesByTimeSpan(@RequestParam Integer userId ,  Date startDate ,Date endDate){
        List<Expense> expenses = expenseRepository.findByDateBetween(userId,startDate,endDate);
        return expenses;
    }

    @GetMapping(value = "get-expenses-on-particular-date-time")
    public List<Expense> getExpenseOnDateTime(@RequestParam Integer userId , Date date , @Nullable Time time){
        List<Expense> expenses = expenseService.findByDateTime(userId,date,time);
        return expenses;
    }


    @GetMapping(value = "get-total-expense-in-a-month")
    public ResponseEntity<String> getTotalExpenseInAMonth(@RequestParam Integer userId , Integer month){
        List<Expense> expenses = expenseRepository.findByMonth(userId,month);
        int total = 0;
        for(Expense expense : expenses){
            total+= expense.getPrice();
        }
        return new ResponseEntity<String>("The total expense for the timePeriod is : " + total , HttpStatus.OK);
    }

    @DeleteMapping(value = "delete-expense")
    public ResponseEntity<String> deleteExpense(@RequestParam Integer userId , Integer expId){
        JSONObject response =expenseService.deleteExpense(userId,expId);
        return new ResponseEntity<String>(response.toString() , HttpStatus.OK);
    }

    @PutMapping(value = "update-expense")
    public ResponseEntity<String> updateExpense(@RequestParam Integer expId , @RequestBody String newExpense){
        JSONObject newExpObj = new JSONObject(newExpense);
        Expense expense = expenseRepository.findById(expId).get();
        expense.setTitle(newExpObj.getString("title"));
        expense.setDescription(newExpObj.getString("description"));
        expense.setPrice(newExpObj.getInt("price"));
        expenseRepository.save(expense);
        return new ResponseEntity<String>("Expense updated with id: " + expId , HttpStatus.OK);

    }



}
