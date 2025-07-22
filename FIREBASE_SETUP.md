# Firebase Setup Guide

This guide will help you set up Firebase Firestore for your Java application.

## Prerequisites

1. A Google account
2. Java 11 or higher
3. Maven installed

## Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or "Add project"
3. Enter a project name (e.g., "csc207-user-app")
4. Choose whether to enable Google Analytics (optional)
5. Click "Create project"

## Step 2: Enable Firestore Database

1. In your Firebase project, click on "Firestore Database" in the left sidebar
2. Click "Create database"
3. Choose "Start in test mode" for development (you can secure it later)
4. Select a location for your database (choose the closest to your users)
5. Click "Done"

## Step 3: Get Your Project ID

1. In Firebase Console, click on the gear icon (⚙️) next to "Project Overview"
2. Select "Project settings"
3. Copy your "Project ID" (e.g., "csc207-user-app-12345")

## Step 4: Configure Your Application

### Option A: Using Default Credentials (Development)

1. Install Google Cloud CLI:
   ```bash
   # On macOS with Homebrew
   brew install google-cloud-sdk
   ```

2. Authenticate with Google Cloud:
   ```bash
   gcloud auth application-default login
   ```

3. Set your project:
   ```bash
   gcloud config set project YOUR_PROJECT_ID
   ```

4. Update `firebase-config.properties`:
   ```properties
   firebase.project.id=YOUR_PROJECT_ID
   firebase.use.default.credentials=true
   ```

### Option B: Using Service Account (Production)

1. In Firebase Console, go to Project Settings > Service Accounts
2. Click "Generate new private key"
3. Download the JSON file and save it securely
4. Update `firebase-config.properties`:
   ```properties
   firebase.project.id=YOUR_PROJECT_ID
   firebase.service.account.path=path/to/your/serviceAccountKey.json
   firebase.use.default.credentials=false
   ```

## Step 5: Update Your Code

1. Open `src/main/java/app/AppBuilder.java`
2. Replace `"your-firebase-project-id"` with your actual project ID
3. If using service account, uncomment the service account line and comment out the default credentials line

## Step 6: Test Your Setup

1. Run your application:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass=app.Main
   ```

2. Try creating a new user account
3. Check your Firebase Console > Firestore Database to see if the user was created

## Security Rules (Optional)

For production, you should set up proper security rules in Firestore:

1. Go to Firestore Database > Rules
2. Update the rules to secure your data:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Troubleshooting

### Common Issues:

1. **"Project not found"**: Make sure your project ID is correct
2. **"Permission denied"**: Ensure you're authenticated with the correct account
3. **"Service account not found"**: Check the path to your service account JSON file

### Debug Mode:

To see more detailed error messages, you can enable debug logging by setting the environment variable:
```bash
export GOOGLE_CLOUD_PROJECT=YOUR_PROJECT_ID
```

## Data Structure

Your users will be stored in Firestore with this structure:
```
users (collection)
  └── username (document)
      ├── username: "string"
      ├── password: "string"
      ├── createdAt: timestamp
      └── updatedAt: timestamp (when password is changed)
```

## Next Steps

1. Consider implementing password hashing for security
2. Add user roles and permissions
3. Implement real-time data synchronization
4. Add data validation and error handling 