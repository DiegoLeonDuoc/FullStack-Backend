package fullstack.backend.service;

import fullstack.backend.exception.DomainValidationException;
import fullstack.backend.exception.ResourceNotFoundException;
import fullstack.backend.model.User;
import fullstack.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByRut(String rut) {
        return userRepository.findByRut(rut);
    }

    public User createUser(User user) {
        validateUser(user);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DomainValidationException("Email already exists: " + user.getEmail());
        }
        if (userRepository.existsByRut(user.getRut())) {
            throw new DomainValidationException("RUT already exists: " + user.getRut());
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        validateUser(userDetails);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new DomainValidationException("Email already exists: " + userDetails.getEmail());
        }

        if (!existingUser.getRut().equals(userDetails.getRut()) &&
                userRepository.existsByRut(userDetails.getRut())) {
            throw new DomainValidationException("RUT already exists: " + userDetails.getRut());
        }

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRut(userDetails.getRut());
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setAge(userDetails.getAge());
        existingUser.setRole(userDetails.getRole());
        existingUser.setIsActive(userDetails.getIsActive());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new DomainValidationException("User payload is required");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new DomainValidationException("Email is required");
        }
        if (!StringUtils.hasText(user.getRut())) {
            throw new DomainValidationException("RUT is required");
        }
        if (!StringUtils.hasText(user.getFirstName()) || !StringUtils.hasText(user.getLastName())) {
            throw new DomainValidationException("User full name is required");
        }
        if (!StringUtils.hasText(user.getPasswordHash())) {
            throw new DomainValidationException("Password hash is required");
        }
        if (!StringUtils.hasText(user.getRole())) {
            throw new DomainValidationException("Role is required");
        }
        if (user.getAge() != null && user.getAge() <= 0) {
            throw new DomainValidationException("Age must be greater than zero");
        }
    }
}