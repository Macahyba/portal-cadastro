package com.sony.engineering.portalcadastro.auth;

import com.sony.engineering.portalcadastro.model.User;
import com.sony.engineering.portalcadastro.repository.UserDao;
import com.sony.engineering.portalcadastro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;
    private PasswordEncoder bcryptEncoder;

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findDistinctByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new JwtUserDetails(user);

    }

    public User save(User user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userService.save(user);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBcryptEncoder(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

}
