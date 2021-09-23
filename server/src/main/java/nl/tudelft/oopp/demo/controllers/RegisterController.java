package nl.tudelft.oopp.demo.controllers;

import java.io.UnsupportedEncodingException;

import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.encodehash.Hashing;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepo;

    @Value("${encryption.secretKey}")
    private String secretKey;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Puts a new user into the database. /n
     * UserType will be student.
     *
     * @param username User-provided username (must be unique from existing ones).
     * @param password User-provided password.
     * @return Returns "nice" if everything works.
     *         Returns "This username already exists!" if the username was already taken.
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @PostMapping("register")
    @ResponseBody
    public boolean register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam int userType) throws UnsupportedEncodingException {
        username = CommunicationMethods.decodeCommunication(username);
        password = CommunicationMethods.decodeCommunication(password);

        String encryptedPassword = Hashing.hashIt(password);

        try {
            if (userRepo.getUser(username) == null) {
                userRepo.insertUser(username, encryptedPassword, userType);
                logger.info("Register: Account created for username '" + username + "'");
                return true;
            }
            logger.warn("Register: Account with the username '" + username + "' already exists");
            return false;
        } catch (Exception e) {
            logger.error("Register: ERROR", e);
            return false;
        }
    }
}
