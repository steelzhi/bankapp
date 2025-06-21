#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# --- Configuration ---
ACCOUNTS_SERVICE_DIR="accounts-service"
ACCOUNTS_IMAGE_NAME="accounts-service"
ACCOUNTS_IMAGE_TAG="latest"

EXCHANGE_GENERATOR_SERVICE_DIR="exchange-generator-service"
EXCHANGE_GENERATOR_IMAGE_NAME="exchange-generator-service"
EXCHANGE_GENERATOR_IMAGE_TAG="latest"

FRONT_UI_SERVICE_DIR="front-ui-service"
FRONT_UI_IMAGE_NAME="front-ui-service"
FRONT_UI_IMAGE_TAG="latest"

NOTIFICATIONS_SERVICE_DIR="exchange-generator-service"
NOTIFICATIONS_IMAGE_NAME="exchange-generator-service"
NOTIFICATIONS_IMAGE_TAG="latest"

# ---------------------

echo "Building Docker image for Accounts Service: ${ACCOUNTS_IMAGE_NAME}:${ACCOUNTS_IMAGE_TAG}"
# Navigate to the accounts service directory
pushd "$ACCOUNTS_SERVICE_DIR" > /dev/null

# Build the Docker image using the base name
docker build -t "${ACCOUNTS_IMAGE_NAME}:${ACCOUNTS_IMAGE_TAG}" .

echo "Accounts Service image built: ${ACCOUNTS_IMAGE_NAME}:${ACCOUNTS_IMAGE_TAG}"

# Navigate back
popd > /dev/null

echo ""
echo "Building Docker image for Exchange generator Service: ${EXCHANGE_GENERATOR_IMAGE_NAME}:${EXCHANGE_GENERATOR_IMAGE_TAG}"
# Navigate to the exchange-generator service directory
pushd "$EXCHANGE_GENERATOR_SERVICE_DIR" > /dev/null

# Build the Docker image using the base name
docker build -t "${EXCHANGE_GENERATOR_IMAGE_NAME}:${EXCHANGE_GENERATOR_IMAGE_TAG}" .

echo "Exchange generator Service image built: ${EXCHANGE_GENERATOR_IMAGE_NAME}:${EXCHANGE_GENERATOR_IMAGE_TAG}"

# Navigate back
popd > /dev/null

echo ""
echo "Building Docker image for Front UI Service: ${FRONT_UI_IMAGE_NAME}:${FRONT_UI_IMAGE_TAG}"
# Navigate to the front-ui service directory
pushd "$FRONT_UI_SERVICE_DIR" > /dev/null

# Build the Docker image using the base name
docker build -t "${FRONT_UI_IMAGE_NAME}:${FRONT_UI_IMAGE_TAG}" .

echo "Front UI Service image built: ${FRONT_UI_IMAGE_NAME}:${FRONT_UI_IMAGE_TAG}"

# Navigate back
popd > /dev/null

echo ""
echo "Building Docker image for Notifications Service: ${NOTIFICATIONS_IMAGE_NAME}:${NOTIFICATIONS_IMAGE_TAG}"
# Navigate to the notifications service directory
pushd "$NOTIFICATIONS_SERVICE_DIR" > /dev/null

# Build the Docker image using the base name
docker build -t "${NOTIFICATIONS_IMAGE_NAME}:${NOTIFICATIONS_IMAGE_TAG}" .

echo "Notifications Service image built: ${NOTIFICATIONS_IMAGE_NAME}:${NOTIFICATIONS_IMAGE_TAG}"

# Navigate back
popd > /dev/null

echo ""
echo "Docker images built successfully!" 