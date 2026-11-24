# CBHS Bank — Application Specification

Living document. We will add labs, API details, and deconstruction notes here as the proof of concept is built.

**Status:** Draft (decisions locked through 2026-08-13)  
**Audience:** Course teacher (primary). Students receive smaller lab specs derived from this document.  
**Production:** https://bank.cbhsstem.org  
**Repo:** [github.com/mcnalj/cbhs-bank](https://github.com/mcnalj/cbhs-bank)

---

## 1. Purpose

CBHS Bank is a **school banking simulation** for Casco Bay High School. It is not a real bank and never holds real money or real credentials.

It has two jobs at once:

1. Be a small, working web app students can open in a browser.
2. Be a sequence of AP Computer Science A programming labs, where students write Java that the website already knows how to display.

We will first build a complete proof of concept, then remove pieces so students can rebuild them.

---

## 2. Learners and constraints

- Course: AP Computer Science A (College Board CED effective Fall 2025).
- Students: high school seniors, no prior computer science.
- Student Java must stay inside the AP CSA Java subset (see §5).
- Prefer the simplest design that still feels like a real product.
- Students work on **backend Java**. The front end should **just work** once their methods are correct.
- Supported student machine: **Chromebook with Linux development enabled**, plus any Mac/Windows laptop with JDK 17 and Maven.

---

## 3. Design principles

1. **Student code is ordinary AP CSA Java.** Classes, methods, `if`/`for`/`while`, arrays, `ArrayList`, `String`, `File`/`Scanner`. No web framework code in student files.
2. **The website is a display for student objects.** HTML/CSS/JS never contain banking rules. Java methods own the rules.
3. **Stable contracts.** Method signatures and JSON shapes are agreed up front. The front end is written against those contracts, including empty lists and error messages.
4. **One concept per lab.** Each student task looks like a short FRQ: write a class or fill in methods. The rest of the app is already there.
5. **Data files before a database.** Start with text files students can read with `Scanner`. SQLite is a later, optional step and is not student AP CSA work.
6. **No inheritance.** Checking vs. savings is a `String` type on `Account`, not `SavingsAccount extends Account`. Inheritance was removed from AP CSA for 2025–26.
7. **Simulation only.** No passwords, sessions, or real personal data.

---

## 4. Tech stack and who owns it

| Layer | Technology | Who writes it | Students see it? |
| --- | --- | --- | --- |
| Student domain | Java 17, AP CSA subset | Students (after PoC is deconstructed) | Yes — this is the course work |
| HTTP + JSON glue | Javalin 6, Maven | Teacher | No (or read-only later) |
| Front end | HTML, CSS, vanilla JavaScript | Teacher | No, except using the live site |
| Seed data | Text files (CSV-style) | Teacher + students in the file-I/O lab | Yes |
| Database | SQLite (later) | Teacher | No |

**Why this split**

- Javalin, JSON, HTML, and CSS are not AP CSA. Putting them in teacher files keeps student work exam-aligned.
- Maven, `pom.xml`, and `App.java` stay teacher-owned.
- Student classes live in `org.cbhs.bank.model`, with no Javalin imports.

**Existing skeleton**

- Java 17 + Javalin 6 + Maven
- Static files under `src/main/resources/public/`
- CBHS branding in `css/variables.css` and `css/style.css`
- Local URL: `http://localhost:8082` (`PORT` override supported)

---

## 5. AP CSA Java subset (student code)

Student `.java` files may use only what AP CSA uses. Align with the 2026 Java Quick Reference.

### Allowed

- Types: `int`, `double`, `boolean`, `String`, object types we define
- Control: `if` / `else`, `while`, `for`, enhanced `for`
- Classes: fields, constructors, methods, getters/setters as needed, `static` where it helps
- `String`: `length`, `substring`, `indexOf`, `equals`, `compareTo`, `split`
- `Integer.parseInt`, `Double.parseDouble`, `Integer.MIN_VALUE` / `MAX_VALUE`
- `Math`: `abs`, `pow`, `sqrt`, `random`
- 1D arrays, 2D arrays (optional later lab)
- `ArrayList`: `size`, `add`, `add(index, obj)`, `get`, `set`, `remove(index)`
- `File` + `Scanner` for **reading** text files (`hasNext`, `next`, `nextLine`, `nextInt`, `nextDouble`, `close`)
- `throws IOException` on methods that open files
- `Object`: `equals`, `toString`

### Not allowed in student code

- Inheritance (`extends`, `super`), interfaces, polymorphism as a design tool
- Writing recursive methods
- `HashMap`, `HashSet`, `TreeMap`, etc.
- Lambdas, streams, generics beyond `ArrayList<Account>`
- `try`/`catch` unless we later decide a tiny exception pattern is worth teaching (default: `throws IOException` only)
- Javalin, JSON libraries, SQL, JDBC
- Writing files, unless a post-AP enrichment lab
- Packages/classes outside `java.lang`, `java.util` (`ArrayList`, `Scanner`), and `java.io` (`File`, `IOException`)

Teacher glue (`App.java`, JSON mapping) may use libraries students do not write.

---

## 6. Architecture: how the front end “just works”

```
Browser (HTML/CSS/JS)
        |
        |  fetch("/api/...")
        v
Teacher: Javalin routes in App.java
        |
        |  calls ordinary Java methods
        v
Student: Bank, Account, Transaction, ...
        |
        v
Text data files (later: SQLite, teacher-owned)
```

**Rules**

1. Student methods return Java values (`boolean`, `double`, `Account`, `ArrayList<Account>`). They do not print HTML or JSON.
2. Teacher routes call those methods and send JSON to the browser.
3. The front end is written first against the JSON contract. Empty `ArrayList`s already render a friendly empty state. When students implement a method, the same page fills in.
4. Banking rules live only in student classes (`withdraw` refuses a negative balance, `deposit` rejects a negative amount, and so on). The browser only displays success/failure from the API.

This is the same idea as an AP CSA FRQ: we publish the method header; students write the body; a tester (here, the website plus a `*Tester` class) already exists.

---

## 7. How students see their code working

Use **both** feedback paths. The website is not a second stack. If they can run this Maven project, they can run the site.

| Path | What they do | When to use it |
| --- | --- | --- |
| Testers | Run `AccountTester` (or similar) with the IDE Run button or `mvn test` later | Fast yes/no while writing a method |
| Local website | Run `App`, open `http://localhost:8082`, use the page that calls their method | See the feature on a real CBHS-branded page |

Students do **not** write JUnit, HTML, or Javalin. Testers are teacher-written `main` classes that print `PASS` / `FAIL` (AP CSA style).

**What we will not add:** live reload, Docker, per-student cloud deploys, or students deploying to bank.cbhsstem.org during labs.

**Classroom vs public demo**

| Place | Role |
| --- | --- |
| GitHub `main` + bank.cbhsstem.org | One shared demo. Selected solutions are restored here after labs. |
| Student clones / branches | Code may diverge during a lab. |
| Student Chromebook or laptop | Testers + local site. |

---

## 8. Product story

CBHS Bank is a **classroom simulation** of a small school bank.

- Each “customer” is a named person or club (for example, a student or the Robotics Club).
- Each customer may have a **checking** account, a **savings** account, or both.
- Students (the programmers) implement how money moves. The class can open the live site and see accounts, balances, and transactions.

Tone: school-friendly, CBHS branded, clearly labeled as a simulation.

---

## 9. Domain model (proof of concept)

Keep three classes for the full app. Do not add a `Customer` class in v1; put the owner name on `Account`. A `Customer` class can be a later lab if we want nested `ArrayList`s.

### 9.1 `Account`

A single bank account.

| Field | Type | Notes |
| --- | --- | --- |
| `accountNumber` | `int` | Unique. Seed file supplies numbers. New accounts use `Bank.nextAccountNumber()`. |
| `ownerName` | `String` | Person or club name |
| `accountType` | `String` | `"checking"` or `"savings"` (compare with `.equals`, not `==`) |
| `balance` | `double` | Dollars. Textbook-style AP CSA `BankAccount`. |
| `transactions` | `ArrayList<Transaction>` | History, newest appended at the end |

**Money:** use `double` so the class matches the classic AP CSA `BankAccount`. Call out floating-point quirks as a teaching note. A later enrichment can store cents as `int`.

**Suggested methods**

```java
public Account(int accountNumber, String ownerName, String accountType, double startingBalance)

public int getAccountNumber()
public String getOwnerName()
public String getAccountType()
public double getBalance()
public ArrayList<Transaction> getTransactions()

public boolean deposit(double amount)
public boolean withdraw(double amount)
public void addTransaction(Transaction t)
public String toString()
```

**Rules**

- `deposit`: amount must be `> 0`. Add to balance, return `true`. Otherwise return `false` and do not change the account.
- `withdraw`: amount must be `> 0` and `<=` balance. No overdraft in v1. Return `true`/`false` the same way.
- `Bank` records `Transaction` objects after a successful deposit, withdraw, transfer, or interest payment. Lab 1 students can implement `deposit`/`withdraw` without knowing `Transaction` yet.
- Do not use inheritance for checking vs. savings. Interest applies only when `accountType.equals("savings")`.

### 9.2 `Transaction`

One completed action.

| Field | Type | Notes |
| --- | --- | --- |
| `type` | `String` | `"deposit"`, `"withdraw"`, `"transfer-in"`, `"transfer-out"`, `"interest"` |
| `amount` | `double` | Always positive; direction lives in `type` |
| `note` | `String` | Short description |
| `stamp` | `String` | Date string such as `"2026-08-13"`. Teacher glue may create it; student code accepts it as a parameter. |

AP CSA has no date library on the exam. Store the time as a `String`.

```java
public Transaction(String type, double amount, String note, String stamp)
public String getType()
public double getAmount()
public String getNote()
public String getStamp()
public String toString()
```

### 9.3 `Bank`

The collection of all accounts. This is the main Unit 4 class.

```java
public static final double INTEREST_RATE = 0.05;

public Bank()

public ArrayList<Account> getAccounts()
public Account findAccount(int accountNumber)    // linear search; null if missing
public ArrayList<Account> findAccountsByName(String name)  // substring, case-sensitive
public boolean addAccount(Account account)       // false if number already exists
public int nextAccountNumber()                   // max existing number + 1, or 1001 if empty

public boolean deposit(int accountNumber, double amount, String note, String stamp)
public boolean withdraw(int accountNumber, double amount, String note, String stamp)
public boolean transfer(int fromNumber, int toNumber, double amount, String note, String stamp)

public double totalAssets()
public int countAccounts()
public double averageBalance()                   // 0 if no accounts
public Account largestAccount()                  // null if none

public int applyInterest(String stamp)           // savings only; 5% once per call; returns how many accounts were updated
public void loadFromFile(String filename) throws IOException  // replaces all accounts, then loads the file
```

`Bank` methods that take an account number should find the `Account` and delegate to it. That keeps `Account` as the place where balance rules live.

---

## 10. Locked decisions

### 10.1 Interest rate

- Constant: `Bank.INTEREST_RATE = 0.05` (5%).
- Each call / Admin button click applies **5% once** to every savings account with a positive balance. Not monthly compounding.
- Checking accounts are unchanged.
- Example: $900 savings becomes $945 after one click.
- A later admin screen can let users change the rate. Until then, the API always uses 5%.

### 10.2 Search

- Case-sensitive.
- Match if `ownerName.indexOf(name) >= 0` (substring).
- Example: `"Rob"` matches `"Robotics Club"`; `"rob"` does not.

### 10.3 Reload from file

- `loadFromFile` **replaces** the current list, then reads the file.
- The Reload button always returns the demo to the known seed list.

### 10.4 Account numbers

Two jobs, one rule:

- **Seed file:** each line includes a number (`1001,Robotics Club,...`). Keep those numbers so Robotics Club is always `1001` in labs and on the demo.
- **Open Account on the website:** the user does not pick a number. `nextAccountNumber()` returns the largest existing number + 1, or `1001` if the bank is empty.

### 10.5 Shared demo vs lab divergence

- One public bank on `main` and bank.cbhsstem.org.
- Student work happens in local clones / branches and may diverge.
- Selected solutions are restored onto `main` for the public demo.

---

## 11. Features (full proof of concept)

### 11.1 Dashboard (home)

- Bank name and “simulation” label
- Number of accounts
- Total assets
- Average balance
- Links to Accounts, Open Account, Transfer

Empty bank: show zeros and a short “No accounts yet” message.

### 11.2 Accounts list

- Table or cards: number, owner, type, balance
- Search box by owner name (uses `findAccountsByName`)
- Click through to account detail
- Empty state if the list is empty

### 11.3 Account detail

- Owner, number, type, balance
- Transaction list (date, type, amount, note)
- Deposit form (amount + optional note)
- Withdraw form (amount + optional note)
- Failed deposit/withdraw shows a clear message (insufficient funds, invalid amount, account not found)

### 11.4 Open account

- Owner name, type (checking/savings), starting balance
- Bank assigns `accountNumber`
- New account appears on the list immediately

### 11.5 Transfer

- From account number, to account number, amount
- Must fail if either account is missing, if `from == to`, or if withdraw would fail
- Records `transfer-out` on the source and `transfer-in` on the destination

### 11.6 Apply interest (demo control)

- Admin page button
- Calls `applyInterest(stamp)` at 5%
- Only savings accounts; each successful application adds an `"interest"` transaction

### 11.7 Reset / reload from file (demo control)

- Reloads seed data so the live demo can be reset after class use
- Student file-I/O lab implements `loadFromFile`

### 11.8 Out of scope for v1

- Login, passwords, PINs, sessions
- Overdraft, fees, loans, credit cards
- Multiple users / permissions
- Editing or deleting accounts (can add later)
- Writing data files from student code
- SQLite
- Real money, real names beyond fictional seed data

---

## 12. JSON API contract (teacher-owned)

The front end talks only to these routes. Paths can be adjusted during PoC, but shapes should stay stable before student labs begin.

All JSON field names below are the contract. Teacher mapping code copies getters into this shape so student classes do not need a zero-argument constructor or Jackson annotations.

### `GET /api/stats`

```json
{
  "accountCount": 3,
  "totalAssets": 1407.50,
  "averageBalance": 469.17
}
```

### `GET /api/accounts`

```json
{
  "accounts": [
    {
      "accountNumber": 1001,
      "ownerName": "Robotics Club",
      "accountType": "checking",
      "balance": 250.00
    }
  ]
}
```

Optional query: `GET /api/accounts?name=Robo` → `findAccountsByName`.

### `GET /api/accounts/{number}`

Account object plus `"transactions": [ ... ]`.  
Missing account: HTTP 404 and `{ "error": "Account not found" }`.

### `POST /api/accounts`

Body: `{ "ownerName": "...", "accountType": "checking", "startingBalance": 50.00 }`  
Result: the created account.

### `POST /api/accounts/{number}/deposit`

Body: `{ "amount": 20.00, "note": "Bake sale" }`  
Result: `{ "ok": true, "balance": 270.00 }` or `{ "ok": false, "error": "Invalid amount" }`

### `POST /api/accounts/{number}/withdraw`

Same shape as deposit.

### `POST /api/transfer`

Body: `{ "fromNumber": 1001, "toNumber": 1002, "amount": 15.00, "note": "Dues" }`  
Result: `{ "ok": true }` or `{ "ok": false, "error": "..." }`

### `POST /api/interest`

Result: `{ "ok": true, "updated": 2 }`  
Always uses `Bank.INTEREST_RATE` (5%).

### `POST /api/reload`

Reloads the data file. Result: `{ "ok": true, "accountCount": 6 }`

**Error style:** do not throw across the API. Student methods return `boolean` or `null`; teacher routes turn that into `{ "ok": false, "error": "..." }`. The front end always knows how to show `error`.

---

## 13. Data files (before SQLite)

**File:** `data/accounts.txt` at the project root (so `new File(filename)` works when students run from the project folder).

```
1001,Robotics Club,checking,250.00
1002,Robotics Club,savings,900.00
1003,Maya Patel,checking,157.50
1004,Jordan Lee,checking,80.00
1005,Jordan Lee,savings,20.00
1006,School Store,checking,430.25
```

Format: `accountNumber,ownerName,accountType,balance`

- No extra spaces around commas.
- `ownerName` must not contain commas in v1.
- Transactions start empty on load.

`loadFromFile` should:

1. Replace the current account list with a new empty `ArrayList`
2. Create a `Scanner` on a `File`
3. Loop `while (input.hasNextLine())`
4. `split(",")` the line
5. `Integer.parseInt` / `Double.parseDouble`
6. `addAccount(new Account(...))`
7. `close()` the scanner

This lab maps directly to new 2025–26 AP CSA file reading.

**Runtime note:** run the app from the project root so `data/accounts.txt` resolves. Document this in the student setup guide.

---

## 14. Front end specification

### 14.1 Choice

**HTML + CSS + vanilla JavaScript**, static files served by Javalin, CBHS styles already in the repo.

Do not use React, Vue, npm, Thymeleaf, JSP, or Java-generated HTML.

### 14.2 Pages (multi-page, not a single-page app)

| Page | File | What it calls |
| --- | --- | --- |
| Home / dashboard | `pages/home.html` | `GET /api/stats` |
| Accounts | `pages/accounts.html` | `GET /api/accounts` |
| Account detail | `pages/account.html` | `GET /api/accounts/{n}`, deposit/withdraw POST |
| Open account | `pages/new-account.html` | `POST /api/accounts` |
| Transfer | `pages/transfer.html` | `POST /api/transfer` |
| Demo / admin | `pages/admin.html` | interest + reload |

Shared:

- `css/variables.css`, `css/style.css`
- `js/api.js` — `fetch` helpers only
- `js/format.js` — money as `$250.00`, account type labels

### 14.3 Empty and error states

Build these in the PoC **before** student methods are deconstructed:

- Empty account list
- Account not found
- `{ "ok": false, "error": "..." }` shown next to the form
- Failed network request (server not running)

### 14.4 What students do not do

- No JavaScript assignments
- No CSS assignments in the core course
- They demonstrate work by using the local site and by running Java testers

---

## 15. Proof of concept, then deconstruct

### Phase A — Teacher builds the full app

Build everything in §§9–14 working end to end, including seed data.

### Phase B — Freeze contracts

Do not change method headers or JSON field names after labs are written, except by adding new methods.

### Phase C — Deconstruct into student labs

For each lab:

1. Leave method headers and Javadoc (FRQ style).
2. Replace bodies with `return 0;`, `return false;`, `return null;`, or empty methods.
3. Keep teacher-written `*Tester.java` classes with `main` that print PASS/FAIL.
4. The website stays runnable. Incomplete methods produce empty lists or `ok: false`, which the UI already handles.

Suggested order (maps to the course):

| Lab | When | Student writes | Site that lights up |
| --- | --- | --- | --- |
| 1. `Account` | Unit 3 — class creation | constructor, getters, `deposit`, `withdraw`, `toString` | Account detail deposit/withdraw (once Bank exists as a stub) |
| 2. `Bank` list | Unit 4 — `ArrayList` | `addAccount`, `getAccounts`, `findAccount` | Accounts list, open account |
| 3. Bank algorithms | Unit 4 — standard algorithms | `totalAssets`, `averageBalance`, `largestAccount`, `findAccountsByName` | Dashboard, search |
| 4. `Transaction` | Unit 3/4 | `Transaction` class; `Bank` records history | Transaction table |
| 5. `transfer` | Unit 4 | find two accounts, withdraw + deposit | Transfer page |
| 6. Interest | Unit 2/4 — loops + `double` | loop savings accounts, update balance | Admin interest button |
| 7. `loadFromFile` | Unit 4 — File/`Scanner` | parse `accounts.txt` | Seeded live demo / reload |

### Phase D — Optional enrichment (not AP CSA)

- Persist with SQLite (teacher adapter; same `Bank` methods)
- Store money as integer cents
- Overdraft fee
- `Customer` with `ArrayList<Account>`
- Simple selection sort of accounts by balance (do not rely on `Collections.sort`)

---

## 16. Testing strategy

- **Student-visible:** `*Tester.java` classes with `main`, print expected vs. actual. Same spirit as AP CSA.
- **Teacher-visible:** Javalin app + browser checks against the JSON contract.
- **Classroom demo:** reload seed file, run deposit/withdraw/transfer on the projector.

Do not require students to test through HTTP.

---

## 17. Student development steps (summary)

Students only edit files in `org.cbhs.bank.model` (and later, a data file). They never need to edit `App.java`, HTML, CSS, or JavaScript.

Typical loop for one method (example: `deposit`):

1. Open the lab’s Java file (for example `Account.java`).
2. Fill in the method body from the lab spec (FRQ-style header is already there).
3. Run the matching tester class (`AccountTester`). Read `PASS` / `FAIL` in the terminal.
4. If a test fails, fix the Java and run the tester again. Do not start the website yet.
5. When testers pass, run `App` (see §19).
6. In Chrome, open `http://localhost:8082` and use the page that exercises that method (for `deposit`: Accounts → pick an account → Deposit).
7. Confirm the balance and (when that lab is in play) the transaction list changed.
8. Stop the server with `Ctrl+C` when finished.

If testers pass but the page does not change, they usually forgot to stop an old server and start a new one after rebuilding.

---

## 18. Chromebook and laptop support

A Chromebook with **Linux development enabled** (Crostini) can run this project.

- Install JDK 17, Maven, and Git inside the Linux terminal.
- Run the jar from the project folder.
- Open Chrome (the main Chrome OS browser) at `http://localhost:8082`. Chrome OS forwards Linux ports into Chrome. Port `8082` is fine.
- If `localhost` fails, try `http://penguin.linux.test:8082`.
- Prefer Terminal + a lightweight editor (or VS Code in Linux). Avoid IntelliJ on 4 GB machines.

**Check on one student-image Chromebook before the first lab:** Linux allowed by admin policy, `apt install` works, `mvn package` works, Chrome opens `localhost:8082`.

If Linux is blocked on a managed device, that student can still run testers in another Java environment, or use a school lab machine / Codespace for the website.

Mac and Windows laptops use the same Git / Maven / `java -jar` commands after JDK 17 and Maven are installed.

---

## 19. Student setup guide

Do this **once** at the start of the unit. Daily labs start at §20.

### 19.1 Accounts and access

1. Create a [GitHub](https://github.com) account if you do not have one. Use your school email if the teacher asks.
2. Tell the teacher your GitHub username so you can access the class repository (required if the repo is private).
3. On a Chromebook: Settings → Advanced → Developers → **Linux development environment** → Turn on. Wait until the Terminal app appears.
4. Confirm you have a few GB of free storage. Linux + Java + Maven need space.

### 19.2 Install tools (Chromebook Linux)

Open the **Terminal** app (penguin). Run these commands one block at a time.

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven git
```

Check that each tool works:

```bash
java -version
javac -version
mvn -version
git --version
```

`java -version` should say **17** (or a later version is OK if the teacher says so).

**Mac:** install Temurin 17 (or another JDK 17) and Maven (Homebrew: `brew install openjdk@17 maven git`).  
**Windows:** install JDK 17, Maven, and Git for Windows; use PowerShell or Git Bash for the same commands below.

### 19.3 Get the code from GitHub

In Terminal, go to your Linux home folder and clone the repository.

HTTPS (simplest on a Chromebook):

```bash
cd ~
git clone https://github.com/mcnalj/cbhs-bank.git
cd cbhs-bank
```

SSH (only if you have already set up a GitHub SSH key):

```bash
cd ~
git clone git@github.com:mcnalj/cbhs-bank.git
cd cbhs-bank
```

If GitHub asks you to sign in over HTTPS, use a **personal access token** as the password, not your GitHub account password. The teacher will show this once if the repo is private.

You should see folders such as `src`, `data`, and `pom.xml`.

### 19.4 First run (prove the website works)

Always run Maven and the app from the **`cbhs-bank` folder** so `data/accounts.txt` can be found.

```bash
cd ~/cbhs-bank
mvn package
java -jar target/cbhs-bank-1.0-SNAPSHOT.jar
```

Leave that terminal window open. In Chrome, open [http://localhost:8082](http://localhost:8082).

You should see the CBHS Bank home page with seed accounts (Robotics Club, Maya Patel, and others).

Stop the server: click the Terminal window and press `Ctrl+C`.

### 19.5 First tester (prove Java tests work)

```bash
cd ~/cbhs-bank
mvn package
java -cp target/cbhs-bank-1.0-SNAPSHOT.jar org.cbhs.bank.testers.AccountTester
```

You should see lines ending in `PASS`. If you use an editor that can run a `main` method, you can also open `AccountTester.java` and press Run.

### 19.6 Optional: editor

You can edit Java with any Linux editor. VS Code in Linux is fine if the Chromebook has enough RAM. You do not need IntelliJ.

Keep the project inside the Linux files area (`/home/yourname/cbhs-bank`), not only in Google Drive, so `javac` / Maven can see the files.

---

## 20. Starting a daily lab

1. **Open Terminal** and go to the project:
   ```bash
   cd ~/cbhs-bank
   ```
2. **Update from GitHub** if the teacher says the class repo changed:
   ```bash
   git pull
   ```
   If `git pull` complains about local changes, stop and ask the teacher before overwriting your work.
3. **Open the lab file** the teacher named (for example `src/main/java/org/cbhs/bank/model/Account.java`).
4. **Write the method** described in that day’s lab spec.
5. **Run the tester** for that lab:
   ```bash
   mvn package
   java -cp target/cbhs-bank-1.0-SNAPSHOT.jar org.cbhs.bank.testers.AccountTester
   ```
   Fix any `FAIL` before going on.
6. **Run the website** (in the same folder):
   ```bash
   java -jar target/cbhs-bank-1.0-SNAPSHOT.jar
   ```
   Chrome: [http://localhost:8082](http://localhost:8082). Use the page listed in the lab (see the table in §15).
7. **When you are done,** stop the server with `Ctrl+C`. Do not close the lid with the server still running if you can avoid it; tomorrow `8082` may already be in use.
8. **Save / turn in** the way the teacher asked (often a commit, a PR, or just leaving the file on the machine). Do not push to `main` on the class demo repo unless the teacher says to.

### Daily lab troubleshooting

| Problem | What to try |
| --- | --- |
| `localhost refused to connect` | The server is not running. `cd ~/cbhs-bank` and run the `java -jar` command again. |
| `localhost` fails on a Chromebook | Try [http://penguin.linux.test:8082](http://penguin.linux.test:8082). |
| `Address already in use` | An old server is still running. In Terminal: `pkill -f cbhs-bank` then start again. |
| Page looks old after you changed Java | You must `mvn package` again, stop the old server (`Ctrl+C`), and start `java -jar` again. |
| `data/accounts.txt` not found | You are not in `~/cbhs-bank`. `cd ~/cbhs-bank` first. |
| `git pull` asks you to stash or commit | You have local edits. Ask the teacher before continuing. |
| `mvn package` downloads for a long time | First build needs internet. School Wi-Fi must allow Maven Central. |

---

## 21. SQLite (later)

When we outgrow files:

- Keep `Bank` method signatures the same.
- Teacher writes a persistence adapter that students do not edit.
- Student code still uses `ArrayList<Account>` in memory for algorithms.

---

## 22. Document history

| Date | Change |
| --- | --- |
| 2026-08-13 | Initial draft: goals, subset, domain, features, API, front end, lab sequence |
| 2026-08-13 | Locked decisions: 5% interest per click, case-sensitive search, reload replaces accounts, account numbers from file + max+1, testers + local site, Chromebook Linux, student setup and daily lab guide |
