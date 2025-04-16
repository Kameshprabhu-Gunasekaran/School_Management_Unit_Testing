package schoolmanagementsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import schoolmanagementsystem.dto.EnrollmentDTO;
import schoolmanagementsystem.dto.EnrollmentRequestDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Enrollment;
import schoolmanagementsystem.service.EnrollmentService;

@RestController
@RequestMapping("/api/v1/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final EnrollmentDTO enrollmentDTO) {
        return this.enrollmentService.create(enrollmentDTO);
    }

    @GetMapping("/retrieve")
    public PaginatedResponseDTO<Enrollment> retrieveAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String enrollmentStatus,
            @RequestParam(required = false) Long id) {

        return this.enrollmentService.retrieveAll(page, size, sortBy, sortDir, enrollmentStatus, id);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final Long id) {
        return this.enrollmentService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO updateById(@PathVariable("id") final Long id, @RequestBody final Enrollment enrollment) {
        return this.enrollmentService.updateById(id, enrollment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO deleteById(@PathVariable("id") final Long id) {
        return this.enrollmentService.remove(id);
    }

    @PostMapping("/enroll-and-increase-salary")
    public ResponseEntity<ResponseDTO> enrollStudentAndIncreaseTutorSalary(@RequestBody EnrollmentRequestDTO requestDTO) {
        final ResponseDTO response = this.enrollmentService.enrollStudentAndIncreaseTutorSalary(
                requestDTO.getStudentId(),
                requestDTO.getCourseId(),
                requestDTO.getTutorId(),
                requestDTO.getIncrementAmount()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
