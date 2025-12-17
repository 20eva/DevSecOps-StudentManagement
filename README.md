# 🎓 Student Management System - DevSecOps Project

A Spring Boot application implementing **DevSecOps** best practices with integrated security testing and CI/CD automation.

---

## 🎯 Overview

RESTful API for student management with comprehensive testing, security scanning, and automated deployment pipeline.

**Key Features:**
- ✅ CRUD operations for student records
- 🔒 SAST & DAST security integration
- 🧪 80%+ test coverage (Unit, Integration, E2E)
- 🚀 Automated CI/CD pipeline
- 📦 Docker containerization
- 🔍 Vulnerability scanning

---

## 🏗️ DevSecOps Pipeline

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   DEVELOP    │ → │    BUILD     │ → │   SECURITY   │ → │    DEPLOY    │
│              │    │              │    │              │    │              │
│ - Code       │    │ - Maven      │    │ - SAST       │    │ - Docker     │
│ - Git Push   │    │ - Unit Tests │    │ - DAST       │    │ - K8s/Cloud  │
│              │    │ - Integration│    │ - Scan       │    │ - Monitor    │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
```

---

## 🛠️ Technologies

### Core Stack
- **Backend:** Spring Boot 3.5.5, Java 17
- **Database:** H2 (dev), MySQL (prod)
- **Build:** Maven 3.8+

### Testing
| Type | Tools | Tests |
|------|-------|-------|
| **Unit** | JUnit 5, Mockito | 8 |
| **Integration** | Spring Boot Test, MockMvc | 10 |
| **E2E** | Full Stack Testing | 5 |
| **Coverage** | JaCoCo | 80%+ |

### Security Tools

**SAST (Static):**
- SpotBugs + FindSecBugs
- OWASP Dependency Check
- SonarQube

**DAST (Dynamic):**
- OWASP ZAP
- Nuclei

### DevOps
- **CI/CD:** GitHub Actions / Jenkins
- **Containers:** Docker, Kubernetes
- **Monitoring:** Prometheus, Grafana

---

## 📦 Project Structure

```
student-management/
├── src/
│   ├── main/java/.../studentmanagement/
│   │   ├── controllers/     # REST Controllers
│   │   ├── services/        # Business Logic
│   │   ├── repositories/    # Data Access
│   │   └── entities/        # JPA Entities
│   └── test/java/
│       ├── controllers/     # Controller Tests
│       ├── services/        # Service Tests (Unit)
│       ├── repositories/    # Repository Tests
│       └── integration/     # E2E Tests
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker (optional)

### Installation

```bash
# Clone repository
git clone https://github.com/YOUR-USERNAME/student-management.git
cd student-management

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

Application available at: `http://localhost:8080`

---

## 🧪 Testing

### Run All Tests
```bash
mvn clean test
```

### Run by Type
```bash
# Unit Tests
mvn test -Dtest=StudentServiceTest

# Integration Tests
mvn test -Dtest=StudentControllerTest

# E2E Tests
mvn test -Dtest=StudentIntegrationTest

# With Coverage
mvn clean verify
```

### View Coverage Report
```bash
open target/site/jacoco/index.html
```

---

## 🔒 Security Scanning

### SAST (Static Analysis)

```bash
# Run all security scans
mvn verify

# Individual tools
mvn spotbugs:check              # Code vulnerabilities
mvn dependency-check:check      # CVE scanning
mvn pmd:check                   # Code quality
```

**View Reports:**
- SpotBugs: `target/spotbugs/spotbugsXml.xml`
- Dependency Check: `target/dependency-check/dependency-check-report.html`

### DAST (Dynamic Analysis)

```bash
# Start application
mvn spring-boot:run

# Run OWASP ZAP (in another terminal)
zap.sh -daemon -port 8090 -config api.disablekey=true

# Scan
curl "http://localhost:8090/JSON/spider/action/scan/?url=http://localhost:8080"
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/students/getAllStudents` | Get all students |
| GET | `/students/getStudent/{id}` | Get student by ID |
| POST | `/students/createStudent` | Create student |
| PUT | `/students/updateStudent` | Update student |
| DELETE | `/students/deleteStudent/{id}` | Delete student |

### Example Usage

```bash
# Create Student
curl -X POST http://localhost:8080/students/createStudent \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com"
  }'

# Get All Students
curl http://localhost:8080/students/getAllStudents
```

---

## 🐳 Docker

### Build & Run

```bash
# Build image
docker build -t student-management:latest .

# Run container
docker run -p 8080:8080 student-management:latest

# Using Docker Compose
docker-compose up -d
```

### Security Scan

```bash
# Scan image for vulnerabilities
trivy image student-management:latest
```

---

## 🔄 CI/CD Pipeline

### Automated Workflow

```
1. Code Push → GitHub
2. Trigger CI Pipeline
3. Build & Compile (Maven)
4. Run Unit Tests
5. Run Integration Tests
6. SAST Scanning (SpotBugs, Dependency Check)
7. Generate Coverage Report (JaCoCo)
8. Build Docker Image
9. Scan Container (Trivy)
10. DAST Testing (OWASP ZAP)
11. Deploy to Environment
12. Monitoring & Alerts
```

### GitHub Actions

See `.github/workflows/ci-cd.yml` for full pipeline configuration.

---

## 📊 Test Coverage

| Component | Coverage |
|-----------|----------|
| Controllers | 85% |
| Services | 90% |
| Repositories | 75% |
| **Overall** | **~80%** |

**Total Tests: 23**
- Unit: 8
- Integration: 10
- E2E: 5

---

## 🔍 Code Quality

### Quality Gates
- ✅ Test Coverage > 80%
- ✅ No Critical Security Vulnerabilities
- ✅ No High Severity CVEs
- ✅ Code Smells < 10
- ✅ Technical Debt < 5%

### Tools
- **SonarQube:** Code quality analysis
- **SpotBugs:** Bug detection
- **PMD:** Code standards
- **Checkstyle:** Code style

---

## 📈 Monitoring

### Metrics (Prometheus)
- Request count & latency
- JVM memory & CPU
- Database connections
- Error rates

### Dashboards (Grafana)
- Application health
- Performance metrics
- Security events
- Custom alerts

### Logging (ELK Stack)
- Centralized logging
- Log aggregation
- Search & analysis
- Alerting

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Development Guidelines
- Write tests for new features
- Maintain 80%+ code coverage
- Run security scans before PR
- Follow Java code conventions
- Update documentation

---

## 📝 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 👥 Authors

- **Yefa Attia ** - [GitHub](https://github.com/20eva)

## 📞 Support

For issues or questions:
- 🐛 [Report Bug](https://github.com/20eva/DevSecOps-StudentManagement/issues)
- 💡 [Request Feature](https://github.com/20eva/DevSecOps-StudentManagement/issues)
- 📧 Email: yefa.attia@esprit.tn

---

**Built with ❤️ following DevSecOps principles**
