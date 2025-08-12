# **miao miao / nyan nyanz – Lost & Found System**

## **Authors & Contributors**
- Yipeng Zhao
- Xiaotong Shen
- Ethan Hapurne
- Franz Shi
- Ya-Chun Ho  

---

## **Summary**
**Purpose:**  
This project is a **Lost & Found system** with real-time posting, account management, admin features, fuzzy searching and direct messaging (DM) features, inspired by platforms like Piazza but enhanced with location-based search, tag filtering, and credibility systems.  

**Why it was made:**  
To create a more efficient and community-driven way for people to report, find, and return lost items in a university or public setting.  

**Problem solved:**  
- Centralizes lost & found communication.  
- Reduces time to match lost items with owners.  
- Encourages engagement via a reward and credibility system.  

---

## **Table of Contents**
1. [Features](#features)
2. [Installation](#installation)
3. [Usage](#usage)
4. [License](#license)
5. [Feedback](#feedback)
6. [Contributing](#contributing)

---

## **Features**
- **Lost & Found Posting:**  
  Post lost or found items with tags (e.g., “wallet”, “airpods”) and general location.
- **Search & Filters:**  
  Search by tag, keyword, location, or time. Includes fuzzy search for typos and related terms.
- **Comment System:**  
  Comment under posts to provide updates or information.
- **Direct Messaging (DM):**  
  Private chat between users to arrange returns.
- **User Blocking:**  
  Block unwanted messages from specific users.
- **Admin Tools:**  
  Delete/edit posts, manage users, handle harassment reports.
- **Data Persistence:**  
  Uses Firebase APIs to store posts, comments, and chat history.

---

## **Installation**
### Requirements
- Java Version: [NEED INFO]
- Dependencies:
    - Firebase Auth REST API (Docs: https://firebase.google.com/docs/reference/rest/auth)
    - Cloud Firestore REST API (Docs: https://firebase.google.com/docs/firestore/use-rest-api)

### Steps
1. Clone this repository:
   git clone https://github.com/Ethan-Hapurne/CSC207-Miao-Miao.git

2. Run the main application:
    - Locate Main.java in:
      main/java/app/Main.java
    - Compile and run it with your preferred IDE (e.g., IntelliJ, Eclipse) or from the command line.

---

## **Usage**
1. **Register/Login:** Create an account with email and password.  
2. **Post Lost/Found Item:** Include description, tags, and location.  
3. **Search:** Use keyword, location, or tag filters to find items.  
4. **Comment:** Provide updates or tips under a post.  
5. **Direct Message:** Privately message a user.  
6. **Admin Actions:**  
   - Remove/edit posts.  
   - Block harassing users.  

---

## **License**
This project is licensed under the MIT License - see the LICENSE file for details.

---

## **Feedback**
### How to Provide Feedback
- **GitHub Issues:**  
  Go to the "Issues" tab of this repository and click "New Issue."  
  Clearly describe your suggestion, bug, or question.

### Feedback Guidelines
- Be respectful and constructive.
- For bug reports, include:
    1. Steps to reproduce the issue
    2. Expected result
    3. Actual result
    4. Screenshots (if applicable)
- For feature requests, explain:
    - The problem it solves
    - Why it would be useful

---

## **Contributing**
- Fork the repository.  
- Create a new branch for your feature:
  ```bash
  git checkout -b feature-name
  ```
- Submit a pull request with:
  - A clear description of changes.
  - Screenshots if applicable.
