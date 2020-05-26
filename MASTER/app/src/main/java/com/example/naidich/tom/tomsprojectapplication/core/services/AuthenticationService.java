package com.example.naidich.tom.tomsprojectapplication.core.services;
import com.example.naidich.tom.tomsprojectapplication.core.models.ApiResult;

import java.util.Dictionary;
import java.util.Hashtable;

public class AuthenticationService {
    private Dictionary<String,String> registeredUsers = new Hashtable<>();

    public AuthenticationService(){
        registeredUsers.put("user", "1234");
    }

    public ApiResult authenticateUser(final String username, final String password) throws IllegalArgumentException {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is either null or empty");
        }

        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("Password is either null or empty");
        }

        String resultUserPassword = registeredUsers.get(username);

        if(resultUserPassword == null)
            return new ApiResult(false,"Username was not found, please try again", null);

        if(!resultUserPassword.equals(password))
            return new ApiResult(false,"Password is incorrect, please try again", null);

        return new ApiResult(true,"", username);
    }
}
