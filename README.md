# AI Fashion Recommendation App

An AI-powered fashion ecommerce Android application built using Jetpack Compose, FastAPI, Room Database, and Retrofit.

The application provides personalized fashion product recommendations based on user search patterns and product popularity. It includes features such as product search, recommendation engine, cart management, purchase history tracking, and profile customization.

---

# Features

- AI-based Product Recommendation System
- Personalized Search Recommendations
- Product Search Functionality
- Popular Products Section
- Cart Management System
- Purchase History Tracking
- Product Detail View
- Dynamic Size Selection
- Quantity Management
- Local Data Persistence using Room Database
- REST API Integration using Retrofit
- FastAPI Backend Integration
- Modern UI built with Jetpack Compose

---

# Tech Stack

## Android Frontend

- Kotlin
- Jetpack Compose
- Room Database
- Retrofit
- Coroutines
- Coil Image Loader
- Material Design 3

## Backend

- FastAPI
- Pandas
- Python

---

# Architecture

```text
Android App
     ↓
Retrofit API Calls
     ↓
FastAPI Backend
     ↓
Recommendation Engine
     ↓
Fashion Dataset
```

---

# Recommendation System

The application uses a rule-based recommendation engine that suggests related fashion products based on user search queries and product categories.

Example:

- Shirt → Pants, T-Shirts
- Hoodie → Joggers, Cargo Pants
- Dress → Heels, Handbags

The app also tracks user search history locally and generates personalized recommendations using the most frequently searched categories.

---

# Project Structure

```text
app/
 ├── data/
 ├── network/
 ├── navigationbar/
 ├── ui/
 ├── room/
 └── theme/

backend/
 ├── main.py
 └── fashion_products.xlsx
```

---

# Screenshots

## Home Screen

(Add screenshot here)

## Product Details

(Add screenshot here)

## Cart Screen

(Add screenshot here)

## Profile Screen

(Add screenshot here)

---

# Future Improvements

- Machine Learning Recommendation System
- Firebase Authentication
- Cloud Backend Deployment
- MVVM Architecture
- Navigation Compose
- Payment Gateway Integration
- Wishlist Feature
- Dark Mode
- Product Reviews

---

# Installation

## Android App

1. Clone the repository

```bash
git clone https://github.com/iamloki143/ai-fashion-recommendation-app.git
```

2. Open in Android Studio

3. Sync Gradle

4. Run the application

---

# Backend Setup

1. Navigate to backend folder

```bash
cd backend
```

2. Install dependencies

```bash
pip install fastapi uvicorn pandas openpyxl
```

3. Start server

```bash
uvicorn main:app --reload
```

---

# Author

Lokesh Kumar

GitHub:
https://github.com/iamloki143
