package schoolmanagementsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

import java.time.LocalDate;

@Service
public class TutorService {
    private final SalaryRepository salaryRepository;
    private final TutorRepository tutorRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;

    public TutorService(SalaryRepository salaryRepository, TutorRepository tutorRepository, SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.salaryRepository = salaryRepository;
        this.tutorRepository = tutorRepository;
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    @Transactional
    public ResponseDTO create(final TutorDTO tutorDTO) {
        final Tutor tutor = this.schoolMapper.toEntity(tutorDTO);

        final School school = this.schoolRepository.findById(Long.parseLong(tutorDTO.getSchoolId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.SCHOOL_ID_NOT_FOUND));
        tutor.setSchool(school);

        final Tutor savedTutor = this.tutorRepository.save(tutor);

        final Salary salary = new Salary();
        salary.setSalaryPaid(true);
        salary.setMonth(LocalDate.now().getMonth().toString());
        salary.setTutor(savedTutor);
        this.salaryRepository.save(salary);

        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.CREATED);
        responseDTO.setStatusCode(HttpStatus.CREATED.value());
        responseDTO.setData(savedTutor);

        return responseDTO;
    }


    public PaginatedResponseDTO<Tutor> retrieveAll(int page, int size, String sortBy, String sortDir, String name, String subject) {
        final Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);

        final Page<Tutor> tutorPage = this.tutorRepository.searchTutor(name, subject, null, pageable);

        final PaginatedResponseDTO<Tutor> response = new PaginatedResponseDTO<>();
        response.setData(tutorPage.getContent());
        response.setPageNumber(tutorPage.getNumber());
        response.setPageSize(tutorPage.getSize());
        response.setTotalElements(tutorPage.getTotalElements());
        response.setTotalPages(tutorPage.getTotalPages());

        return response;
    }

    public ResponseDTO retrieveById(final Long id) {
        final Tutor tutor = this.tutorRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(tutor);
        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateById(final Long id, final Tutor tutor) {
        final Tutor existingTutor = this.tutorRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        if (tutor.getName() != null) {
            existingTutor.setName(tutor.getName());
        }

        if (tutor.getSubject() != null) {
            existingTutor.setSubject(tutor.getSubject());
        }

        if (tutor.getSalary() != 0) {
            existingTutor.setSalary(tutor.getSalary());
        }

        if (tutor.getSchool() != null) {
            existingTutor.setSchool(tutor.getSchool());
        }

        this.tutorRepository.save(existingTutor);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.UPDATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(existingTutor);
        return responseDTO;

    }

    public ResponseDTO remove(final Long id) {
        final ResponseDTO responseDTO = new ResponseDTO();
        final boolean exist = this.tutorRepository.existsById(id);
        if (exist) {
            this.tutorRepository.deleteById(id);
            responseDTO.setMessage(Constant.DELETED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(null);
            return responseDTO;
        } else {
            throw new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST);
        }
    }
}