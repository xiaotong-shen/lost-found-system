# CSC207-Miao-Miao
* Franz - user story

Project Specification for Group # 8

[see additional instructions on Quercus for filling in all parts of the blueprint]

Team Name: miao miao/nyan nyanz

Domain:

[Establish what the domain is and what the broad purpose of your proposed software will be — this can be quite brief]



Lost and found system with real time post and chat features

Lost item finding app (piazza) + more features
Chatting (DM)


Software Specification:

[In plain English, what should the program be able to do (not how should it do it)]

[think in terms of nouns and verbs, which will map onto variables and methods in the program]

General piazza functions (post, comment, react, etc)  
User posts lost object, tagging items (e.g. “airpods”, “wallet”) and selecting general location
Search function (or.. In turn... tags such as ‘airpods’, etc)
Commenting under posts
Direct messaging (DM) between users
Reward system for the users that find the item
Potential ranking system
Storing posts and chat history using an external API
Time decay/visibility changes (older posts slowly get deleted)


User Stories:

[statements of interactions between the user and the system]

[see additional instructions on Quercus]

[aim for at least one user story per group member + 1 extra; in the table below, each group member must be assigned to one user story + mark one user story as being a team user story — this one should be the one that is most central to the basic functionality of your system. That is, the one you would probably want to implement first.]

Team Story:

[Team story]: Alex loses his wallet in the library and posts in the “Lost” section with tags "wallet" and "library." Another user comments information about the lost item. A third user sends a private message to Alex, saying that the item was found.
[Yipeng's story]: Sam finds an AirPods case and posts it in the "Found" section. He enters the location as "Science Wing" and adds a photo.
[Xiaotong's story]: Jamie searches for "water bottle" and finds a matching post. She sends a DM to the user to confirm if it's hers.
[Ethan’s story]: Jim receives an unwanted DM from another user on the platform and then blocks the other user to no longer receive messages from them.
[Franz’s story]: An admin user sees an unwanted post on the site, and deletes the post to keep the community free from these issues.
[Rex’s story]: Adem finds the lost item from a post on the way home and gets $30 as a reward from the person who lost it.


TA Comments:

Search functionality (regex?) can be sort by time or location or user or tag or etc. Depends on how complicated the function you guys want to implement
Render photo in Java is doable or not? Need to check
Admin functions (manage users, delete posts, change contents)
Post check piazza to see the functions  
Things like edit post, duplicate, status ...


Proposed Entities for the Domain:

[based on your specification, indicate a few potential entities for your domain — including their names and instance variables]


User:  
userID: int
username: String

password: String
posts: List<int>
comments: List<int>
chats: List<int>
points: int
rating: float
userType: String

potential
profilePictureURL: String



POST:  
postID: int
postType: Boolean
title: String
description: String
tags: List<String>
timestamp: LocalDateTime
author: User
comments: List<int>
numberOfLikes: int
reactions: Map<userID: int, reactionID: int>
itemsPictureURL: String



COMMENT:
commentID: String
content: String
author: User
numberOfLikes: int



Reaction:

reactionID: String

reaction

————

Chat/DM features:
chatID: int
user1ID: int
user2ID: int
messages: List<int>

Message:

messageID: int
messageContent: String
timeStamp: LocalDateTime



Proposed API for the project:

[links to one or more APIs your team plans to make use of; include brief notes about what services the API provides and whether you have successfully tried calling the API]

Firebase Auth REST API: https://firebase.google.com/docs/reference/rest/auth
- useful for the authentication of the user

Cloud Firestore REST API: https://firebase.google.com/docs/firestore/use-rest-api
- useful for storing data that is used inside the app, e.g. posts, users, comments etc.

TA Comments:

AI API for advanced search functionality
Adding admin feature for difficulty (like a different subclass of user??? )  
Scheduled Meeting Times + Mode of Communication:

[when will your team meet each week — you MUST meet during the weekly tutorial timeslot and we strongly recommend scheduling one more regular meeting time]

Meeting time outside of lab: [indicate day and time here]

Thursday 8-10

Mode of Communication: [indicate mode of communication here]

discord 