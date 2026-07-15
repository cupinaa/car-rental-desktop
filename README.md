# Car Rental Desktop

A role-based Java Swing application for managing a small car-rental operation with file-backed persistence, reservations, rentals, subscriptions, reporting, and charts.

## Overview

Car Rental Desktop demonstrates an end-to-end object-oriented desktop system for administrators, rental agents, and customers. It is intended as an educational reference and portfolio project for developers studying Java, Swing, persistence, and domain modeling.

The application keeps its demo data in human-readable, pipe-delimited CSV files. No database server, network service, or API key is required.

## Key features

- Role-specific interfaces for administrators, agents, and customers
- Vehicle, model, user, service, price-list, subscription, and reservation management
- Availability checks that prevent overlapping vehicle reservations
- Price calculation with customer-category discounts and optional services
- Rental checkout and return workflows with mileage and late-fee tracking
- Operational and financial reports with XChart visualizations
- CSV persistence repositories and 31 JUnit tests

## Technical highlights

- Domain objects separate users, vehicles, pricing, reservations, rentals, and subscriptions.
- Repository classes load and save the file-backed data model.
- Reservation rules cover subscription eligibility, license age, availability, cancellation blocks, and rental-duration extensions.
- Swing panels expose different workflows according to the authenticated user's role.

## Technology stack

- Java 26
- Java Swing
- JUnit 4.13.2 with Hamcrest 1.3
- XChart 3.8.8
- PowerShell build scripts
- Pipe-delimited CSV persistence

## Getting started

### Prerequisites

- JDK 26 or a compatible newer JDK
- PowerShell 7 or Windows PowerShell 5.1

The required JUnit, Hamcrest, and XChart JAR files are included in `lib/` so the project can be built without a dependency manager.

### Build

```powershell
./build.ps1
```

### Run

```powershell
./run.ps1
```

The application reads and updates the CSV files in the repository root. Use a disposable copy if you want to preserve the original demo state.

### Demo accounts

The accounts below are fictional and intentionally public. They are suitable only for this local demo.

| Role | Username | Password |
| --- | --- | --- |
| Administrator | `admin@example.test` | `demo-admin` |
| Agent | `agent@example.test` | `demo-agent` |
| Customer | `customer@example.test` | `demo-customer` |

### Test

```powershell
./test.ps1
```

The test script compiles the production and test sources, discovers the JUnit test classes, and runs them with `JUnitCore`.

## Project structure

```text
src/
  data/          CSV repositories and settings
  gui/           Swing windows, dialogs, panels, reports, and charts
  main/          application entry point and composition root
  pricing/       price lists and daily-rate items
  rental/        completed/active rental records
  reservation/   reservations, services, and reservation statuses
  users/         roles, subscriptions, and customer rules
  vehicles/      vehicles, models, categories, and availability status
  test/data/     repository-focused JUnit tests
```

## Known limitations

- Data is stored in local files and is not safe for concurrent application instances.
- Passwords are plain text because authentication is a local educational demonstration; do not reuse this design for real credentials.
- Monetary values use `double`; production financial software should use decimal arithmetic.
- The interface is a desktop application and has no hosted web demo.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for the expected workflow.

## Security

This project is not designed to store real customer or credential data. See [SECURITY.md](SECURITY.md) before using or reporting a security issue.

## License

Released under the [MIT License](LICENSE).
