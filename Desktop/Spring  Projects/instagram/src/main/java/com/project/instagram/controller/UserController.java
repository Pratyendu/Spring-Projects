package com.project.instagram.controller;

import com.project.instagram.model.Users;
import com.project.instagram.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping(value = "/create-user")
    public ResponseEntity saveUser(@RequestBody String userData) {

        Users user = setUser(userData);
        int userId = service.saveUser(user);
        return new ResponseEntity("user saved with id- " +userId, HttpStatus.CREATED);

    }

    @GetMapping(value = "/get-user")
    public ResponseEntity<String> getUser(@Nullable @RequestParam String userId) {

        JSONArray userDetails = service.getUser(userId);
        return new ResponseEntity(userDetails.toString(), HttpStatus.OK);
    }



    @PutMapping(value = "/update-user/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody String userData) {

        Users user = setUser(userData);
        service.updateUser(user, userId);

        return new ResponseEntity("user updated", HttpStatus.OK);

    }


    private Users setUser(String userData) {

        JSONObject jsonObject = new JSONObject(userData);
        Users user = new Users();

        user.setAge(jsonObject.getInt("age"));
        user.setEmail(jsonObject.getString("email"));
        user.setFirstName(jsonObject.getString("firstName"));
        user.setLastName(jsonObject.getString("lastName"));
        user.setPhoneNumber(jsonObject.getString("phoneNumber"));


        return user;

    }
}
