package schoolmanagementsystem.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import schoolmanagementsystem.dto.EnrollmentDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.*;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import schoolmanagementsystem.util.Constant;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private EnrollmentDTO enrollmentDTO;
    private Enrollment enrollment;
    private Student student;
    private Course course;
    private Tutor tutor;

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceTest.class);

    @BeforeAll
    public static void toStartEnrollmentService() {
        logger.info(Constant.ENROLLMENT_SERVICE_STARTED);
    }

    @BeforeEach
    void setup() {
        enrollmentDTO = new EnrollmentDTO(1L, "NO", "ENROLLED", "1", "2");

        enrollment = new Enrollment();
        enrollment.setEnrollmentStatus("ENROLLED");
        enrollment.setFeesPaid("NO");

        student = new Student();
        student.setId(1L);

        course = new Course();
        course.setId(2L);

        tutor = new Tutor();
        tutor.setId(3L);
        tutor.setSalary(20000L);
    }

    @Test
    void testCreateEnrollment() {
        when(schoolMapper.toEntity(enrollmentDTO)).thenReturn(enrollment);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        ResponseDTO response = enrollmentService.create(enrollmentDTO);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(enrollment, response.getData());
    }

    @Test
    void testCreateEnrollmentStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> enrollmentService.create(enrollmentDTO));
    }

    @Test
    void testCreateEnrollmentCourseNotFound() {
        when(schoolMapper.toEntity(enrollmentDTO)).thenReturn(enrollment);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> enrollmentService.create(enrollmentDTO));
    }

    @Test
    void testRetrieveAllEnrollments() {
        Enrollment e1 = new Enrollment();
        e1.setEnrollmentStatus("ENROLLED");
        Page<Enrollment> page = new PageImpl<>(List.of(e1));

        when(enrollmentRepository.searchEnrollment(anyString(), anyLong(), any(Pageable.class))).thenReturn(page);

        PaginatedResponseDTO<Enrollment> response = enrollmentService.retrieveAll(0, 5, "id", "asc", "ENROLLED", 1L);

        assertEquals(1, response.getData().size());
        assertEquals("ENROLLED", response.getData().get(0).getEnrollmentStatus());
    }

    @Test
    void testRetrieveById() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        ResponseDTO response = enrollmentService.retrieveById(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(enrollment, response.getData());
    }

    @Test
    void testRetrieveByIdNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> enrollmentService.retrieveById(1L));
    }

    @Test
    void testUpdateEnrollment() {
        Enrollment updated = new Enrollment();
        updated.setEnrollmentStatus("COMPLETED");
        updated.setFeesPaid("YES");

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        ResponseDTO response = enrollmentService.updateById(1L, updated);

        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testUpdateEnrollmentNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> enrollmentService.updateById(1L, new Enrollment()));
    }

    @Test
    void testDeleteEnrollment() {
        when(enrollmentRepository.existsById(1L)).thenReturn(true);

        ResponseDTO response = enrollmentService.remove(1L);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testDeleteEnrollmentNotFound() {
        when(enrollmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestServiceAlertException.class, () -> enrollmentService.remove(1L));
    }

    @Test
    void testEnrollStudentAndIncreaseTutorSalary() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(tutorRepository.findById(3L)).thenReturn(Optional.of(tutor));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor);

        ResponseDTO response = enrollmentService.enrollStudentAndIncreaseTutorSalary(1L, 2L, 3L, 5000L);

        assertEquals(Constant.STUDENT_ENROLLED_AND_TUTOR_SALARY_UPDATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void testEnrollStudentAndIncreaseTutorSalaryInvalidStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class,
                () -> enrollmentService.enrollStudentAndIncreaseTutorSalary(1L, 2L, 3L, 1000L));
    }

    @Test
    void testEnrollStudentAndIncreaseTutorSalaryNegativeIncrement() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(tutorRepository.findById(3L)).thenReturn(Optional.of(tutor));

        assertThrows(BadRequestServiceAlertException.class,
                () -> enrollmentService.enrollStudentAndIncreaseTutorSalary(1L, 2L, 3L, -100L));
    }

    @AfterAll
    public static void toEndEnrollmentService() {
        logger.info(Constant.ENROLLMENT_SERVICE_FINISHED);
    }
}
