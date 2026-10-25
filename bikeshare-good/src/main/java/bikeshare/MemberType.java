package bikeshare;

import java.util.Locale;

public enum MemberType {
    STUDENT(2),
    STAFF(3);

    private final int bahtPerMinute;

    MemberType(int bahtPerMinute) {
        this.bahtPerMinute = bahtPerMinute;
    }

    public int getBahtPerMinute() {
        return bahtPerMinute;
    }

    public static MemberType fromString(String value) {
        try {
            return valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Member type must be student or staff", exception);
        }
    }
}
