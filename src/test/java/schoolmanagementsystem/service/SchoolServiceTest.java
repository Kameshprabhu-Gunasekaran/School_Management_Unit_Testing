package schoolmanagementsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.SchoolRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private SchoolService schoolService;

    @Test
    void testCreateSchool() {
        School school = new School();
        school.setName("Test School");

        when(schoolRepository.save(any(School.class))).thenReturn(school);

        ResponseDTO response = this.schoolService.createSchool(school);

        assertEquals("CREATED", response.getMessage());
        assertEquals(200, response.getStatusCode());
        assertEquals(school, response.getData());
    }

    @Test
    void testRetrieveById() {
        School school = new School();
        school.setId(1L);

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

        ResponseDTO response = this.schoolService.retrieveById(1L);

        assertEquals("RETRIEVE", response.getMessage());
        assertEquals(school, response.getData());
    }

    @Test
    void testRetrieveByIdNotFound() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestServiceAlertException exception = assertThrows(BadRequestServiceAlertException.class,() -> schoolService.retrieveById(1L));

        assertEquals("Id does not exist", exception.getMessage());
    }

    @Test
    void testUpdateById() {
        School oldSchool = new School();
        oldSchool.setId(1L);
        oldSchool.setName("SRM School");

        School updatedSchool = new School();
        updatedSchool.setName("SSN School");

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(oldSchool));
        when(schoolRepository.save(any(School.class))).thenReturn(oldSchool);

        ResponseDTO response = schoolService.updateById(1L, updatedSchool);

        assertEquals("UPDATED", response.getMessage());
        assertEquals("SSN School", ((School) response.getData()).getName());
    }
}
