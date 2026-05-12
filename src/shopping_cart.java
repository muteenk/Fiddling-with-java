import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class shopping_cart {

    private static String displayMainMenu(Scanner scn) {
        System.out.println(" ---  Shopping Cart  --- ");
        System.out.println("Main Menu: ");
        System.out.println("[1] List Shop Items");
        System.out.println("[2] Cart Items");
        System.out.println("[3] Add Item to Cart");
        System.out.println("[4] Remove Item from Cart");
        System.out.println("[5] Checkout");
        System.out.println("[6] Exit");
        System.out.print("\nChoose option >>> ");
        return scn.next();
    }

    private static void printShopItems(
            Map<String, Double> inventory
    ){
        System.out.println("--- Shopping Items List ---");
        System.out.println("Name    |    Price");
        for (Map.Entry<String, Double> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + "    |   " + entry.getValue());
        }
    }

    private static void printCartItems(
            Map<String, Integer> cart,
            Map<String, Double> inventory
    ){
        System.out.println("--- Cart Items List ---");
        System.out.println("Name    |    Count    |    Item Price    |    Total Price");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            System.out.println(
                    entry.getKey()+ "    |   "+entry.getValue()+ "    |   "+inventory.get(entry.getKey())+ "    |   "+inventory.get(entry.getKey())*entry.getValue()
            );
        }
    }

    private static void addItemToCart(
            Map<String, Integer> cart,
            Map<String, Double> inventory,
            Scanner scn
    ){
        shopping_cart.printShopItems(inventory);
        System.out.print("\n Choose Item to add >>> ");
        String item_key = scn.next();
        scn.nextLine();
        if (!inventory.containsKey(item_key)){
            System.out.println("\n[X] INVALID ITEM");
            return;
        }
        if (cart.containsKey(item_key)){
            cart.compute(item_key, (_, oldValue) -> oldValue+1);
        } else {
            cart.put(item_key, 1);
        }
        System.out.println("\n[$] CART UPDATED");
    }

    private static void removeItemFromCart(
            Map<String, Integer> cart,
            Map<String, Double> inventory,
            Scanner scn
    ) {
        shopping_cart.printCartItems(cart, inventory);
        System.out.print("\n Choose Item to remove >>> ");
        String item_key = scn.next();
        scn.nextLine();
        if (!cart.containsKey(item_key)){
            System.out.println("\n[X] INVALID ITEM");
            return;
        }

        System.out.print("\n How many to remove >>> ");
        int countToRemove = scn.nextInt();
        scn.nextLine();
        int diff = cart.get(item_key) - countToRemove;
        if (diff <= 0)
            cart.remove(item_key);
        else
            cart.replace(item_key, diff);

        System.out.println("\n[$] CART UPDATED");
    }

    private static void checkout(
            Map<String, Integer> cart,
            Map<String, Double> inventory
    ){
        shopping_cart.printCartItems(cart, inventory);
        double paymentRequired = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            paymentRequired += entry.getValue()*inventory.get(entry.getKey());
        }
        System.out.println("Total payment required: Rs."+paymentRequired);
    }

    public static void main() {
        Scanner scn = new Scanner(System.in);
        Map<String, Integer> cart = new HashMap<>();
        Map<String, Double> inventory = new HashMap<>(){{
            put("Watch", 2000.0);
            put("Shirt", 4500.0);
            put("Jeans", 3000.0);
            put("Shoes", 8000.0);
            put("Socks", 800.0);
            put("Underwear", 80000.0);
        }};

        boolean isNotClosed = true;
        while (isNotClosed) {
            String option = shopping_cart.displayMainMenu(scn);
            scn.nextLine();
            switch (option) {
                case "1" -> {
                    shopping_cart.printShopItems(inventory);
                    System.out.println("\nPress Enter to Continue .");
                    scn.nextLine();
                }
                case "2" -> {
                    shopping_cart.printCartItems(cart, inventory);
                    System.out.println("\nPress Enter to Continue .");
                    scn.nextLine();
                }
                case "3" -> {
                    shopping_cart.addItemToCart(cart, inventory, scn);
                    System.out.println("\nPress Enter to Continue .");
                    scn.nextLine();
                }
                case "4" -> {
                    shopping_cart.removeItemFromCart(cart, inventory, scn);
                    System.out.println("\nPress Enter to Continue .");
                    scn.nextLine();
                }
                case "5" -> {
                    shopping_cart.checkout(cart, inventory);
                    System.out.println("\nPress Enter to Continue .");
                    scn.nextLine();
                }
                case "6" ->
                    isNotClosed = false;
                default -> {
                    System.out.println("[X] INVALID ENTRY.");
                    scn.nextLine();
                }
            }
        }

        scn.close();
    }
}
