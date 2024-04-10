package com.example.users.controller;
import com.example.users.connectDB.ConnectDB;
import com.example.users.connectDB.CustomException;
import com.example.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
public class UserController {
//    ConnectDB connectDB = new ConnectDB();
    @Autowired
    private ConnectDB connectDB;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getUser(@RequestParam String login) throws CustomException {
        try{
            User user = connectDB.selectUserByLogin(login);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(CustomException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> userKey) {
        if ((userKey.size() != 3) || (!userKey.containsKey("login") || !userKey.containsKey("password")|| !userKey.containsKey("email")) ||
                isValidString(userKey.get("login"),userKey.get("password"),userKey.get("email")) || isValidEmpty(userKey.get("login"),userKey.get("password"),userKey.get("email"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            User user = new User(userKey.get("login").toString(), userKey.get("password").toString(), new java.sql.Date(new Date().getTime()), userKey.get("email").toString());
            int rowsAffected = connectDB.insertUser(user);
            return new ResponseEntity<>("{\nUser created: ok,\n"+"Rows affected: "+rowsAffected+"\n}", HttpStatus.CREATED);
        }
    }

    //строки ++
    private boolean isValidString(Object login, Object password, Object email) {
        return !(login instanceof String && password instanceof String && email instanceof String);
    }

    //пустое значение "" ++
    private boolean isValidEmpty(Object login, Object password, Object email) {
        return ((String)login).isEmpty() || ((String)password).isEmpty() || ((String)email).isEmpty();
    }

}

