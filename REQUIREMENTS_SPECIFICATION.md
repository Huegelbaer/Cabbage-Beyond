# Goal
The goal of the "Cabbage Beyond" project is to develop an Android app that assists in the management and maintenance of game-related content for Pen and Paper games. Typically, only the game masters have access to the books containing information about the game world and adventures. The app aims to enable game masters to digitize and present the content from the handbooks, providing all necessary information to the players. The Minimal Viable Product (MVP) will support scenarios based on the "Savage Worlds" rule system.

# Target Audience
The target audience for the "Cabbage Beyond" app consists of two main groups: the game masters and the players. Game masters are responsible for planning and running the game sessions. They provide the players with the necessary information to shape their characters. During a session, the game master takes on various roles and guides the players through an adventurous world. The players, on the other hand, create their own characters and explore and save the world crafted by the game master. For more information about these two target audience groups, please refer to the glossary.

# Functional Requirements

## Provision of Game-Relevant Content
The game master can provide players with all the necessary information from the handbooks. As the game master has the admin role, they have the ability to edit all content, including that created by other users and admins.

## Simple Content Creation
Game masters should be able to transfer content from the handbook into the app by either typing the text manually or using the camera. By taking photos of the book pages, it should be possible to extract the text quickly and easily, allowing the text recognition feature to select the relevant passages for the game master to incorporate into the app.

## Content Search
The main functionality of the app is to browse through game-relevant content. Users should have the ability to access, search, and filter all available items and abilities in order to assign them to their own characters. This feature allows users to easily find and select the desired content for their characters within the app.

## Character Management
Users (both game masters and players) can create and manage their characters within the app. It is important to ensure that players can only edit their own characters. This feature allows users to create, modify, and track the progress of their characters, including attributes, skills, and equipment. Game masters can also access and manage all characters within the app.

# Non-Functional Requirements

## Account
To use the app, users are required to have a registered account. The account creation process is not available within the app itself but must be done through the project's backend. This approach ensures that unauthorized individuals cannot access the data and helps maintain the security and integrity of the app.

## Minimizing Access
The application's backend is provided through Firebase. As the application is intended for a small user base, the free version of Firebase will be utilized. Firebase offers a limited quota without incurring costs. To conserve the quota, the app should minimize the number of database accesses. This can be achieved by implementing efficient data retrieval and storage strategies, caching frequently accessed data, and optimizing database queries. These measures will help ensure that the app operates within the limits of the Firebase free tier and avoids unnecessary resource consumption.

## Usability
The app should provide a responsive user interface, ensuring that it quickly responds to user interactions and commands. Users should experience smooth navigation and minimal latency when accessing different features and functionalities. Additionally, the app should support offline functionality, allowing users to access and view content even without an internet connection. This ensures that users can continue to use the app and access important information, such as character sheets and game rules, regardless of their online status.

# User Interfaces
The app will feature lists for game worlds, equipment, characters, talents, skills, handicaps, and races. The structure of these lists will be consistent, primarily differing in the cards displayed within them. Users will have the ability to filter, sort, and search within the lists, enabling efficient navigation and exploration of the available content. These functionalities enhance the user experience by allowing users to quickly find and access specific items of interest within the app.

<img src="/docs/mockups/list_view.png" width=300/>

Upon clicking on an item in the list, the corresponding detail view will open, providing users with more comprehensive information about the selected item. The detail view will present additional details, such as descriptions, attributes, and any related information associated with the item. It allows users to access in-depth information and gain a better understanding of the selected item within the context of the game.
For regular users, the detail view serves as a read-only interface where they can view the detailed information of an item without the ability to make changes. Regular users can explore and gather information about game worlds, equipment, talents, skills, handicaps, races, and other relevant content.

<img src="/docs/mockups/details_view.png" width=300/>

On the other hand, an admin user has additional privileges. When an admin user accesses the detail view, they have the option to enter the editing mode. In the editing mode, the admin user can modify and update the content of the item. This allows admins to make changes to any entry, including those created by other users and admins.
However, it's important to note that regular users are limited to editing only their own characters. This ensures that each player has control over their own character's details, such as attributes, abilities, and inventory. 

<img src="/docs/mockups/details_view_with_edit_button.png" width=300/>
<img src="/docs/mockups/edit_view.png" width=300/>

# Assumptions and Constraints
In consideration of the limitations of Firebase's free tier, the following assumptions and constraints are made for the development of the app. By taking these assumptions and constraints into account during the development process, the app can efficiently utilize Firebase resources, provide offline capabilities, and ensure a smooth user experience for both game masters and players.

## Limited Firebase Quota 
The app will operate within the limitations of Firebase's free tier, which provides a restricted quota for read and write operations. This constraint requires careful management of data access and updates to ensure efficient utilization of Firebase resources.

## Offline Functionality
To conserve resources and enhance user experience, the app will prioritize offline functionality. Users will be able to access and view previously loaded content even without an internet connection. Updates to the data stored in Firebase may occur when the app is launched or manually triggered by the users.

## Minimal Firebase Access
To optimize resource usage and prevent excessive Firebase access, the app will minimize unnecessary read and write operations to the database. This can be achieved through effective data caching, intelligent synchronization strategies, and implementing efficient algorithms for data retrieval and storage.

# Deliverables

## Android App
The first version of the app will be developed for Android 13 (SDK 33). The minimum supported Android version will be 5.0 (SDK 21). The app will require the "Internet" permission to fetch data from Firebase. The "Camera" permission is not mandatory but can facilitate content input.

## Development Environment
The app will be developed using Android Studio, a popular integrated development environment (IDE) for Android app development.

## Version Control and Hosting
Git will be used as the version control system, and the source code will be hosted on GitHub. This enables efficient collaboration, version management, and code review.

## Firebase Local Emulator Suite
 The test environment will utilize the [Firebase Local Emulator Suite], which allows for the creation of a local Firebase project. The suite includes emulators for "auth," "firestore," and "ui." Detailed instructions for setting up the Firebase Local Emulator Suite can be found in the Firebase-setup.md file.

## Mock Data
Mock data will be provided alongside the app. This data can be used to populate the Firebase test environment for testing purposes. Instructions for importing the mock data can also be found in the Firebase-setup.md file.

# Glossary
This glossary provides a quick reference for understanding the key terms and concepts related to the "Cabbage Worlds" project and the pen and paper gaming context.

## Pen and Paper Games
Pen and Paper Games (RPGs) are a type of role-playing games typically played with paper, pencil, and dice. In Pen and Paper games, players assume the roles of fictional characters and embark on adventures in a game world created by the Game Master (GM). The gameplay involves a combination of storytelling, improvisation, and rule mechanics. The specific rules may vary from game to game but generally determine how character actions are determined through dice rolls and define the abilities and attributes of the characters.
Pen and Paper games can encompass a wide range of genres and settings, from fantasy and science fiction to horror and crime. They provide a unique opportunity for players to immerse themselves in a fictional world, utilize their imagination, and engage in collaborative storytelling with friends. The games foster creativity, problem-solving, and social interaction among the participants.
One of the most well-known Pen and Paper games is Dungeons and Dragons, which has gained widespread popularity since its introduction in the 1970s. However, there are numerous other Pen and Paper games available, each offering its own unique setting, rules, and gameplay experience.
Pen and Paper games continue to captivate players worldwide, offering a dynamic and immersive form of entertainment that combines imagination, camaraderie, and the thrill of cooperative storytelling.

## Game Master (GM)
The Game Master, also known as the Dungeon Master (DM), Storyteller, or Game Moderator, plays a central role in Pen and Paper games. They are responsible for creating and shaping the game world and guiding the narrative of the game. The Game Master designs the adventures and establishes the rules that the players must adhere to. They ensure that all players have equal opportunities and that the game runs smoothly and fairly.
The responsibilities of the Game Master may vary depending on the specific game being played. Generally, they include creating a believable world, crafting non-player characters (NPCs), developing story arcs, and facilitating game sessions. A skilled Game Master is able to immerse the players in the world and engage them in the storyline.
Being a Game Master requires significant preparation and improvisational skills. They must be able to react to unexpected events and adapt the game's narrative accordingly. Additionally, they should have a strong understanding of the game's rules and be able to explain them clearly to the players. A skilled Game Master is a crucial factor in the success of a Pen and Paper game, as they guide the players through the adventure and create memorable experiences.

## Players
A player is an individual who participates in a Pen and Paper game. Unlike the Game Master, the player is not part of the game's mechanics but instead embodies a character within the game. The player immerses themselves in the game's narrative and world, making decisions for their character based on the game rules and storyline. A player may assume various roles in the game, such as a warrior, a wizard, or a healer. They must familiarize themselves with their character's abilities and attributes and learn how to apply them effectively in the game.
As a player, one's enjoyment and success in the game depend on their creativity, problem-solving skills, and ability to collaborate with other players. They contribute to the collective storytelling experience by interacting with the game world, engaging with NPCs, and participating in quests or challenges. Players often face obstacles and make choices that impact the progression of the story, working together with the Game Master and other players to create an immersive and exciting gameplay experience.

## Savage Worlds
[Savage Worlds] is a fast and flexible role-playing system developed by [Pinnacle Entertainment Group]. The system is designed to support various genres and game worlds and is characterized by simple rules and fast-paced, action-packed combat. The focus of Savage Worlds is on storytelling and interaction between players and the Game Master, with the use of dice and character attributes determining the outcome of actions.
The system employs a unified mechanic for all actions, based on the combination of a four, six, eight, ten, or twelve-sided die (referred to as a "d4," "d6," "d8," "d10," and "d12," respectively) and an attribute or skill of the character. This allows for quick and streamlined resolution of tasks and encounters.
One of the key features of Savage Worlds is its versatility in accommodating different genres. The system provides a wide range of templates and rules tailored to various settings, including fantasy, horror, science fiction, and superhero adventures. This flexibility allows players and Game Masters to explore and immerse themselves in diverse and immersive worlds, adapting the mechanics to suit the specific themes and tones of their campaigns.
Savage Worlds has gained popularity for its accessible ruleset, emphasis on storytelling, and ability to support dynamic and action-oriented gameplay. It offers players a framework to unleash their imagination, engage in thrilling adventures, and create memorable moments within the collaborative narrative.

## Admin Role
Refers to the administrative role within the app. Admins have the ability to edit all content, including that created by other users and admins. They have additional privileges and responsibilities compared to regular users.

## MVP (Minimum Viable Product)
The initial version of the app that includes essential features and functionality to meet the basic requirements and demonstrate its core value proposition.

## Backend
The server-side component of the application that handles data storage, retrieval, and processing. In this project, Firebase is used as the backend infrastructure.

## Firebase
A cloud-based platform provided by Google that offers various services, including real-time databases, authentication, hosting, and more. It serves as the backend infrastructure for the app, handling data storage and user authentication.
