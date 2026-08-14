import java.util.*;

// ==========================================
// 1. OBSERVER INTERFACE & CONCRETE OBSERVERS
// ==========================================

interface Observer {
    void update(String productName, int newPrice, boolean inStock);
}

// Concrete Observer 1: Email Notification
class EmailObserver implements Observer {
    private final String emailId;

    public EmailObserver(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public void update(String productName, int newPrice, boolean inStock) {
        System.out.println("📧 EMAIL SENT to [" + emailId + "]: " +
                productName + " update! New Price: ₹" + newPrice +
                " | Stock Status: " + (inStock ? "IN STOCK" : "OUT OF STOCK"));
    }
}

// Concrete Observer 2: SMS Notification
class SMSObserver implements Observer {
    private final String mobileNumber;

    public SMSObserver(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public void update(String productName, int newPrice, boolean inStock) {
        System.out.println("📱 SMS SENT to [" + mobileNumber + "]: " +
                productName + " is now ₹" + newPrice + "! Hurry, buy now.");
    }
}

// Concrete Observer 3: Mobile App Push Notification
class AppPushObserver implements Observer {
    private final String userId;

    public AppPushObserver(String userId) {
        this.userId = userId;
    }

    @Override
    public void update(String productName, int newPrice, boolean inStock) {
        System.out.println("🔔 APP PUSH NOTIFICATION for User [" + userId + "]: " +
                productName + " price updated to ₹" + newPrice);
    }
}

// ==========================================
// 2. SUBJECT INTERFACE & CONCRETE SUBJECT
// ==========================================

interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

// Concrete Subject: Product Being Tracked
class Product implements Subject {
    private final String productName;
    private int price;
    private boolean inStock;
    
    // Thread-safe list of observers
    private final List<Observer> observers = new ArrayList<>();

    public Product(String productName, int initialPrice, boolean inStock) {
        this.productName = productName;
        this.price = initialPrice;
        this.inStock = inStock;
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(productName, price, inStock);
        }
    }

    // Business Logic Method: Triggers Notification on State Change
    public void updateProductDetails(int newPrice, boolean newStockStatus) {
        boolean hasChanged = (this.price != newPrice) || (this.inStock != newStockStatus);
        
        this.price = newPrice;
        this.inStock = newStockStatus;

        if (hasChanged) {
            System.out.println("\n📢 [SYSTEM EVENT] Product '" + productName + "' state changed! Triggering observers...");
            notifyObservers();
        }
    }

    public String getProductName() { return productName; }
    public int getPrice() { return price; }
    public boolean isInStock() { return inStock; }
}

// ==========================================
// 3. INTERVIEW TEST RUNNER (MAIN CLASS)
// ==========================================

public class Main {
    public static void main(String[] args) {
        // Create Product (Subject)
        Product iphone15 = new Product("iPhone 15 (128GB)", 79900, false);

        // Create Observers
        Observer user1Email = new EmailObserver("rahul@gmail.com");
        Observer user2SMS = new SMSObserver("+91-9876543210");
        Observer user3Push = new AppPushObserver("user_id_402");

        // Users subscribe ("Notify Me")
        iphone15.attach(user1Email);
        iphone15.attach(user2SMS);
        iphone15.attach(user3Push);

        // EVENT 1: Price drops & item comes back in stock -> All 3 observers get notified!
        iphone15.updateProductDetails(59999, true);

        // User 2 cancels subscription ("Unsubscribe")
        System.out.println("\n>>> User 2 unsubscribes from SMS notifications.");
        iphone15.detach(user2SMS);

        // EVENT 2: Price drops further -> Only User 1 and User 3 get notified
        iphone15.updateProductDetails(54999, true);
    }
}