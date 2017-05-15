package com.example.web;

import com.example.domain.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (StringUtils.isEmpty(user.getUsername())) {
            errors.rejectValue("username", "NotEmpty.userForm.username", "Username is required.");
        } else if (user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username", "The length of username should be less than or equal to 32.");
        } else if (userRepository.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username", "The username already exists.");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            errors.rejectValue("password", "NotEmpty.userForm.password", "Password is required.");
        } else if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "The length of password should between 8 and 32.");
        } else if (StringUtils.isEmpty(user.getPasswordConfirm())
                || !user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm", "The passwords does NOT match.");
        }
    }
}
