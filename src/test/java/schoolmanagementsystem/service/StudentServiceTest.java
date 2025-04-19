package schoolmanagementsystem.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.StudentCourseDTO;
import schoolmanagementsystem.dto.StudentDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.entity.Student;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.repository.StudentRepository;
import schoolmanagementsystem.repository.TutorRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private StudentService studentService;

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceTest.class);

    @BeforeAll
    public static void toStartStudentService() {
        logger.info(Constant.STUDENT_SERVICE_STARTED);
    }

    @Test
    void testCreateStudent() {
        StudentDTO dto = new StudentDTO(null, "John", 123456789L, "9876543210", "1", "1");

        Student student = new Student();
        Tutor tutor = new Tutor();
        School school = new School();

        when(schoolMapper.toEntity(any(StudentDTO.class))).thenReturn(student);
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        ResponseDTO response = studentService.create(dto);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(student, response.getData());
    }

    @Test
    void testRetrieveById() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        ResponseDTO response = studentService.retrieveById(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(student, response.getData());
    }

    @Test
    void testRetrieveByIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> studentService.retrieveById(1L));
    }

    @Test
    void testUpdateStudent() {
        Student input = new Student();
        input.setName("Alice");
        input.setDob(123456789L);
        input.setContactNumber("9876543210");

        Student existing = new Student();
        existing.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenReturn(existing);

        ResponseDTO response = studentService.updateById(1L, input);

        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(existing, response.getData());
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        ResponseDTO response = studentService.remove(1L);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testDeleteStudentNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestServiceAlertException.class, () -> studentService.remove(1L));
    }

    @Test
    void testGetStudentsByCourseAndSchool() {
        List<StudentCourseDTO> list = List.of(
                new StudentCourseDTO("John", 1L, "Math", "101", "Greenwood High")
        );

        when(studentRepository.findStudentsByCourseAndSchool(1L, 1L)).thenReturn(list);

        ResponseDTO response = studentService.getStudentsByCourseAndSchool(1L, 1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(list, response.getData());
    }


    @Test
    void testGetStudentsByTutorId() {
        List<Student> students = List.of(new Student());

        when(studentRepository.findStudentsByTutorId(1L)).thenReturn(students);

        ResponseDTO response = studentService.getStudentsByTutorId(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(students, response.getData());
    }

    @Test
    void testRetrieveAll() {
        Student student = new Student();
        Page<Student> page = new PageImpl<>(List.of(student));

        when(studentRepository.searchStudent(eq("John"), eq(123456789L), isNull(), any(Pageable.class))).thenReturn(page);

        var result = studentService.retrieveAll(0, 10, "name", "asc", "John", 123456789L);

        assertEquals(1, result.getData().size());
    }

    @AfterAll
    public static void toEndStudentService() {
        System.out.println(Constant.STUDENT_SERVICE_FINISHED);
    }
}
