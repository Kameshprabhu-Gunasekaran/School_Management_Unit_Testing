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
import schoolmanagementsystem.dto.*;
import schoolmanagementsystem.entity.Student;
import schoolmanagementsystem.service.StudentService;
import schoolmanagementsystem.util.Constant;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Student student;

    private static final Logger logger = LoggerFactory.getLogger(StudentControllerTest.class);

    @BeforeAll
    public static void toStartStudentController() {
        logger.info(Constant.STUDENT_CONTROLLER_STARTED);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
    }

    @Test
    public void testCreateStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("John Doe");
        studentDTO.setSchoolId("1");
        studentDTO.setTutorId("1");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.CREATED);
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setData(student);

        when(studentService.create(any(StudentDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(Constant.CREATED))
                .andExpect(jsonPath("$.data.name").value("John Doe"));

        verify(studentService, times(1)).create(any(StudentDTO.class));
    }

    @Test
    public void testRetrieveAllStudents() throws Exception {
        PaginatedResponseDTO<Student> response = new PaginatedResponseDTO<>();
        response.setData(List.of(student));
        response.setPageNumber(0);
        response.setPageSize(10);
        response.setTotalElements(1);
        response.setTotalPages(1);

        when(studentService.retrieveAll(
                anyInt(), anyInt(), anyString(), anyString(), nullable(String.class), nullable(Long.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/student/retrieve")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));

        verify(studentService, times(1)).retrieveAll(
                anyInt(), anyInt(), anyString(), anyString(), nullable(String.class), nullable(Long.class)
        );
    }


    @Test
    public void testRetrieveStudentById() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.RETRIEVED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(student);

        when(studentService.retrieveById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/student/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"));

        verify(studentService, times(1)).retrieveById(anyLong());
    }

    @Test
    public void testUpdateStudentById() throws Exception {
        Student updated = new Student();
        updated.setId(1L);
        updated.setName("Jane Doe");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.UPDATED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(updated);

        when(studentService.updateById(anyLong(), any(Student.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/student/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Jane Doe"));

        verify(studentService, times(1)).updateById(anyLong(), any(Student.class));
    }

    @Test
    public void testRemoveStudent() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.DELETED);
        response.setStatusCode(HttpStatus.OK.value());

        when(studentService.remove(anyLong())).thenReturn(response);

        mockMvc.perform(delete("/api/v1/student/remove/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Constant.DELETED));

        verify(studentService, times(1)).remove(anyLong());
    }

    @Test
    public void testGetStudentsByCourseAndSchool() throws Exception {
        StudentCourseDTO studentCourseDTO = new StudentCourseDTO();
        studentCourseDTO.setStudentName("John Doe");
        studentCourseDTO.setStudentDob(123456789L);
        studentCourseDTO.setStudentContact("1234567890");
        studentCourseDTO.setCourseName("Math");
        studentCourseDTO.setSchoolName("ABC School");

        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.RETRIEVED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(List.of(studentCourseDTO));

        when(studentService.getStudentsByCourseAndSchool(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/student/retrieve/course/1/school/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].studentName").value("John Doe"))
                .andExpect(jsonPath("$.data[0].courseName").value("Math"))
                .andExpect(jsonPath("$.data[0].schoolName").value("ABC School"));

        verify(studentService, times(1)).getStudentsByCourseAndSchool(anyLong(), anyLong());
    }

    @Test
    public void testGetStudentsByTutorId() throws Exception {
        ResponseDTO response = new ResponseDTO();
        response.setMessage(Constant.RETRIEVED);
        response.setStatusCode(HttpStatus.OK.value());
        response.setData(List.of(student));

        when(studentService.getStudentsByTutorId(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/v1/student/retrieve/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));

        verify(studentService, times(1)).getStudentsByTutorId(anyLong());
    }

    @AfterAll
    public static void toEndStudentController() {
        logger.info(Constant.STUDENT_CONTROLLER_FINISHED);
    }
}

