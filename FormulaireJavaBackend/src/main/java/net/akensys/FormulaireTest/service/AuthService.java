package net.akensys.FormulaireTest.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.akensys.FormulaireTest.entity.Client;
import net.akensys.FormulaireTest.entity.User;
import net.akensys.FormulaireTest.model.SignupRequest;
import net.akensys.FormulaireTest.repository.ClientRepository;
import net.akensys.FormulaireTest.repository.UserRepository;
import net.akensys.FormulaireTest.util.JwtUtil;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository; 

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, ClientRepository clientId) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.clientRepository = clientId; 
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Méthode pour enregistrer un nouvel utilisateur
    public String signup(User user) {
        // Vérifiez si l'utilisateur existe déjà
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Enregistrez l'utilisateur
        userRepository.save(user);
        return "User created successfully";
    }

    public String signup(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            return "Username already exists";
        }

        // Vérifiez que le client existe
        Client client = clientRepository.findById(signupRequest.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        // Créez un nouvel utilisateur
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setClient(client);

        // Sauvegardez l'utilisateur
        userRepository.save(user);
        return "User created successfully";
    }

    // Méthode pour le login et la génération de token
    public Optional<String> login(String username, String password) {
        // Rechercher l'utilisateur
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // Génération d'un token JWT
            String token = jwtUtil.generateToken(username);
            return Optional.of(token);
        }

        // Retourne vide si l'authentification échoue
        return Optional.empty();
    }

    public boolean clientExists(String clientName) {
    return clientRepository.findByNom(clientName).isPresent();
}

public boolean userExists(String username) {
    return userRepository.findByUsername(username).isPresent();
}

public String createDemoClientAndUser() {
    // Créer le client
    Client client = new Client();
    client.setNom("demoDocker");
    client.setCreatedAt(LocalDateTime.now());
    client = clientRepository.save(client); // pour avoir l'ID

    // Créer l'utilisateur
    User user = new User();
    user.setUsername("demoDocker");
    user.setPassword(passwordEncoder.encode("demoDocker"));
    user.setClient(client);

    userRepository.save(user);

    return "Client et utilisateur 'demoDocker' créés avec succès.";
}
}
