package com.unica.cherryLoves.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private Long id;
    private String token;
    private String type = "Bearer";
    private String email;
    private List<String> roles;

    public JwtResponse(Long id, String token, String email, List<String> roles) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.roles = roles;
    }
}
