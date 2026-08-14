# Phase 1: High-Level Concept & Real-World Analogy
### What is it?
The **Factory Pattern** decouples the **creation logic** of an object from its **usage.** Instead of calling **new ConcreteClass()** directly in your business logic, you delegate object creation to a central **Factory.**

**Real-World Analogy: Vehicle Booking (Uber/Ola) or Payment Provider Engine**
- **Uber/Ola App:** When you select ride type (e.g., **Auto, Bike, Cab, SUV**), the app doesn't write **new Cab()** or **new Auto()** inside the UI layer. It calls **VehicleFactory.createVehicle("AUTO")**.
- **Payment Provider Gateway (Razorpay/Paytm/Stripe):** Based on user region or merchant settings, the system dynamically instantiates the correct gateway provider (**PaytmGateway, RazorpayGateway, or StripeGateway**).
# Phase 2: Factory Method vs. Abstract Factory (Crucial Interview Distinction!)
Interviewer: "*What is the difference between Factory Method and Abstract Factory?*"
| Aspect | Factory Method Pattern | Abstract Factory Pattern |
| --- | --- | --- |
| Focus | Creates a single product (e.g., Vehicle). | Creates a family of related products (e.g., AndroidButton + AndroidCheckbox vs iOSButton + iOSCheckbox). |
| Mechanism | Uses Inheritance / Method Overriding in subclasses. | Uses Composition (A factory object containing multiple factory methods). |
| Complexity | Simple (Single method createProduct()).	 | Higher (Factory of Factories). |		
	
# Phase 3: Problem It Solves
### Without Factory Pattern (The Bad Way):
```java
public void processNotification(String channel, String message) {
    Notification notification;
    if (channel.equalsIgnoreCase("SMS")) {
        notification = new SMSNotification();
    } else if (channel.equalsIgnoreCase("EMAIL")) {
        notification = new EmailNotification();
    } else if (channel.equalsIgnoreCase("PUSH")) {
        notification = new PushNotification();
    }
    notification.send(message); // Tight coupling to 'new' key words everywhere!
}
```
### With Factory Pattern (The Clean Way):
```java
public void processNotification(String channel, String message) {
    Notification notification = NotificationFactory.createNotification(channel);
    notification.send(message);
}
```
# Phase 4: Complete Java Implementation (Notification Provider Engine - INR Context)

### Visual ASCII UML Box Architecture

```text
  ┌─────────────────────────────────────────────────────────────┐
  │                    NotificationFactory                      │
  │                          (Factory)                          │
  ├─────────────────────────────────────────────────────────────┤
  │ + createNotification(channel: ChannelType): Notification    │
  └──────────────────────────────┬──────────────────────────────┘
                                 │
                                 │ (creates)
                                 ▼
  ┌─────────────────────────────────────────────────────────────┐
  │                        <<interface>>                        │
  │                        Notification                         │
  ├─────────────────────────────────────────────────────────────┤
  │ + sendNotification(recipient: String, msg: String): void    │
  └──────────────────────────────▲──────────────────────────────┘
                                 ┆
                 ┌───────────────┼───────────────┐
                 ┆ (implements)  ┆               ┆
  ┌──────────────┴──────────┐   ┌┴───────────────┴────────┐   ┌─┴───────────────────────┐
  │     SMSNotification     │   │    EmailNotification    │   │  WhatsAppNotification   │
  ├─────────────────────────┤   ├─────────────────────────┤   ├─────────────────────────┤
  │ + sendNotification()    │   │ + sendNotification()    │   │ + sendNotification()    │
  └─────────────────────────┘   └─────────────────────────┘   └─────────────────────────┘
```

# Phase 5: Common Interview Follow-Up Questions

#### Q1: "How do you avoid modifying the Factory `switch-case` when adding a new Product (violating Open/Closed Principle)?"
> **Answer:** Use a **Factory Registry Map**! Register concrete types in a `Map<ChannelType, Supplier<Notification>>` at startup. To add a new notification type, register it with the map without modifying the factory creation method.

```java
public class DynamicNotificationFactory {
    private static final Map<String, Supplier<Notification>> registry = new HashMap<>();

    public static void registerNotification(String type, Supplier<Notification> supplier) {
        registry.put(type, supplier);
    }

    public static Notification create(String type) {
        Supplier<Notification> supplier = registry.get(type);
        if (supplier == null) throw new IllegalArgumentException("Unknown type");
        return supplier.get();
    }
}
```
Q2: "When should you NOT use Factory Pattern?"
**Answer:** If object creation is simple (e.g., **new Point(x, y)**), using a Factory adds unnecessary abstraction and boilerplate. Only use a Factory when creation logic is complex, requires configuration, or varies dynamically at runtime.