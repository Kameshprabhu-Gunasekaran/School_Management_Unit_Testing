package schoolmanagementsystem.dto;

import schoolmanagementsystem.entity.School;

public class SchoolDTO {

    private Long id;
    private String name;
    private String address;

    public SchoolDTO(School school) {
        this.id = school.getId();
        this.name = school.getName();
        this.address = school.getAddress();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
