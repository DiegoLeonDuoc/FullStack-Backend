package fullstack.backend.service;

import fullstack.backend.model.User;
import fullstack.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        // Validate unique constraints
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        if (userRepository.existsByRut(user.getRut())) {
            throw new RuntimeException("RUT already exists: " + user.getRut());
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();

            // Check email uniqueness if changed
            if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
                    userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists: " + userDetails.getEmail());
            }

            // Check RUT uniqueness if changed
            if (!existingUser.getRut().equals(userDetails.getRut()) &&
                    userRepository.existsByRut(userDetails.getRut())) {
                throw new RuntimeException("RUT already exists: " + userDetails.getRut());
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
        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}