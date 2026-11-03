package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto create(CreateUserRequest request) {
        User user = UserMapper.mapToUser(request);
        if (isEmailOccupied(user)) {
            throw new ConflictException("User with email " + user.getEmail() + " already exist");
        }
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto read(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " does not exist"));
    }

    public UserDto update(Long id, UpdateUserRequest request) {
        User updatedUser = userRepository.findById(id)
                .map(user -> UserMapper.updateUser(user, request))
                .orElseThrow(() -> new NotFoundException("User with id " + id + " does not exist"));
        if (isEmailOccupied(updatedUser)) {
            throw new ConflictException("User with email " + updatedUser.getEmail() + " already exist");
        }
        updatedUser = userRepository.save(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private boolean isEmailOccupied(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            User currentUser = userRepository.findByEmail(user.getEmail()).get();
            return !currentUser.getId().equals(user.getId());
        }
        return false;
    }
}
