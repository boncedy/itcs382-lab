package parcelpoint;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvParcelStore implements ParcelStore {
    private final Path path;

    public CsvParcelStore(String fileName) {
        path = Path.of(fileName);
    }

    @Override
    public void save(List<Parcel> parcels) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Parcel parcel : parcels) {
                writer.write(parcel.getCode() + "," + parcel.getStatus());
                writer.newLine();
            }
        }
    }
}
