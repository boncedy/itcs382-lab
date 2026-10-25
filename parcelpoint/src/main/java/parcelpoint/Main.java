package parcelpoint;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ParcelService service = new ParcelService();
        System.out.println("Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit");
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] words = line.split("\\s+");
                    if (words[0].equals("store")) {
                        Parcel parcel = new Parcel(words[1], words[2], Integer.parseInt(words[3]));
                        Locker locker = service.storeParcel(parcel);
                        if (locker == null) {
                            System.out.println("No free locker");
                        } else {
                            System.out.println("SMS to " + parcel.getStudentId() + ": parcel "
                                    + parcel.getCode() + " is in locker " + locker.getNumber());
                            System.out.println("Locker " + locker.getNumber());
                        }
                    } else if (words[0].equals("pickup")) {
                        System.out.println("Fee: " + service.pickUp(words[1], Integer.parseInt(words[2])));
                    } else if (words[0].equals("list")) {
                        service.getParcels().stream()
                                .filter(parcel -> parcel.getStatus() == Parcel.Status.STORED)
                                .forEach(parcel -> System.out.println(parcel.getCode() + " is waiting"));
                    } else if (words[0].equals("staff")) {
                        service.getParcels().forEach(parcel -> System.out.println(
                                parcel.getCode() + " " + parcel.getStudentId() + " " + parcel.getStatus()));
                        System.out.println("Free lockers: " + service.getFreeLockerCount());
                    } else if (words[0].equals("quit")) {
                        break;
                    }
                }
            }
        }
    }
}
