package schoolmanagementsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import schoolmanagementsystem.entity.Role;
import schoolmanagementsystem.entity.User;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1);
        role.setName(schoolmanagementsystem.util.Role.ROLE_USER);

        user = new User();
        user.setId(100L);
        user.setUsername("kamesh");
        user.setEmail("kamesh@gamil.com");
        user.setPassword("password");
        user.setRoles(Set.of(role));

        userDetails = UserDetailsImpl.build(user);
    }

    @Test
    void testUserDetailsFields() {
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void testAuthorities() {
        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEquals_sameId_returnsTrue() {
        User anotherUser = new User();
        anotherUser.setId(100L);
        anotherUser.setUsername("anotherUser");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("pass");
        anotherUser.setRoles(Collections.emptySet());

        UserDetailsImpl anotherDetails = UserDetailsImpl.build(anotherUser);
        assertEquals(userDetails, anotherDetails);
    }

    @Test
    void testEquals_differentId_returnsFalse() {
        User anotherUser = new User();
        anotherUser.setId(101L);
        anotherUser.setUsername("anotherUser");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("pass");
        anotherUser.setRoles(Collections.emptySet());

        UserDetailsImpl anotherDetails = UserDetailsImpl.build(anotherUser);
        assertNotEquals(userDetails, anotherDetails);
    }

    @Test
    void testEquals_null_returnsFalse() {
        assertNotEquals(userDetails, null);
    }

    @Test
    void testEquals_differentClass_returnsFalse() {
        assertNotEquals(userDetails, "not a user detail object");
    }
}
