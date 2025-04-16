package schoolmanagementsystem.dto;

public class SearchRequestDTO {

    private int page = 0;
    private int size = 10;
    private String sortBy = "name";
    private String sortDir = "asc";
    private String name;
    private String address;
    private Long id;

//    public SearchRequestDTO() {
//    }
//
//    public SearchRequestDTO(int page, int size, String sortBy, String sortDir, String name, String address, Long id) {
//        this.page = page;
//        this.size = size;
//        this.sortBy = sortBy;
//        this.sortDir = sortDir;
//        this.name = name;
//        this.address = address;
//        this.id = id;
//    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
