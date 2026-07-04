# Task Manager - Android Application

A simple, clean, and fully functional Android application built with Kotlin that demonstrates modern Android development practices using MVVM architecture, Room Database, and View Binding.

## 📱 About The Project

Task Manager is a lightweight Android application designed for managing daily tasks. The app allows users to create, view, and delete tasks with an intuitive interface. It features a unique swipe-to-delete functionality with an undo option, making task management efficient and user-friendly.

This project was developed following clean code principles and modern Android architecture guidelines, making it easy to understand, maintain, and extend.


https://github.com/user-attachments/assets/e7c7af68-6481-4547-8056-15544fbea9b5

## ✨ Key Features

- **Add Tasks**: Create new tasks with a title and description through a Floating Action Button (FAB)
- **Edit Tasks**: Tap any task card to edit its title and description in a dialog
- **View Tasks**: Display all tasks in a scrollable list with card-based UI
- **Timestamp Display**: Each task automatically shows its creation time
- **Swipe-to-Delete**: Delete tasks by swiping left or right on any item
- **Undo Action**: Restore accidentally deleted tasks using the Snackbar undo button
- **Delete All**: Remove all tasks at once through the options menu
- **Empty State**: Shows a helpful message when no tasks exist
- **Persistent Storage**: All tasks are saved locally using Room Database

## 🏗️ Technical Architecture

### MVVM (Model-View-ViewModel) Pattern

The application follows the MVVM architecture pattern, which provides:

- **Model**: Data layer with Room Database entities and DAOs
- **View**: UI layer with Activities and XML layouts using View Binding
- **ViewModel**: Business logic layer that connects the Model and View

#### Architecture Components:

```
┌─────────────────────────────────────────────┐
│              MainActivity                    │
│         (View - UI Layer)                   │
│    Uses ViewBinding (NO findViewById)       │
└──────────────┬──────────────────────────────┘
               │ Observes LiveData
               ▼
┌─────────────────────────────────────────────┐
│           TaskViewModel                      │
│     (ViewModel - Logic Layer)               │
│   Manages UI-related data lifecycle         │
└──────────────┬──────────────────────────────┘
               │ Calls Repository
               ▼
┌─────────────────────────────────────────────┐
│          TaskRepository                      │
│      (Repository - Data Layer)              │
│   Abstraction layer for data sources        │
└──────────────┬──────────────────────────────┘
               │ Uses DAO
               ▼
┌─────────────────────────────────────────────┐
│     Room Database (TaskDatabase)            │
│   TaskDao + Task Entity                     │
│   Local persistent storage                  │
└─────────────────────────────────────────────┘
```

### Technology Stack

#### Core Technologies:
- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 36

#### Architecture & Components:
- **MVVM Architecture**: Clean separation of concerns
- **View Binding**: Type-safe view access (NO findViewById used)
- **LiveData**: Observable data holder for lifecycle-aware data observation
- **ViewModel**: Lifecycle-aware UI data management
- **Room Database**: Local data persistence
- **Coroutines**: Asynchronous programming for database operations

#### UI Components:
- **RecyclerView**: Efficient list display
- **ListAdapter**: Optimized adapter with DiffUtil
- **CardView**: Material Design card-based UI
- **FloatingActionButton (FAB)**: Quick task creation button
- **MaterialToolbar**: Custom toolbar with options menu
- **ItemTouchHelper**: Swipe gesture handling
- **Snackbar**: Undo action notification
- **AlertDialog**: User dialogs for adding/editing tasks and confirmations
- **Options Menu**: Main navigation menu

## 📂 Project Structure

```
app/src/main/java/com/example/myapplicationfinalexam/
│
├── data/
│   ├── Task.kt                    # Room Entity (data model)
│   ├── TaskDao.kt                 # Data Access Object (database operations)
│   ├── TaskDatabase.kt            # Room Database instance
│   └── TaskRepository.kt          # Repository (data abstraction layer)
│
├── viewmodel/
│   └── TaskViewModel.kt           # ViewModel (business logic)
│
├── adapter/
│   └── TaskAdapter.kt             # RecyclerView Adapter with ViewHolder
│
└── MainActivity.kt                # Main Activity (UI Controller)

app/src/main/res/
├── layout/
│   ├── activity_main.xml          # Main screen layout
│   ├── item_task.xml              # RecyclerView item layout (CardView)
│   └── dialog_add_task.xml        # Add task dialog layout
│
├── menu/
│   └── main_menu.xml              # Options menu
│
└── values/
    └── strings.xml                # String resources
```

## 🔑 Key Implementation Details

### 1. View Binding
View Binding is enabled in all activities and adapters, providing type-safe access to views:

```kotlin
// In build.gradle.kts
buildFeatures {
    viewBinding = true
}

// In MainActivity
private lateinit var binding: ActivityMainBinding
binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
```

**NO findViewById() is used anywhere in the project.**

### 2. Room Database

#### Entity Definition:
```kotlin
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long
)
```

#### DAO Operations:
- `insert()`: Add new task with automatic timestamp
- `update()`: Modify existing task and update timestamp
- `delete()`: Remove specific task
- `deleteAll()`: Clear all tasks
- `getAllTasks()`: Retrieve all tasks as LiveData

### 3. Swipe-to-Delete Feature (Exclusive New Feature)

The app implements swipe-to-delete using `ItemTouchHelper`:

```kotlin
ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
)
```

When a task is swiped:
1. The task is deleted from the database
2. A Snackbar appears with "Task deleted" message
3. An "Undo" button allows restoring the task
4. If undo is clicked, the task is re-inserted into the database

This provides a smooth, user-friendly deletion experience with a safety net.

### 4. LiveData Observation

The ViewModel exposes LiveData that the Activity observes:

```kotlin
taskViewModel.allTasks.observe(this) { tasks ->
    adapter.submitList(tasks)
    // Update UI based on task list
}
```

This ensures the UI automatically updates when data changes.

## 🚀 How to Run

1. **Clone or download** this project
2. **Open** the project in Android Studio
3. **Sync** Gradle files (should happen automatically)
4. **Run** the app on an emulator or physical device (API 24+)

## 📖 How to Use

1. **Add a Task**: Tap the round Floating Action Button (FAB) in the bottom-right corner, enter title and description, then tap "Save"
2. **Edit a Task**: Tap on any task card to open the edit dialog with pre-filled data, modify it, and save
3. **View Tasks**: All tasks appear in a scrollable list with their creation timestamp
4. **Delete a Task**: Swipe any task left or right to delete it
5. **Undo Delete**: Immediately tap "Undo" in the Snackbar to restore the deleted task
6. **Delete All Tasks**: Tap the three-dot menu in the top-right corner → "Delete All Tasks" → Confirm

## 🎓 University Defense Notes

### Why This Architecture?

- **MVVM**: Separates UI logic from business logic, making code testable and maintainable
- **Repository Pattern**: Provides a clean API for data access, abstracting the data source
- **LiveData**: Ensures UI updates automatically and respects lifecycle, preventing memory leaks
- **View Binding**: Type-safe, null-safe, and faster than findViewById()
- **Room**: Provides compile-time SQL verification and seamless Kotlin integration

### Design Decisions:

1. **Single Activity**: Keeps the app simple and focused
2. **FAB for Adding**: Provides quick, intuitive access to task creation
3. **Tap-to-Edit**: Natural interaction pattern for modifying existing tasks
4. **Timestamp Storage**: Tracks task creation time using `System.currentTimeMillis()`
5. **Options Menu**: Simpler than Bottom Navigation for this use case
6. **CardView**: Provides visual hierarchy and modern Material Design
7. **ListAdapter with DiffUtil**: Efficient RecyclerView updates
8. **Coroutines**: Clean asynchronous code for database operations

### Unique Feature Justification:

**Swipe-to-Delete with Undo** was chosen because:
- It's intuitive and widely recognized in mobile apps
- Provides quick deletion without confirmation dialogs
- The undo action prevents accidental data loss
- Demonstrates advanced RecyclerView interaction (ItemTouchHelper)
- Shows proper Snackbar implementation

**Tap-to-Edit Functionality** enhances user experience by:
- Allowing quick modifications without complex navigation
- Reusing the same dialog interface for consistency
- Pre-filling existing data for convenient editing
- Updating timestamps to track last modification time

## 📦 Dependencies

```kotlin
// Core
implementation("androidx.core:core-ktx:1.18.0")
implementation("androidx.appcompat:appcompat:1.7.1")
implementation("com.google.android.material:material:1.13.0")

// Lifecycle (ViewModel, LiveData)
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
```

## 🎯 Learning Outcomes

This project demonstrates proficiency in:

- Modern Android development with Kotlin
- MVVM architecture implementation
- Room Database integration
- View Binding usage
- RecyclerView with custom adapters
- LiveData and lifecycle-aware components
- Coroutines for asynchronous operations
- Material Design principles
- User experience considerations (undo functionality)

## 📝 License

This project is created for educational purposes as part of a university final exam.

## 👨‍💻 Development

- **IDE**: Android Studio
- **Build System**: Gradle with Kotlin DSL
- **Language**: Kotlin 2.1.0
- **Gradle Plugin**: 9.1.1

---

