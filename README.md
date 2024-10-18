
# Multi-Vendor Subscription Model App

This is a **multi-vendor subscription-based application** designed for managing milk and vegetable subscriptions, supporting multiple vendors. The app allows users to sign up, select vendors, and create subscriptions with options for recurring orders. It includes OTP verification for secure access and utilizes access/refresh token mechanisms for session management.

## Features

- **User Registration & Authentication**
  - Sign up via mobile number
  - OTP verification for secure sign-in
  - Access and Refresh tokens for session management
- **Multi-Vendor Support**
  - Users can select from multiple vendors
  - Each vendor can manage their own subscription items
  - Support for various subscription items such as milk, vegetables, and more
- **Subscription Management**
  - Create, modify, and manage subscriptions
  - Recurring subscription options available
  - Vendor-specific subscription statuses
- **Notifications**
  - Users receive notifications when subscriptions are created or deliveries begin
- **Tenant-Based Model**
  - Multi-tenant architecture, allowing multiple vendors to operate independently
  - Each tenant (vendor) manages their products, prices, and subscriptions

## Technologies Used

- **Backend**: Spring Boot 3.2 ,Java 21
- **Database**: PostgreSQL (supports multi-tenant data management)
- **Caching**: InMemory/Redis (used for tracking OTP attempts and other caching needs)
- **Security**: Spring Security for managing authentication, token-based access control
- **Messaging**: SMS-based OTP service for user verification
- **API Documentation**: Swagger

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/s713278/ECommerceApp.git
   cd ECommerceApp
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

- **User Sign Up**: `POST /api/v1/auth/signup`
- **OTP Verification**: `POST /api/v1/auth/verify-otp`
- **Vendor Listing**: `GET /api/v1/auth/vendors`
- **Create Subscription**: `POST /api/v1/subscriptions`

## Future Enhancements
- Integrae with Social Login
- Integration with payment gateways
- Advanced reporting for vendors
- Mobile app support for users and vendors
- Promotions and Discounts
- Cross Selling
## Swagger API Reference

![image](https://github.com/user-attachments/assets/1b34ff24-e681-441e-8161-6ccf0c7752e7)

![image](https://github.com/user-attachments/assets/c3b0e3ae-29c7-413d-bad2-1b18b84e1cf1)


![image](https://github.com/user-attachments/assets/badbe07f-73db-4974-8a7f-31544a795240)

![image](https://github.com/user-attachments/assets/424a1bce-d234-4869-b049-2903816d4bbb)

![image](https://github.com/user-attachments/assets/b18df813-4423-4d1d-9755-b082b5e021fe)





