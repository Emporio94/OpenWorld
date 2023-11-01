package openworld.Adventurer;

import java.util.ArrayList;
import java.util.Scanner;

import openworld.Coordinates;
import openworld.Damage;
import openworld.World;
import openworld.entityTypes.TravellingWorldEntity;
import openworld.entityTypes.WorldEntity;

public class Adventurer extends TravellingWorldEntity {

    private Damage[] attacks = new Damage[3];
    private int totalAttacks = 1;

    public Adventurer(String name, Coordinates location, int maxHealth, World world, Damage attack) {
        super(name, location, maxHealth, world, attack);
        attacks[0] = attack;
        this.world = world;
    }

    private ArrayList<WorldEntity> getEntitiesAtLocation(Coordinates location) {
        ArrayList<WorldEntity> entities = new ArrayList<>();
        entities.addAll(world.getMonstersHere(location));
        entities.addAll(world.getNPCHere(location));
        // If you want to interact with Terrain as well, add it here
        // entities.addAll(world.getTerrainHere(location));
        return entities;
    }

    public void setLocation(Coordinates newLocation) {
        this.location = newLocation;
    }

    public void addAttack(Damage attack) {
        if (totalAttacks < attacks.length) {
            attacks[totalAttacks] = attack;
            totalAttacks++;
        }
    }

    public void attack(WorldEntity target) {
        for (int i = 0; i < totalAttacks; i++) {
            target.takeDamage(attacks[i]);
        }
    }

    public Damage[] getAttacks() {
        return attacks;
    }

    public String printOptions() {
        ArrayList<String> options = new ArrayList<>();
        // Movement options
        if (this.getLocation().getY() < world.getyDimension() - 1) {
            options.add("Move East: Click 1");
        }
        if (this.getLocation().getX() < world.getxDimension() - 1) {
            options.add("Move North: Click 2");
        }
        if (this.getLocation().getY() > 0) {
            options.add("Move West: Click 3");
        }
        if (this.getLocation().getX() > 0) {
            options.add("Move South: Click 4");
        }
        // Interaction options
        ArrayList<WorldEntity> entitiesAtLocation = getEntitiesAtLocation(this.getLocation());
        for (WorldEntity entity : entitiesAtLocation) {
            options.add("Interact with " + entity.getName() + ": Click " + (options.size() + 1));
        }
        String allOptions = String.join("\n", options);
        return allOptions;
    }

    public void takeTurn() {
        boolean validInput = false;
        Scanner userInput = new Scanner(System.in);

        while (!validInput) {
            try {
                System.out.println(printOptions());
                while (!userInput.hasNextInt()) {
                    System.out.println("Please enter a number.");
                    userInput.next(); // Clear the invalid input
                }
                int selection = userInput.nextInt();
                resolveTurn(selection);
                validInput = true; // Input was valid, exit the loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.println("Please try again.");
                // The loop will continue, prompting the user for input again
            }
        }
    }

    public void resolveTurn(int selection) {
        switch (selection) {
            case 1:
                this.setLocation(new Coordinates(this.getLocation().getX(), this.getLocation().getY() + 1));
                break;
            case 2:
                this.setLocation(new Coordinates(this.getLocation().getX() + 1, this.getLocation().getY()));
                break;
            case 3:
                this.setLocation(new Coordinates(this.getLocation().getX(), this.getLocation().getY() - 1));
                break;
            case 4:
                this.setLocation(new Coordinates(this.getLocation().getX() - 1, this.getLocation().getY()));
                break;
            // Add cases for interacting with entities
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }

    // TODO: Add any other methods or functionalities you need for the Adventurer class

}
