package schoolmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.dto.EnrollmentDTO;
import schoolmanagementsystem.dto.EnrollmentRequestDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Enrollment;
import schoolmanagementsystem.service.EnrollmentService;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EnrollmentControllerTest {

    private MockMvc mockMvc;
    private EnrollmentService enrollmentService;
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentControllerTest.class);

    @BeforeAll
    public static void toStartEnrollmentController() {
        logger.info(Constant.ENROLLMENT_CONTROLLER_STARTED);
    }

    @BeforeEach
    void setUp() {
        enrollmentService = Mockito.mock(EnrollmentService.class);
        EnrollmentController enrollmentController = new EnrollmentController(enrollmentService);
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateEnrollment() throws Exception {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId("1");
        dto.setCourseId("2");

        Enrollment enrollment = new Enrollment();

        ResponseDTO response = new ResponseDTO();
        response.setMessage("Created");
        response.setStatusCode(201);
        response.setData(enrollment);

        when(enrollmentService.create(any(EnrollmentDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/enrollment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created"))
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    void testRetrieveAll() throws Exception {
        PaginatedResponseDTO<Enrollment> paginatedResponse = new PaginatedResponseDTO<>();
        paginatedResponse.setData(List.of(new Enrollment()));
        paginatedResponse.setPageNumber(0);
        paginatedResponse.setPageSize(10);
        paginatedResponse.setTotalElements(1L);
        paginatedResponse.setTotalPages(1);

        when(enrollmentService.retrieveAll(anyInt(), anyInt(), anyString(), anyString(), any(), any()))
                .thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/v1/enrollment/retrieve")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testRetrieveById() throws Exception {
        Enrollment enrollment = new Enrollment();
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Retrieved");
        response.setStatusCode(200);
        response.setData(enrollment);

        when(enrollmentService.retrieveById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/enrollment/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retrieved"));
    }

    @Test
    void testUpdateById() throws Exception {
        Enrollment enrollment = new Enrollment();
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Updated");
        response.setStatusCode(200);
        response.setData(enrollment);

        when(enrollmentService.updateById(eq(1L), any(Enrollment.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/enrollment/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"));
    }

    @Test
    void testDeleteById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Deleted");
        response.setStatusCode(200);
        response.setData(null);

        when(enrollmentService.remove(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/enrollment/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void testEnrollStudentAndIncreaseSalary() throws Exception {
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setCourseId(2L);
        requestDTO.setTutorId(3L);
        requestDTO.setIncrementAmount(500L);

        Enrollment enrollment = new Enrollment();
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Student enrolled successfully, and tutor's salary updated");
        response.setStatusCode(201);
        response.setData(enrollment);

        when(enrollmentService.enrollStudentAndIncreaseTutorSalary(
                eq(1L), eq(2L), eq(3L), eq(500L))).thenReturn(response);

        mockMvc.perform(post("/api/v1/enrollment/enroll-and-increase-salary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Student enrolled successfully, and tutor's salary updated"));
    }

    @AfterAll
    public static void toEndEnrollmentController() {
        logger.info(Constant.ENROLLMENT_CONTROLLER_FINISHED);
    }
}
