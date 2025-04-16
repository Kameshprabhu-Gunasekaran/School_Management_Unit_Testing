package schoolmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import schoolmanagementsystem.entity.Tutor;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Query("SELECT t FROM Tutor t WHERE " +
            "(:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:id IS NULL OR t.id = :id)")
    Page<Tutor> searchTutor(@Param("name") String name,
                            @Param("subject") String subject,
                            @Param("id") Long id,
                            Pageable pageable);
}
