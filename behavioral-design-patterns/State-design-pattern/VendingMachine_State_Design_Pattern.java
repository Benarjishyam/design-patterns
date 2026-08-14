import java.util.*;

// ==========================================
// 1. ENUMS & CORE DOMAIN ENTITIES
// ==========================================

// Indian Currency Denominations
enum Coin {
    RE_1(1),
    RS_2(2),
    RS_5(5),
    RS_10(10),
    RS_20(20),
    RS_50(50),
    RS_100(100),
    RS_500(500);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

// Product Entity
class Item {
    private final String name;
    private final int price; // Price in INR (Rupees)

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
}

// Shelf holding items for a specific code (e.g., A1, A2)
class ItemShelf {
    private final String code;
    private final Item item;
    private int quantity;

    public ItemShelf(String code, Item item, int quantity) {
        this.code = code;
        this.item = item;
        this.quantity = quantity;
    }

    public boolean isAvailable() { return quantity > 0; }
    public void deductQuantity() { if (quantity > 0) quantity--; }
    
    public String getCode() { return code; }
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
}

// Inventory class managing shelves
class Inventory {
    private final Map<String, ItemShelf> shelves = new HashMap<>();

    public void addItem(Item item, String code, int quantity) {
        shelves.put(code, new ItemShelf(code, item, quantity));
    }

    public ItemShelf getItemShelf(String code) {
        return shelves.get(code);
    }

    public void displayInventory() {
        System.out.println("\n----------------- VENDING MACHINE MENU -----------------");
        for (ItemShelf shelf : shelves.values()) {
            System.out.println("Code: " + shelf.getCode() + 
                               " | Product: " + shelf.getItem().getName() + 
                               " | Price: ₹" + shelf.getItem().getPrice() + 
                               " | Available: " + shelf.getQuantity());
        }
        System.out.println("--------------------------------------------------------\n");
    }
}

// ==========================================
// 2. STATE PATTERN INTERFACE & CONCRETE STATES
// ==========================================

interface VendingMachineState {
    void insertCoin(VendingMachine machine, Coin coin);
    void selectProduct(VendingMachine machine, String code);
    void dispense(VendingMachine machine);
    void refund(VendingMachine machine);
}

// --- State 1: IdleState ---
class IdleState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.println("Inserted: ₹" + coin.getValue() + " | Current Balance: ₹" + machine.getCurrentBalance());
        machine.setState(machine.getHasMoneyState());
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        System.out.println("❌ ERROR: Please insert money before selecting a product.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("❌ ERROR: Insert money and select a product first.");
    }

    @Override
    public void refund(VendingMachine machine) {
        System.out.println("❌ ERROR: No money to refund.");
    }
}

// --- State 2: HasMoneyState ---
class HasMoneyState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.println("Inserted: ₹" + coin.getValue() + " | Total Balance: ₹" + machine.getCurrentBalance());
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        ItemShelf shelf = machine.getInventory().getItemShelf(code);

        if (shelf == null) {
            System.out.println("❌ ERROR: Invalid Product Code: " + code);
            return;
        }

        if (!shelf.isAvailable()) {
            System.out.println("❌ ERROR: Product " + shelf.getItem().getName() + " is OUT OF STOCK!");
            return;
        }

        if (machine.getCurrentBalance() < shelf.getItem().getPrice()) {
            int shortAmount = shelf.getItem().getPrice() - machine.getCurrentBalance();
            System.out.println("❌ ERROR: Insufficient balance for " + shelf.getItem().getName() + 
                               ". Price: ₹" + shelf.getItem().getPrice() + 
                               " | Inserted: ₹" + machine.getCurrentBalance() + 
                               " | Needs ₹" + shortAmount + " more.");
            return;
        }

        machine.setSelectedProductCode(code);
        System.out.println("✅ Product SELECTED: " + shelf.getItem().getName() + " (Price: ₹" + shelf.getItem().getPrice() + ")");
        machine.setState(machine.getDispenseState());
        machine.dispense(); // Auto-trigger dispensing step
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("❌ ERROR: Please select a product code first.");
    }

    @Override
    public void refund(VendingMachine machine) {
        System.out.println("🔄 REFUNDING FULL BALANCE: ₹" + machine.getCurrentBalance() + " returned.");
        machine.clearBalance();
        machine.setState(machine.getIdleState());
    }
}

// --- State 3: DispenseState ---
class DispenseState implements VendingMachineState {
    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println("❌ ERROR: Dispensing in progress. Cannot insert coins now.");
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        System.out.println("❌ ERROR: Dispensing in progress. Cannot select products.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        String code = machine.getSelectedProductCode();
        ItemShelf shelf = machine.getInventory().getItemShelf(code);

        // Deduct item count
        shelf.deductQuantity();
        System.out.println("🎉 DISPENSED ITEM: " + shelf.getItem().getName() + " dropped to tray.");

        // Calculate Change
        int change = machine.getCurrentBalance() - shelf.getItem().getPrice();
        if (change > 0) {
            System.out.println("💵 DISPENSED CHANGE: ₹" + change + " returned in coin tray.");
        }

        // Reset state
        machine.clearBalance();
        machine.setSelectedProductCode(null);
        machine.setState(machine.getIdleState());
    }

    @Override
    public void refund(VendingMachine machine) {
        System.out.println("❌ ERROR: Cannot refund. Product dispensing has already started!");
    }
}

// ==========================================
// 3. VENDING MACHINE CONTEXT CLASS
// ==========================================

class VendingMachine {
    private final VendingMachineState idleState;
    private final VendingMachineState hasMoneyState;
    private final VendingMachineState dispenseState;

    private VendingMachineState currentState;
    private final Inventory inventory;
    private final List<Coin> insertedCoins;
    private int currentBalance;
    private String selectedProductCode;

    public VendingMachine() {
        this.idleState = new IdleState();
        this.hasMoneyState = new HasMoneyState();
        this.dispenseState = new DispenseState();

        this.currentState = idleState;
        this.inventory = new Inventory();
        this.insertedCoins = new ArrayList<>();
        this.currentBalance = 0;
    }

    // Helper operations
    public void addCoin(Coin coin) {
        insertedCoins.add(coin);
        currentBalance += coin.getValue();
    }

    public void clearBalance() {
        insertedCoins.clear();
        currentBalance = 0;
    }

    // Actions delegated to Current State
    public void insertCoin(Coin coin) { currentState.insertCoin(this, coin); }
    public void selectProduct(String code) { currentState.selectProduct(this, code); }
    public void dispense() { currentState.dispense(this); }
    public void refund() { currentState.refund(this); }

    // Getters and Setters
    public void setState(VendingMachineState state) { this.currentState = state; }
    public VendingMachineState getIdleState() { return idleState; }
    public VendingMachineState getHasMoneyState() { return hasMoneyState; }
    public VendingMachineState getDispenseState() { return dispenseState; }

    public Inventory getInventory() { return inventory; }
    public int getCurrentBalance() { return currentBalance; }
    public String getSelectedProductCode() { return selectedProductCode; }
    public void setSelectedProductCode(String code) { this.selectedProductCode = code; }
}

// ==========================================
// 4. INTERVIEW TEST RUNNER (MAIN CLASS)
// ==========================================

public class Main {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();

        // Stocking Vending Machine with Indian Snacks/Drinks
        Inventory inv = machine.getInventory();
        inv.addItem(new Item("Lays Magic Masala", 20), "A1", 5);
        inv.addItem(new Item("Coca-Cola 300ml", 40), "A2", 2);
        inv.addItem(new Item("Dairy Milk Silk", 10), "B1", 10);
        inv.addItem(new Item("Bisleri Water 1L", 15), "B2", 0); // OUT OF STOCK

        inv.displayInventory();

        // -------------------------------------------------------------------
        System.out.println(">>> SCENARIO 1: Buy Coca-Cola (₹40) using ₹50 Note (Change returned)");
        // -------------------------------------------------------------------
        machine.insertCoin(Coin.RS_50);
        machine.selectProduct("A2"); // Coca Cola ₹40 -> Should dispense item & ₹10 change

        // -------------------------------------------------------------------
        System.out.println("\n>>> SCENARIO 2: Insert money and press REFUND / CANCEL");
        // -------------------------------------------------------------------
        machine.insertCoin(Coin.RS_20);
        machine.refund(); // Should refund ₹20 and reset to Idle

        // -------------------------------------------------------------------
        System.out.println("\n>>> SCENARIO 3: Try buying Lays (₹20) with Insufficient Funds (₹10)");
        // -------------------------------------------------------------------
        machine.insertCoin(Coin.RS_10);
        machine.selectProduct("A1"); // Needs ₹20 -> Fails with error message
        machine.insertCoin(Coin.RS_10); // Insert another ₹10 (Total ₹20)
        machine.selectProduct("A1"); // Succeeds!

        // -------------------------------------------------------------------
        System.out.println("\n>>> SCENARIO 4: Try buying Out-of-Stock Item (Bisleri Water)");
        // -------------------------------------------------------------------
        machine.insertCoin(Coin.RS_20);
        machine.selectProduct("B2"); // Quantity is 0 -> Fails
        machine.refund(); // Recover money
    }
}