package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.constants.ImageContants;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.models.RoleEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.repositories.IRoleRepository;
import com.se.jewelryauction.repositories.IUserRepository;
import com.se.jewelryauction.requests.UpdateUserRequest;
import com.se.jewelryauction.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;


    @Override
    public UserEntity createAccountStaff(UserEntity user) {

        user.setEmail_verified(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(ImageContants.DEFAULT_AVATAR);
        user.setIs_active(true);

       return userRepository.save(user);
    }

    @Override
    public UserEntity createAccountManager(UserEntity user) {
        user.setEmail_verified(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(ImageContants.DEFAULT_AVATAR);
        user.setIs_active(true);

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));
    }

    @Override
    public UserEntity updateUser(Long id, UpdateUserRequest update) {
        UserEntity existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));

        if(update.getRoleId() != null && Long.parseLong(update.getRoleId()) != existingUser.getRole_id().getId()){
            RoleEntity role = roleRepository
                    .findById(Long.parseLong(update.getRoleId()))
                    .orElseThrow(()
                            -> new DataNotFoundException("Role", "id", Long.parseLong(update.getRoleId())));

            existingUser.setRole_id(role);
        }

        if(update.getPassword() != null){
            update.setPassword(passwordEncoder.encode(update.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity banUser(Long id) {
        UserEntity existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));
        existingUser.setIs_active(false);
        return userRepository.save(existingUser);
    }
}
