package com.project.chatApplication.service;

import com.project.chatApplication.dao.UserRepository;
import com.project.chatApplication.model.Users;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public int saveUser(Users user){
        Users userObj = userRepository.save(user);
        return userObj.getUserId();
    }

    public JSONArray getUsers(Integer userId) {
        JSONArray response = new JSONArray();
        if (userId != null){
            List<Users> usersList = userRepository.getUserByUserId(userId);
            for(Users user : usersList){
                JSONObject userObj = createResponse(user);
                response.put(userObj);
            }
        }else {
            List<Users> usersList = userRepository.getAllUsers();
            for (Users user : usersList){
                JSONObject userObj = createResponse(user);
                response.put(userObj);
            }
        }

        return response;
    }

    private JSONObject createResponse(Users user) {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("userId" , user.getUserId());
        jsonObj.put("username" , user.getUsername());
        jsonObj.put("firstName" , user.getFirstName());
        jsonObj.put("lastName" , user.getLastName());
        jsonObj.put("age" , user.getAge());
        jsonObj.put("email" , user.getEmail());
        jsonObj.put("phoneNumber" , user.getPhoneNumber());
        jsonObj.put("gender" , user.getGender());
        jsonObj.put("createdDate" , user.getCreatedDate());

        return jsonObj;
    }

    public void deleteUserByUserId(String userId) {
        userRepository.deleteUserByUserId(Integer.valueOf(userId));
    }
}

