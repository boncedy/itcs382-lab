package parcelpoint;

import java.io.IOException;
import java.util.List;

public interface ParcelStore {
    void save(List<Parcel> parcels) throws IOException;
}
