package com.project.chatApplication.controller;

import com.project.chatApplication.Utils.CommonUtils;
import com.project.chatApplication.dao.StatusRepository;
import com.project.chatApplication.dao.UserRepository;
import com.project.chatApplication.model.Status;
import com.project.chatApplication.model.Users;
import com.project.chatApplication.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody String userData){
        JSONObject isValid = validateUserRequest(userData);
        Users user = null;

        if(isValid.isEmpty()) {
             user = setUser(userData);
             userService.saveUser(user);
        }else{
            return new ResponseEntity<String>(isValid.toString() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Saved" , HttpStatus.CREATED);

    }
    private JSONObject validateUserRequest(String userData){
        JSONObject userJson  = new JSONObject(userData);
        JSONObject errorList = new JSONObject();

        if(userJson.has("username")){
            String username = userJson.getString("username");
            //TODO : find by JPA
            Users user = userRepository.findByUsername(username);
            if (user != null){
                errorList.put("username" , "Already Exists");
            }
        }else {
            errorList.put("username" , "Missing Parameter");
        }

        if(userJson.has("password")){
            String password = userJson.getString("password");
            if(!CommonUtils.isValidPassword(password)){
                errorList.put("password" , "Please enter valid password): eg: Pratyendu@123");
            }
        }else {
            errorList.put("password" , "Missing Parameter");
        }

        if(userJson.has("firstName")){
            String firstName = userJson.getString("firstName");
        }else {
            errorList.put("firstName" , "Missing Parameter");
        }

        if(userJson.has("email")){
            String email = userJson.getString("email");
            if(!CommonUtils.isValidEmail(email)){
                errorList.put("email" , "Please enter valid email! eg: abc@xyz.com");
            }
        }else {
            errorList.put("email" , "Missing Parameter");
        }

        if(userJson.has("phoneNumber")){
            String phoneNumber = userJson.getString("phoneNumber");
            if(!CommonUtils.isValidPhoneNumber(phoneNumber)){
                errorList.put("phoneNumber" , "Please enter valid phone number! eg: 916291666657");
            }
        }else {
            errorList.put("phoneNumber" , "Missing Parameter");
        }

        //NOT MANDATORY
//        if(userJson.has("age")){
//            String age = userJson.getString("age");
//        }else {
//            errorList.put("age" , "Missing Parameter");
//        }
//
//        if(userJson.has("lastName")){
//            String lastName = userJson.getString("lastName");
//        }else {
//            errorList.put("lastName" , "Missing Parameter");
//        }

        return errorList;
    }

    private Users setUser(String userData){
        Users user = new Users();
        JSONObject json = new JSONObject(userData);

        user.setEmail(json.getString("email"));
        user.setUsername(json.getString("username"));
        user.setPassword(json.getString("password"));
        user.setFirstName(json.getString("firstName"));
        user.setPhoneNumber(json.getString("phoneNumber"));

        if(json.has("age")){
            user.setAge(json.getInt("age"));
        }

        if(json.has("lastName")){
            user.setLastName(json.getString("lastName"));
        }

        if(json.has("gender")){
            user.setGender(json.getString("gender"));
        }

        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        user.setCreatedDate(createdTime);

        Status status = statusRepository.findById(1).get();
        user.setStatusId(status);

        return user;
    }

    @GetMapping(value = "/get-users")
    public ResponseEntity<String> getUsers(@Nullable @RequestParam String userId){
        JSONArray usersArr = userService.getUsers(Integer.valueOf(userId));
        return new ResponseEntity<String>("Found user" + usersArr.toString() , HttpStatus.OK);
    }

    @DeleteMapping("delete-user/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId){
        userService.deleteUserByUserId(userId);
        return new ResponseEntity<String>("Deleted User with userId: " + userId , HttpStatus.NO_CONTENT);
    }
}
