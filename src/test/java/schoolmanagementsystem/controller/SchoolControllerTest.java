package schoolmanagementsystem.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.SchoolDTO;
import schoolmanagementsystem.dto.SearchRequestDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.service.SchoolService;
import schoolmanagementsystem.service.SchoolServiceTest;
import schoolmanagementsystem.util.Constant;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SchoolControllerTest {

    @InjectMocks
    private SchoolController schoolController;

    @Mock
    private SchoolService schoolService;

    private static final Logger logger = LoggerFactory.getLogger(SchoolControllerTest.class);

    @BeforeAll
    public static void toStartSchoolController() {
        logger.info(Constant.SCHOOL_CONTROLLER_STARTED);
    }

//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testCreateSchool() {
        School school = new School();
        school.setName("Test School");
        ResponseDTO expectedResponse = new ResponseDTO(HttpStatus.CREATED.value(), school, Constant.CREATED);

        when(schoolService.createSchool(any(School.class))).thenReturn(expectedResponse);

        ResponseDTO response = schoolController.create(school);

        assertEquals(Constant.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(school, response.getData());
    }

    @Test
    void testSearchSchool() {
        SearchRequestDTO searchRequest = new SearchRequestDTO();

        School school = new School();
        school.setId(1L);
        school.setName("Test School");

        SchoolDTO schoolDTO = new SchoolDTO(school);

        PaginatedResponseDTO<SchoolDTO> expectedResponse = new PaginatedResponseDTO<>();
        expectedResponse.setData(Collections.singletonList(schoolDTO));
        expectedResponse.setPageNumber(0);
        expectedResponse.setPageSize(10);
        expectedResponse.setTotalElements(1);
        expectedResponse.setTotalPages(1);

        when(schoolService.searchSchools(searchRequest)).thenReturn(expectedResponse);

        PaginatedResponseDTO<SchoolDTO> response = schoolController.searchSchools(searchRequest);

        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getPageNumber());
        assertEquals(10, response.getPageSize());
        assertEquals("Test School", response.getData().get(0).getName());
    }

    @Test
    void testRetrieveById() {
        Long id = 1L;
        School school = new School();
        school.setId(id);
        ResponseDTO expectedResponse = new ResponseDTO(HttpStatus.OK.value(), school, Constant.RETRIEVED);
        when(schoolService.retrieveById(id)).thenReturn(expectedResponse);

        ResponseDTO response = schoolController.retrieveById(id);

        assertEquals(Constant.RETRIEVED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(id, ((School) response.getData()).getId());
    }

    @Test
    void testUpdateById() {
        Long id = 1L;
        School school = new School();
        school.setName("HCL");

        ResponseDTO expectedResponse = new ResponseDTO(HttpStatus.OK.value(), school, Constant.UPDATED);

        when(schoolService.updateById(id, school)).thenReturn(expectedResponse);

        ResponseDTO response = schoolController.updateById(id, school);

        assertEquals(Constant.UPDATED, response.getMessage());
        assertEquals("HCL", ((School) response.getData()).getName());
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        ResponseDTO extendedResponse = new ResponseDTO(HttpStatus.OK.value(), null, Constant.DELETED);
        when(schoolService.remove(id)).thenReturn(extendedResponse);

        ResponseDTO response = schoolController.deleteById(id);

        assertEquals(Constant.DELETED, response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNull(response.getData());
    }

    @AfterAll
    public static void toEndSchoolController() {
        logger.info(Constant.SCHOOL_CONTROLLER_FINISHED);
    }

}
