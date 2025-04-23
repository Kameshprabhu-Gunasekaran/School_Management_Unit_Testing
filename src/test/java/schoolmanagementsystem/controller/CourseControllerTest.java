package schoolmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schoolmanagementsystem.dto.CourseDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Course;
import schoolmanagementsystem.service.CourseService;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Course course;

    private static final Logger logger = LoggerFactory.getLogger(CourseControllerTest.class);

    @BeforeAll
    public static void toStartCourseController() {
        logger.info(Constant.COURSE_CONTROLLER_STARTED);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        course = new Course();
        course.setId(1L);
        course.setName("Physics");
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Physics");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.CREATED);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(course);

        when(courseService.create(any(CourseDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(Constant.CREATED))
                .andExpect(jsonPath("$.data.name").value("Physics"));

        verify(courseService, times(1)).create(any(CourseDTO.class));
    }

    @Test
    public void testRetrieveAllCourses() throws Exception {
        PaginatedResponseDTO<Course> response = new PaginatedResponseDTO<>();
        Course course = new Course();
        course.setName("Physics");
        response.setData(List.of(course));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(1);
        response.setTotalPages(1);

        when(courseService.retrieveAll(
                anyInt(), anyInt(), anyString(), anyString(),
                nullable(String.class), nullable(Long.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/course/retrieve")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Physics"));

        verify(courseService, times(1))
                .retrieveAll(anyInt(), anyInt(), anyString(), anyString(),
                        nullable(String.class), nullable(Long.class));
    }


    @Test
    public void testRetrieveCourseById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.RETRIEVED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(course);

        when(courseService.retrieveById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/course/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Physics"));

        verify(courseService, times(1)).retrieveById(anyLong());
    }

    @Test
    public void testUpdateCourseById() throws Exception {
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setName("Advanced Physics");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.UPDATED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(updatedCourse);

        when(courseService.updateById(anyLong(), any(Course.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/course/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Advanced Physics"));

        verify(courseService, times(1)).updateById(anyLong(), any(Course.class));
    }

    @Test
    public void testDeleteCourseById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.DELETED);
        response.setStatusCode(HttpStatus.OK.value());

        when(courseService.remove(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/v1/course/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constant.DELETED));

        verify(courseService, times(1)).remove(anyLong());
    }

    @AfterAll
    public static void toEndCourseController() {
        logger.info(Constant.COURSE_CONTROLLER_FINISHED);
    }
}
