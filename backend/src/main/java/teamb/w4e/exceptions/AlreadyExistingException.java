package teamb.w4e.exceptions;

public class AlreadyExistingException extends Exception {

    private String conflictingName;

    public AlreadyExistingException(String name) {
        conflictingName = name;
    }

    public AlreadyExistingException() {
    }

    public String getConflictingName() {
        return conflictingName;
    }

    public void setConflictingName(String conflictingName) {
        this.conflictingName = conflictingName;
    }

}
