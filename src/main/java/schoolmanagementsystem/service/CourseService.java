package schoolmanagementsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import schoolmanagementsystem.dto.CourseDTO;
import schoolmanagementsystem.dto.PaginatedResponseDTO;
import schoolmanagementsystem.dto.ResponseDTO;
import schoolmanagementsystem.entity.Course;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.entity.Tutor;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.CourseRepository;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.repository.TutorRepository;
import schoolmanagementsystem.util.Constant;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final SchoolRepository schoolRepository;
    private final TutorRepository tutorRepository;
    private final SchoolMapper schoolMapper;

    public CourseService(CourseRepository courseRepository, SchoolRepository schoolRepository, TutorRepository tutorRepository, SchoolMapper schoolMapper) {
        this.courseRepository = courseRepository;
        this.schoolRepository = schoolRepository;
        this.tutorRepository = tutorRepository;
        this.schoolMapper = schoolMapper;
    }

    @Transactional
    public ResponseDTO create(final CourseDTO courseDTO) {
        if (courseDTO.getName() == null || courseDTO.getName().isEmpty()) {
            throw new BadRequestServiceAlertException(Constant.COURSE_NAME_REQUIRED);
        }

        final Course course = this.schoolMapper.toEntity(courseDTO);

        if (course.getFees() == null) {
            course.setFees(0L);
        }

        final Tutor tutor = this.tutorRepository.findById(Long.parseLong(courseDTO.getTutorId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.TUTOR_ID_NOT_FOUND));
        course.setTutor(tutor);

        final School school = this.schoolRepository.findById(Long.parseLong(courseDTO.getSchoolId()))
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.SCHOOL_ID_NOT_FOUND));
        course.setSchool(school);

        final Course savedCourse = this.courseRepository.save(course);

        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.CREATED);
        responseDTO.setStatusCode(HttpStatus.CREATED.value());
        responseDTO.setData(savedCourse);

        return responseDTO;
    }


    public PaginatedResponseDTO<Course> retrieveAll(int page, int size, String sortBy, String sortDir, String name, Long id) {
        final Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);

        final Page<Course> coursePage = this.courseRepository.searchCourse(name, id, pageable);

        final PaginatedResponseDTO<Course> response = new PaginatedResponseDTO<>();
        response.setData(coursePage.getContent());
        response.setPageNumber(coursePage.getNumber());
        response.setPageSize(coursePage.getSize());
        response.setTotalElements(coursePage.getTotalElements());
        response.setTotalPages(coursePage.getTotalPages());

        return response;
    }

    public ResponseDTO retrieveById(final Long id) {
        final Course course = this.courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(course);
        return responseDTO;

    }

    @Transactional
    public ResponseDTO updateById(final Long id, final Course course) {
        final Course existingCourse = this.courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        if (course.getName() != null) {
            existingCourse.setName(course.getName());
        }

        if (course.getFees() != null) {
            existingCourse.setFees(course.getFees());
        }

        if (course.getSchool() != null) {
            existingCourse.setSchool(course.getSchool());
        }

        this.courseRepository.save(existingCourse);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.UPDATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(existingCourse);
        return responseDTO;
    }

    public ResponseDTO remove(final Long id) {
        final boolean exist = this.courseRepository.existsById(id);
        if (exist) {
            this.courseRepository.deleteById(id);
            final ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setMessage(Constant.DELETED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(null);
            return responseDTO;
        } else {
            throw new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST);
        }
    }
}
