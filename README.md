# Mapping ISO 27001 to Modern Software Engineering
# Engineering Trust: Integrating Security into Your Modern SDLC

See [Presentation: Mapping ISO 27001 to Modern Software Engineering: Secure by Design](https://jfokus.se/talks/2316)

See [Presentation: Engineering Trust: Integrating Security into Your Modern SDLC](https://oredev.org/program/647c84a5-1a13-4641-8d8f-49109cadf78b)

In today's increasingly complex digital landscape, security is no longer an afterthought but a foundational pillar of software engineering. Achieving ISO 27001 certification has become essential for many organizations striving to build trust, minimize risks, and ensure regulatory compliance. In this talk, we will explore why this certification is so crucial for software companies and delve into the specifics of ISO 27001 controls (especially clause A.8) that focus on secure development. We will map these security requirements onto a modern Software Development Lifecycle (SDLC), highlighting practical approaches that integrate nicely with agile frameworks and DevOps principles.

Furthermore, we will explore a range of tools, such as static analysis software, dependency scanners, automated deployment checks and many more to effectively meet ISO standards. We will also discuss how to adopt and integrate OWASP SAMM (Software Assurance Maturity Model) into the development process as a way to continuously assess and improve the security posture of your projects, ensuring that security becomes a continuous, iterative effort within your teams and your organization.

## Usage

```bash
# build and run the service, or use Tilt
./gradlew quarkusDev
tilt up

# call the service endpoints
http get localhost:8080/openapi/
http get localhost:8080/api/cves/CVE-2021-44228
```

### Google ErrorProne

Find common programming mistakes early during development as part of the Java compile phase.
See https://errorprone.info

```groovy
plugins {
    id 'java'
    id "net.ltgt.errorprone" version "3.1.0"
}

dependencies {
    // dependency for the javac compiler plugin
    errorprone "com.google.errorprone:error_prone_core:2.19.1"
}

tasks.named("compileJava").configure {
    options.errorprone.enabled = true
    // and many other options
}
```

### SonarCloud Security Analysis

Sonar can detect 54 security vulnerabilities and 38 security hotspots using static code analysis.
See https://rules.sonarsource.com/java/type/Vulnerability

```groovy
plugins {
    id "jacoco"
	id "org.sonarqube" version "6.0.1.5171"
}

jacocoTestReport {
    dependsOn test // tests are required to run before generating the report
	reports {
		xml.required = true
	}
}

sonar {
  properties {
    property "sonar.projectKey", "lreimer_iso27001-secure-se"
    property "sonar.organization", "lreimer"
    property "sonar.host.url", "https://sonarcloud.io"
  }
}
```

See https://sonarcloud.io/project/overview?id=lreimer_iso27001-secure-se
Also, it can easily be integrated into your CI build as well as your IDE (e.g. VS Code) using SonarLint.

### Docker Image Vulnerability Scanning

Several suitable tools can be used to scan your Docker images for vulnerable OS packages and
other software components.

```bash
# to manually build the Docker image use on of the following commands
./gradlew build
docker build -f src/main/docker/Dockerfile.jvm -t iso27001-service:1.0.0 .
docker build -t iso27001-service:1.0.0 .

# Installation and usage instructions for Docker Lint
# https://github.com/projectatomic/dockerfile_lint
dockerfile_lint -f Dockerfile -r src/test/docker/basic_rules.yaml
dockerfile_lint -f Dockerfile -r src/test/docker/security_rules.yaml

# Installation and usage instructions for Trivy
# https://github.com/aquasecurity/trivy
trivy image -s HIGH,CRITICAL iso27001-service:1.0.0

# Installation and usage instructions for Snyk
# https://docs.snyk.io/snyk-cli/install-the-snyk-cli
snyk container test --file=Dockerfile iso27001-service:1.0.0
```

### Kubernetes Security Scanning

Many security misconfigurations are possible when deploying Kubernetes workloads.
Most can be found easily via static code analysis using different tools.

```bash
# see https://www.kubeval.com
kubeval src/main/k8s/base/microservice-deployment.yaml

# see https://github.com/yannh/kubeconform
kubeconform src/main/k8s/base/microservice-deployment.yaml

# see https://github.com/zegl/kube-score
kubectl score src/main/k8s/base/microservice-deployment.yaml

# Checkov, see https://github.com/bridgecrewio/checkov
checkov --directory src/main/k8s/base
checkov --directory src/main/k8s/overlays/int

# Snyk, see https://docs.snyk.io/snyk-cli/install-the-snyk-cli
snyk iac test src/main/k8s/base
snyk iac test src/main/k8s/overlays/int

# Trivy, see https://github.com/aquasecurity/trivy
trivy src/main/k8s -n default --report summary all
trivy src/main/k8s -n default --report all all
```

### Terraform Security Scanning

Many security misconfigurations of your cloud infrastructure are possible when working with Terraform.
Most can be found easily via static code analysis using different tools.

```bash
# TFLint und Rule Sets
# see https://github.com/terraform-linters/tflint
# see https://github.com/terraform-linters/tflint-ruleset-aws
terraform init
terraform plan
tflint

# Checkov, see https://github.com/bridgecrewio/checkov
checkov --directory src/main/terraform/

# Snyk, see https://docs.snyk.io/snyk-cli/install-the-snyk-cli
snyk iac test src/main/terraform/
```

### Continuous Developer Experience

The linters and static analysis tools are ideally run before and with every Git commit and push.

```bash
# see https://github.com/pre-commit/pre-commit
brew install pre-commit

# see https://pre-commit.com/hooks.html
# see https://github.com/gruntwork-io/pre-commit
# see https://github.com/antonbabenko/pre-commit-terraform

# install the Git hook scripts
pre-commit install
pre-commit run --all-files
```

### Continuous Integration

GitHub and many other platforms provide CI and security integration functionality that can be used.

```bash
# see https://github.com/lreimer/iso27001-secure-se/actions
# see https://github.com/lreimer/iso27001-secure-se/actions/new?category=security
```

### AI Augmented Security Reviews

GitHub Copilot or maybe Claude Code or any other AI coding agent can help to write secure code if instructed properly. These can also help to perform security reviews.

```bash
# Github Copilot can be customized using instructions, prompts and chat modes
# see .github/instructions/
# see .github/prompts/
# see .github/chatmodes/

# Claude Code also provides Github actions to perform code reviews
# install the actions using the /install-github-app command on the Claude terminal
# see .github/workflows/claude-code-review.yml
# see .github/workflows/claude.yml
```

### ZAP - API Scan

The ZAP API scan is a script that is available in the ZAP Docker images. It is tuned for performing scans against APIs defined by OpenAPI, SOAP, or GraphQL via either a local file or a URL.

```bash
# see https://www.zaproxy.org/docs/docker/api-scan/
docker pull owasp/zap2docker-weekly  
docker run -t owasp/zap2docker-weekly zap-api-scan.py \  
    -t http://localhost:8080/openapi/ \
    -f openapi \
    -l INFO
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
