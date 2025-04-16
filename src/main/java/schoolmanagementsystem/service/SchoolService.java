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
import schoolmanagementsystem.dto.SchoolDTO;
import schoolmanagementsystem.dto.SearchRequestDTO;
import schoolmanagementsystem.entity.School;
import schoolmanagementsystem.exception.BadRequestServiceAlertException;
import schoolmanagementsystem.mapper.SchoolMapper;
import schoolmanagementsystem.repository.SchoolRepository;
import schoolmanagementsystem.util.Constant;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;

    public SchoolService(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    @Transactional
    public ResponseDTO createSchool(final School school) {
        final School savedSchool = this.schoolRepository.save(school);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.CREATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(savedSchool);
        return responseDTO;
    }

    public PaginatedResponseDTO<SchoolDTO> searchSchools(SearchRequestDTO searchRequest) {
        final Sort sort = searchRequest.getSortDir().equalsIgnoreCase("desc") ?
                Sort.by(searchRequest.getSortBy()).descending() :
                Sort.by(searchRequest.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);

        Page<School> schoolPage = schoolRepository.searchSchool(
                searchRequest.getName(),
                searchRequest.getAddress(),
                searchRequest.getId(),
                pageable
        );

        List<SchoolDTO> schoolDTOs = schoolPage.getContent().stream()
                .map(school -> new SchoolDTO(school))
                .collect(Collectors.toList());

        PaginatedResponseDTO<SchoolDTO> response = new PaginatedResponseDTO<>();
        response.setData(schoolDTOs);
        response.setPageNumber(schoolPage.getNumber());
        response.setPageSize(schoolPage.getSize());
        response.setTotalElements(schoolPage.getTotalElements());
        response.setTotalPages(schoolPage.getTotalPages());

        return response;
    }

    public ResponseDTO retrieveById(final Long id) {
        final School school = this.schoolRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.RETRIEVED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(school);
        return responseDTO;
    }

    @Transactional
    public ResponseDTO updateById(final Long id, final School school) {
        final School existingSchool = this.schoolRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST));

        if (school.getName() != null) {
            existingSchool.setName(school.getName());
        }

        if (school.getAddress() != null) {
            existingSchool.setAddress(school.getAddress());
        }

        if (school.getContactNumber() != null) {
            existingSchool.setContactNumber(school.getContactNumber());
        }

        this.schoolRepository.save(existingSchool);
        final ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(Constant.UPDATED);
        responseDTO.setStatusCode(HttpStatus.OK.value());
        responseDTO.setData(existingSchool);
        return responseDTO;
    }


    @Transactional
    public ResponseDTO remove(final Long id) {
        final ResponseDTO responseDTO = new ResponseDTO();
        final boolean exist = this.schoolRepository.existsById(id);
        if (exist) {
            this.schoolRepository.deleteById(id);
            responseDTO.setMessage(Constant.DELETED);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(null);
            return responseDTO;
        } else {
            throw new BadRequestServiceAlertException(Constant.ID_DOES_NOT_EXIST);
        }
    }
}
