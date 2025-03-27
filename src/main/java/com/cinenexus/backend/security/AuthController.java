package com.cinenexus.backend.security;

import com.cinenexus.backend.dto.user.UserMapper;
import com.cinenexus.backend.dto.user.UserRequestDTO;
import com.cinenexus.backend.dto.user.UserResponseDTO;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.UserRepository;
import com.cinenexus.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  public AuthController(
      UserRepository userRepository,
      JwtUtil jwtUtil,
      PasswordEncoder passwordEncoder,
      UserService userService) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }


  @PostMapping("/register")
  public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO user) {
    UserResponseDTO savedUser = userService.createUser(user);
    return ResponseEntity.ok(savedUser);
  }


  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
    Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
    if (userOptional.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
    }

    User user = userOptional.get();
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
    }

    String token = jwtUtil.generateToken(user);

    return ResponseEntity.ok(Map.of("token", token));
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserResponseDTO userDTO = userService.getCurrentUser(userDetails);
    return ResponseEntity.ok(userDTO);
  }
}
