package schoolmanagementsystem.mapper;

import org.mapstruct.Mapper;
import schoolmanagementsystem.dto.CourseDTO;
import schoolmanagementsystem.dto.EnrollmentDTO;
import schoolmanagementsystem.dto.StudentDTO;
import schoolmanagementsystem.dto.TutorDTO;
import schoolmanagementsystem.entity.Course;
import schoolmanagementsystem.entity.Enrollment;
import schoolmanagementsystem.entity.Student;
import schoolmanagementsystem.entity.Tutor;

@Mapper(componentModel = "spring")
public interface SchoolMapper {
    StudentDTO toDTO(Student student);

    Student toEntity(StudentDTO studentDTO);

    TutorDTO toDTO(Tutor tutor);

    Tutor toEntity(TutorDTO tutorDTO);

    CourseDTO toDTO(CourseDTO courseDTO);

    Course toEntity(CourseDTO courseDTO);

    EnrollmentDTO toDTO(EnrollmentDTO enrollmentDTO);

    Enrollment toEntity(EnrollmentDTO enrollmentDTO);
}
