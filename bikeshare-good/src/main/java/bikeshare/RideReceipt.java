package bikeshare;

public record RideReceipt(String memberId, String bikeId, int minutes, double fee) {
}
