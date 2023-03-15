package com.mct.expensetracker.controller;

import com.mct.expensetracker.dao.UserRepository;
import com.mct.expensetracker.model.Users;
import com.mct.expensetracker.service.UserService;
import com.mct.expensetracker.utils.CommonUtils;
import org.apache.coyote.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("create-user")
    public ResponseEntity<String> createUser(@RequestBody String userData){
        JSONObject isValid = validateUserRequest(userData);
        Users user = null;
        int userId = 0;
        if(isValid.isEmpty()){
            user = setUser(userData);
            userId =  userService.saveUser(user);
        }else {
            return new ResponseEntity<String>(isValid.toString() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("User Saved with id : " + userId , HttpStatus.CREATED);
    }

    private Users setUser(String userData) {
        Users user = new Users();
        JSONObject userObj = new JSONObject(userData);

        user.setEmail(userObj.getString("email"));
        user.setUsername(userObj.getString("username"));
        user.setPassword(userObj.getString("password"));
        user.setFirstName(userObj.getString("firstName"));
        user.setLastName(userObj.getString("lastName"));
        user.setPhoneNumber(userObj.getString("phoneNumber"));
        if (userObj.has("gender")){
            user.setGender(userObj.getString("gender"));
        }

        Timestamp createdDate = new Timestamp(System.currentTimeMillis());
        user.setCreatedDate(createdDate);

        return user;
    }

    private JSONObject validateUserRequest(String userData) {
        JSONObject userJson = new JSONObject(userData);
        JSONObject errorList = new JSONObject();
        if (userJson.has("username")){
            String username = userJson.getString("username");
            Users user = userRepository.findByUsername(username);
            if (user != null){
                errorList.put("username" , "Already Exists!");
            }
        }else {
            errorList.put("username" , "Missing Parameter");
        }
        if (userJson.has("password")){
            String password = userJson.getString("password");
            if (!CommonUtils.isValidPassword(password)){
                errorList.put("password" , "Please enter valid password ): eg : Pratyendu@123");
            }
        }else {
            errorList.put("password" , "Missing Parameter");
        }

        if(!userJson.has("firstName")){
            errorList.put("firstName" , "Missing Parameter");
        }

        if(!userJson.has("lastName")){
            errorList.put("lastName" , "Missing Parameter");
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
        return errorList;
    }

    @GetMapping("get-users")
    public ResponseEntity<String> getUsers(@Nullable @RequestParam Integer userId){
        JSONArray usersArr = userService.getUsers(userId);
        return new ResponseEntity<String>("Found user(s)" + usersArr.toString() , HttpStatus.OK);
    }

    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody String requestData){
        JSONObject requestJson = new JSONObject(requestData);
        JSONObject isValidLogin = validateLogin(requestJson);
        if(isValidLogin.isEmpty()){
            String username = requestJson.getString("username");
            String password = requestJson.getString("password");
            JSONObject responseObj =  userService.login(username , password);
            if (responseObj.has("errorMessage")){
                return new ResponseEntity<String>(responseObj.toString() , HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<String>(responseObj.toString() , HttpStatus.OK);
            }
        }else{
            return  new ResponseEntity<String>(isValidLogin.toString() , HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete-user/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer userId){
        userService.deleteUserByUserId(userId);
        return new ResponseEntity<String>("Deleted User with userId: " + userId , HttpStatus.NO_CONTENT);
    }

    private JSONObject validateLogin(JSONObject requestJson) {
        JSONObject errorList = new JSONObject();

        if(!requestJson.has("username")){
            errorList.put("username" , "missing parameter");
        }
        if(!requestJson.has("password")){
            errorList.put("password" , "missing parameter");
        }

        return errorList;
    }

}
