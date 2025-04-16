package schoolmanagementsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import schoolmanagementsystem.dto.JwtResponse;
import schoolmanagementsystem.dto.LoginRequest;
import schoolmanagementsystem.dto.MessageResponse;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.SignupRequest;
import schoolmanagementsystem.entity.User;
import schoolmanagementsystem.repository.RoleRepository;
import schoolmanagementsystem.repository.UserRepository;
import schoolmanagementsystem.security.JwtService;
import schoolmanagementsystem.service.UserDetailsImpl;
import schoolmanagementsystem.util.Constant;
import schoolmanagementsystem.util.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder encoder, JwtService jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = this.jwtUtils.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (this.userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(Constant.USERNAME_EXIT));
        }

        if (this.userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(Constant.EMAIL_EXIT));
        }

        final User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        final Set<String> strRoles = signUpRequest.getRole();
        Set<schoolmanagementsystem.entity.Role> roles = new HashSet<>();

        if (strRoles == null) {
            schoolmanagementsystem.entity.Role userRole = this.roleRepository.findByName(Role.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(Constant.ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        schoolmanagementsystem.entity.Role adminRole = this.roleRepository.findByName(Role.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(Constant.ROLE_NOT_FOUND));
                        roles.add(adminRole);

                        break;
                    case "moderator":
                        schoolmanagementsystem.entity.Role modRole = this.roleRepository.findByName(Role.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(Constant.ROLE_NOT_FOUND));
                        roles.add(modRole);

                        break;

                    case "student":
                        schoolmanagementsystem.entity.Role studentRole = this.roleRepository.findByName(Role.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException(Constant.ROLE_NOT_FOUND));
                        roles.add(studentRole);
                        break;

                    case "tutor":
                        schoolmanagementsystem.entity.Role teacherRole = this.roleRepository.findByName(Role.ROLE_TUTOR)
                                .orElseThrow(() -> new RuntimeException(Constant.ROLE_NOT_FOUND));
                        roles.add(teacherRole);
                        break;

                    default:
                        schoolmanagementsystem.entity.Role userRole = this.roleRepository.findByName(Role.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(Constant.ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        this.userRepository.save(user);

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(),user,Constant.USER_REGISTERED));
    }
}
