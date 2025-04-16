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
import schoolmanagementsystem.entity.Salary;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.repository.SalaryRepository;
import schoolmanagementsystem.util.Constant;

@Service
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @Transactional
    public ResponseDTO create(final Salary salary) {
        final Salary savedSalary = this.salaryRepository.save(salary);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.CREATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(savedSalary);
        return responseDTO;
    }

    public PaginatedResponseDTO<Salary> retrieveAll(int page, int size, String sortBy, String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);

        final Page<Salary> salaryPage = this.salaryRepository.findAll(pageable);

        final PaginatedResponseDTO<Salary> response = new PaginatedResponseDTO<>();
        response.setData(salaryPage.getContent());
        response.setPageNumber(salaryPage.getNumber());
        response.setPageSize(salaryPage.getSize());
        response.setTotalElements(salaryPage.getTotalElements());
        response.setTotalPages(salaryPage.getTotalPages());

        return response;
    }

    public ResponseDTO retrieveById(final Long id) {
        final Salary salary = this.salaryRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(salary);
        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateById(final Long id, final Salary salary) {
        final Salary existingSalary = this.salaryRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        if (salary.getMonth() != null) {
            existingSalary.setMonth(salary.getMonth());
        }
        if (salary.getSalaryPaid() != null) {
            existingSalary.setSalaryPaid(salary.getSalaryPaid());
        }
        if (salary.getTutor() != null) {
            existingSalary.setTutor(salary.getTutor());
        }

        this.salaryRepository.save(existingSalary);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.UPDATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(existingSalary);
        return responseDTO;
    }

    @Transactional
    public ResponseDTO remove(final Long id) {
        final ResponseDTO responseDTO = new ResponseDTO();
        final boolean exist = this.salaryRepository.existsById(id);
        if (exist) {
            this.salaryRepository.deleteById(id);
            responseDTO.setMessage(Constant.DELETED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(null);
            return responseDTO;
        } else {
            throw new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST);
        }
    }
}
