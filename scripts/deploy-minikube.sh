#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PROFILE="${MINIKUBE_PROFILE:-minikube}"
IMAGE_NAME="${IMAGE_NAME:-student-crud-grpc:local}"

if [[ -f "${ROOT_DIR}/.mvn/wrapper/maven-wrapper.properties" ]]; then
  MAVEN_CMD=("${ROOT_DIR}/mvnw")
else
  MAVEN_CMD=("mvn")
fi

echo "Starting Minikube profile: ${PROFILE}"
minikube start -p "${PROFILE}" --driver=docker

echo "Building the application JAR"
(cd "${ROOT_DIR}" && "${MAVEN_CMD[@]}" clean package -DskipTests)

echo "Building Docker image inside Minikube"
eval "$(minikube -p "${PROFILE}" docker-env)"
(cd "${ROOT_DIR}" && docker build -t "${IMAGE_NAME}" .)

echo "Deploying PostgreSQL and application manifests"
kubectl apply -f "${ROOT_DIR}/k8s-postgres.yml"
kubectl apply -f "${ROOT_DIR}/k8s-deployment.yml"
kubectl apply -f "${ROOT_DIR}/k8s-service.yml"

echo "Waiting for the deployments to become ready"
kubectl rollout status deployment/postgres --timeout=180s
kubectl rollout status deployment/student-crud-grpc --timeout=180s

echo
echo "Pods"
kubectl get pods -o wide
echo
echo "Services"
kubectl get svc
echo
echo "Access the application with one of these commands:"
echo "  HTTP: kubectl port-forward service/student-crud-grpc-service 8083:8083"
echo "  gRPC: kubectl port-forward service/student-crud-grpc-service 9090:9090"
echo
echo "If you want Minikube-generated URLs in a separate terminal, run:"
echo "  minikube -p ${PROFILE} service student-crud-grpc-service --url"
