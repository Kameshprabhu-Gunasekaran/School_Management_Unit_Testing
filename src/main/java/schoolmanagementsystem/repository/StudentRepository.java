package schoolmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import schoolmanagementsystem.dto.StudentCourseDTO;
import schoolmanagementsystem.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT new schoolmanagementsystem.dto.StudentCourseDTO(" +
            "st.name, st.dob, st.contactNumber, c.name, sc.name) " +
            "FROM Student st " +
            "JOIN Enrollment e ON st.id = e.student.id " +
            "JOIN Course c ON e.course.id = c.id " +
            "JOIN School sc ON st.school.id = sc.id " +
            "WHERE c.id = :courseId AND st.school.id = :schoolId")
    List<StudentCourseDTO> findStudentsByCourseAndSchool(@Param("courseId") Long courseId,
                                                         @Param("schoolId") Long schoolId);

    @Query(value = "select * from student s where s.tutor_id= :tutorId", nativeQuery = true)
    List<Student> findStudentsByTutorId(@Param("tutorId") Long tutorId);

    @Query("SELECT s FROM Student s WHERE " +
            "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:dob IS NULL OR s.dob = :dob) AND " +
            "(:id IS NULL OR s.id = :id)")
    Page<Student> searchStudent(@Param("name") String name,
                                @Param("dob") Long dob,
                                @Param("id") Long id,
                                Pageable pageable);

}
