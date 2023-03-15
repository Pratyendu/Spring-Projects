package com.mct.expensetracker.service;

import com.mct.expensetracker.dao.UserRepository;
import com.mct.expensetracker.model.Users;
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
        userRepository.save(user);
        return user.getUserId();
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
        jsonObj.put("email" , user.getEmail());
        jsonObj.put("phoneNumber" , user.getPhoneNumber());
        jsonObj.put("gender" , user.getGender());
        jsonObj.put("createdDate" , user.getCreatedDate());

        return jsonObj;
    }


    public JSONObject login(String username, String password) {
        JSONObject response = new JSONObject();
        Users user = userRepository.findByUsername(username);
        if(user == null){
            response.put("errorMessage :" , "Username doesn't exist ! ");
            return response;
        }else {
            if (password.equals(user.getPassword())){
                response = createResponse(user);
            }else{
                response.put("errorMessage :" , "Invalid Password!!");
            }
        }
        return response;
    }

    public void deleteUserByUserId(Integer userId) {
        userRepository.deleteById(userId);
    }
}
