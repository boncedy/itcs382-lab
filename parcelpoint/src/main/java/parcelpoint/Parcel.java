package parcelpoint;

public class Parcel {
    public enum Status {
        NEW,
        STORED,
        PICKED_UP
    }

    private final String code;
    private final String studentId;
    private final int size;          // 1 = small, 2 = medium, 3 = large
    private Status status = Status.NEW;

    public Parcel(String code, String studentId, int size) {
        this.code = code;
        this.studentId = studentId;
        this.size = size;
    }

    public String getCode() {
        return code;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getSize() {
        return size;
    }

    public Status getStatus() {
        return status;
    }

    public void markStored() {
        status = Status.STORED;
    }

    public void markPickedUp() {
        status = Status.PICKED_UP;
    }
}
