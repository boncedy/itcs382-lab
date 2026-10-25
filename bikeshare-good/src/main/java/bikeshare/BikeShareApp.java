package bikeshare;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class BikeShareApp {
    public static void main(String[] args) {
        BikeShareService service = new BikeShareService(new CsvRideRepository(Path.of("rides.csv")));
        System.out.println("Commands: rent <memberId> <student|staff> <station>, "
                + "return <memberId> <station> <minutes>, quit");
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] words = line.split("\\s+");
                try {
                    if (words[0].equals("rent") && words.length == 4) {
                        Bike bike = service.rentBike(words[1], MemberType.fromString(words[2]), words[3]);
                        System.out.println("Bike " + bike.getId() + " rented to " + words[1]);
                    } else if (words[0].equals("return") && words.length == 4) {
                        RideReceipt receipt = service.returnBike(
                                words[1], words[2], Integer.parseInt(words[3]));
                        System.out.println("Bike: " + receipt.bikeId() + ", Minutes: "
                                + receipt.minutes() + ", Fee: " + receipt.fee());
                    } else if (words[0].equals("quit")) {
                        break;
                    } else {
                        System.out.println("Unknown command");
                    }
                } catch (IllegalArgumentException | IOException exception) {
                    System.err.println("Error: " + exception.getMessage());
                }
            }
        }
    }
}
