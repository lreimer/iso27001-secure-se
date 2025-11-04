# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a demonstration project for **ISO 27001 secure software engineering practices**, showcasing how to integrate security controls (especially clause A.8) into a modern SDLC. The project is a Quarkus-based microservice that queries the NVD (National Vulnerability Database) for CVE information, serving as a practical example of secure development with multiple security scanning tools integrated.

**Purpose**: Educational reference for implementing ISO 27001 controls in software development, demonstrating integration of static analysis, security scanning, and continuous security practices.

## Technology Stack

- **Framework**: Quarkus 3.21.1 (Java 21)
- **Build Tool**: Gradle with Gradle Wrapper
- **Architecture**: REST API microservice with external NVD API integration
- **Deployment**: Docker + Kubernetes (Kustomize-based manifests)
- **Infrastructure**: Terraform for cloud provisioning
- **Local Dev**: Tilt for Kubernetes development

## Development Commands

### Build and Run
```bash
# Development mode with hot reload
./gradlew quarkusDev

# Full build (includes tests, ErrorProne, JaCoCo coverage)
./gradlew build

# Run specific test
./gradlew test --tests de.qaware.cloud.NvdResourceTest

# Clean build artifacts
./gradlew clean

# Local Kubernetes development with Tilt
tilt up
```

### Testing API Endpoints
```bash
# OpenAPI documentation
http get localhost:8080/openapi/

# Query CVE information
http get localhost:8080/api/cves/CVE-2021-44228
```

### Security Analysis

**Static Code Analysis - ErrorProne**:
- Runs automatically during compilation (`compileJava` task)
- Finds common programming mistakes at compile time
- Enabled: production code, Disabled: test code

**Code Coverage - JaCoCo**:
```bash
./gradlew test jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```

**SonarCloud Analysis**:
```bash
./gradlew jacocoTestReport sonar
# View results: https://sonarcloud.io/project/overview?id=lreimer_iso27001-secure-se
```

**Docker Image Scanning**:
```bash
# Build Docker image
docker build -t iso27001-service:1.0.0 .

# Trivy vulnerability scan
trivy image -s HIGH,CRITICAL iso27001-service:1.0.0

# Snyk container test
snyk container test --file=Dockerfile iso27001-service:1.0.0

# Dockerfile linting
dockerfile_lint -f Dockerfile -r src/test/docker/security_rules.yaml
```

**Kubernetes Security Scanning**:
```bash
# Kube-score
kubectl score src/main/k8s/base/microservice-deployment.yaml

# Checkov IaC security
checkov --directory src/main/k8s/base

# Trivy Kubernetes scan
trivy src/main/k8s -n default --report summary all
```

**Terraform Security Scanning**:
```bash
# TFLint
tflint

# Checkov for Terraform
checkov --directory src/main/terraform/

# Snyk IaC
snyk iac test src/main/terraform/
```

### Pre-commit Hooks
```bash
# Install hooks (runs on every commit)
pre-commit install

# Run all hooks manually
pre-commit run --all-files
```

Configured hooks: YAML validation, trailing whitespace, end-of-file fixer, Checkov security scanning.

## Architecture

### Application Structure
```
de.qaware.cloud/
├── NvdResource.java      # REST endpoint /api/cves/{cveId}
├── NvdConnector.java     # REST client to NVD API
├── CveResource.java      # REST client interface for NVD
└── LivenessCheck.java    # Health check endpoint
```

**API Flow**:
1. Client → `NvdResource` (`/api/cves/{cveId}`)
2. `NvdResource` → `NvdConnector` (injected service)
3. `NvdConnector` → NVD API (`https://services.nvd.nist.gov/rest/json/cves/2.0`)
4. Response forwarded back to client

### Security Tooling Integration

The project demonstrates **defense-in-depth** with multiple layers of security scanning:

1. **Compile-time**: ErrorProne static analysis
2. **Test-time**: JUnit tests, JaCoCo coverage
3. **Pre-commit**: Git hooks with Checkov
4. **CI Pipeline**: GitHub Actions (build + dependency submission for Dependabot)
5. **Post-build**: SonarCloud analysis
6. **Container**: Docker image vulnerability scanning (Trivy, Snyk)
7. **Deployment**: Kubernetes manifest security scanning (Checkov, kube-score, Trivy)
8. **Infrastructure**: Terraform IaC security scanning

### Deployment

**Kubernetes Manifests**:
- Base: `src/main/k8s/base/` (deployment, service, kustomization)
- Overlays: `src/main/k8s/overlays/{dev,int}/` (environment-specific configs)
- Uses Kustomize for environment management

**Docker**:
- Multiple Dockerfile variants: JVM, native, legacy-jar
- Default: `Dockerfile.jvm`

**Tilt Development**:
- Auto-builds on source changes
- Deploys to local Kubernetes (Rancher Desktop compatible)
- Port-forward: localhost:18080 → pod:8080

## Important Notes

- **Java 21** is required (defined in build.gradle)
- **NVD API**: External dependency on `https://services.nvd.nist.gov` (configured in application.properties)
- **Quarkus Dev Mode**: Use `./gradlew quarkusDev` for hot reload during development
- **Security Focus**: This is a demonstration project - all security tools should be run and passing before commits
- **CI Integration**: GitHub Actions runs build on push/PR to main branch
