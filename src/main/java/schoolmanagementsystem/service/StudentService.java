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
import schoolmanagementsystem.dto.StudentCourseDTO;
import schoolmanagementsystem.dto.StudentDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.entity.Student;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.repository.StudentRepository;
import schoolmanagementsystem.repository.TutorRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolMapper schoolMapper;
    private final TutorRepository tutorRepository;
    private final SchoolRepository schoolRepository;

    public StudentService(StudentRepository studentRepository, SchoolMapper schoolMapper, TutorRepository tutorRepository, SchoolRepository schoolRepository) {
        this.studentRepository = studentRepository;
        this.schoolMapper = schoolMapper;
        this.tutorRepository = tutorRepository;
        this.schoolRepository = schoolRepository;

    }


    @Transactional
    public ResponseDTO create(final StudentDTO studentDTO) {
        final Student student = this.schoolMapper.toEntity(studentDTO);

        final Tutor tutor = this.tutorRepository.findById(Long.parseLong(studentDTO.getTutorId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.TUTOR_ID_NOT_FOUND));

        student.setTutor(tutor);

        final School school = this.schoolRepository.findById(Long.parseLong(studentDTO.getSchoolId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.SCHOOL_ID_NOT_FOUND));

        student.setSchool(school);

        final Student savedStudent = this.studentRepository.save(student);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.CREATED);
        responseDTO.setStatusCode(HttpStatus.CREATED.value());
        responseDTO.setData(savedStudent);
        return responseDTO;
    }

    public PaginatedResponseDTO<Student> retrieveAll(int page, int size, String sortBy, String sortDir, String name, Long dob) {
        final Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);

        final Page<Student> studentPage = this.studentRepository.searchStudent(name, dob, null, pageable);

        final PaginatedResponseDTO<Student> response = new PaginatedResponseDTO<>();
        response.setData(studentPage.getContent());
        response.setPageNumber(studentPage.getNumber());
        response.setPageSize(studentPage.getSize());
        response.setTotalElements(studentPage.getTotalElements());
        response.setTotalPages(studentPage.getTotalPages());

        return response;
    }

    public ResponseDTO retrieveById(final Long id) {
        final Student student = this.studentRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(student);
        return responseDTO;
    }


    @Transactional
    public ResponseDTO updateById(final Long id, final Student student) {
        final Student existingStudent = this.studentRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        if (student.getName() != null) {
            existingStudent.setName(student.getName());
        }

        if (student.getDob() != null) {
            existingStudent.setDob(student.getDob());
        }

        if (student.getContactNumber() != null) {
            existingStudent.setContactNumber(student.getContactNumber());
        }

        if (student.getSchool() != null) {
            existingStudent.setSchool(student.getSchool());
        }

        this.studentRepository.save(existingStudent);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.UPDATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(existingStudent);
        return responseDTO;
    }


    public ResponseDTO remove(final Long id) {
        final ResponseDTO responseDTO = new ResponseDTO();
        final boolean exist = this.studentRepository.existsById(id);
        if (exist) {
            this.studentRepository.deleteById(id);
            responseDTO.setMessage(Constant.DELETED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(null);
            return responseDTO;
        } else {
            throw new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST);
        }
    }

    public ResponseDTO getStudentsByCourseAndSchool(Long courseId, Long schoolId) {
        final List<StudentCourseDTO> students = this.studentRepository.findStudentsByCourseAndSchool(courseId, schoolId);
        final ResponseDTO responseDTO = new ResponseDTO();

        if (students == null) {
            responseDTO.setMessage(Constant.ID_DOES_NOT_EXIST);
            responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseDTO.setData(null);
        } else {
            responseDTO.setMessage(Constant.RETRIEVED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(students);
        }

        return responseDTO;
    }

    public ResponseDTO getStudentsByTutorId(final Long tutorId) {
        final List<Student> students = this.studentRepository.findStudentsByTutorId(tutorId);

        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(students);

        return responseDTO;
    }
}
