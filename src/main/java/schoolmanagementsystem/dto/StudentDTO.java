package schoolmanagementsystem.dto;

public class StudentDTO {

    private Long id;
    private String name;
    private Long dob;
    private String contactNumber;
    private String schoolId;
    private String tutorId;

    public StudentDTO(Long id, String name, Long dob, String contactNumber, String schoolId, String tutorId) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.contactNumber = contactNumber;
        this.schoolId = schoolId;
        this.tutorId = tutorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }
}

