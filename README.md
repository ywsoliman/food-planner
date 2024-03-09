# Food Planner App

This is a meal planner app designed to help users discover new meals, plan their weekly meals, and manage their favorites. The app allows users to search for meals based on various criteria such as country, ingredient, and category. Users can also add meals to their favorites and synchronize their data for backup using Firebase.

## Features

- **Meal of the Day**: Users can view an arbitrary meal for inspiration.
- **Search**: Users can search for meals based on country, ingredient, or category.
- **Categories**: Show a list of categories available for users to choose from.
- **Countries**: Show a list of countries so that users can view popular meals in each one.
- **Favorites**: Users can add a meal to favorites or remove one from it. (Local storage using Room)
- **Synchronization**: Users can synchronize their data to backup and access it again upon login. (Firebase)
- **Weekly Meal Planning**: Users can view and add meals for the current week.
- **Offline Support**: Users can view favorite meals and the plan for the current week even without network connectivity.
- **Authentication**: Simple login, sign up, and social networking authentication options (Facebook, Google, Twitter) are provided using Firebase authentication. Registered users can access their archived data from the server.
- **Guest Mode**: Users can choose to be a guest, allowing them to only view categories, use search, and view the meal of the day.
- **Meal Details**: Once a meal is chosen, users can view its name, image, origin country, ingredients, steps, and an embedded video. They can also add or remove the meal from their favorites.
- **Splash Screen**: The application displays a splash screen with animation using Lottie.
- **RxJava Integration**: The app utilizes RxJava for reactive programming.

## Technologies Used

- MVP Architecture Pattern
- Repository Pattern
- Singleton Pattern
- RxJava
- Retrofit
- Room
- LiveData
- Broadcast Receiver
- Navigation Component
- Firebase Authentication and Database

## Demo

Watch the demo video [here](https://www.youtube.com/watch?v=CgEVXlV_Xwc).

---

Feel free to contribute or suggest improvements! If you encounter any issues, please report them in the [Issues](https://github.com/ywsoliman/food-planner/issues) section.
