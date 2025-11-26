# 🐠 Aquarium Simulator  
### Java • Swing • Multithreading • MVC Architecture

This project is a multithreaded aquarium simulator where each fish and jellyfish runs as an independent Thread.  
The application uses Java Swing for the GUI and follows the MVC design pattern.

---

## 🎯 Project Goals

- Create and manage multiple threads in Java  
- Implement synchronization between threads  
- Use MVC to structure the GUI  
- Draw and animate objects in a Swing panel  
- Allow user interactions such as adding animals, feeding them, pausing, and showing statistics

---

## 🛠 Features

### 🐟 Add Animals  
Users can add up to **5 animals** to the aquarium:

- Fish or Jellyfish  
- Size: 20–320 pixels  
- Horizontal speed: 1–10  
- Vertical speed: 1–10  
- Selectable colors  

Each animal starts moving immediately in its own thread.

---

### 💤 Thread Control

- **Sleep** – pause all animals (`wait()`)  
- **Wake up** – resume all animals (`notify()`)  
- **Reset** – remove all animals and clear counters  
- **Exit** – close the application  

---

### 🪱 Food Mechanics (Synchronization)

When the user presses **Food**:

1. A worm appears at the center of the aquarium  
2. A `CyclicBarrier` is created with the number of animals  
3. All animals synchronize and start racing toward the worm  
4. The first animal within 5 pixels “eats” the worm  
5. The animal’s **Eat counter** and the global **Total Eat counter** are updated  

---

### 📊 Statistics (Info Button)

Pressing **Info** toggles a JTable:

- Odd press → show table  
- Even press → hide table  

Columns include:  
Animal | Color | Size | Horizontal speed | Vertical speed | Eat counter | Total

---

### 🎨 Background Settings

Menu options:

- **Image** – set a background image  
- **Blue** – solid blue background  
- **None** – no background  

---

## 🧩 Architecture (MVC)

### Model
- `Swimmable` (abstract Thread class)  
- `Fish`  
- `Jellyfish`  

### View
- `AquaPanel` – renders animals, background, food, and statistics  
- `AddAnimalDialog` – UI for creating new animals  

### Controller
- `AquaFrame` – main application window, menus, and buttons  

---

## 📁 Project Structure

## Project Structure

The project follows a basic MVC separation:

```text
Aquarium/
  src/
    Controller/
      AquaFrame.java
    Model/
      Swimmable.java
      Fish.java
      Jellyfish.java
    View/
      AquaPanel.java
      AddAnimalDialog.java
