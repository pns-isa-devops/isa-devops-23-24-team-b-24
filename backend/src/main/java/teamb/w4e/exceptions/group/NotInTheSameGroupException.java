package teamb.w4e.exceptions.group;

public class NotInTheSameGroupException extends Exception{
    private final String name;

    public String getName() {
        return name;
    }

    public NotInTheSameGroupException(String name) {
        this.name = name;
    }
}
