package es.nextdigital.musicapp.infrastructure.adapter.in;

import es.nextdigital.musicapp.application.FindUserByIdService;
import es.nextdigital.musicapp.application.RegisterUserService;
import es.nextdigital.musicapp.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegisterUserService registerUserService;
    private final FindUserByIdService findUserByIdService;

    public UserController(
            RegisterUserService registerUserService,
            FindUserByIdService findUserByIdService
    ) {
        this.registerUserService = registerUserService;
        this.findUserByIdService = findUserByIdService;
    }

    @PostMapping
    public String registerUser(@RequestBody User user) {
        return registerUserService.registerUser(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        try {
            return ResponseEntity.accepted().body(findUserByIdService.getUserById(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
