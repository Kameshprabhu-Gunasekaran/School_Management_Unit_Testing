package schoolmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.dto.*;
import schoolmanagementsystem.entity.Role;
import schoolmanagementsystem.entity.User;
import schoolmanagementsystem.repository.RoleRepository;
import schoolmanagementsystem.repository.UserRepository;
import schoolmanagementsystem.security.JwtService;
import schoolmanagementsystem.service.UserDetailsImpl;
import schoolmanagementsystem.util.Constant;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthController authController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testAuthenticateUserReturnsJwtResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "john", "john@example.com", "encodedPass", List.of(() -> "ROLE_USER"));

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateJwtToken(authentication)).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void testRegisterUser_UsernameExists_ReturnsBadRequest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("john");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constant.USERNAME_EXIT));
    }
}