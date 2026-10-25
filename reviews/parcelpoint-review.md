# AI review of the ParcelPoint starter

Files read: `Main.java`, `Parcel.java`, `ParcelService.java`, and `Locker.java` under `parcelpoint/src/main/java/parcelpoint`.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---:|---|---|---|---|---|
| 1 | 1 | Main.java:8 | The `Scanner` wrapping `System.in` is not closed. | Should fix | Close it with try-with-resources if the application owns the input stream. |
| 2 | 1 | Main.java:11-17 | The command parser indexes `words` and parses integers without checking token counts or input values; malformed input can terminate the program. | Must fix | Validate the command and its arguments before indexing or parsing them. |
| 3 | 1 | ParcelService.java:42 | Parcel codes are compared with `==`, which compares String references and can fail for equal text from separate input strings. | Must fix | Compare the codes with `equals()`. |
| 4 | 1 | ParcelService.java:76 | The `FileWriter` is never closed, so output may not be flushed and the file resource leaks. | Must fix | Use try-with-resources for the writer. |
| 5 | 1 | ParcelService.java:80-81 | The empty catch block silently discards file-write failures. | Must fix | Propagate the `IOException` or report it through the application's error-handling path. |
| 6 | 1 | ParcelService.java:12 | `maxParcels` is never read, so it has no effect. | Should fix | Remove the field or enforce the intended capacity limit. |
| 7 | 1 | ParcelService.java:6 | `java.util.Date` is imported but unused. | Should fix | Remove the unused import. |
| 8 | 1 | ParcelService.java:20-22 | `getLockers()` returns the service's mutable internal list, allowing callers to change service state. | Should fix | Return an unmodifiable view or a defensive copy. |
| 9 | 1 | Parcel.java:7 | `status` is public, so callers can change parcel state without using a controlled operation. | Should fix | Make the field private and expose state transitions through methods. |
| 10 | 2 | ParcelService.java:64-72 | `printParcels(boolean)` has logical cohesion: one method selects between two different output modes based on a flag. | Should fix | Split the staff and waiting-list output into separate operations. |
| 11 | 2 | Main.java:19,21; ParcelService.java:64-72 | Passing `true` or `false` to `printParcels` is control coupling because the caller dictates which behavior the callee executes. | Should fix | Use separate methods or an explicit output operation instead of a behavior-changing boolean. |
| 12 | 2 | ParcelService.java:24-82 | `ParcelService` combines locker allocation, parcel state, fees, console/SMS output, and file persistence, indicating weak class cohesion. | Should fix | Keep the service focused on parcel operations and move presentation, fee calculation, and persistence behind separate responsibilities. |
| 13 | 2 | Parcel.java:7,13; ParcelService.java:28,44,68 | Parcel state is represented by string literals shared across classes, creating connascence of convention: both sides must use the same spellings. | Should fix | Represent state with an enum and use it consistently. |
| 14 | 2 | ParcelService.java:46,52-61 | `fee(Parcel, int)` receives a whole `Parcel` but uses only its size, which is stamp coupling. | Note | Pass only the size needed by the fee calculation. |

Findings by check:
- Check 1: 9
- Check 2: 5
