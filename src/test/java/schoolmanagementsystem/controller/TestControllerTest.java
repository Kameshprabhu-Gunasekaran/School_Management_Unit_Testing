package schoolmanagementsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TestControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TestController testController = new TestController();
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    void testAllAccess() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.PUBLIC_ACCESS));
    }

    @Test
    void testUserAccess() throws Exception {
        setSecurityContextWithRoles("ROLE_USER");

        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.USER_ACCESS));
    }

    @Test
    void testModeratorAccess() throws Exception {
        setSecurityContextWithRoles("ROLE_MODERATOR");

        mockMvc.perform(get("/api/test/mod"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.MODERATOR_ACCESS));
    }

    @Test
    void testAdminAccess() throws Exception {
        setSecurityContextWithRoles("ROLE_ADMIN");

        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.ADMIN_ACCESS));
    }

    @Test
    void testStudentAccess() throws Exception {
        setSecurityContextWithRoles("ROLE_STUDENT");

        mockMvc.perform(get("/api/test/student"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.STUDENT_ACCESS));
    }

    @Test
    void testTeacherAccess() throws Exception {
        setSecurityContextWithRoles("ROLE_TEACHER");

        mockMvc.perform(get("/api/test/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.TEACHER_ACCESS));
    }

    private void setSecurityContextWithRoles(String... roles) {
        var authorities = List.of(roles).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var auth = new UsernamePasswordAuthenticationToken("mockUser", null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
