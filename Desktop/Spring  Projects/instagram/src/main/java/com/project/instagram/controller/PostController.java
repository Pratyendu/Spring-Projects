package com.project.instagram.controller;

import com.project.instagram.dao.UserRepository;
import com.project.instagram.model.Post;
import com.project.instagram.model.Users;
import com.project.instagram.service.PostService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("api/v1/post")
public class PostController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostService service;
    @PostMapping(value = "/create-post")
    public ResponseEntity<String> savePost(@RequestBody String postRequest) {

        Post post = setPost(postRequest);
        int postId = service.savePost(post);
        return new ResponseEntity<String>(String.valueOf(postId), HttpStatus.CREATED);
    }

    @GetMapping(value = "/find-post")
    public ResponseEntity<String> getPost(@RequestParam String userId, @Nullable @RequestParam String postId) {

        JSONArray postArr = service.getPost(Integer.valueOf(userId), postId);
        return new ResponseEntity<String>(postArr.toString(), HttpStatus.OK);
    }

    @PutMapping(value = "/update-post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable String postId, @RequestBody String postRequest) {

        Post post = setPost(postRequest);
        service.updatePost(postId, post);
        return  new ResponseEntity<>("Post updated", HttpStatus.OK);

    }

    private Post setPost(String postRequest) {
        JSONObject jsonObject = new JSONObject(postRequest);
        Users user = null;
        int userId = jsonObject.getInt("userId");
        if(userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        } else {
            return null;
        }
        Post post = new Post();
        post.setUser(user);
        post.setPostData(jsonObject.getString("postData"));
        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        post.setCreatedDate(createdTime);
        return post;


    }

}
