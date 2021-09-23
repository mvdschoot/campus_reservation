package nl.tudelft.oopp.demo.controllers;

import java.io.UnsupportedEncodingException;

import nl.tudelft.oopp.demo.encodehash.CommunicationMethods;
import nl.tudelft.oopp.demo.encodehash.Hashing;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${encryption.secretKey}")
    private String secretKey;

    /**
     * Authenticates the user and retrieves the user type.
     *
     * @param username The userprovided username.
     * @param password The unencrypted userprovided password
     * @return Returns the user types when successful (admin, teacher, student).
     *         Returns "not_found" when username doesn't exist.
     *         Returns "wrong_password" when the password doesn't match.
     * @throws UnsupportedEncodingException Tells the user that they have used the wrong encoding.
     */
    @GetMapping("login")
    @ResponseBody
    public String getUser(@RequestParam String username,
                          @RequestParam String password) throws UnsupportedEncodingException {

        username = CommunicationMethods.decodeCommunication(username);
        password = CommunicationMethods.decodeCommunication(password);

        String hashedPassword = Hashing.hashIt(password);
        User user = userRepo.getUser(username);
        if (user == null) {
            logger.info("Login: '" + username + "' not found");
            return "not_found";
        } else if (!user.getPassword().equals(hashedPassword)) {
            logger.info("Login: '" + username + "' wrong password");
            return "wrong_password";
        } else if (user.getType() == 0) {
            logger.info("Login: '" + username + "' logged in as admin");
            return "admin";
        } else if (user.getType() == 1) {
            logger.info("Login: '" + username + "' logged in as teacher");
            return "teacher";
        } else if (user.getType() == 2) {
            logger.info("Login: '" + username + "' logged in as student");
            return "student";
        }
        logger.warn("Login: '" + username + "' login failed for unknown reason");
        return "error";
    }
}
