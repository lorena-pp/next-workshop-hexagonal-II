package es.nextdigital.musicapp.infrastructure.adapter.in;

import es.nextdigital.musicapp.application.AddFavouriteSongService;
import es.nextdigital.musicapp.application.FindUserByIdService;
import es.nextdigital.musicapp.application.RegisterUserService;
import es.nextdigital.musicapp.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegisterUserService registerUserService;
    private final FindUserByIdService findUserByIdService;
    private final AddFavouriteSongService addFavouriteSongService;

    public UserController(
            RegisterUserService registerUserService,
            FindUserByIdService findUserByIdService,
            AddFavouriteSongService addFavouriteSongService
    ) {
        this.registerUserService = registerUserService;
        this.findUserByIdService = findUserByIdService;
        this.addFavouriteSongService = addFavouriteSongService;
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

    @PatchMapping("/{userId}/favorites/{songId}")
    public ResponseEntity<Void> addFavouriteSong(@PathVariable String userId, @PathVariable String songId) {
        addFavouriteSongService.addFavouriteSong(userId, songId);
        return ResponseEntity.noContent().build();
    }
}
