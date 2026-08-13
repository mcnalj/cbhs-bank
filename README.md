# CBHS Bank

Student-developed banking simulation for Casco Bay High School.

**Production:** [https://bank.cbhsstem.org](https://bank.cbhsstem.org)

This is a **skeleton**. Application features will be added from a separate spec. It is independent from the [STEM website](https://github.com/mcnalj/cbhs-stem) and [Flight Tracker](https://github.com/mcnalj/cbhs-flight-tracker). Platform docs live in [cbhs-stem-sandbox](https://github.com/mcnalj/cbhs-stem-sandbox).

## Run locally

```bash
git clone git@github.com:mcnalj/cbhs-bank.git
cd cbhs-bank
mvn package
java -jar target/cbhs-bank-1.0-SNAPSHOT.jar
```

Open [http://localhost:8082](http://localhost:8082).

```bash
PORT=9092 java -jar target/cbhs-bank-1.0-SNAPSHOT.jar
```

## Tech stack

- Java 17 + Javalin 6
- HTML / CSS (CBHS branding)
- Maven
