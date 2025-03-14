package project.mealPlan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import project.mealPlan.entity.User;
import project.mealPlan.service.UserService;

@RestController
@RequestMapping("/guest")
public class guestController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // Log the incoming user data
    //    System.out.println("Received user data: " + user);

        try {
            return userService.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            return userService.login(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
