package schoolmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.SchoolDTO;
import schoolmanagementsystem.dto.SearchRequestDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.service.SchoolService;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SchoolControllerTest {

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private SchoolController schoolController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private School school;

    @BeforeAll
    public static void toStartSchoolController() {
        System.out.println("School Controller Test case execution has started.");
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(schoolController).build();
        school = new School();
        school.setId(1L);
        school.setName("Test School");
        school.setAddress("Test Address");
        school.setContactNumber("1234567890");
    }

    @Test
    public void testCreateSchool() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.CREATED);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(school);

        when(schoolService.createSchool(any(School.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/school/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(school)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(Constant.CREATED))
                .andExpect(jsonPath("$.data.name").value("Test School"))
                .andExpect(jsonPath("$.data.address").value("Test Address"));

        verify(schoolService, times(1)).createSchool(any(School.class));
    }

    @Test
    public void testRetrieveSchoolById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.RETRIEVED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(school);

        when(schoolService.retrieveById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/school/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(Constant.RETRIEVED))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test School"));

        verify(schoolService, times(1)).retrieveById(anyLong());
    }

    @Test
    public void testDeleteSchool() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.DELETED);
        response.setStatusCode(HttpStatus.OK.value());

        when(schoolService.remove(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/v1/school/remove/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(Constant.DELETED));

        verify(schoolService, times(1)).remove(anyLong());
    }

    @Test
    public void testSearchSchools() throws Exception {
        SearchRequestDTO searchRequest = new SearchRequestDTO();
        searchRequest.setName("Test");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        SchoolDTO schoolDTO = new SchoolDTO("Test School", "Test Address", "1234567890");

        PaginatedResponseDTO<SchoolDTO> paginatedResponse = new PaginatedResponseDTO<>(
                List.of(schoolDTO), 0, 10, 1, 1);

        when(schoolService.searchSchools(any(SearchRequestDTO.class))).thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/v1/school/search")
                        .param("name", "Test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Test School"));

        verify(schoolService, times(1)).searchSchools(any(SearchRequestDTO.class));
    }


    @AfterAll
    public static void toEndSchoolController() {
        System.out.println("School Controller Test case execution has finished.");
    }
}
