package com.aida.usefy_lite.service;

import com.aida.usefy_lite.dto.UserRegistrationDto;
import com.aida.usefy_lite.model.User;

public interface UserService {

    User registerUser(UserRegistrationDto userData);

    User findByUsername(String username);
}
