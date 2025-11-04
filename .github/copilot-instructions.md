# Copilot Instructions for iso27001-secure-se

## Project Overview
This repository demonstrates ISO 27001 secure software engineering practices using a Quarkus-based microservice. It integrates security controls (especially clause A.8) into a modern SDLC, with defense-in-depth via static analysis, security scanning, and continuous security practices.

## Architecture & Key Components
- **Quarkus 3.21.1 (Java 21)** REST API microservice
- **Main package:** `de.qaware.cloud`
  - `NvdResource.java`: REST endpoint `/api/cves/{cveId}`
  - `NvdConnector.java`: Service for NVD API calls
  - `CveResource.java`: REST client interface for NVD
  - `LivenessCheck.java`: Health check endpoint
- **External dependency:** NVD API (`https://services.nvd.nist.gov/rest/json/cves/2.0`)
- **Configuration:** `src/main/resources/application.properties`
- **Dockerfiles:** Multiple variants in `src/main/docker/`
- **Kubernetes manifests:**
  - Base: `src/main/k8s/base/`
  - Overlays: `src/main/k8s/overlays/{dev,int}/`
- **Terraform:** Infrastructure as code in `src/main/terraform/`

## Developer Workflows
- **Build & Run:**
  - Dev mode: `./gradlew quarkusDev`
  - Full build: `./gradlew build`
  - Clean: `./gradlew clean`
  - Local K8s dev: `tilt up`
- **Testing:**
  - Run all tests: `./gradlew test`
  - Run specific test: `./gradlew test --tests de.qaware.cloud.NvdResourceTest`
- **API Testing:**
  - OpenAPI: `http get localhost:8080/openapi/`
  - CVE query: `http get localhost:8080/api/cves/CVE-2021-44228`

## Security & Quality Tooling
- **Static Analysis:** ErrorProne (enabled for production code)
- **Coverage:** JaCoCo (`./gradlew test jacocoTestReport`)
- **SonarCloud:** `./gradlew jacocoTestReport sonar`
- **Docker Image Scanning:** Trivy, Snyk, dockerfile_lint
- **Kubernetes Scanning:** kube-score, Checkov, Trivy
- **Terraform Scanning:** TFLint, Checkov, Snyk
- **Pre-commit Hooks:** YAML validation, trailing whitespace, end-of-file fixer, Checkov

## Conventions & Patterns
- **Security-first:** All security tools should pass before commit/PR
- **Quarkus Dev Mode:** Use for hot reload during development
- **Kustomize:** Used for environment overlays in Kubernetes
- **CI:** GitHub Actions runs build and security checks on push/PR to main
- **Java 21 required** (see `build.gradle`)

## Examples
- To scan Docker image for vulnerabilities:
  ```bash
  docker build -t iso27001-service:1.0.0 .
  trivy image -s HIGH,CRITICAL iso27001-service:1.0.0
  snyk container test --file=Dockerfile iso27001-service:1.0.0
  ```
- To scan Kubernetes manifests:
  ```bash
  checkov --directory src/main/k8s/base
  kube-score src/main/k8s/base/microservice-deployment.yaml
  trivy src/main/k8s -n default --report summary all
  ```

## References
- See `README.md` and `CLAUDE.md` for detailed workflow and architecture guidance.
- See `.github/instructions/` for commenting, Java, and security best practices.

---
**Maintainer:** M.-Leander Reimer (@lreimer)
