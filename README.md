# Multi-Vendor Subscription Model App

This is a **multi-vendor subscription-based application** designed for managing milk and vegetable subscriptions, supporting multiple vendors. The app allows users to sign up, select vendors, and create subscriptions with options for recurring orders. It includes OTP verification for secure access and utilizes access/refresh token mechanisms for session management.

## Features

- **User Registration & Authentication**
  - Sign up via mobile number
  - OTP verification for secure sign-in
  - Access and Refresh tokens for session management
- **Multi-Vendor Catalog**
  - Location based vendors listing
  - Preferred vendor's product listing
  - Each vendor can manage their own subscription items
  - Support for ITEM develivery and SERVICE delivery
- **Subscription Management**
  - Create, modify, and manage subscriptions
  - Recurring subscription options available
  - Vendor-specific subscription statuses
  - Dynamic Pricing
- **Notifications**
  - Users receive notifications when subscriptions are created or deliveries begin
  - Billing notiifications
  - Order notifications
  - Price Change notifications
- **Tenant-Based Model**
  - Multi-tenant architecture, allowing multiple vendors to operate independently
  - Each tenant (vendor) manages their products, prices, and subscriptions

## Technologies Used

- **Backend**: Spring Boot 3.2.11 ,Java 21
- **Database**: PostgreSQL (supports multi-tenant data management)
- **Caching**: Redis, InMemory (used for tracking OTP attempts and other caching needs)
- **Async Communication**: SQS,SNS,Apache Nifi
- **Security**: Spring Security for managing authentication, token-based access control
- **Messaging**: SMS-based OTP service for user verification
- **API Documentation**: Swagger

## Setup Instructions

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/multi-vendor-subscription-app.git
   cd multi-vendor-subscription-app
   ```
2. **Configure environment variables:**
   - Set up database details, Cache configurations, and API keys for SMS/OTP services.
3. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```
4. **Access API documentation (Swagger UI):**
   Navigate to `http://localhost:8080/api/swagger-ui.html` to explore available API endpoints.

## API Endpoints

- **User Sign Up**: `POST /api/v1/signup`
- **OTP Verification**: `POST /api/v1/verify-otp`
- **Vendor Listing**: `GET /api/v1/vendors`
- **Create Subscription**: `POST /api/v1/subscriptions`

## Future Enhancements

- Integration with payment gateways
- Advanced reporting for vendors
- Redis can be used for tracking OTP attempts and other caching needs

