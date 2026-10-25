package bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class BikeSharePoor {
    public static final Map<String, String> bikeStations = new LinkedHashMap<>();
    public static final Set<String> availableBikes = new LinkedHashSet<>();
    public static final Map<String, Ride> activeRides = new HashMap<>();
    private static final Path ridesFile = Path.of("rides.csv");

    public static class Ride {
        public String memberId;
        public int memberType;
        public String bikeId;

        public Ride(String memberId, int memberType, String bikeId) {
            this.memberId = memberId;
            this.memberType = memberType;
            this.bikeId = bikeId;
        }
    }

    public static void main(String[] args) {
        initializeBikes();
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
                        int memberType = words[2].equalsIgnoreCase("student") ? 1
                                : words[2].equalsIgnoreCase("staff") ? 2 : 0;
                        rentBike(words[1], memberType, words[3]);
                    } else if (words[0].equals("return") && words.length == 4) {
                        finishRide(words[1], words[2], Integer.parseInt(words[3]), true);
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

    private static void initializeBikes() {
        for (String station : new String[]{"Library", "Canteen", "Dorm"}) {
            for (int number = 1; number <= 2; number++) {
                String bikeId = station + "-" + number;
                bikeStations.put(bikeId, station);
                availableBikes.add(bikeId);
            }
        }
    }

    private static void rentBike(String memberId, int memberType, String station) {
        if (memberType != 1 && memberType != 2) {
            System.out.println("Member type must be student or staff");
            return;
        }
        if (activeRides.containsKey(memberId)) {
            System.out.println("Member already has an active ride");
            return;
        }
        Iterator<String> iterator = availableBikes.iterator();
        while (iterator.hasNext()) {
            String bikeId = iterator.next();
            if (bikeStations.get(bikeId).equalsIgnoreCase(station)) {
                iterator.remove();
                activeRides.put(memberId, new Ride(memberId, memberType, bikeId));
                System.out.println("Bike " + bikeId + " rented to " + memberId);
                return;
            }
        }
        System.out.println("No bike available at " + station);
    }

    private static void finishRide(String memberId, String station, int minutes,
                                   boolean printReceipt) throws IOException {
        Ride ride = activeRides.get(memberId);
        String returnStation = bikeStations.values().stream()
                .filter(name -> name.equalsIgnoreCase(station))
                .findFirst()
                .orElse(null);
        if (ride == null || minutes < 0 || returnStation == null) {
            System.out.println("No active ride or invalid ride time");
            return;
        }
        double fee = minutes * (ride.memberType == 1 ? 2.0 : 3.0);
        appendRide(ride, minutes, fee);
        bikeStations.put(ride.bikeId, returnStation);
        availableBikes.add(ride.bikeId);
        activeRides.remove(memberId);
        if (printReceipt) {
            System.out.println("Bike: " + ride.bikeId + ", Minutes: " + minutes + ", Fee: " + fee);
        }
    }

    private static void appendRide(Ride ride, int minutes, double fee) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(ridesFile,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(ride.memberId + "," + ride.bikeId + "," + minutes + "," + fee);
            writer.newLine();
        }
    }
}
