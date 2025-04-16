package schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import schoolmanagementsystem.util.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<schoolmanagementsystem.entity.Role, Long> {

    Optional<schoolmanagementsystem.entity.Role> findByName(Role name);
}
