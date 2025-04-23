package schoolmanagementsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import schoolmanagementsystem.entity.User;
import schoolmanagementsystem.repository.UserRepository;
import schoolmanagementsystem.util.Constant;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("kamesh");
        user.setEmail("kamesh@gmail.com");
        user.setPassword("password");
    }

    @Test
    void testLoadUserByUsername() {
        Mockito.when(userRepository.findByUsername("kamesh")).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("kamesh");

        assertNotNull(userDetails);
        assertEquals("kamesh", userDetails.getUsername());
        assertEquals("kamesh@gmail.com", userDetails.getEmail());
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        Mockito.when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknownUser"));

        assertEquals(Constant.USERNAME_NOT_FOUND + "unknownUser", exception.getMessage());
    }

    @Test
    void testLoadUserByUsernameEmptyUsername() {
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(""));

        assertEquals(Constant.USERNAME_NOT_FOUND + "", exception.getMessage());
    }
}
