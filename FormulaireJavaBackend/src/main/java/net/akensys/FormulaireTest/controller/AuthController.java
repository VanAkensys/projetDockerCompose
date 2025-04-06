package net.akensys.FormulaireTest.controller;



import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import net.akensys.FormulaireTest.entity.User;
import net.akensys.FormulaireTest.model.SignupRequest;
import net.akensys.FormulaireTest.repository.UserRepository;
import net.akensys.FormulaireTest.service.AuthService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        String message = authService.signup(signupRequest);
        if (message.equals("Username already exists")) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + message + "\"}");
        }
        return ResponseEntity.ok("{\"message\":\"" + message + "\"}");
    }

    // Endpoint pour le login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignupRequest loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword())
                .map(token -> ResponseEntity.ok("{\"token\":\"" + token + "\"}"))
                .orElse(ResponseEntity.status(401).body("{\"error\":\"Invalid credentials\"}"));
    }

     @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        // Récupérer l'utilisateur connecté via Spring Security
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        // Retourner les informations nécessaires (par exemple, clientId et username)
        return ResponseEntity.ok(Map.of(
            "username", user.getUsername(),
            "clientId", user.getClient().getId()
        ));
    }

    @GetMapping("/init-demo")
public ResponseEntity<?> initDemoClientAndUser() {
    // Vérifier si le client existe déjà
    if (authService.clientExists("demoDocker") || authService.userExists("demoDocker")) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Client ou utilisateur déjà existant.");
    }

    // Créer le client et l'utilisateur associé
    String message = authService.createDemoClientAndUser();
    return ResponseEntity.ok(Map.of("message", message));
}
    
}
