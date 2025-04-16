package schoolmanagementsystem.dto;

import java.util.List;

public class TutorWithStudentsDTO {

    private TutorDTO tutor;
    private List<StudentDTO> students;

    public TutorWithStudentsDTO(TutorDTO tutor, List<StudentDTO> students) {
        this.tutor = tutor;
        this.students = students;
    }

    public TutorDTO getTutor() {
        return tutor;
    }

    public void setTutor(TutorDTO tutor) {
        this.tutor = tutor;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}
