import java.util.*;

// ==========================================
// 1. PRODUCT INTERFACE
// ==========================================

interface Notification {
    void sendNotification(String recipient, String message);
}

// ==========================================
// 2. CONCRETE PRODUCTS
// ==========================================

// Product 1: SMS Notification
class SMSNotification implements Notification {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("📱 [SMS Gateway] Sent to (" + recipient + "): " + message);
    }
}

// Product 2: Email Notification
class EmailNotification implements Notification {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("📧 [Email Server] Sent to (" + recipient + "): " + message);
    }
}

// Product 3: WhatsApp Notification
class WhatsAppNotification implements Notification {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("💬 [WhatsApp Business API] Sent to (" + recipient + "): " + message);
    }
}

// ==========================================
// 3. FACTORY CLASS (Decoupled Object Creation)
// ==========================================

class NotificationFactory {
    // Enum for type safety
    public enum ChannelType {
        SMS, EMAIL, WHATSAPP
    }

    // Static Factory Method
    public static Notification createNotification(ChannelType channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel type cannot be null!");
        }

        switch (channel) {
            case SMS:
                return new SMSNotification();
            case EMAIL:
                return new EmailNotification();
            case WHATSAPP:
                return new WhatsAppNotification();
            default:
                throw new IllegalArgumentException("Unknown channel type: " + channel);
        }
    }
}

// ==========================================
// 4. INTERVIEW TEST RUNNER (MAIN CLASS)
// ==========================================

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> FLIPKART ORDER NOTIFICATION ENGINE <<<\n");

        // Order Confirmation via SMS
        Notification sms = NotificationFactory.createNotification(NotificationFactory.ChannelType.SMS);
        sms.sendNotification("+91-9876543210", "Your order #FLIP-10294 has been placed for ₹1,499.");

        // Invoice via Email
        Notification email = NotificationFactory.createNotification(NotificationFactory.ChannelType.EMAIL);
        email.sendNotification("rahul@gmail.com", "Your tax invoice for ₹1,499 is attached.");

        // Delivery Updates via WhatsApp
        Notification whatsapp = NotificationFactory.createNotification(NotificationFactory.ChannelType.WHATSAPP);
        whatsapp.sendNotification("+91-9876543210", "Out for delivery! Delivery Executive: Suresh (+91-9988776655).");
    }
}
/*
Console Output:

>>> FLIPKART ORDER NOTIFICATION ENGINE <<<

📱 [SMS Gateway] Sent to (+91-9876543210): Your order #FLIP-10294 has been placed for ₹1,499.
📧 [Email Server] Sent to (rahul@gmail.com): Your tax invoice for ₹1,499 is attached.
💬 [WhatsApp Business API] Sent to (+91-9876543210): Out for delivery! Delivery Executive: Suresh (+91-9988776655).
*/