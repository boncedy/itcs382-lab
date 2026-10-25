# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/boncedy/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | `java -version` on PATH reports Java 8.0.511; Maven runs with Eclipse Adoptium Java 17.0.20.1 |
| Maven | Apache Maven 3.10.0 |
| IDE | Visual Studio Code 1.143.1 |
| SonarQube for IDE | SonarQube for IDE (SonarLint) 6.2.0 |
| AI tool and exact model | Copilot SDK in VS Code; exact model not exposed in this session |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change:
`336d7b6 Add lab starter files` (`336d7b6d59d4839e75e4ed1c74667f874916bbc5`)

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 2 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 3 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 4 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |
| 5 | java:S1172 | Main.java:6 | Remove this unused method parameter "args". |
| 6 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 7 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 8 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 9 | java:S1698 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 10 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 11 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 12 | java:S2095 | ParcelService.java:76 | Resources should be closed. |
| 13 | java:S2093 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 14 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |

Analysis note: SonarQube for IDE was triggered on an isolated copy of the committed starter sources because the working tree already contains uncommitted ParcelPoint edits. The IDE's Problems view showed these findings. Maven 3.10.0 is installed and uses Java 17.0.20.1, but the `java` command on PATH still reports Java 8.0.511; the standalone Java command therefore does not meet the lab's JDK 17 requirement.

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 2026-10-24 |
| AI tool and exact model | Copilot SDK in VS Code; exact model not exposed in this session |
| Commit reviewed | 336d7b6d59d4839e75e4ed1c74667f874916bbc5 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | Read the committed starter Java files from the workspace |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | Main.java:8 | Scanner is not closed. | AI | Resource leak | Yes; the Scanner is created and not closed. |
| 2 | Main.java:11-17 | Malformed commands can cause indexing or number-parsing exceptions. | AI | Input validation | Yes; token count and numeric format are unchecked. |
| 3 | Main.java:6 | Unused `args` parameter. | SonarQube | java:S1172 | Yes; the method does not read the parameter. |
| 4 | Main.java:9,15,17,22 | Console output uses System.out. | SonarQube | java:S106 | Yes; each listed line writes to System.out. |
| 5 | Parcel.java:7 | `status` is public and directly mutable. | AI | Encapsulation | Yes; callers can assign any string value. |
| 6 | ParcelService.java:6 | Unused Date import. | SonarQube | java:S1128 | Yes; Date is not referenced. |
| 7 | ParcelService.java:12 | Unused `maxParcels` field. | SonarQube | java:S1068 | Yes; its value is never read. |
| 8 | ParcelService.java:20-22 | `getLockers()` exposes the mutable internal collection. | AI | Encapsulation / strong coupling | Yes; callers can add or remove lockers directly. |
| 9 | ParcelService.java:28,44,68 | State is represented using repeated string literals. | AI | Connascence of convention | Yes; the same state spellings are shared between classes. |
| 10 | ParcelService.java:30,67,69 | Console output uses System.out. | SonarQube | java:S106 | Yes; each listed line writes to System.out. |
| 11 | ParcelService.java:42 | String codes are compared using `==`. | SonarQube and AI | java:S1698 / probable bug | Yes; equal text in distinct String objects may not match. |
| 12 | ParcelService.java:46,52-61 | Fee calculation is given the whole Parcel although it only needs size. | AI | Stamp coupling | Yes; only `getSize()` is used by `fee`. |
| 13 | ParcelService.java:64-72 | One method selects between two output behaviors using a boolean. | AI | Logical cohesion / control coupling | Yes; the flag selects staff versus waiting-list output. |
| 14 | ParcelService.java:24-82 | ParcelService handles domain operations, fees, output, and persistence. | AI | Weak class cohesion | Yes; the class mixes several responsibilities. |
| 15 | ParcelService.java:76 | FileWriter is not closed. | SonarQube and AI | java:S2095, java:S2093 / resource leak | Yes; no close or try-with-resources is used. |
| 16 | ParcelService.java:80-81 | IOException is silently swallowed by an empty catch block. | SonarQube and AI | java:S108 / error handling | Yes; a failed save is not reported. |

2.1 Two problems only the AI review found: `getLockers()` exposes the mutable internal list (ParcelService.java:20-22), and `ParcelService` has weak class cohesion because it mixes domain, presentation, fee, and persistence responsibilities (ParcelService.java:24-82).

2.2 One problem only SonarQube found: the unused `args` parameter in `Main.main` (java:S1172, Main.java:6).

2.3 The cohesion and coupling findings are design judgments rather than mechanical facts. I checked the method branches, boolean argument, and responsibilities in the source; the named `printParcels` control coupling and the class's mixed responsibilities are specifically supported by those code paths.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1-4, 8, 10-11 | Kept | Console output is the ParcelPoint command-line interface's user-visible output. Service-owned printing was moved to `Main`, but the UI still uses `System.out`; the current IDE analysis reports 8 java:S106 findings there. No commit made. |
| 5 | Kept | `String[] args` is the standard Java `main` entry-point signature. The current project analysis did not report it; it appeared only when the starter file was analyzed outside the Maven project context. |
| 6 | Fixed | Removed the unused `java.util.Date` import. No commit made. |
| 7 | Fixed | Removed the unused `maxParcels` field. No commit made. |
| 9 | Fixed | Changed parcel-code comparison from `==` to `.equals(code)`. No commit made. |
| 12-13 | Fixed | Replaced the unclosed `FileWriter` with a try-with-resources `BufferedWriter` in `CsvParcelStore`. No commit made. |
| 14 | Fixed | Removed the empty catch by propagating `IOException` through the service and CLI. No commit made. |

An additional loop warning appeared after the first refactor because the command loop had both `continue` and `break`. Blank-line handling now uses a conditional instead, preserving the behavior while removing that warning.

Number of SonarQube issues that remain after your fixes: **8**, all java:S106 `System.out` findings in the console UI. The current SonarQube for IDE Problems view reports them at `Main.java:9,19,21,23,26,30,32,34`.

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Weak class cohesion and tight responsibility coupling | Moved fee logic into `ParcelFeeCalculator`, introduced `ParcelStore` with a `CsvParcelStore` implementation, injected those dependencies into `ParcelService`, and kept command-line presentation in `Main`. | Uncommitted working-tree changes; no commit |
| Weak encapsulation and connascence of convention | Replaced public string parcel status with a private `Status` enum and controlled state transitions; made parcel identity and size fields final and return a defensive copy from `getParcels()`. | Uncommitted working-tree changes; no commit |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 30.0
P1 6501 PICKED_UP
Free lockers: 3

parcels.csv:
P1,PICKED_UP
```

I reran this scenario with the Java 17 runtime used by Maven 3.10.0; the output and CSV contents matched the report. The `java` command on PATH still selects Java 8.0.511, so update the JDK on PATH before using the lab's plain `java` launch command.

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 2026-10-24 | 2026-10-24 |
| AI tool and exact model | Copilot SDK in VS Code; exact model not exposed in this session | Copilot SDK in VS Code; exact model not exposed in this session |
| Prompt used | Prompt B with the specification inserted; exact text is reproduced below | Prompt C with the specification inserted; exact text is reproduced below |
| Builds with mvn package (yes or no) | Yes; `mvn package` succeeded with Maven 3.10.0 and Java 17.0.20.1; the rent/return smoke test passed | Yes; `mvn package` succeeded with Maven 3.10.0 and Java 17.0.20.1; the rent/return smoke test passed |
| Number of classes, interfaces, enums and records | 2 classes (including nested `Ride`); 0 interfaces, 0 enums, 0 records | 5 classes, 1 interface, 1 enum, 2 records |
| SonarQube issues, total | 0 reported in the SonarQube for IDE Problems view for the analyzed file | 0 reported in the SonarQube for IDE Problems view for the analyzed files |
| SonarQube issues, by rule | None reported | None reported |
| AI review findings in Check 1 | 1 | 1 |
| AI review findings in Check 2 | 6 | 1 |
| Review output file in the reviews folder | `reviews/bikeshare-poor-review.md` | `reviews/bikeshare-good-review.md` |

Exact prompts used:

Prompt B:

```text
Write a Java 17 command-line program, built with Maven and with no external library, for the specification below. Give it a poor design on purpose. Put the menu, the fee rules, the bike records and the file saving into as few classes as possible, keep shared data in public static fields, use int codes for the member types, and pass a boolean flag that changes what a method does. Keep the code itself tidy, so close every file, handle every exception and leave no unused code. Keep the program short. Give me the pom.xml and every Java file.

Specification:
BikeShare lends bikes at three campus stations, Library, Canteen and Dorm, and each station starts with two bikes.
1. A member rents an available bike at a station by entering the member ID, the member type (student or staff) and the station.
2. The member returns the bike at any station and enters the ride time in minutes.
3. The fee is 2 baht per minute for a student and 3 baht per minute for a staff member.
4. The program prints a receipt with the bike, the minutes and the fee.
5. The program adds every finished ride to the file rides.csv.
6. The program reads commands from the keyboard until the user types quit.
```

Prompt C:

```text
Write a Java 17 command-line program, built with Maven and with no external library, for the specification below. Design it for high cohesion and low coupling. Give each class one responsibility, keep every field private, pass each method only the data it needs, use an enum for the member types, hide the file saving behind an interface, and keep the menu apart from the classes that hold the rules. Keep the code tidy and the program short. Give me the pom.xml and every Java file.

Specification:
BikeShare lends bikes at three campus stations, Library, Canteen and Dorm, and each station starts with two bikes.
1. A member rents an available bike at a station by entering the member ID, the member type (student or staff) and the station.
2. The member returns the bike at any station and enters the ride time in minutes.
3. The fee is 2 baht per minute for a student and 3 baht per minute for a staff member.
4. The program prints a receipt with the bike, the minutes and the fee.
5. The program adds every finished ride to the file rides.csv.
6. The program reads commands from the keyboard until the user types quit.
```

SonarQube IDE analysis note: file analysis was triggered for the Java source files, and no findings were returned in the Problems view for either BikeShare project. These are IDE results, not a Maven build or a server-side whole-project scan.

4.1 No cohesion or coupling rule/message was reported by SonarQube for bikeshare-poor in the IDE analysis. The design findings below are from the AI review, not SonarQube.

4.2 Yes. The poor version exposes mutable shared `public static` state and puts the menu, bike inventory, rental rules, fee calculation, receipt output, and CSV persistence in one class. Those findings do not apply to the good version, which separates these responsibilities and hides persistence behind `RideRepository`.

4.3 I would extend bikeshare-good. Adding `VISITOR(4)` to `MemberType` naturally extends the fee rule, and the existing rent flow already parses a member type and stores it in `Rental`. The poor version would require more integer-code conditionals and would increase the responsibility load on its monolithic class.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

- It found an issue in the ParcelPoint starter flagging comparing string using `==` instead of `.equals()`, which can cause a correctness issue (rule `java:S1698`). 
- However, an analysis alone does not prove that a program is bug-free or well designed, and SonarQube does not replace testing or human judgment about design. 
- For example, the IDE analysis reported no issues for the BikeShare projects, even though the poor version still has public shared state and combines several responsibilities in one class.

5.2 When should you not trust the AI review? Give an example from this lab.
- I should check AI findings instead of accepting them automatically. 
- For example, its comment about `BikeShareApp.main` is a design judgment, not quite a bug.

5.3 What will you do differently in Project Phase 2 after this lab?
- I will run SonarQube during development, not just at the end, and review its findings before making any decision.
- Use AI for suggestion, then verify its statements.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Github Copilot | find SonarQube mistakes and rewrite some wording for more comprehension and academicity. | LAB_REPORT.md | asking the reason and location where it looked up for then verify by a real proof. |
