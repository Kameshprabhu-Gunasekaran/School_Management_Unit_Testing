package schoolmanagementsystem.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.SchoolDTO;
import schoolmanagementsystem.dto.SearchRequestDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private SchoolService schoolService;

    private static final Logger logger = LoggerFactory.getLogger(SchoolServiceTest.class);

    @BeforeAll
    public static void toStartSchoolService() {
        logger.info(Constant.SCHOOL_SERVICE_STARTED);
    }

    @Test
    void testCreateSchool() {
        School school = new School();
        school.setName("Test School");

        when(schoolRepository.save(any(School.class))).thenReturn(school);

        ResponseDTO response = this.schoolService.createSchool(school);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(school, response.getData());
    }

    @Test
    void testRetrieveById() {
        School school = new School();
        school.setId(1L);

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

        ResponseDTO response = this.schoolService.retrieveById(1L);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(school, response.getData());
    }

    @Test
    void testRetrieveByIdNotFound() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestServiceAlertException exception = assertThrows(BadRequestServiceAlertException.class, () -> schoolService.retrieveById(1L));

        assertEquals(Constant.ID_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void testUpdateById() {
        School updatedSchool = new School();
        updatedSchool.setName("HCL");
        updatedSchool.setAddress("Kodaikanal");
        updatedSchool.setContactNumber("123456");

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(new School()));

        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> {
            School school = invocation.getArgument(0);
            school.setId(1L);
            return school;
        });

        ResponseDTO response = schoolService.updateById(1L, updatedSchool);

        School result = (School) response.getData();
        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("HCL", result.getName());
        assertEquals("Kodaikanal", result.getAddress());
        assertEquals("123456", result.getContactNumber());
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeleteById() {
        when(schoolRepository.existsById(1L)).thenReturn(true);

        ResponseDTO response = schoolService.remove(1L);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void testDeleteByIdNotFound() {
        when(schoolRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestServiceAlertException.class, () -> schoolService.remove(1L));
    }

    @Test
    void testSearchSchool() {
        SearchRequestDTO searchRequest = new SearchRequestDTO();
        searchRequest.setPage(0);
        searchRequest.setSize(2);
        searchRequest.setSortBy("hcl");
        searchRequest.setSortDir("asc");
        searchRequest.setName("Test");
        searchRequest.setAddress(null);
        searchRequest.setId(null);

        School school = new School();
        school.setName("Test School");

        Page<School> page = new PageImpl<>(List.of(school));
        when(schoolRepository.searchSchool(eq("Test"), isNull(), isNull(), any(Pageable.class))).thenReturn(page);

        PaginatedResponseDTO<SchoolDTO> response = this.schoolService.searchSchools(searchRequest);

        assertEquals(1, response.getData().size());
        assertEquals("Test School", response.getData().get(0).getName());
    }

    @AfterAll
    public static void toEndSchoolService() {
        logger.info(Constant.SCHOOL_SERVICE_FINISHED);
    }
}
