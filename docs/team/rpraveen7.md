# GitSwole Project Portfolio Page (Rajan Praveen)

## Project: GitSwole

GitSwole provides fast, CLI-based workout tracking for gym-goers who dislike slow GUI apps and manual logs. 
Single-command entry enables efficient logging of exercises, sets, and weights, with instant access to workout history 
and progress tracking optimized for keyboard-comfortable users.
It is written in Java, and has about 3000 LoC.

Given below are my contributions to the project.

---

### New Feature: Multi-Tiered Listing Mechanism (`ListCommand`)

* **What it does:** provides data at three granularities: a summary of workout sessions (`list`), a detailed 
view of a specific workout (`list w/WORKOUT`), and a global exercise inventory (`list all`).
* **Justification:** Optimizes routine navigation by allowing users to switch between high-level progress 
tracking and granular training data via a single, flag-routed command.
* **Highlights:** Implementation uses flag-based routing to determine display depth. Integrates completion 
status indicators (`[X]` or `[ ]`) and handles sanitized, whitespace-resilient input for reliable CLI performance.

---

### New Feature: Smart Workout Logging (`LogCommand`)

* **What it does:** Records daily performance via `log e/EXERCISE` with support for weight (`wt/`), 
sets (`s/`), reps (`r/`), and `remark/`. It also manages session state through `log w/WORKOUT`.
* **Justification:** Minimizes friction via **"Sticky Sessions"**—remembering active workouts to allow 
logs to omit the workout flag hence supporting partial updates through field inheritance from templates.
* **Highlights:** Uses dual-state logic to manage session headers vs. exercise updates. Includes a specialized 
terminal parser for `remark/` to support multi-word strings and prevents numeric overflows via strict validation.

---

### New Feature: Persistent History Storage (`HistoryStorage`)

* **What it does:** Manages the `history.txt` file, recording every training session with a "Smart Overwriting" 
mechanism to handle user typos without creating duplicates.
* **Justification:** Essential for maintaining a clean chronological log. It ensures re-entering a log corrects the 
existing entry for the day, providing a reliable reference for performance tracking over time.
* **Highlights:** Identifies session blocks by date and workout name, performing surgical updates within the file. 
Uses precise suffix-matching to prevent collisions between similar workout names (e.g., "ARM" vs "WARM").

---

### Code Contributed

[RepoSense Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=rpraveen7&tabRepo=AY2526S2-CS2113-W10-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Project Management

* **Architectural Oversight:** Designed and implemented the core command routing logic for logging and history, 
ensuring a scalable pattern for persistent session tracking
* **Code Quality Assurance:** Implemented extensive assertion checks in the command classes to ensure system state 
integrity during execution.

---

### Enhancements to Existing Features

* **Data Integrity & Overflow Protection:** Refactored `Parser` with `parseAndValidateInt` to prevent numeric 
overflows (e.g., `wt/999999`) and implemented physical limits (1000kg) to ensure data quality in storage.
* **Context-Aware Guidance:** Developed a global exercise search utility in `LogCommand` to proactively suggest 
the correct workout if an exercise is logged in the wrong session (e.g., *"Did you mean to log under Push Day?"*).
* **Strict Command Validation:** Enhanced parser to detect unrecognized flags (e.g., `wol/` vs `wt/`) and resolve 
collisions (e.g., `remark/` bleeding into other fields), preventing silent failures from user typos.
* **Header Collision Resolution:** Fixed a critical bug in `HistoryStorage` where shorter workout names (e.g., "ARM") 
incorrectly matched longer suffixes (e.g., "WARM"). Implemented precise suffix-matching for accurate history attribution.
* **Operational Guardrails:** Added validation to block empty workout session creation or marking workouts 
without exercises as "done," ensuring a robust "Define -> Log" workflow.
* **Case-Insensitive Input:** Optimized `ListCommand` and `LogCommand` to handle input case-insensitively, 
significantly reducing user friction. (e.g., `list w/puSh dAy` targets "Push Day").

---

## Community

* PRs reviewed before merging (with non-trivial review comments).
* Reported bugs and suggestions for other teams in the class.
* Created Issues and Milestone tracker for ease of workflow tracking.

---

### Documentation

#### User Guide
* Updated **Feature 6 (List Summary)**, **Feature 7 (List Workout)**, and added **Feature 8 (List All)** to 
reflect the multi-tiered listing implementation.
* Added **Feature 17 (Log Workout Session)** and **Feature 18 (Log Exercise Stats)** to explain real-time 
performance tracking and the "Sticky Session" workflow.
* Added **Feature 19 (History Storage)** to document the purpose and automatic nature of session history 
persistence in `history.txt`.

#### Developer Guide
* Added implementation details for the **Tiered Listing Mechanism** (`ListCommand`), explaining the design 
routing and justification for centralizing display logic.
* Documented the **Smart Workout Logging** (`LogCommand`) feature, including the "sticky session" state logic 
and UX improvements.
* Detailed the **Persistent History Storage** (`HistoryStorage`) mechanism, explaining the "Smart Overwriting" 
logic and file format specifications for `history.txt`.
* Created and integrated PlantUML sequence diagrams to illustrate core logic:
    * `listSD.puml`: Determining listing scope and object retrieval.
    * `logSD.puml`: Execution flow for active session logging and sticky state management.
    * `historystorageSD.puml`: Internal logic for session block identification and surgical file updates.
    * `architectureDiagram.puml`: High-level component overview mapping the relationships between UI, Parser, 
  Command, Storage, and Assets.
    * `architectureSD.puml`: Architectural sequence diagram tracing the end-to-end lifecycle of an `add` command 
  from input to persistence.

---
