package schoolmanagementsystem.dto;

public class EnrollmentRequestDTO {

    private Long studentId;
    private Long courseId;
    private Long tutorId;
    private Long incrementAmount;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Long getIncrementAmount() {
        return incrementAmount;
    }

    public void setIncrementAmount(Long incrementAmount) {
        this.incrementAmount = incrementAmount;
    }
}
