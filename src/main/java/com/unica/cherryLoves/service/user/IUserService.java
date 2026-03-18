package com.unica.cherryLoves.service.user;

import com.unica.cherryLoves.models.User;
import com.unica.cherryLoves.request.CreateUserRequest;
import com.unica.cherryLoves.request.UserUpdateRequest;
import com.unica.cherryLoves.dto.UserDto;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToDto(User user);
}
