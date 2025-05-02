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
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.TutorDTO;
import schoolmanagementsystem.entity.Salary;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.SalaryRepository;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.repository.TutorRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest {

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private TutorService tutorService;

    private TutorDTO tutorDTO;
    private Tutor tutor;
    private School school;

    private static final Logger logger = LoggerFactory.getLogger(TutorServiceTest.class);

    @BeforeAll
    public static void toStarTutorService() {
        logger.info(Constant.TUTOR_SERVICE_STARTED);
    }

    @BeforeEach
    void setup() {
        tutorDTO = new TutorDTO();
        tutorDTO.setName("John");
        tutorDTO.setSubject("Math");
        tutorDTO.setSalary(10000.0);
        tutorDTO.setSchoolId("1");

        tutor = new Tutor();
        tutor.setName("John");
        tutor.setSubject("Math");
        tutor.setSalary(10000.0);

        school = new School();
        school.setId(1L);
        school.setName("ABC School");
    }

    @Test
    void testCreateTutor() {
        when(schoolMapper.toEntity(any(TutorDTO.class))).thenReturn(tutor);
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor);

        ResponseDTO response = tutorService.create(tutorDTO);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(tutor, response.getData());
        verify(this.salaryRepository, times(1)).save(any(Salary.class));
    }

    @Test
    void testCreateTutorSchoolNotFound() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BadRequestServiceAlertException.class, () -> tutorService.create(tutorDTO));
    }

    @Test
    void testRetrieveAllTutors() {
        Tutor tutor1 = new Tutor();
        tutor1.setName("Alice");

        Page<Tutor> tutorPage = new PageImpl<>(List.of(tutor1));
        when(tutorRepository.searchTutor(anyString(), anyString(), isNull(), any(Pageable.class)))
                .thenReturn(tutorPage);

        PaginatedResponseDTO<Tutor> response = tutorService.retrieveAll(0, 10, "name", "asc", "Alice", "Science");

        assertEquals(1, response.getData().size());
        assertEquals("Alice", response.getData().get(0).getName());
    }

    @Test
    void testRetrieveByIdSuccess() {
        Tutor tutor = new Tutor();
        tutor.setId(1L);
        tutor.setName("John");

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));

        ResponseDTO response = tutorService.retrieveById(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(tutor, response.getData());
    }

    @Test
    void testRetrieveByIdNotFound() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> tutorService.retrieveById(1L));
    }

    @Test
    void testUpdateTutor() {
        Tutor existingTutor = new Tutor();
        existingTutor.setId(1L);

        Tutor updatedTutor = new Tutor();
        updatedTutor.setName("Updated Name");
        updatedTutor.setSubject("Updated Subject");
        updatedTutor.setSalary(50000.0);

        when(tutorRepository.findById(1L)).thenReturn(Optional.of(existingTutor));
        when(tutorRepository.save(any(Tutor.class))).thenReturn(existingTutor);

        ResponseDTO response = tutorService.updateById(1L, updatedTutor);

        Tutor result = (Tutor) response.getData();
        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Subject", result.getSubject());
        assertEquals(50000.0, result.getSalary());
    }

    @Test
    void testUpdateTutorNotFound() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> tutorService.updateById(1L, new Tutor()));
    }

    @Test
    void testDeleteTutorSuccess() {
        when(tutorRepository.existsById(1L)).thenReturn(true);

        ResponseDTO response = tutorService.remove(1L);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testDeleteTutorNotFound() {
        when(tutorRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestServiceAlertException.class, () -> tutorService.remove(1L));
    }

    @AfterAll
    public static void toEndTutorService() {
        logger.info(Constant.TUTOR_SERVICE_FINISHED);
    }
}
