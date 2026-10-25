# AI review of bikeshare-poor

Files read: `BikeSharePoor.java`.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---:|---|---|---|---|---|
| 1 | 1 | BikeSharePoor.java:120 | The ride is written as comma-separated text without escaping a comma in a member ID; the command parser permits a token such as `A,B`, which would produce a malformed CSV row. | Should fix | Validate or encode CSV fields with a CSV-aware writer. |
| 2 | 2 | BikeSharePoor.java:17-19 | The mutable inventory and rental collections are public static fields, allowing any caller to change shared application state directly; this is common coupling. | Must fix | Make the state private and expose operations that preserve the inventory rules. |
| 3 | 2 | BikeSharePoor.java:22-25 | The nested `Ride` class exposes its member ID, type, and bike ID as public mutable fields. | Should fix | Make these fields private and expose read-only accessors. |
| 4 | 2 | BikeSharePoor.java:34-62 | `main` has logical cohesion: it reads and dispatches several distinct command types through different branches. | Note | Keep command dispatch in a dedicated menu/controller and move operations to focused classes. |
| 5 | 2 | BikeSharePoor.java:16-125 | `BikeSharePoor` combines command parsing, inventory, rentals, fee calculation, receipt output, and CSV persistence, indicating weak class cohesion. | Must fix | Separate the menu, bike/station state, rental rules, and ride persistence into focused classes. |
| 6 | 2 | BikeSharePoor.java:47-49,75-76,107 | Member type meaning is encoded by the shared integer convention `1 = student`, `2 = staff`, with another conditional mapping those values to rates. | Should fix | Replace the integer codes with a `MemberType` enum that owns each rate. |
| 7 | 2 | BikeSharePoor.java:51,96-97,112-114 | `finishRide` receives `printReceipt` and branches on it, so the caller controls an alternate behavior through a boolean (control coupling). | Should fix | Separate ride completion from receipt presentation, or always return a receipt for the caller to print. |

Findings by check:
- Check 1: 1
- Check 2: 6
