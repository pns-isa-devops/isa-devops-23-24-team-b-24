package teamb.w4e.exceptions;

public class IdNotFoundException extends Exception {

    private Long id;

    public IdNotFoundException(Long id) {
        this.id = id;
    }

    public IdNotFoundException() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
