package schoolmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import schoolmanagementsystem.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

}
