package com.project.instagram.service;

import com.project.instagram.dao.UserRepository;
import com.project.instagram.model.Users;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public int saveUser(Users user) {
        Users userObj = userRepository.save(user);
        return userObj.getUserId();
    }

    public JSONArray getUser(String userId) {

        JSONArray userArray = new JSONArray();

        if( null != userId && userRepository.findById(Integer.valueOf(userId)).isPresent()) {

            Users user = userRepository.findById(Integer.valueOf(userId)).get();
            JSONObject userObj = setUser(user);
            userArray.put(userObj);
        } else {
            List<Users> userList = userRepository.findAll();
            for (Users user: userList) {
                JSONObject userObj = setUser(user);
                userArray.put(userObj);
            }
        }
        return userArray;
    }


    private JSONObject setUser (Users user) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", user.getUserId());
        jsonObject.put("firstName", user.getFirstName());
        jsonObject.put("lastName", user.getLastName());
        jsonObject.put("age", user.getAge());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("phoneNumber", user.getPhoneNumber());

        return jsonObject;

    }

    public void updateUser(Users newUser, String userId) {

        if(userRepository.findById(Integer.valueOf(userId)).isPresent()) {
            Users user = userRepository.findById(Integer.valueOf(userId)).get();
            newUser.setUserId(user.getUserId());
            userRepository.save(newUser);
        }

    }
}
