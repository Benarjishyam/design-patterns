The Observer Design Pattern is another fundamental Behavioral pattern frequently asked in system design and object-oriented design (OOD) interviews (e.g., Design a Stock Price Monitor, Notification System, Weather Station, or Pub-Sub System).
# Phase 1: High-Level Concept & Real-World Analogy
### What is it?
The Observer Pattern defines a **one-to-many dependency between objects** . When the core object (called the **Subject** or **Observable**) changes its state, all its registered dependents (called **Observers**) are automatically notified and updated.
### Real-World Analogy: YouTube Channel / Flipkart "Notify Me"
**Without Observer (Polling):** You want to know when a channel uploads a new video. You manually open the channel page every 10 minutes to check. (In efficient, wastes CPU cycles).

**With Observer:** You click "Subscribe" (Bell Icon). Now, whenever the channel uploads a new video, YouTube automatically pushes a notification to your phone.
## Phase 2: Problem It Solves & Core Components
Problems It Solves
Eliminates Polling: Removes continuous checking loops like while(true) { checkStatus(); }.
Promotes Loose Coupling: The Subject doesn't need to know the concrete class of its subscribers—it only knows that they implement an Observer interface.
The 3 Core Components
```text
 ┌──────────────────────────────────────────────┐
 │             Subject / Observable             │
 │  - observers: List<Observer>                 │
 │  + attach(observer: Observer)                │
 │  + detach(observer: Observer)                │
 │  + notifyObservers()                         │
 └──────────────────────┬───────────────────────┘
                        │ 1
                        │
                        │ *
 ┌──────────────────────▼───────────────────────┐
 │               <<interface>>                  │
 │                 Observer                     │
 ├──────────────────────────────────────────────┤
 │ + update(price: int, inStock: boolean): void │
 └──────────────────────▲───────────────────────┘
                        │ (implements)
      ┌─────────────────┼─────────────────┐
      │                 │                 │
┌─────┴──────────┐┌─────┴──────────┐┌─────┴──────────┐
│ EmailObserver  ││ SMSObserver    ││ AppPushObserver│
└────────────────┘└────────────────┘└────────────────┘
```
**Subject (Interface/Class):** Maintains a collection of observers and provides methods to register (attach), unregister (detach), and notify observers.

**Observer (Interface):** Declares the update() method that the Subject calls when its state changes.

**Concrete Observers:** Implement update() to perform specific tasks (e.g., send Email, send SMS, update UI display).
# Phase 3: Real-World Interview Use Case: Flipkart Price & Stock Notification System (INR Context)
Let's design a real-time system where users subscribe to an iPhone 15 on Flipkart. When the price drops below ₹60,000 or comes back in stock, all subscribed channels (Email, SMS, App Push) get notified instantly.
### Visual ASCII UML Box Architecture
```text
  ┌─────────────────────────────────────────────────────────────┐
  │                        <<interface>>                        │
  │                           Subject                           │
  ├─────────────────────────────────────────────────────────────┤
  │ + attach(observer: Observer): void                          │
  │ + detach(observer: Observer): void                          │
  │ + notifyObservers(): void                                   │
  └──────────────────────────────▲──────────────────────────────┘
                                 ┆
                                 ┆ (implements)
  ┌──────────────────────────────┴──────────────────────────────┐
  │                           Product                           │
  ├─────────────────────────────────────────────────────────────┤
  │ - productName: String                                       │
  │ - price: int                                                │
  │ - inStock: boolean                                          │
  │ - observers: List<Observer>                                 │
  ├─────────────────────────────────────────────────────────────┤
  │ + attach(observer: Observer): void                          │
  │ + detach(observer: Observer): void                          │
  │ + notifyObservers(): void                                   │
  │ + updateProductDetails(newPrice: int, newStock: boolean):void│
  └──────────────────────────────┬──────────────────────────────┘
                                 │
                                 │ 1..* (notifies)
                                 ▼
  ┌─────────────────────────────────────────────────────────────┐
  │                        <<interface>>                        │
  │                          Observer                           │
  ├─────────────────────────────────────────────────────────────┤
  │ + update(productName: String, newPrice: int, inStock: bool) │
  └──────────────────────────────▲──────────────────────────────┘
                                 ┆
                 ┌───────────────┼───────────────┐
                 ┆ (implements)  ┆               ┆
  ┌──────────────┴──────────┐   ┌┴───────────────┴────────┐   ┌─┴───────────────────────┐
  │      EmailObserver      │   │       SMSObserver       │   │     AppPushObserver     │
  ├─────────────────────────┤   ├─────────────────────────┤   ├─────────────────────────┤
  │ - emailId: String       │   │ - mobileNumber: String  │   │ - userId: String        │
  ├─────────────────────────┤   ├─────────────────────────┤   ├─────────────────────────┤
  │ + update(): void        │   │ + update(): void        │   │ + update(): void        │
  └─────────────────────────┘   └─────────────────────────┘   └─────────────────────────┘

```


# Phase 4: Common Interview Follow-Up Questions
***Q1: "Push Model vs. Pull Model in Observer Pattern—What's the difference?"***
Push Model (used in code above): The Subject sends the updated data directly inside **update(data)** as parameters.
Pro: Simple.
Con: Observers might receive data they don't need.

Pull Model: The Subject passes a reference to itself inside **update(Subject subject)**. Observers then call getter methods on subject to fetch only the data they require.
Pro: Highly flexible.

***Q2: "How do you handle memory leaks in the Observer Pattern?"***
Answer: This is known as the **Lapsed Listener Problem.** If a client attaches an observer but forgets to detach it, the Subject retains a strong reference to the observer, preventing the Garbage Collector (GC) from reclaiming its memory.
Solution: Use WeakReference<Observer> or Java’s WeakHashMap so observers can be garbage collected when no longer used elsewhere.

***Q3: "How do you make the Observer Pattern thread-safe under high concurrency?"***
Answer: Use **CopyOnWriteArrayList<Observer>** for storing observers inside the Subject. This ensures safe iteration during notifyObservers() even if other threads call attach() or detach() simultaneously without throwing ConcurrentModificationException.