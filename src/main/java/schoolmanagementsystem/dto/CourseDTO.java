package schoolmanagementsystem.dto;

public class CourseDTO {
    private String name;
    private Long fees;
    private String schoolId;
    private String tutorId;

    public CourseDTO(String name, Long fees, String schoolId, String tutorId) {
        this.name = name;
        this.fees = fees;
        this.schoolId = schoolId;
        this.tutorId = tutorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String courseName) {
        this.name = courseName;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long courseFees) {
        this.fees = courseFees;
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

    @Override
    public String toString() {
        return "CourseDTO{" +
                "name='" + name + '\'' +
                ", tutorId='" + tutorId + '\'' +
                ", schoolId='" + schoolId + '\'' +
                ", fees=" + fees +
                '}';
    }
}
