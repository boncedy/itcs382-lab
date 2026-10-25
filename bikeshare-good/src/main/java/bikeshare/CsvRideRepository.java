package bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvRideRepository implements RideRepository {
    private final Path path;

    public CsvRideRepository(Path path) {
        this.path = path;
    }

    @Override
    public void save(RideReceipt receipt) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(receipt.memberId() + "," + receipt.bikeId() + ","
                    + receipt.minutes() + "," + receipt.fee());
            writer.newLine();
        }
    }
}
