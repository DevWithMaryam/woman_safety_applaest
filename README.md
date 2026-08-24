# SafeHer — Women Safety App

A modern Android safety application built with Java and XML Views, following MVVM architecture with a Repository pattern. SafeHer helps women raise emergency alerts, share their live location, and reach trusted contacts, guardians, and police in a single tap.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Language](https://img.shields.io/badge/language-Java-orange)
![Architecture](https://img.shields.io/badge/architecture-MVVM-blue)

---

## Overview

SafeHer is a role-based emergency response app supporting three types of users — **Woman**, **Guardian**, and **Police** — connected through a single, unified authentication flow. When a woman triggers an SOS, her live location and emergency type are broadcast in real time to the relevant guardians and, where appropriate, police, using Firebase Realtime Database and Google Maps intents.

## Problem Statement

Personal safety apps are often either overloaded with unnecessary features or too simplistic to be genuinely useful in an emergency. SafeHer focuses on the essentials: get help requested, get a location shared, and get it in front of the right people — fast, and without requiring a paid Maps SDK or complex backend.

## Key Features

- One-tap **SOS** with a confirmation countdown to avoid accidental triggers
- Emergency type selection — **Police**, **Medical**, or **General** — each routed to the appropriate audience
- Real-time location sharing via **Firebase Realtime Database**
- Emergency contacts management (add, call, remove)
- Nearby police station / hospital search via **Google Maps intents** (no API key required)
- Role-based dashboards for Woman, Guardian, and Police
- Full loading / success / error states on every network-dependent action

## User Roles

| Role | Capabilities |
|---|---|
| **Woman** | Trigger SOS, manage emergency contacts, view safety tips, share/view location |
| **Guardian** | View live emergency alerts from linked women, open shared location in Maps |
| **Police** | View police-relevant alerts only (medical-only alerts are excluded), navigate to the location |

## Emergency Flow

1. Woman presses the **SOS** button on her home screen
2. A short countdown gives her the chance to cancel
3. She selects an emergency type: **Police**, **Medical**, or **General**
4. The app acquires her current location via `FusedLocationProviderClient`
5. An alert is written to Firebase Realtime Database with her name, type, coordinates, and timestamp
6. Guardians see the alert immediately; Police see it too **unless it's medical-only**
7. Guardian/Police can open the location directly in Google Maps, or start navigation
8. The alert can be marked **Resolved** once the situation is handled

Every step degrades gracefully — permission denial, disabled GPS, no internet, or a failed database write all show a clear, specific message instead of a blank screen.

## Screenshots

*(Ensure these images are present in your `screenshots` folder. The names below match your project structure)*

<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="screenshots/welcome.png" width="200" alt="Welcome Screen" />
  <img src="screenshots/woman_home.png" width="200" alt="Woman Home" />
  <img src="screenshots/sos_flow.png" width="200" alt="SOS Flow" />
  <img src="screenshots/emergency_active.png" width="200" alt="Emergency Active" />
  <img src="screenshots/guardian_dashboard.png" width="200" alt="Guardian Dashboard" />
  <img src="screenshots/nearby_help.png" width="200" alt="Nearby Help" />
</div>

## Tech Stack

- **Language:** Java
- **UI:** XML Views, ViewBinding, Material Components
- **Architecture:** MVVM + Repository Pattern
- **Auth:** Firebase Authentication (Email/Password)
- **Database:** Firebase Realtime Database
- **Location:** FusedLocationProviderClient (Google Play Services)
- **Maps:** Google Maps via Android intents — no embedded SDK, no API key billing
- **Build:** Gradle, Android Gradle Plugin

## Architecture

```text
UI (Activities / Fragments)
        ↓ observes
ViewModel (LiveData<Resource<T>>)
        ↓ calls
Repository (Firebase / Location APIs)
        ↓
Firebase Realtime Database / FusedLocationProviderClient / Google Maps Intents