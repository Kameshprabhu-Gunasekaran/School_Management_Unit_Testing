package schoolmanagementsystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.dto.TutorDTO;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.service.TutorService;

@RestController
@RequestMapping("/api/v1/tutor")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(final TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO create(@Valid @RequestBody final TutorDTO tutorDTO) {
        return this.tutorService.create(tutorDTO);
    }

    @GetMapping("/retrieve")
    @PreAuthorize("hasRole('ADMIN')")
    public PaginatedResponseDTO<Tutor> retrieveAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subject) {

        return this.tutorService.retrieveAll(page, size, sortBy, sortDir, name, subject);
    }

    @GetMapping("/retrieve/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.data.name == authentication.name")
    public ResponseDTO retrieveById(@PathVariable("id") final Long id) {
        return this.tutorService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isTutorOwner(#id)")
    public ResponseDTO updateById(@PathVariable("id") final Long id, @RequestBody final Tutor tutor) {
        return this.tutorService.updateById(id, tutor);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDTO deleteById(@PathVariable("id") final Long id) {
        return this.tutorService.remove(id);
    }

}
