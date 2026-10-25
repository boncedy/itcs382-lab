package bikeshare;

import java.io.IOException;

public interface RideRepository {
    void save(RideReceipt receipt) throws IOException;
}
