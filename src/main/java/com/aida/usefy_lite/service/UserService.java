package com.aida.usefy_lite.service;

import com.aida.usefy_lite.dto.RegistrationRequest;
import com.aida.usefy_lite.model.User;

public interface UserService {

    User registerUser(RegistrationRequest userData) throws IllegalArgumentException;

    User findByUsername(String username);
}
