package schoolmanagementsystem.dto;

public class TutorDTO {
    private String name;
    private String subject;
    private Double salary;
    private String month;
    private String schoolId;

    public TutorDTO() {
    }

    public TutorDTO(String name, String subject, Double salary, String month, String schoolId) {
        this.name = name;
        this.subject = subject;
        this.salary = salary;
        this.month = month;
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
