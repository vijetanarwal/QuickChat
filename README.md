# QuickChat 💬

A real-time Android chat application built with Kotlin and Firebase, featuring modern Android architecture patterns.

## Features
- 💬 Real-time messaging with Firebase Firestore
- 🔐 Email/Password authentication
- 🟢 Online / Last seen status indicator
- 🖼️ Edit or remove profile picture
- 🏗️ MVVM Architecture with Dagger Hilt

## ScreenShots
<img width="260" height="592" alt="image" src="https://github.com/user-attachments/assets/cbe7fc52-f118-427a-b412-93adda58155a" /> <img width="259" height="592" alt="image" src="https://github.com/user-attachments/assets/6938de10-65de-4015-83e4-06182d437af5" />  <img width="274" height="591" alt="image" src="https://github.com/user-attachments/assets/8fafb819-26bd-4949-8f46-ac4e0af20515" />




## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Backend:** Firebase Firestore, Firebase Auth, FCM
- **Dependency Injection:** Dagger Hilt
- **Async:** Kotlin Coroutines & LiveData
- **Networking:** Retrofit
- **Min SDK:** 21 | **Target SDK:** 31

## Setup
1. Clone the repo
2. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
3. Enable Authentication (Email/Password) and Firestore
4. Download `google-services.json` and place it in the `app/` folder
5. Build and run in Android Studio

## Architecture
This app follows MVVM clean architecture:
- **UI Layer** — Fragments + ViewBinding
- **ViewModel Layer** — Business logic with Coroutines
- **Repository Layer** — Firebase data operations
- **DI** — Dagger Hilt for dependency injection
