package com.example.cs202pz.Interfaces;

import com.example.cs202pz.Requests.UserRequest;

public interface IUserService {
    void getUser(UserRequest request);
    int login(String pin);
}
