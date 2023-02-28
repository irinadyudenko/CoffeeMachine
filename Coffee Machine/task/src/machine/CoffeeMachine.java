package machine;

import java.util.Scanner;


public class CoffeeMachine {

    private int waterLeft;
    private int milkLeft;
    private int coffeeLeft;
    private int cupsLeft;
    private int moneyLeft;

    private enum State {MAIN_MENU, BUY_MENU, FILL_MENU, EXIT}

    private enum FillState {WATER, MILK, COFFEE, CUPS}
    State currentState;
    FillState curFillState;

    public CoffeeMachine(int waterLeft, int milkLeft, int coffeeLeft, int cupsLeft, int moneyLeft) {
        this.waterLeft = waterLeft;
        this.milkLeft = milkLeft;
        this.coffeeLeft = coffeeLeft;
        this.cupsLeft = cupsLeft;
        this.moneyLeft = moneyLeft;
        this.currentState = State.MAIN_MENU;
    }

    public void continueWorking() {
        this.currentState = State.MAIN_MENU;
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }
    public boolean proceedUserChoice(String choice) {
        switch (this.currentState) {
            case MAIN_MENU: {
                switch (choice) {
                    case "buy" : {
                        this.currentState = State.BUY_MENU;
                        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu): ");
                        return true;
                    }
                    case "fill": {
                        this.currentState = State.FILL_MENU;
                        this.curFillState = FillState.WATER;
                        System.out.println("Write how many ml of water you want to add:");
                        return true;
                    }
                    case "take": {
                        take();
                        continueWorking();
                        return true;
                    }
                    case "remaining": {
                        this.printMachineState();
                        continueWorking();
                        return true;
                    }
                    case "exit": {
                        return false;
                    }
                }
            }
            case BUY_MENU: {
                buy(choice);
                continueWorking();
                return true;
            }
            case FILL_MENU: {
                if(fill(choice, curFillState)) {
                    switch (curFillState) {
                        case WATER: {
                            curFillState = FillState.MILK;
                            System.out.println("Write how many ml of milk you want to add:");
                            return true;
                        }
                        case MILK: {
                            curFillState = FillState.COFFEE;
                            System.out.println("Write how many grams of coffee beans you want to add:");
                            return true;
                        }
                        case COFFEE: {
                            curFillState = FillState.CUPS;
                            System.out.println("Write how many disposable cups you want to add:");
                            return true;
                        }
                    }
                    continueWorking();
                }
                return true;
            }
            case EXIT: {
                return false;
            }
        }
        return false;
    }

    //products needed per espresso, latte, cappuccino
    static int[] waterPerCup = {250, 350, 200};
    static int[] milkPerCup = {0, 75, 100};
    static int[] coffeePerCup = {16, 20, 12};
    static int[] moneyPerCup = {4, 7, 6};

    void printMachineState() {
        System.out.printf("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, this.waterLeft, this.milkLeft, this.coffeeLeft, this.cupsLeft, this.moneyLeft);
    }

    void buy(String choice) {
        Scanner sc = new Scanner(choice.trim());
        if(sc.hasNextInt()) {
            int drinkType = sc.nextInt()-1;
            if(drinkType >= 0 && drinkType < 4) {
                if (waterPerCup[drinkType] > waterLeft) {
                    System.out.println("Sorry, not enough water!");
                    return;
                }
                if (milkPerCup[drinkType] > milkLeft) {
                    System.out.println("Sorry, not enough milk!");
                    return;
                }
                if (coffeePerCup[drinkType] > coffeeLeft) {
                    System.out.println("Sorry, not enough coffee!");
                    return;
                }
                if (cupsLeft < 1) {
                    System.out.println("Sorry, not enough cups!");
                    return;
                }
                waterLeft -= waterPerCup[drinkType];
                milkLeft -= milkPerCup[drinkType];
                coffeeLeft -= coffeePerCup[drinkType];
                cupsLeft--;
                moneyLeft += moneyPerCup[drinkType];
                System.out.println("I have enough resources, making you a coffee!");
            }
        }
    }

    boolean fill(String choice, FillState curFillState) {
        Scanner sc = new Scanner(choice.trim());
        if(sc.hasNextInt()) {
            int howMuchToAdd = Integer.parseInt(choice);
            switch (curFillState) {
                case WATER: {
                    waterLeft += howMuchToAdd;
                    break;
                }
                case MILK: {
                    milkLeft += howMuchToAdd;
                    break;
                }
                case COFFEE: {
                    coffeeLeft += howMuchToAdd;
                    break;
                }
                case CUPS: {
                    cupsLeft += howMuchToAdd;
                }
            }
            return true;
        }
        else {
            System.out.println("It's not a number, nothing is added, enter the number of product:");
            return false;
        }
    }

    void take() {
        System.out.printf("I gave you $%d\n", this.moneyLeft);
        this.moneyLeft = 0;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);
        coffeeMachine.continueWorking();
        String choice;
        do {
            choice = scanner.next();
        }
        while(coffeeMachine.proceedUserChoice(choice));
    }
}
