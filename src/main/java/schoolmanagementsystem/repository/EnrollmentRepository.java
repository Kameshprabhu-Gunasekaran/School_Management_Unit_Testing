package schoolmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import schoolmanagementsystem.entity.Enrollment;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e WHERE" +
            "(:enrollmentStatus IS NULL or LOWER (e.enrollmentStatus) LIKE LOWER(CONCAT('%', :enrollmentStatus, '%'))) AND " +
            "(:id IS NULL OR e.id = :id)")
    Page<Enrollment> searchEnrollment(@Param("enrollmentStatus") String enrollmentStatus,
                                      @Param("id") Long id,
                                      Pageable pageable);
}

