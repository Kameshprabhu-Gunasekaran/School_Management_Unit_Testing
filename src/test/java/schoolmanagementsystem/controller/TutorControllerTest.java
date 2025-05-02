package schoolmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.TutorDTO;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.exception.GlobalExceptionHandler;
import schoolmanagementsystem.exception.ResourceNotFoundException;
import schoolmanagementsystem.service.TutorService;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TutorControllerTest {

    @Mock
    private TutorService tutorService;

    @InjectMocks
    private TutorController tutorController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Tutor tutor;

    private static final Logger logger = LoggerFactory.getLogger(TutorControllerTest.class);

    @BeforeAll
    public static void toStartTutorController() {
        logger.info(Constant.TUTOR_CONTROLLER_STARTED);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorController)
//                .setControllerAdvice(new GlobalExceptionHandler()) // Add this line
                .build();
        tutor = new Tutor();
        tutor.setId(1L);
        tutor.setName("Alice Smith");
    }

    @Test
    public void testCreateTutor() throws Exception {
        TutorDTO tutorDTO = new TutorDTO();
        tutorDTO.setName("Alice Smith");
        tutorDTO.setSubject("Mathematics");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.CREATED);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(tutor);

        when(tutorService.create(any(TutorDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/tutor/create").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tutorDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.message").value(Constant.CREATED)).andExpect(jsonPath("$.data.name").value("Alice Smith"));

        verify(tutorService, times(1)).create(any(TutorDTO.class));
    }

    @Test
    void testCreateTutor_InvalidName_ThrowsException() throws Exception {
        TutorDTO tutorDTO = new TutorDTO();
        tutorDTO.setName("");
        tutorDTO.setSchoolId("1");
        tutorDTO.setSalary(0d);

        mockMvc.perform(post("/api/v1/tutor/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorDTO)))

                .andExpect(status().isBadRequest());

        verify(tutorService, times(0)).create(any(TutorDTO.class));
    }

    @Test
    void testRetrieveTutorById_NotFound_ThrowsException() throws Exception {
        long id = 100L;

        when(tutorService.retrieveById(id))
                .thenThrow(new ResourceNotFoundException("Tutor not found"));
        mockMvc = MockMvcBuilders.standaloneSetup(tutorController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Add this line
                .build();
        mockMvc.perform(get("/api/v1/tutor/retrieve/{id}", id))
                .andExpect(status().isNotFound());
//                .andExpect(jsonPath("$.message").value("Tutor not found")) // Ensure the message is correct
//                .andExpect(jsonPath("$.statusCode").value(404))
//                .andExpect(jsonPath("$.data").value(nullValue()));

        verify(tutorService, times(1)).retrieveById(id);
    }


    @Test
    public void testRetrieveAllTutors() throws Exception {
        PaginatedResponseDTO<Tutor> response = new PaginatedResponseDTO<>();
        response.setData(List.of(tutor));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(1);
        response.setTotalPages(1);

        when(tutorService.retrieveAll(anyInt(), anyInt(), anyString(), anyString(), nullable(String.class), nullable(String.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/tutor/retrieve").param("page", "0").param("size", "10").param("sortBy", "name").param("sortDir", "asc")).andExpect(status().isOk()).andExpect(jsonPath("$.data[0].name").value("Alice Smith"));

        verify(tutorService, times(1)).retrieveAll(anyInt(), anyInt(), anyString(), anyString(), nullable(String.class), nullable(String.class));
    }


    @Test
    public void testRetrieveTutorById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.RETRIEVED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(tutor);

        when(tutorService.retrieveById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/tutor/retrieve/1")).andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(1)).andExpect(jsonPath("$.data.name").value("Alice Smith"));

        verify(tutorService, times(1)).retrieveById(anyLong());
    }

    @Test
    public void testUpdateTutorById() throws Exception {
        Tutor updatedTutor = new Tutor();
        updatedTutor.setId(1L);
        updatedTutor.setName("Alice Johnson");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.UPDATED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(updatedTutor);

        when(tutorService.updateById(anyLong(), any(Tutor.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/tutor/update/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedTutor))).andExpect(status().isOk()).andExpect(jsonPath("$.data.name").value("Alice Johnson"));

        verify(tutorService, times(1)).updateById(anyLong(), any(Tutor.class));
    }

    @Test
    public void testDeleteTutorById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.DELETED);
        response.setStatusCode(HttpStatus.OK.value());

        when(tutorService.remove(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/v1/tutor/delete/1")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value(Constant.DELETED));

        verify(tutorService, times(1)).remove(anyLong());
    }

    @AfterAll
    public static void toEndTutorController() {
        logger.info(Constant.TUTOR_CONTROLLER_FINISHED);
    }
}