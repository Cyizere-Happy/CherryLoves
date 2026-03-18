package com.unica.cherryLoves.controller;

import com.unica.cherryLoves.request.LoginRequest;
import com.unica.cherryLoves.response.ApiResponse;
import com.unica.cherryLoves.response.JwtResponse;
import com.unica.cherryLoves.security.jwt.JwtUtils;
import com.unica.cherryLoves.security.user.UserDetailsImpl;
import com.unica.cherryLoves.service.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtTokenForUser(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt, userDetails.getEmail(), roles);
            return ResponseEntity.ok(new ApiResponse("Login Success!", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid email or password", null));
        }
    }

    // Since I'm using CreateUserRequest from com.unica.cherryLoves.request
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody com.unica.cherryLoves.request.CreateUserRequest request) {
        try {
            com.unica.cherryLoves.models.User user = userService.createUser(request);
            com.unica.cherryLoves.dto.UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("Sign up Success!", userDto));
        } catch (com.unica.cherryLoves.exceptions.AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
