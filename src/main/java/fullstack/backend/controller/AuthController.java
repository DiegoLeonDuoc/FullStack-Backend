package fullstack.backend.controller;

import fullstack.backend.dto.JwtResponse;
import fullstack.backend.dto.LoginRequest;
import fullstack.backend.model.Usuario;
import fullstack.backend.repository.UsuarioRepository;
import fullstack.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        UsuarioRepository usuarioRepository;

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());

                // Fetch user to get RUT
                Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return ResponseEntity.ok(new JwtResponse(jwt,
                                userDetails.getUsername(),
                                usuario.getRut(),
                                roles));
        }
}
