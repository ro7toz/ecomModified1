# E-Commerce Microservices - Build & Deployment Guide

## 🔧 Quick Build

```bash
# Clone the repository
git clone https://github.com/ro7toz/ecomModified1.git
cd ecomModified1

# Build all services
mvn clean install -DskipTests

# Or run tests
mvn clean install
```

## 📋 Services

- **service-discovery**: Eureka Service Registry (Port 8761)
- **cloud-config**: Spring Cloud Config Server (Port 9296)
- **api-gateway**: API Gateway with JWT Auth (Port 8200)
- **user-service**: User Management (Port 8700)
- **order-service**: Order Processing with Kafka Producer (Port 8701)
- **product-service**: Product Catalog (Port 8710)
- **payment-service**: Payment Processing (Port 8080)
- **shipping-service**: Shipping Management (Port 8082)
- **favourite-service**: User Favorites (Port 8083)
- **notification-service**: Kafka Consumer + FCM Notifications (Port 8086)
- **proxy-client**: Client Proxy (Port 8085)

## 🔐 JWT Security Setup

1. **Generate JWT Secret (32+ characters)**:
   ```bash
   openssl rand -base64 32
   ```

2. **Set Environment Variables**:
   ```bash
   export JWT_SECRET="your-generated-secret-key"
   export JWT_EXPIRATION=86400000  # 24 hours in milliseconds
   ```

3. **Or update application-prod.properties in each service**:
   ```properties
   jwt.secret=${JWT_SECRET:your-default-key}
   jwt.expiration=${JWT_EXPIRATION:86400000}
   ```

## 🔔 Firebase Setup (Optional - for Notifications)

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project
3. Generate service account key (JSON)
4. Place at project root as `firebase-service-account.json`

## 🐳 Docker & Docker Compose

```bash
# Start infrastructure (Kafka, Zookeeper, MySQL)
docker-compose up -d

# Build Docker images for each service
for service in user-service order-service notification-service api-gateway
do
  docker build -f $service/Dockerfile -t ecom-$service:latest .
done

# Run a service
docker run -p 8700:8700 -e SPRING_PROFILES_ACTIVE=dev ecom-user-service:latest
```

## 🚀 AWS ECS Deployment

```bash
# 1. Configure AWS credentials
aws configure

# 2. Create ECR repositories
for service in user-service order-service notification-service api-gateway
do
  aws ecr create-repository --repository-name ecom-$service --region us-east-1
done

# 3. Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com

# 4. Build and push images
for service in user-service order-service notification-service api-gateway
do
  docker build -f $service/Dockerfile -t ecom-$service:latest .
  docker tag ecom-$service:latest $AWS_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/ecom-$service:latest
  docker push $AWS_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/ecom-$service:latest
done
```

## ✅ Health Checks

```bash
# Check if service is up
curl http://localhost:8700/user-service/health

# Check metrics
curl http://localhost:8700/user-service/actuator/metrics

# Check Kafka topics
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092
```

## 🐛 Troubleshooting

### "Module not found: notification-service"
- **Cause**: notification-service directory wasn't created
- **Fix**: Run `git pull` to get the latest code

### "Failed to resolve org.springframework.kafka"
- **Cause**: Parent POM dependency management issue
- **Fix**: Run `mvn clean install` without `-DskipTests`

### "Kafka broker unavailable"
- **Cause**: Kafka container not running
- **Fix**: `docker-compose up -d`

### "Firebase config not found"
- **Cause**: firebase-service-account.json missing
- **Fix**: Create file or disable Firebase (notifications will be skipped)

## 📊 CI/CD Pipeline

GitHub Actions workflows automatically run on:
- **Push to master**: Full build + tests + Docker push
- **Pull requests**: Build + tests only

Workflows are in `.github/workflows/`

## 📝 Environment Variables (Production)

```bash
export DB_URL=jdbc:mysql://db-host:3306/ecommerce
export DB_USER=root
export DB_PASSWORD=secretpassword
export KAFKA_BOOTSTRAP_SERVERS=kafka:9092
export JWT_SECRET=your-production-secret-key-32-chars-min
export FIREBASE_CONFIG_PATH=/app/firebase-service-account.json
export SPRING_PROFILES_ACTIVE=prod
```

## 🔗 API Gateway Routes

- `/user-service/**` → User Service (8700)
- `/order-service/**` → Order Service (8701)
- `/product-service/**` → Product Service (8710)
- `/payment-service/**` → Payment Service (8080)
- `/shipping-service/**` → Shipping Service (8082)
- `/favourite-service/**` → Favourite Service (8083)
- `/app/**` → Proxy Client (8085)

All requests require JWT token in `Authorization: Bearer <token>` header (except `/auth/*` endpoints).

## 🏗️ Architecture

```
┌─────────────────┐
│  Client/Mobile  │
└────────┬────────┘
         │
    ┌────▼─────┐
    │  API GW  │  (JWT Auth Filter)
    │  :8200   │
    └────┬─────┘
         │
    ┌────┴────────────────────────────┬────────────────────┐
    │                                 │                    │
  User-Svc                        Order-Svc          Product-Svc
  :8700                           :8701              :8710
 (JWT Gen)                    (Kafka Producer)
    │                              │
    │                              │
    └──────────────┬───────────────┘
                   │
               ┌───▼───┐
               │ Kafka │
               └───┬───┘
                   │
         ┌─────────▼──────────┐
         │  Notification-Svc  │
         │  (Kafka Consumer)  │
         │  FCM/Email         │
         │  :8086             │
         └────────────────────┘
```

## 📞 Support

For issues, check GitHub Actions logs at: https://github.com/ro7toz/ecomModified1/actions
