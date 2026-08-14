**Builder Design Pattern**—one of the most popular Creational Design Patterns used extensively in production frameworks (like Java's **HttpRequest.newBuilder()**, Lombok's **@Builder**, and Android's **AlertDialog.Builder)**.

## Phase 1: High-Level Concept & Real-World Analogy
### What is it?
The **Builder Pattern** separates the **construction of a complex object** from its **representation**, allowing you to produce different types and representations of an object using the same construction process **step-by-step**.

**Real-World Analogy: Subway / Swiggy Custom Meal Order**
Think of ordering a custom meal on **Swiggy** or **Subway:**
- **Mandatory fields:** Bread type & Patty type.
- **Optional add-ons:** Extra Cheese? Jalapenos? Choice of Sauce? Extra Drink? Cutlery required?

Instead of the restaurant staff asking you all 15 questions in a single rigid line, you build your sandwich step-by-step. If you don't want jalapenos, you simply skip that step!
## Phase 2: Problems It Solves
**1. Solves the "Telescoping Constructor" Anti-Pattern**
Without Builder, if an object has 8 fields (some mandatory, some optional), you end up writing a nightmare of overloaded constructors:

```java
// BAD: Telescoping Constructors
public User(String name, String email) { ... }
public User(String name, String email, String phone) { ... }
public User(String name, String email, String phone, int age) { ... }
public User(String name, String email, String phone, int age, String address) { ... }

// What if 'phone' is null but 'age' is present? You end up passing nulls everywhere:
User user = new User("Rahul", "rahul@gmail.com", null, 25, null); // Dirty and prone to bugs!
```

**2. Avoids Inconsistent State via Setters & Preserves Immutability**
Using empty constructors + setters (**user.setPhone(...)**) makes the object **mutable** and leaves it in an **inconsistent state** halfway through initialization. The Builder Pattern produces **fully immutable objects**.

## Phase 3: Complete Java Implementation (MakeMyTrip Flight Booking Search - INR Context)
Let's build an immutable FlightSearchQuery object where passengers filter flights by mandatory source and destination, plus optional filters (e.g.,** maxPrice, isNonStop, airlinePreference, cabinClass**).
#### Visual ASCII UML Box Architecture

```text
  ┌─────────────────────────────────────────────────────────────┐
  │                      FlightSearchQuery                      │
  │                      (Immutable Product)                    │
  ├─────────────────────────────────────────────────────────────┤
  │ - source: String                                            │
  │ - destination: String                                       │
  │ - departureDate: String                                     │
  │ - maxPriceInRupees: int                                     │
  │ - isNonStopOnly: boolean                                    │
  │ - preferredAirline: String                                  │
  │ - cabinClass: String                                        │
  │ - passengerCount: int                                       │
  ├─────────────────────────────────────────────────────────────┤
  │ - FlightSearchQuery(builder: Builder)                       │
  │ + getSource(): String                                       │
  │ + getDestination(): String                                  │
  │ + ... [Getters Only - No Setters]                           │
  └──────────────────────────────▲──────────────────────────────┘
                                 │
                                 │ (creates)
                                 │
  ┌──────────────────────────────┴──────────────────────────────┐
  │                   Builder (Static Inner)                    │
  ├─────────────────────────────────────────────────────────────┤
  │ - source: String (mandatory)                                │
  │ - destination: String (mandatory)                           │
  │ - departureDate: String (mandatory)                         │
  │ - maxPriceInRupees: int                                     │
  │ - isNonStopOnly: boolean                                    │
  │ - preferredAirline: String                                  │
  │ - cabinClass: String                                        │
  │ - passengerCount: int                                       │
  ├─────────────────────────────────────────────────────────────┤
  │ + Builder(source, destination, date)                        │
  │ + withMaxPrice(price: int): Builder                         │
  │ + setNonStopOnly(isNonStop: boolean): Builder               │
  │ + withPreferredAirline(airline: String): Builder            │
  │ + withCabinClass(classType: String): Builder                │
  │ + withPassengerCount(count: int): Builder                   │
  │ + build(): FlightSearchQuery                                │
  └─────────────────────────────────────────────────────────────┘
```
## Phase 5: Common Interview Follow-Up Questions

#### Q1: "Builder Pattern vs. Factory Pattern—What's the core difference?"
*   **Factory Pattern:** Focuses on creating **various types of objects belonging to a family/hierarchy** in a single step (e.g., creating `SMSNotification` vs `EmailNotification`).
*   **Builder Pattern:** Focuses on constructing **a single, highly complex object step-by-step** with many optional configurations.

#### Q2: "Where should validation logic be placed inside the Builder?"
> **Answer:** Put mandatory parameter checks in the `Builder(...)` constructor, and complex cross-field validation checks (e.g., `source != destination` or `price > 0`) inside the `build()` method *right before* invoking `new Product(this)`.

#### Q3: "How does Lombok `@Builder` work under the hood in Java?"
> **Answer:** The Lombok annotation automatically generates a private constructor, a static `builder()` method, a static inner `Builder` class with fluent setter methods returning `this`, and a `build()` method at compile-time via annotation processing!