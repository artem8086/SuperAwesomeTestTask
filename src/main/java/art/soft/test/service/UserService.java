package art.soft.test.service;

import art.soft.test.dto.UserDTO;
import art.soft.test.security.JwtTokenProvider;
import art.soft.test.exception.CustomException;
import art.soft.test.model.*;
import art.soft.test.repository.PostRepository;
import art.soft.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtToken signin(UserDTO userDTO) {
        try {
            User user = userRepository.findByLogin(userDTO.getLogin());
            if (user == null) {
                throw new CustomException("User not found!", HttpStatus.NOT_FOUND);
            }
            if (!user.isActive()) {
                throw new CustomException("User is inactive!", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getLogin(), userDTO.getPassword()));
            return new JwtToken(jwtTokenProvider.createToken(userDTO.getLogin(), user.getRoles()));
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid login or password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public JwtToken confirm(String token) {
        try {
            User user = userRepository.findByToken(token);
            user.setToken(null);
            user.setActive(true);
            userRepository.save(user);
            return new JwtToken(jwtTokenProvider.createToken(user.getLogin(), user.getRoles()));
        } catch (Exception e) {
            throw new CustomException("Confirm link not valid!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private String genereteToken(User user) {
        return passwordEncoder.encode(user.getId() + ":" + UUID.randomUUID());
    }

    public String signup(UserDTO userDTO) {
        User user = new User(
            userDTO.getLogin(),
            userDTO.getEmail(),
            passwordEncoder.encode(userDTO.getPassword()),
            false);
        try {
            userRepository.insert(user);
            String token = genereteToken(user);
            user.setToken(token);
            userRepository.save(user);
            return token;
        } catch (DuplicateKeyException e) {
            throw new CustomException("Login or email already taken!", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            throw new CustomException("Can't create new user!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public List<UserInfo> getUsers() {
        return userRepository.findAll().stream().map(u -> new UserInfo(u)).collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User search(String userId) {
        try {
            return userRepository.findById(userId).get();
        } catch (NoSuchElementException e) {}
        User user = userRepository.findByLogin(userId);
        if (user != null) return user;
        throw new CustomException("User '" + userId + "' not found!", HttpStatus.NOT_FOUND);
    }

    public User whoami(HttpServletRequest req) {
        return userRepository.findByLogin(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public User subscribe(HttpServletRequest req, String userId) {
        User userCur = whoami(req);
        User userSub = search(userId);
        if (userCur.equals(userSub)) {
            throw new CustomException("You can't subscribe on yourself!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (userCur.getSubscribes().add(userSub)) {
            userRepository.save(userCur);
        } else {
            throw new CustomException("You already subscribe on '" + userSub.getLogin() + "'!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return userSub;
    }

    public User unsubscribe(HttpServletRequest req, String userId) {
        User userCur = whoami(req);
        User userSub = search(userId);
        if (userCur.equals(userSub)) {
            throw new CustomException("You can't subscribe on yourself!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (userCur.getSubscribes().remove(userSub)) {
            userRepository.save(userCur);
        } else {
            throw new CustomException("You already unsubscribe at '" + userSub.getLogin() + "'!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return userSub;
    }

    public User modify(User user, UserDTO userDTO) {
        if (userDTO.getLogin() != null) user.setLogin(userDTO.getLogin());
        if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (userDTO.getActive() != null) user.setActive(userDTO.getActive());
        return userRepository.save(user);
    }

    public User delete(User user) {
        postRepository.deleteAll(postRepository.findByOwner(user));
        userRepository.delete(user);
        userRepository.findAll().forEach(u -> {
            if (u.getSubscribes().remove(user)) {
                userRepository.save(u);
            }
        });
        return user;
    }
}
