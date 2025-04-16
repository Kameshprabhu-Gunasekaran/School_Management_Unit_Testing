package schoolmanagementsystem.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.SchoolDTO;
import schoolmanagementsystem.dto.SearchRequestDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.service.SchoolService;

@RestController
@RequestMapping("/api/v1/school")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final School school) {
        return this.schoolService.createSchool(school);
    }

    @GetMapping("/search")
    public PaginatedResponseDTO<SchoolDTO> searchSchools(SearchRequestDTO searchRequest) {
        return this.schoolService.searchSchools(searchRequest);
    }


    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final Long id) {
        return this.schoolService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO updateById(@PathVariable("id") final Long id, @RequestBody final School school) {
        return this.schoolService.updateById(id, school);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO deleteById(@PathVariable("id") final Long id) {
        return this.schoolService.remove(id);
    }
}
