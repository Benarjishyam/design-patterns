import java.util.*;

// ==========================================
// 1. BASE COMPONENT (Abstract Class or Interface)
// ==========================================

abstract class BasePizza {
    String description = "Unknown Pizza";

    public String getDescription() {
        return description;
    }

    public abstract int getCost(); // Cost in INR (Rupees)
}

// ==========================================
// 2. CONCRETE BASE COMPONENTS (Base Pizzas)
// ==========================================

class MargheritaPizza extends BasePizza {
    public MargheritaPizza() {
        description = "Margherita Pizza";
    }

    @Override
    public int getCost() {
        return 200; // Base cost ₹200
    }
}

class FarmhousePizza extends BasePizza {
    public FarmhousePizza() {
        description = "Farmhouse Pizza";
    }

    @Override
    public int getCost() {
        return 300; // Base cost ₹300
    }
}

// ==========================================
// 3. ABSTRACT DECORATOR (Has-A & Is-A BasePizza)
// ==========================================

abstract class ToppingDecorator extends BasePizza {
    protected BasePizza pizza; // Wrapped Pizza object

    public ToppingDecorator(BasePizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public abstract String getDescription();
}

// ==========================================
// 4. CONCRETE DECORATORS (Toppings)
// ==========================================

// Topping 1: Extra Cheese (+₹50)
class ExtraCheeseTopping extends ToppingDecorator {
    public ExtraCheeseTopping(BasePizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " + Extra Cheese";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 50; // Add ₹50 to wrapped pizza cost
    }
}

// Topping 2: Mushroom (+₹40)
class MushroomTopping extends ToppingDecorator {
    public MushroomTopping(BasePizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " + Mushroom";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 40; // Add ₹40
    }
}

// Topping 3: Paneer (+₹60)
class PaneerTopping extends ToppingDecorator {
    public PaneerTopping(BasePizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + " + Paneer";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 60; // Add ₹60
    }
}

// ==========================================
// 5. INTERVIEW TEST RUNNER (MAIN CLASS)
// ==========================================

public class Main {
    public static void main(String[] args) {
        // Order 1: Plain Margherita Pizza
        BasePizza order1 = new MargheritaPizza();
        System.out.println("Order 1: " + order1.getDescription() + " | Total: ₹" + order1.getCost());

        // Order 2: Margherita + Extra Cheese + Mushroom
        BasePizza order2 = new MargheritaPizza();
        order2 = new ExtraCheeseTopping(order2); // Wrap with Cheese
        order2 = new MushroomTopping(order2);    // Wrap with Mushroom

        System.out.println("Order 2: " + order2.getDescription() + " | Total: ₹" + order2.getCost());
        // Breakdown: Margherita (200) + Cheese (50) + Mushroom (40) = ₹290

        // Order 3: Farmhouse + Double Cheese + Paneer
        BasePizza order3 = new FarmhousePizza();
        order3 = new ExtraCheeseTopping(order3); // Cheese 1
        order3 = new ExtraCheeseTopping(order3); // Cheese 2 (Double Cheese!)
        order3 = new PaneerTopping(order3);      // Paneer

        System.out.println("Order 3: " + order3.getDescription() + " | Total: ₹" + order3.getCost());
        // Breakdown: Farmhouse (300) + Cheese (50) + Cheese (50) + Paneer (60) = ₹460
    }
}


/*
Console Output:

Order 1: Margherita Pizza | Total: ₹200
Order 2: Margherita Pizza + Extra Cheese + Mushroom | Total: ₹290
Order 3: Farmhouse Pizza + Extra Cheese + Extra Cheese + Paneer | Total: ₹460
Phase 4: Copy-and-Paste Markdown Block for README.md
code

# 🍕 Decorator Design Pattern: Pizza Customizer System
*/