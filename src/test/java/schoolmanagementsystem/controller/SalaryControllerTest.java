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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Salary;
import schoolmanagementsystem.service.SalaryService;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SalaryControllerTest {

    private MockMvc mockMvc;
    private SalaryService salaryService;
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(SalaryControllerTest.class);

    @BeforeAll
    public static void toStartSalaryController() {
        logger.info(Constant.SALARY_CONTROLLER_STARTED);
    }

    @BeforeEach
    void setUp() {
        salaryService = Mockito.mock(SalaryService.class);
        SalaryController salaryController = new SalaryController(salaryService);
        mockMvc = MockMvcBuilders.standaloneSetup(salaryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSalary() throws Exception {
        Salary salary = new Salary();

        ResponseDTO response = new ResponseDTO();
        response.setMessage("Salary created");
        response.setStatusCode(201);
        response.setData(salary);

        when(salaryService.create(any(Salary.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/total-salary/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salary)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary created"))
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    void testRetrieveAllSalaries() throws Exception {
        PaginatedResponseDTO<Salary> paginatedResponse = new PaginatedResponseDTO<>();
        paginatedResponse.setData(List.of(new Salary()));
        paginatedResponse.setPageNumber(0);
        paginatedResponse.setPageSize(10);
        paginatedResponse.setTotalElements(1L);
        paginatedResponse.setTotalPages(1);

        when(salaryService.retrieveAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/v1/total-salary/retrieve")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testRetrieveSalaryById() throws Exception {
        Salary salary = new Salary();
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Salary found");
        response.setStatusCode(200);
        response.setData(salary);

        when(salaryService.retrieveById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/total-salary/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary found"))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void testUpdateSalaryById() throws Exception {
        Salary salary = new Salary();
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Salary updated");
        response.setStatusCode(200);
        response.setData(salary);

        when(salaryService.updateById(eq(1L), any(Salary.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/total-salary/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salary)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary updated"));
    }

    @Test
    void testDeleteSalaryById() throws Exception {
        ResponseDTO response =  new ResponseDTO();
        response.setMessage("Salary deleted");
        response.setStatusCode(200);
        response.setData(null);

        when(salaryService.remove(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/total-salary/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Salary deleted"))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @AfterAll
    public static void toEndSalaryController() {
        logger.info(Constant.SALARY_CONTROLLER_FINISHED);
    }
}
