package com.cinenexus.backend.service;

import com.cinenexus.backend.dto.user.UserRequestDTO;
import com.cinenexus.backend.dto.user.UserResponseDTO;
import com.cinenexus.backend.dto.user.UserMapper;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public UserResponseDTO findById(Long id) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    return userMapper.toDTO(user);
  }

  public Page<UserResponseDTO> findAll(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return userRepository.findAll(pageable).map(userMapper::toDTO);
  }

  @Transactional
  public UserResponseDTO createUser(@Valid UserRequestDTO request) {
    User user = userMapper.toEntity(request);
    user = userRepository.save(user);
    return userMapper.toDTO(user);
  }

  @Transactional
  public UserResponseDTO updateUser(Long id, @Valid UserRequestDTO request) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    user = userMapper.updateUser(user, request);
    user = userRepository.save(user);
    return userMapper.toDTO(user);
  }

  @Transactional
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
