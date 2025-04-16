package schoolmanagementsystem.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import schoolmanagementsystem.dto.CourseDTO;
import schoolmanagementsystem.dto.EnrollmentDTO;
import schoolmanagementsystem.dto.StudentDTO;
import schoolmanagementsystem.dto.TutorDTO;
import schoolmanagementsystem.entity.Course;
import schoolmanagementsystem.entity.Enrollment;
import schoolmanagementsystem.entity.Student;
import schoolmanagementsystem.entity.Tutor;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-08T16:12:26-0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class SchoolMapperImpl implements SchoolMapper {

    @Override
    public StudentDTO toDTO(Student student) {
        if ( student == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Long dob = null;
        String contactNumber = null;

        id = student.getId();
        name = student.getName();
        dob = student.getDob();
        contactNumber = student.getContactNumber();

        String schoolId = null;
        String tutorId = null;

        StudentDTO studentDTO = new StudentDTO( id, name, dob, contactNumber, schoolId, tutorId );

        return studentDTO;
    }

    @Override
    public Student toEntity(StudentDTO studentDTO) {
        if ( studentDTO == null ) {
            return null;
        }

        Student student = new Student();

        student.setId( studentDTO.getId() );
        student.setName( studentDTO.getName() );
        student.setDob( studentDTO.getDob() );
        student.setContactNumber( studentDTO.getContactNumber() );

        return student;
    }

    @Override
    public TutorDTO toDTO(Tutor tutor) {
        if ( tutor == null ) {
            return null;
        }

        TutorDTO tutorDTO = new TutorDTO();

        tutorDTO.setName( tutor.getName() );
        tutorDTO.setSubject( tutor.getSubject() );
        tutorDTO.setSalary( tutor.getSalary() );

        return tutorDTO;
    }

    @Override
    public Tutor toEntity(TutorDTO tutorDTO) {
        if ( tutorDTO == null ) {
            return null;
        }

        Tutor tutor = new Tutor();

        tutor.setName( tutorDTO.getName() );
        tutor.setSubject( tutorDTO.getSubject() );
        if ( tutorDTO.getSalary() != null ) {
            tutor.setSalary( tutorDTO.getSalary() );
        }

        return tutor;
    }

    @Override
    public CourseDTO toDTO(CourseDTO courseDTO) {
        if ( courseDTO == null ) {
            return null;
        }

        String name = null;
        Long fees = null;
        String schoolId = null;
        String tutorId = null;

        name = courseDTO.getName();
        fees = courseDTO.getFees();
        schoolId = courseDTO.getSchoolId();
        tutorId = courseDTO.getTutorId();

        CourseDTO courseDTO1 = new CourseDTO( name, fees, schoolId, tutorId );

        return courseDTO1;
    }

    @Override
    public Course toEntity(CourseDTO courseDTO) {
        if ( courseDTO == null ) {
            return null;
        }

        Course course = new Course();

        course.setName( courseDTO.getName() );
        course.setFees( courseDTO.getFees() );

        return course;
    }

    @Override
    public EnrollmentDTO toDTO(EnrollmentDTO enrollmentDTO) {
        if ( enrollmentDTO == null ) {
            return null;
        }

        Long id = null;
        String feesPaid = null;
        String enrollmentStatus = null;
        String studentId = null;
        String courseId = null;

        id = enrollmentDTO.getId();
        feesPaid = enrollmentDTO.getFeesPaid();
        enrollmentStatus = enrollmentDTO.getEnrollmentStatus();
        studentId = enrollmentDTO.getStudentId();
        courseId = enrollmentDTO.getCourseId();

        EnrollmentDTO enrollmentDTO1 = new EnrollmentDTO( id, feesPaid, enrollmentStatus, studentId, courseId );

        return enrollmentDTO1;
    }

    @Override
    public Enrollment toEntity(EnrollmentDTO enrollmentDTO) {
        if ( enrollmentDTO == null ) {
            return null;
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setId( enrollmentDTO.getId() );
        enrollment.setFeesPaid( enrollmentDTO.getFeesPaid() );
        enrollment.setEnrollmentStatus( enrollmentDTO.getEnrollmentStatus() );

        return enrollment;
    }
}
