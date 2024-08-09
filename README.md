# Keycloak TOTP API Extension

This Keycloak extension enables generating, registering, and verifying TOTP (Time-Based One-Time Password) credentials via API. It provides a set of endpoints to manage TOTP credentials for users programmatically.

## Features

- Generate TOTP secrets and QR codes
- Register TOTP credentials for users
- Verify TOTP codes

## Compatibility

Tested on Keycloak 25

## Building the Project

This project uses Gradle for building. To build the project, follow these steps:

1. Clone this repository:
   ```
   git clone https://github.com/medihause/keycloak-totp-api.git
   cd keycloak-totp-api
   ```
2. Build the project using the `shadowJar` task:
   ```
   ./gradlew shadowJar
   ```

This will create a JAR file in the `build/libs` directory.

## Installation

### Downloading the Extension

1. Go to the [Releases](https://github.com/medihause/keycloak-totp-api/releases) page of this repository.
2. Download the latest release, making sure to choose the JAR file with the 'all' suffix (e.g., `keycloak-totp-api-1.0.0-all.jar`), as it includes all necessary dependencies.

### Installing the Extension

#### Standalone (without container)

1. Copy the downloaded JAR file to the `providers` folder in your Keycloak installation directory.
2. Run the following command to build Keycloak with the new extension:

   ```bash
   ${KEYCLOAK_HOME}/bin/kc.sh build
   ```

#### Docker

When using Docker, you need to make the extension available to the Keycloak container. You can do this by:

1. Mounting the JAR file into the container:
   
   Add this volume mount to your Docker run command or docker-compose file:
   ```
   -v /path/to/keycloak-totp-api-1.0.0-all.jar:/opt/keycloak/providers/keycloak-totp-api-1.0.0-all.jar
   ```

   OR

2. Copying the JAR file into a custom Docker image:
   
   If you're building a custom Keycloak image, add this line to your Dockerfile:
   ```
   COPY keycloak-totp-api-1.0.0-all.jar /opt/keycloak/providers/
   ```

After adding the extension, make sure to build the Keycloak image if you're using a custom Dockerfile.

## API Endpoints

### Generate TOTP Secret

Generates a new TOTP secret and QR code for a user.

- **Method**: GET
- **URL**: `{{BASE_URL}}/realms/{{REALM}}/totp-api/{{USER_ID}}/generate`
- **Response**:
  ```json
  {
    "encodedSecret": "OFIWESBQGBLFG432HB5G6TTLIVIEGU2O",
    "qrCode": "iVBO...."
  }
  ```
  The `qrCode` is a base64-encoded image.

### Register TOTP Credential

Registers a TOTP credential for a user.

- **Method**: POST
- **URL**: `{{BASE_URL}}/realms/{{REALM}}/totp-api/{{USER_ID}}/register`
- **Request Body**:
  ```json
  {
    "deviceName": "DeviceOne",
    "encodedSecret": "OFIWESBQGBLFG432HB5G6TTLIVIEGU2O",
    "initialCode": "128356",
    "overwrite": true
  }
  ```
  Set `overwrite` to `true` to replace an existing TOTP credential.
- **Response**:
  ```json
  {
    "message": "TOTP credential registered"
  }
  ```

### Verify TOTP Code

Verifies a TOTP code for a user.

- **Method**: POST
- **URL**: `{{BASE_URL}}/realms/{{REALM}}/totp-api/{{USER_ID}}/verify`
- **Request Body**:
  ```json
  {
    "deviceName": "DeviceOne",
    "code": "866359"
  }
  ```
- **Response**:
  ```json
  {
    "message": "TOTP code is valid"
  }
  ```

## Authentication

All API requests must be authenticated. The requester must:

1. Be authenticated and provide a valid bearer token.
2. Be a service account.
3. Have the `manage-totp` realm role.
