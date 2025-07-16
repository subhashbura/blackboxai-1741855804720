# NTLM Functionality Analysis Report

## Overview
This Android application contains NTLM (NT LAN Manager) authentication functionality for connecting to Exchange Web Services (EWS). The implementation is found in the `app/app/src/main/java/com/provizit/qrscanner/Services/` directory.

## NTLM Implementation Details

### 1. NTLMAuthenticator Class
**Location**: `app/app/src/main/java/com/provizit/qrscanner/Services/NTLMAuthenticator.java`

**Purpose**: Implements NTLM authentication for OkHttp requests using the three-way handshake protocol.

**Key Features**:
- Implements the `okhttp3.Authenticator` interface
- Handles NTLM Type 1 (Negotiate), Type 2 (Challenge), and Type 3 (Authenticate) messages
- Uses Base64 encoding for message transmission
- Supports domain-based authentication

**Constructor Parameters**:
- `username`: Domain username
- `password`: User password
- `domain`: Windows domain name

**Authentication Flow**:
1. **Type 1 Message**: Client sends negotiate message with domain info
2. **Type 2 Message**: Server responds with challenge
3. **Type 3 Message**: Client sends authentication response

### 2. Dependencies
The project uses the **jcifs-ng** library for NTLM implementation:

**Version**: 2.1.6 (from `app/gradle/libs.versions.toml`)
**Library**: `eu.agno3.jcifs:jcifs-ng`

**Required Imports**:
```java
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;
```

### 3. Integration Status

#### Current Usage:
- **EwsClient.java**: Uses basic authentication instead of NTLM
- **DataManger.java**: Uses standard OkHttp client without NTLM

#### Potential Issue:
The `NTLMAuthenticator` class references `Type1Message` but doesn't import it. This will cause a compilation error.

## Code Issues Found

### 1. Missing Import
**File**: `NTLMAuthenticator.java`
**Issue**: `Type1Message` is used but not imported
**Fix Required**: Add `import jcifs.ntlmssp.Type1Message;`

### 2. Commented Implementation
**File**: `NTLMAuthenticator.java` (lines 75-77)
**Issue**: Usage example is commented out
**Status**: Documentation only, not a functional issue

### 3. Hardcoded Credentials
**File**: `EwsClient.java`
**Issue**: Contains hardcoded credentials:
- Username: `serviceuser@provizit.com`
- Password: `Skhan@123`
**Security Risk**: High - credentials should be externalized

## Current Authentication Methods

### EwsClient.java
- **Method**: Basic Authentication
- **Implementation**: Uses `okhttp3.Credentials.basic()`
- **Target**: Exchange Web Services at `https://ews.provizit.com/EWS/Exchange.asmx`

## Recommendations

### 1. Fix Compilation Error
Add missing import to `NTLMAuthenticator.java`:
```java
import jcifs.ntlmssp.Type1Message;
```

### 2. Integrate NTLM Authentication
Replace basic authentication in `EwsClient.java` with NTLM:
```java
OkHttpClient client = new OkHttpClient.Builder()
    .authenticator(new NTLMAuthenticator(username, password, domain))
    .build();
```

### 3. Security Improvements
- Remove hardcoded credentials
- Use secure configuration management
- Consider using environment variables or secure storage

### 4. Testing
- Test NTLM authentication against the Exchange server
- Verify domain authentication works correctly
- Test error handling for authentication failures

## Project Structure
- **Android App**: QR Scanner application with EWS integration
- **Target SDK**: 35
- **Min SDK**: 24
- **Language**: Java
- **Build System**: Gradle with Kotlin DSL

## Conclusion
The NTLM functionality is implemented but not currently used. The codebase contains a complete NTLM authenticator that can be integrated with the existing EWS client. However, there are compilation issues that need to be resolved and security concerns regarding hardcoded credentials that should be addressed before deployment.