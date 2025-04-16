package schoolmanagementsystem.dto;

public class EnrollmentDTO {
    private Long id;
    private String feesPaid;
    private String enrollmentStatus;
    private String studentId;
    private String courseId;

    public EnrollmentDTO(Long id, String feesPaid, String enrollmentStatus, String studentId, String courseId) {
        this.id = id;
        this.feesPaid = feesPaid;
        this.enrollmentStatus = enrollmentStatus;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeesPaid() {
        return feesPaid;
    }

    public void setFeesPaid(String feesPaid) {
        this.feesPaid = feesPaid;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
