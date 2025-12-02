package fullstack.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private Integer rut;
    private List<String> roles;

    public JwtResponse(String accessToken, String email, Integer rut, List<String> roles) {
        this.token = accessToken;
        this.email = email;
        this.rut = rut;
        this.roles = roles;
    }
}
