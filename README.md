
# Auto Mower Simulation Application Deployment on Spring Batch

This guide provides a streamlined overview of deploying a Spring Batch application using Docker and Kubernetes.

## Prerequisites

- Docker installed and running
- Kubernetes cluster access configured (e.g., kubectl configured)
- CI/CD tool (optional for automation)

## Packaging the Application

Build your application with Maven or Gradle to create an executable JAR.

```bash
# Maven
mvn clean package
```

## Dockerization

Create a `Dockerfile` in the root directory of your project with the following content:

```Dockerfile
FROM openjdk:11-jre-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build the Docker image:

```bash
docker build -t springbatch-app:latest .
```

Push the Docker image to a registry:

```bash
docker push username/springbatch-app:latest
```

## Kubernetes Deployment

Create a Kubernetes Job configuration file named `springbatch-job.yaml`:

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: springbatch-job
spec:
  template:
    spec:
      containers:
      - name: springbatch
        image: username/springbatch-app:latest
      restartPolicy: Never
```

Deploy the job to your Kubernetes cluster:

```bash
kubectl apply -f springbatch-job.yaml
```

## Automating with CI/CD

Set up your CI/CD pipeline to automate the above steps. The pipeline should include code checkout, build, test, Docker image build, and Kubernetes deployment.

## Scheduling Jobs with CronJobs

To schedule the batch job to run at regular intervals, use Kubernetes `CronJob`:

Create `springbatch-cronjob.yaml` with the following content:

```yaml
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: springbatch-cronjob
spec:
  schedule: "0 */4 * * *"  # Every 4 hours
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: springbatch
            image: username/springbatch-app:latest
          restartPolicy: OnFailure
```

Apply the CronJob configuration:

```bash
kubectl apply -f springbatch-cronjob.yaml
```

---

Please adapt each step to fit your specific deployment environment and requirements.
