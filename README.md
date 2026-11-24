# CBHS Bank

Student-developed banking simulation for Casco Bay High School.

**Production:** [https://bank.cbhsstem.org](https://bank.cbhsstem.org)

Full product, lab, and student setup notes: [bank-app-specifications.md](bank-app-specifications.md).

This app is independent from the [STEM website](https://github.com/mcnalj/cbhs-stem) and [Flight Tracker](https://github.com/mcnalj/cbhs-flight-tracker). Platform docs live in [cbhs-stem-sandbox](https://github.com/mcnalj/cbhs-stem-sandbox).

## Run locally

Run these commands from the `cbhs-bank` folder so `data/accounts.txt` can be found.

```bash
git clone https://github.com/mcnalj/cbhs-bank.git
cd cbhs-bank
mvn package
java -jar target/cbhs-bank-1.0-SNAPSHOT.jar
```

Open [http://localhost:8082](http://localhost:8082).

```bash
PORT=9092 java -jar target/cbhs-bank-1.0-SNAPSHOT.jar
```

## Testers

```bash
mvn package
java -cp target/cbhs-bank-1.0-SNAPSHOT.jar org.cbhs.bank.testers.AccountTester
java -cp target/cbhs-bank-1.0-SNAPSHOT.jar org.cbhs.bank.testers.BankTester
java -cp target/cbhs-bank-1.0-SNAPSHOT.jar org.cbhs.bank.testers.TransactionTester
```

## Tech stack

- Java 17 + Javalin 6
- HTML / CSS / vanilla JavaScript (CBHS branding)
- Maven
- Seed data in `data/accounts.txt` (SQLite later)
