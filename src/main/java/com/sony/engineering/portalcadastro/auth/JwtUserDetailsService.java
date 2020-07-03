package com.sony.engineering.portalcadastro.auth;

import com.sony.engineering.portalcadastro.model.JwtUserDetails;
import com.sony.engineering.portalcadastro.repository.GenericDao;
import com.sony.engineering.portalcadastro.repository.JwtUserDetailsDao;
import com.sony.engineering.portalcadastro.service.GenericServiceImpl;
import com.sony.engineering.portalcadastro.service.MailService;
import com.sony.engineering.portalcadastro.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class JwtUserDetailsService extends GenericServiceImpl<JwtUserDetails> implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

    private UserService userService;
    private PasswordEncoder bcryptEncoder;
    private JwtUserDetailsDao jwtUserDetailsDao;
    private MailService mailService;

    public JwtUserDetailsService(GenericDao<JwtUserDetails> dao) {
        super(dao);
    }

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            JwtUserDetails user = jwtUserDetailsDao.findDistinctByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;

    }

    public void resetPassword(Integer id) {
        JwtUserDetails user = findById(id);
        String password = generateRandomString();
        user.setPassword(password);

        try {
            mailService.sendMailReset(user);
        } catch (MessagingException | GeneralSecurityException | IOException e) {

            throw new RuntimeException(e);
        }

        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        jwtUserDetailsDao.save(user);
    }

    @Override
    public JwtUserDetails findById(Integer id){

        return jwtUserDetailsDao.findById(id).<NoSuchElementException>orElseThrow(() -> {
            logger.error("Invalid UserDetail Id!");
            throw new NoSuchElementException();
        });
    }

    public List<JwtUserDetails> getUsers(){

        return jwtUserDetailsDao.findAll();
    }

    public JwtUserDetails findOneUser(Integer id){
        return jwtUserDetailsDao.findById(id).<NoSuchElementException>orElseThrow(() -> {
            logger.error("Invalid UserDetail Id!");
            throw new NoSuchElementException();
        });
    }

    private String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public List<JwtUserDetails> findDistinctByProfileNot(String profile) {
        return jwtUserDetailsDao.findDistinctByProfileNot(profile);
    }

    public JwtUserDetails save(JwtUserDetails user){
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return jwtUserDetailsDao.save(user);
    }

    public JwtUserDetails patch(JwtUserDetails user){
        JwtUserDetails userDb = jwtUserDetailsDao.findById(user.getId()).<NoSuchElementException>orElseThrow(() ->
                new NoSuchElementException("Invalid UserDetails Id!")
        );

        merge(user, userDb);

        if (user.getPassword() == null) return jwtUserDetailsDao.save(userDb);
        return this.save(userDb);

    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBcryptEncoder(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    @Autowired
    public void setJwtUserDetailsDao(JwtUserDetailsDao jwtUserDetailsDao) {
        this.jwtUserDetailsDao = jwtUserDetailsDao;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
