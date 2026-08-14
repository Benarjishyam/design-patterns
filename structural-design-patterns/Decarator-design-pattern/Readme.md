# Phase 1: High-Level Concept & Real-World Analogy
### What is it?
The **Decorator Pattern** attaches **additional responsibilities/features to an object dynamically** at runtime without altering its original structure or creating a subclass for every possible combination.
### The "Class Explosion" Problem It Solves
Imagine you are building a Pizza Shop System (or Starbucks Coffee System).
You start with base pizzas:
- MargheritaPizza (₹200)
- FarmhousePizza (₹300)

Now, customers want to add toppings dynamically: **Extra Cheese (₹50), Mushroom (₹40), Jalapeno (₹30), Paneer (₹60)**.
### Without the Decorator Pattern (The Bad Way):
If you try to use standard class inheritance, you will fall into **Class Explosion:**
- MargheritaWithCheese
- MargheritaWithCheeseAndMushroom
- MargheritaWithCheeseAndMushroomAndJalapeno
- FarmhouseWithCheese
- ...You would end up writing **hundreds of classes** for every topping combination!

# Phase 2: The Decorator Pattern Solution
Instead of creating hundreds of subclasses, you create **Wrappers (Decorators)**. A Decorator **"has-a"** component AND **"is-a"** component at the same time.
```text
               ┌──────────────────────────────────────────────┐
               │              <<abstract class>>              │
               │                   BasePizza                  │
               ├──────────────────────────────────────────────┤
               │ + getDescription(): String                   │
               │ + getCost(): int                             │
               └──────────────────────▲───────────────────────┘
                                      │
            ┌─────────────────────────┴─────────────────────────┐
            │                                                   │
 ┌──────────┴──────────┐                             ┌──────────┴──────────┐
 │   MargheritaPizza   │                             │   ToppingDecorator  │
 ├─────────────────────┤                             │  (Abstract Decorator│
 │ + getDescription()  │                             ├─────────────────────┤
 │ + getCost(): 200    │                             │ - pizza: BasePizza  │
 └─────────────────────┘                             └──────────▲──────────┘
                                                                │
                                              ┌─────────────────┴─────────────────┐
                                   ┌──────────┴──────────┐             ┌──────────┴──────────┐
                                   │  ExtraCheeseTopping │             │   MushroomTopping   │
                                   ├─────────────────────┤             ├─────────────────────┤
                                   │ + getCost(): +₹50   │             │ + getCost(): +₹40   │
                                   └─────────────────────┘             └─────────────────────┘
```
**When calculating cost:**
👉 CheeseDecorator calls **wrappedPizza.getCost() + 50**
👉 MushroomDecorator calls **wrappedPizza.getCost() + 40**

# Phase 3: Complete Java Implementation (Indian Rupee / INR Context)
Let's build a fully working Pizza Customizer where toppings wrap around each other dynamically at runtime.

### Visual ASCII UML Box Architecture

```text
  ┌─────────────────────────────────────────────────────────────┐
  │                    <<abstract class>>                       │
  │                        BasePizza                            │
  ├─────────────────────────────────────────────────────────────┤
  │ # description: String                                       │
  ├─────────────────────────────────────────────────────────────┤
  │ + getDescription(): String                                  │
  │ + getCost(): int                                            │
  └──────────────────────────────▲──────────────────────────────┘
                                 │
          ┌──────────────────────┴──────────────────────┐
          │ (extends)                                   │ (extends)
  ┌───────┴─────────────────┐               ┌───────────┴─────────────┐
  │     MargheritaPizza     │               │    ToppingDecorator     │
  │  (Concrete Component)   │               │   (Abstract Decorator)  │
  ├─────────────────────────┤               ├─────────────────────────┤
  │ + getCost(): 200        │               │ # pizza: BasePizza      │
  └─────────────────────────┘               └───────────▲─────────────┘
                                                        │
                                    ┌───────────────────┴───────────────────┐
                                    │ (extends)                             │ (extends)
                        ┌───────────┴─────────────┐             ┌───────────┴─────────────┐
                        │   ExtraCheeseTopping    │             │     MushroomTopping     │
                        ├─────────────────────────┤             ├─────────────────────────┤
                        │ + getDescription()      │             │ + getDescription()      │
                        │ + getCost(): cost + 50  │             │ + getCost(): cost + 40  │
                        └─────────────────────────┘             └─────────────────────────┘
```

### Phase 5: Common Interview Follow-Up Questions

#### Q1: "Real-world Standard Library Example of Decorator Pattern?"
> **Answer:** Java's **I/O Streams (`java.io`)** is the textbook example!
> ```java
> BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")));
> ```
> Here, `FileInputStream` is wrapped by `InputStreamReader`, which is wrapped by `BufferedReader`. Each decorator adds functionality (byte stream $\rightarrow$ character stream $\rightarrow$ buffered reading).

#### Q2: "How is Decorator different from Adapter and Proxy patterns?"
*   **Decorator:** Enhances/adds behavior to an object *without changing its interface*.
*   **Adapter:** Changes an incompatible interface into a target interface so two different systems can talk.
*   **Proxy:** Controls access to an object (e.g., lazy loading, authentication, caching) *without changing the behavior*.