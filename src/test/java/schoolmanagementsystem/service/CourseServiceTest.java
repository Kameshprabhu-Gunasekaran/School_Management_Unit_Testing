package schoolmanagementsystem.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import schoolmanagementsystem.dto.CourseDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Course;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.CourseRepository;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.repository.TutorRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private CourseService courseService;

    private CourseDTO courseDTO;
    private Course course;
    private Tutor tutor;
    private School school;

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceTest.class);

    @BeforeAll
    public static void toStartCourseService() {
        logger.info(Constant.COURSE_SERVICE_STARTED);
    }

    @BeforeEach
    void setup() {
        courseDTO = new CourseDTO("Math 101", 5000L, "1", "1");

        course = new Course();
        course.setName("Math 101");
        course.setFees(5000L);

        tutor = new Tutor();
        tutor.setId(1L);
        tutor.setName("John Doe");

        school = new School();
        school.setId(1L);
        school.setName("ABC School");
    }

    @Test
    void testCreateCourse() {
        when(schoolMapper.toEntity(any(CourseDTO.class))).thenReturn(course);
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        ResponseDTO response = courseService.create(courseDTO);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(course, response.getData());
    }

    @Test
    void testCreateCourseTutorNotFound() {
        when(schoolMapper.toEntity(any(CourseDTO.class))).thenReturn(course);

        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> courseService.create(courseDTO));
    }


    @Test
    void testCreateCourseSchoolNotFound() {
        when(schoolMapper.toEntity(any(CourseDTO.class))).thenReturn(course);

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));

        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> courseService.create(courseDTO));
    }


    @Test
    void testRetrieveAllCourses() {
        Course course1 = new Course();
        course1.setName("Science 101");

        Page<Course> coursePage = new PageImpl<>(List.of(course1));
        when(courseRepository.searchCourse(anyString(), anyLong(), any(Pageable.class)))
                .thenReturn(coursePage);

        PaginatedResponseDTO<Course> response = courseService.retrieveAll(0, 10, "name", "asc", "Science", 1L);

        assertEquals(1, response.getData().size());
        assertEquals("Science 101", response.getData().get(0).getName());
    }

    @Test
    void testRetrieveById() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Math 101");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseDTO response = courseService.retrieveById(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(course, response.getData());
    }

    @Test
    void testRetrieveByIdNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> courseService.retrieveById(1L));
    }

    @Test
    void testUpdateCourse() {
        Course existingCourse = new Course();
        existingCourse.setId(1L);

        Course updatedCourse = new Course();
        updatedCourse.setName("Updated Course");
        updatedCourse.setFees(10000L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

        ResponseDTO response = courseService.updateById(1L, updatedCourse);

        Course result = (Course) response.getData();
        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals("Updated Course", result.getName());
        assertEquals(10000L, result.getFees());
    }

    @Test
    void testUpdateCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> courseService.updateById(1L, new Course()));
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        ResponseDTO response = courseService.remove(1L);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testDeleteCourseNotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestServiceAlertException.class, () -> courseService.remove(1L));
    }

    @AfterAll
    public static void toEndCourseService() {
        logger.info(Constant.COURSE_SERVICE_FINISHED);
    }
}
