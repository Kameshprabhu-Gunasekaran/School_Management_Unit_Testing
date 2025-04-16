package schoolmanagementsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import schoolmanagementsystem.dto.EnrollmentDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Course;
import schoolmanagementsystem.entity.Enrollment;
import schoolmanagementsystem.entity.Student;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.CourseRepository;
import schoolmanagementsystem.repository.EnrollmentRepository;
import schoolmanagementsystem.repository.StudentRepository;
import schoolmanagementsystem.repository.TutorRepository;
import schoolmanagementsystem.util.Constant;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final SchoolMapper schoolMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
                             TutorRepository tutorRepository, StudentRepository studentRepository, SchoolMapper schoolMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
        this.schoolMapper = schoolMapper;
    }

    @Transactional
    public ResponseDTO create(final EnrollmentDTO enrollmentDTO) {
        final Enrollment enrollment = this.schoolMapper.toEntity(enrollmentDTO);

        final Student student = this.studentRepository.findById(Long.parseLong(enrollmentDTO.getStudentId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.STUDENT_ID_NOT_FOUNT));
        enrollment.setStudent(student);

        final Course course = this.courseRepository.findById(Long.parseLong(enrollmentDTO.getCourseId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.COURSE_ID_NOT_FOUND));
        enrollment.setCourse(course);

        final Enrollment savedEnrollment = this.enrollmentRepository.save(enrollment);

        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.CREATED);
        responseDTO.setStatusCode(HttpStatus.CREATED.value());
        responseDTO.setData(savedEnrollment);

        return responseDTO;
    }


    public PaginatedResponseDTO<Enrollment> retrieveAll(int page, int size, String sortBy, String sortDir, String enrollmentStatus, Long id) {
        final Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);

        final Page<Enrollment> enrollmentPage = this.enrollmentRepository.searchEnrollment(enrollmentStatus, id, pageable);

        final PaginatedResponseDTO<Enrollment> response = new PaginatedResponseDTO<>();
        response.setData(enrollmentPage.getContent());
        response.setPageNumber(enrollmentPage.getNumber());
        response.setPageSize(enrollmentPage.getSize());
        response.setTotalElements(enrollmentPage.getTotalElements());
        response.setTotalPages(enrollmentPage.getTotalPages());

        return response;
    }

    public ResponseDTO retrieveById(final Long id) {
        final Enrollment enrollment = this.enrollmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(enrollment);
        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateById(final Long id, final Enrollment enrollment) {
        final Enrollment existingEnrollment = this.enrollmentRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        if (enrollment.isFeesPaid() != null) {
            existingEnrollment.setFeesPaid(enrollment.isFeesPaid());
        }
        if (enrollment.getEnrollmentStatus() != null) {
            existingEnrollment.setEnrollmentStatus(enrollment.getEnrollmentStatus());
        }

        if (enrollment.getStudent() != null) {
            existingEnrollment.setStudent(enrollment.getStudent());
        }

        if (enrollment.getCourse() != null) {
            existingEnrollment.setCourse(enrollment.getCourse());
        }

        this.enrollmentRepository.save(existingEnrollment);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.UPDATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(existingEnrollment);
        return responseDTO;
    }

    public ResponseDTO remove(final Long id) {
        final boolean exist = this.enrollmentRepository.existsById(id);
        if (exist) {
            this.enrollmentRepository.deleteById(id);
            final ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setMessage(Constant.DELETED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(null);
            return responseDTO;
        } else {
            throw new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST);
        }
    }

    @Transactional
    public ResponseDTO enrollStudentAndIncreaseTutorSalary(Long studentId, Long courseId, Long tutorId, Long incrementAmount) {
        final Student student = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        final Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        final Tutor tutor = this.tutorRepository.findById(tutorId)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        final Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentStatus(Constant.ENROLLED);
        enrollment.setFeesPaid(Constant.NO);
        this.enrollmentRepository.save(enrollment);

        if (incrementAmount > 0) {
            tutor.setSalary(tutor.getSalary() + incrementAmount);
            this.tutorRepository.save(tutor);
        } else {
            throw new BadRequestServiceAlertException(Constant.SALARY_INCREMENT);
        }

        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Student enrolled successfully, and tutor's salary updated");
        responseDTO.setStatusCode(HttpStatus.CREATED.value());
        responseDTO.setData(enrollment);
        return responseDTO;
    }

}
