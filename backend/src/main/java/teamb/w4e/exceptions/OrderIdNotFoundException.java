package teamb.w4e.exceptions;

public class OrderIdNotFoundException extends Exception {

    private Long id;

    public OrderIdNotFoundException(Long id) {
        this.id = id;
    }

    public OrderIdNotFoundException() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
