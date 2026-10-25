package parcelpoint;

public class ParcelFeeCalculator {
    public double calculateFee(Parcel parcel, int days) {
        if (days <= 3) {
            return 0;
        }
        if (parcel.getSize() == 1) {
            return (days - 3) * 10.0;
        } else if (parcel.getSize() == 2) {
            return (days - 3) * 15.0;
        }
        return (days - 3) * 20.0;
    }
}
