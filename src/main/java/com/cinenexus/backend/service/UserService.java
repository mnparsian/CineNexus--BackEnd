package com.cinenexus.backend.service;

import com.cinenexus.backend.configuration.CloudinaryConfig;
import com.cinenexus.backend.dto.user.*;
import com.cinenexus.backend.enumeration.RoleType;
import com.cinenexus.backend.enumeration.UserStatusType;
import com.cinenexus.backend.model.user.Role;
import com.cinenexus.backend.model.user.User;
import com.cinenexus.backend.model.user.UserStatus;
import com.cinenexus.backend.repository.RoleRepository;
import com.cinenexus.backend.repository.UserRepository;
import com.cinenexus.backend.repository.UserStatusRepository;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final CloudinaryConfig cloudinaryConfig;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final UserStatusRepository userStatusRepository;

  public UserService(
      UserRepository userRepository,
      UserMapper userMapper,
      CloudinaryConfig cloudinaryConfig,
      PasswordEncoder passwordEncoder,
      RoleRepository roleRepository,
      UserStatusRepository userStatusRepository) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.cloudinaryConfig = cloudinaryConfig;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.userStatusRepository = userStatusRepository;
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
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new DuplicateKeyException("Username already exists");
    }
    user = userRepository.save(user);
    return userMapper.toDTO(user);
  }

  @Transactional
  public UserResponseDTO createUserByAdmin(UserAdminRequestDTO request) {
    User user = userMapper.AdminRequestDTOToUser(request);
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new DuplicateKeyException("Username already exists");
    }
    user = userRepository.save(user);
    return userMapper.toDTO(user);
  }

  @Transactional
  public UserResponseDTO updateUser(Long id, @Valid UserUpdateRequestDTO request) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    user = userMapper.updateUser(user, request);
    user = userRepository.save(user);
    return userMapper.toDTO(user);
  }

  @Transactional
  public boolean changeUserPassword(UserChangePasswordDTO dto) {
    User user =
        userRepository
            .findById(dto.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
    if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
      throw new IllegalArgumentException("The current password is incorrect");
    }
    if (!Objects.equals(dto.getNewPassword(), dto.getConfirmNewPassword())) {
      throw new IllegalArgumentException("new Password and confirm password are not equal");
    }
    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    return true;
  }

  @Transactional
  public boolean updateUserByAdmin(Long userId, UserAdminUpdateRequestDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
    if (request.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    if (request.getRole() != null) {
      RoleType roleType;
      try {
        roleType = RoleType.valueOf(request.getRole().toUpperCase()); // تبدیل String به Enum
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid role: " + request.getRole());
      }

      Role role =
          roleRepository
              .findByName(roleType)
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "The Role with this name " + request.getRole() + " not found"));
      user.setRole(role);
    }
    if (request.getStatus() != null) {

      UserStatusType userStatusType;
      try {
        userStatusType = UserStatusType.valueOf(request.getStatus().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid User Status: " + request.getStatus());
      }
      UserStatus userStatus =
          userStatusRepository
              .findByName(userStatusType)
              .orElseThrow(() -> new RuntimeException("Status Not Found!"));
      user.setStatus(userStatus);
    }
    return true;
  }

  @Transactional
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Transactional
  public UserResponseDTO uploadImage(Long userId, MultipartFile file) throws IOException {

    Map uploadResult =
        cloudinaryConfig.uploader().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    String imageUrl = (String) uploadResult.get("url");

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    user.setProfileImage(imageUrl);
    return userMapper.toDTO(user);
  }

  public UserResponseDTO getCurrentUser(UserDetails userDetails) {
    User user =
        userRepository
            .findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    return userMapper.toDTO(user);
  }

  public UserProfileDTO getUserProfileById(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("user Not Found"));
    return userMapper.toProfileDTO(user);
  }

  public List<UserResponseDTO> searchUsers(String query) {
    List<User> userList =
        userRepository
            .findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(
                query, query, query);
    return userList.stream().map(userMapper::toDTO).toList();
  }
}
