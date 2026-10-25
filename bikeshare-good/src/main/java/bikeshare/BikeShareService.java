package bikeshare;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BikeShareService {
    private final Map<String, Station> stations = new LinkedHashMap<>();
    private final Map<String, Rental> activeRentals = new LinkedHashMap<>();
    private final RideRepository rideRepository;

    public BikeShareService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
        addStation("Library");
        addStation("Canteen");
        addStation("Dorm");
    }

    public Bike rentBike(String memberId, MemberType memberType, String stationName) {
        if (memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be blank");
        }
        if (activeRentals.containsKey(memberId)) {
            throw new IllegalArgumentException("Member already has an active ride");
        }
        Station station = findStation(stationName);
        Bike bike = station.rentAvailableBike()
                .orElseThrow(() -> new IllegalArgumentException("No bike available at " + station.getName()));
        activeRentals.put(memberId, new Rental(memberId, memberType, bike));
        return bike;
    }

    public RideReceipt returnBike(String memberId, String stationName, int minutes) throws IOException {
        if (minutes < 0) {
            throw new IllegalArgumentException("Ride time cannot be negative");
        }
        Rental rental = activeRentals.get(memberId);
        if (rental == null) {
            throw new IllegalArgumentException("Member has no active ride");
        }
        Station station = findStation(stationName);
        double fee = rental.memberType().getBahtPerMinute() * minutes;
        RideReceipt receipt = new RideReceipt(memberId, rental.bike().getId(), minutes, fee);
        rideRepository.save(receipt);
        activeRentals.remove(memberId);
        station.acceptReturnedBike(rental.bike());
        return receipt;
    }

    private void addStation(String name) {
        Station station = new Station(name);
        stations.put(key(name), station);
        for (int number = 1; number <= 2; number++) {
            station.addBike(new Bike(name + "-" + number, name));
        }
    }

    private Station findStation(String name) {
        Station station = stations.get(key(name));
        if (station == null) {
            throw new IllegalArgumentException("Unknown station: " + name);
        }
        return station;
    }

    private String key(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
