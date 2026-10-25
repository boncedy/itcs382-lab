package bikeshare;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Station {
    private final String name;
    private final List<Bike> bikes = new ArrayList<>();

    public Station(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public Optional<Bike> rentAvailableBike() {
        Iterator<Bike> iterator = bikes.iterator();
        if (iterator.hasNext()) {
            Bike bike = iterator.next();
            iterator.remove();
            return Optional.of(bike);
        }
        return Optional.empty();
    }

    public void acceptReturnedBike(Bike bike) {
        bike.returnTo(name);
        bikes.add(bike);
    }
}
