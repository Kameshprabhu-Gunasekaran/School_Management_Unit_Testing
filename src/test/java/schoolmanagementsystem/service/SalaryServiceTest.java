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
import schoolmanagementsystem.entity.Salary;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.repository.SalaryRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalaryServiceTest {

    @Mock
    private SalaryRepository salaryRepository;

    @InjectMocks
    private SalaryService salaryService;

    private Salary salary;
    private Tutor tutor;

    private static final Logger logger = LoggerFactory.getLogger(SalaryServiceTest.class);

    @BeforeAll
    public static void toStartSalaryService() {
        logger.info(Constant.SALARY_SERVICE_STARTED);
    }

    @BeforeEach
    void setup() {
        tutor = new Tutor();
        tutor.setId(1L);
        tutor.setName("Jane Doe");

        salary = new Salary();
        salary.setId(1L);
        salary.setMonth("April");
        salary.setSalaryPaid(true);
        salary.setTutor(tutor);
    }

    @Test
    void testCreateSalary() {
        when(salaryRepository.save(any(Salary.class))).thenReturn(salary);

        ResponseDTO response = salaryService.create(salary);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(salary, response.getData());
    }

    @Test
    void testRetrieveAllSalaries() {
        Page<Salary> salaryPage = new PageImpl<>(List.of(salary));
        when(salaryRepository.findAll(any(Pageable.class))).thenReturn(salaryPage);

        PaginatedResponseDTO<Salary> response = salaryService.retrieveAll(0, 10, "month", "asc");

        assertEquals(1, response.getData().size());
        assertEquals("April", response.getData().get(0).getMonth());
    }

    @Test
    void testRetrieveSalaryByIdSuccess() {
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));

        ResponseDTO response = salaryService.retrieveById(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(salary, response.getData());
    }

    @Test
    void testRetrieveSalaryByIdNotFound() {
        when(salaryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> salaryService.retrieveById(1L));
    }

    @Test
    void testUpdateSalarySuccess() {
        Salary updatedSalary = new Salary();
        updatedSalary.setMonth("May");
        updatedSalary.setSalaryPaid(true);
        updatedSalary.setTutor(tutor);

        when(salaryRepository.findById(1L)).thenReturn(Optional.of(salary));
        when(salaryRepository.save(any(Salary.class))).thenReturn(salary);

        ResponseDTO response = salaryService.updateById(1L, updatedSalary);

        Salary result = (Salary) response.getData();
        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals("May", result.getMonth());
        assertEquals(true, result.getSalaryPaid());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testUpdateSalaryNotFound() {
        when(salaryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestServiceAlertException.class, () -> salaryService.updateById(1L, new Salary()));
    }

    @Test
    void testDeleteSalarySuccess() {
        when(salaryRepository.existsById(1L)).thenReturn(true);

        ResponseDTO response = salaryService.remove(1L);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testDeleteSalaryNotFound() {
        when(salaryRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestServiceAlertException.class, () -> salaryService.remove(1L));
    }

    @AfterAll
    public static void toEndSalaryService() {
        logger.info(Constant.SALARY_SERVICE_FINISHED);
    }
}
