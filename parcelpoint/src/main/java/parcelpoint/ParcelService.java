package parcelpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParcelService {
    private final List<Locker> lockers = new ArrayList<>();
    private final List<Parcel> parcels = new ArrayList<>();
    private final ParcelStore parcelStore;
    private final ParcelFeeCalculator feeCalculator;

    public ParcelService() {
        this(new CsvParcelStore("parcels.csv"), new ParcelFeeCalculator());
    }

    public ParcelService(ParcelStore parcelStore, ParcelFeeCalculator feeCalculator) {
        this.parcelStore = parcelStore;
        this.feeCalculator = feeCalculator;
        lockers.add(new Locker(1, 1));
        lockers.add(new Locker(2, 2));
        lockers.add(new Locker(3, 3));
    }

    public List<Parcel> getParcels() {
        return List.copyOf(parcels);
    }

    public long getFreeLockerCount() {
        return lockers.stream().filter(Locker::isFree).count();
    }

    public Locker storeParcel(Parcel parcel) throws IOException {
        for (Locker locker : lockers) {
            if (locker.isFree() && locker.getSize() >= parcel.getSize()) {
                locker.setParcel(parcel);
                parcel.markStored();
                parcels.add(parcel);
                saveToFile();
                return locker;
            }
        }
        return null;
    }

    public double pickUp(String code, int days) throws IOException {
        for (Locker locker : lockers) {
            Parcel parcel = locker.getParcel();
            if (parcel != null && parcel.getCode().equals(code)) {
                locker.setParcel(null);
                parcel.markPickedUp();
                saveToFile();
                return feeCalculator.calculateFee(parcel, days);
            }
        }
        return 0;
    }

    private void saveToFile() throws IOException {
        parcelStore.save(parcels);
    }
}
