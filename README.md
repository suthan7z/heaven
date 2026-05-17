# Heaven Hotel Reservation System

A comprehensive Spring Boot application for managing hotel reservations, room inventory, bookings, and administrative functions.

## Project Overview

Heaven Hotel is a full-featured hotel management system designed to handle:
- **User Management**: Guest registration, staff management, admin controls
- **Room Inventory**: Room management, availability tracking, maintenance requests
- **Booking & Payment**: Reservation system, payment processing, invoicing
- **Reception Management**: Check-in/check-out, key card management
- **Admin Dashboard**: Reporting, configuration, activity logs
- **Reviews & Loyalty**: Guest reviews, loyalty rewards program

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (Development) / MySQL (Production)
- **Security**: Spring Security with BCrypt password encoding
- **Build Tool**: Maven

### Frontend
- **Templating**: Thymeleaf
- **CSS Framework**: Bootstrap 5
- **Icons**: Font Awesome 6.4
- **JavaScript**: Vanilla JS + Bootstrap JS

### Key Dependencies
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- Lombok
- Apache Commons Lang

## Project Structure

```
heaven/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/heaven/hotel/
│   │   │       ├── config/
│   │   │       │   ├── AppConfig.java
│   │   │       │   ├── FilePathConfig.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/
│   │   │       ├── exception/
│   │   │       ├── filehandler/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── utils/
│   │   │       └── HotelReservationApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── templates/
│   │           ├── layout.html
│   │           ├── fragments/
│   │           ├── auth/
│   │           ├── booking/
│   │           ├── admin/
│   │           └── ...
│   └── test/
└── pom.xml
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- IDE: IntelliJ IDEA, Eclipse, or VSCode

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd heaven
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/heaven`

### Database Configuration

**Development (H2 Console)**
- Access H2 Console: `http://localhost:8080/heaven/h2-console`
- JDBC URL: `jdbc:h2:mem:heavendb`
- Username: `sa`
- Password: (empty)

**Production (MySQL)**
Uncomment the MySQL dependency in `pom.xml` and update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/heaven_hotel
spring.datasource.username=root
spring.datasource.password=your_password
```

## Application Features

### Phase 1: Foundation ✅
- [x] Spring Boot project setup
- [x] Folder structure
- [x] Bootstrap/Tailwind CSS integration
- [x] Navbar/sidebar/footer layouts
- [x] File handling utilities
- [x] Helper classes (Constants, DateUtil, IdGenerator, etc.)
- [x] Base CSS and theme
- [x] Security configuration

### Phase 2: User Management (In Progress)
- [ ] User models (abstract, GuestUser, StaffUser, AdminUser)
- [ ] Authentication services
- [ ] Login/registration/forgot password
- [ ] User management admin panel
- [ ] Profile management

### Phase 3: Room Inventory (Coming Soon)
- [ ] Room models
- [ ] Room availability tracking
- [ ] Maintenance requests
- [ ] Room admin interface

### Phase 4: Booking & Payment (Coming Soon)
- [ ] Booking system
- [ ] Payment processing
- [ ] Price calculations with tax
- [ ] Invoice generation

### Phase 5: Reception Management (Coming Soon)
- [ ] Check-in/check-out procedures
- [ ] Key card management
- [ ] Extra charges

### Phase 6: Admin Management (Coming Soon)
- [ ] Dashboard and reporting
- [ ] Hotel configuration
- [ ] Activity logs
- [ ] User management

### Phase 7: Reviews & Loyalty (Coming Soon)
- [ ] Review submission and approval
- [ ] Loyalty points system
- [ ] Tier-based rewards

### Phase 8: Finalization (Coming Soon)
- [ ] UI/UX polish
- [ ] Exception handling
- [ ] Testing and validation
- [ ] Documentation

## File Handling Utilities

The system uses pipe-delimited text files for data persistence:

- **users.txt**: User profiles and credentials
- **rooms.txt**: Room inventory and status
- **bookings.txt**: Reservation records
- **payments.txt**: Payment transactions
- **reviews.txt**: Guest reviews
- **maintenance.txt**: Maintenance requests
- **keycards.txt**: Key card assignments
- **loyalty.txt**: Loyalty program data
- **admin_logs.txt**: Admin activity logs
- **hotel_config.txt**: Hotel configuration

### Utility Classes

#### FileManager
Handles file creation, deletion, reading, and writing operations.

#### FileReaderUtil
Provides methods to read and search records from files.

#### FileWriterUtil
Handles writing, updating, and deleting records.

#### DataParser
Parses delimited data and converts between formats.

#### Other Utilities
- **Constants**: Application-wide constants and configurations
- **DateUtil**: Date/time operations and formatting
- **IdGenerator**: Unique ID generation for entities
- **PriceCalculator**: Financial calculations and rounding
- **ValidationUtil**: Input validation and sanitization

## Security

The application implements Spring Security with:
- BCrypt password encoding
- Role-based access control (Admin, Staff, Guest)
- CSRF protection
- Session management (30 minutes)
- Remember-me functionality (7 days)

### Role Hierarchy
- **ADMIN**: Full system access
- **STAFF**: Reception and room management
- **GUEST**: Booking and profile management

## Configuration

Key properties in `application.properties`:

```properties
# Server
server.port=8080
server.servlet.context-path=/heaven

# Database
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# File paths
app.data.path=src/main/resources/data/

# Logging
logging.level.com.heaven.hotel=DEBUG

# Session
server.servlet.session.timeout=30m
```

## API Endpoints (Planned)

### Authentication
- `POST /auth/login` - Login
- `POST /auth/logout` - Logout
- `POST /auth/register` - Register guest
- `POST /auth/forgot-password` - Request password reset

### User Management
- `GET /users/{id}` - Get user details
- `PUT /users/{id}` - Update user
- `GET /admin/users` - List users (Admin)
- `DELETE /users/{id}` - Delete user (Admin)

### Rooms
- `GET /rooms` - List available rooms
- `GET /rooms/{id}` - Room details
- `POST /admin/rooms` - Add room (Admin)
- `PUT /admin/rooms/{id}` - Update room (Admin)

### Bookings
- `POST /bookings` - Create booking
- `GET /bookings/{id}` - Booking details
- `PUT /bookings/{id}` - Update booking
- `DELETE /bookings/{id}` - Cancel booking

## Testing

Run tests with:
```bash
mvn test
```

Test files are located in `src/test/java/`

## Development Workflow

1. Start application: `mvn spring-boot:run`
2. H2 Console: `http://localhost:8080/heaven/h2-console`
3. Make changes and the application will auto-reload (DevTools)
4. Check logs in console for debugging

## Troubleshooting

**Port already in use**
```bash
# Change port in application.properties
server.port=8081
```

**Database reset needed**
```properties
spring.jpa.hibernate.ddl-auto=create
```

**Clear H2 database**
- Use H2 Console and drop tables manually
- Or delete `~/.h2/heavendb*` files

## Contributing

Guidelines for contributing:
1. Create feature branches
2. Follow Java naming conventions
3. Add comments for complex logic
4. Test all changes
5. Commit with descriptive messages

## License

This project is part of the educational curriculum and is not licensed for external use.

## Contact & Support

For questions or issues:
- Email: support@heavenhotel.com
- Issues: GitHub Issues
- Documentation: See `/docs` folder

## Changelog

### v1.0.0 - Phase 1 Foundation
- Initial Spring Boot setup
- Project structure created
- Core utilities implemented
- CSS theming and layouts
- Security configuration

---

**Last Updated**: May 14, 2026
**Status**: In Active Development




run local

'C:\apache-maven-3.9.6\bin\mvn.cmd' spring-boot:run


..............................





Fix the VS Code errors first:

Press Ctrl+Shift+P → type "Java: Clean Java Language Server Workspace" → click Restart and Delete. This forces VS Code to re-read the pom.xml and re-index everything.

How to actually run the project — pick one:

Option A — Terminal (most reliable)

Open a terminal in VS Code (Ctrl+`` ) and run:


cd c:\Users\Mr.Cherry\Downloads\Heaven
Then:


"C:\Users\Mr.Cherry\AppData\Local\Temp\chocolatey\ChocolateyScratch\maven\3.9.15\apache-maven-3.9.15\bin\mvn.cmd" spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx256m"
Wait until you see Started HotelReservationApplication, then open http://localhost:8080/heaven

Option B — VS Code Spring Boot Dashboard

Install the Spring Boot Extension Pack from the Extensions panel (Ctrl+Shift+X), search Spring Boot Extension Pack by VMware
After install, a Spring Boot Dashboard icon appears in the left sidebar (looks like a leaf 🍃)
Click it → your app appears → press the ▶ Run button
Option C — Run directly from the main class

Open HotelReservationApplication.java
Click the Run button that appears above the main method (VS Code shows a small ▶ code lens above public static void main)
Once started, go to: http://localhost:8080/heaven