package ui;

import model.Animal;
import model.AnimalShelter;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.*;

/**
 * This class represents the animal shelter manage application
 */
public class AnimalShelterApp {
    private static final String JSON_STORE = "./data/myShelter.json";
    private Scanner input;
    private AnimalShelter myShelter;
    private Animal manipulatingAnimal;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //EFFECTS: construct my shelter, run the application
    public AnimalShelterApp() throws FileNotFoundException {
        myShelter = new AnimalShelter();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        run();
    }

    //MODIFIES: this
    //EFFECTS: process user inputs
    private void run() {
        greeting();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        String loadOrNot = input.nextLine();
        processLoading(loadOrNot);

        String command = "";
        while (!command.equalsIgnoreCase("Q")) {
            displayMainMenu();
            command = input.nextLine();
            doCommand(command);
        }
        System.out.println("Goodbye! Have a nice Day!");

    }

    private void processLoading(String loadOrNot) {
        if (loadOrNot.equalsIgnoreCase("Y")) {
            loadAnimalShelter();
        }
    }

    private void greeting() {
        System.out.println("Welcome to my shelter!\n");
        System.out.println("Would you like to load the animal shelter data from the file?");
        System.out.println("\tY -> yes");
        System.out.println("\tN -> no");
    }

    //EFFECTS: display the main menu
    //         user can choose to receive an animal, find an animal
    //         provide the adoption service, view the shelter's information in
    //         different categories, or leave the application
    private void displayMainMenu() {
        System.out.println("\nMenu of command:");
        System.out.println("\tR -> receive an animal");
        System.out.println("\tF -> find an animal");
        System.out.println("\tA -> Assign the animal to the new owner");
        System.out.println("\tV -> view my shelter in different categories");
        System.out.println("\tQ -> quit");
    }

    //MODIFIES: this
    //EFFECTS: process user's command in main menu
    private void doCommand(String command) {
        if (command.equalsIgnoreCase("R")) {
            doReceiving();
        } else if (command.equalsIgnoreCase("F")) {
            doFind();
        } else if (command.equalsIgnoreCase("A")) {
            doAdoptionService();
        } else if (command.equalsIgnoreCase("V")) {
            doView();
        } else if (command.equalsIgnoreCase("Q")) {
            System.out.println("You will be log out soon...");
            leavingOptions();
            String saveOrNot = input.nextLine();
            processSaving(saveOrNot);
        } else {
            System.out.println("Please make a valid command.");
        }
    }

    private void processSaving(String saveOrNot) {
        if (saveOrNot.equalsIgnoreCase("Y")) {
            saveAnimalShelter();
        }
    }

    private void leavingOptions() {
        System.out.println("Would you like to save data to the file?");
        System.out.println("\tY -> yes");
        System.out.println("\tN -> no");
    }


    //MODIFIES: this
    //EFFECTS: if the shelter still have space
    //            - if the name of the new one is not the same as other animals' names,
    //              receive the animal, print out the successful message
    //            - otherwise, ask the user to think up a new name and receive the animal with the new name
    //         otherwise, print out a message telling the user that the shelter is full
    private void doReceiving() {
        if (myShelter.isOkToReceive()) {
            manipulatingAnimal = getAnimalFromInput();
            Animal toBeReceived = myShelter.find(manipulatingAnimal.getName(),manipulatingAnimal.getSpecies());
            if (toBeReceived == null) {
                myShelter.receiveAnimal(manipulatingAnimal);
                System.out.println("Successfully received!");
            } else {
                System.out.println("Please rename the animal and do receiving again");
            }
        } else {
            System.out.println("Our shelter has reached full capacity...");
        }
    }

    //MODIFIES: manipulatingAnimal
    //EFFECT: find the animal by name and species from user input
    //        if such animal exists in the shelter, print out the message telling the user
    //        that it has been found, and the user can choose to edit the animal's profile
    //        otherwise, print out the "we don't have such animal" message
    private void doFind() {
        manipulatingAnimal = getAnimalFromInput();
        Animal toBeFoundAnimal = myShelter.find(manipulatingAnimal.getName(),manipulatingAnimal.getSpecies());
        if (toBeFoundAnimal == null) {
            System.out.println("We don't have such animal");
        } else {
            System.out.println("The animal is found. Now you can edit its profile");
            doEdit(toBeFoundAnimal);
        }
    }

    //EFFECTS: get the Animal object from user's input
    private Animal getAnimalFromInput() {
        System.out.println("Please enter the animal's name");
        String name = input.nextLine();
        System.out.println("Please enter its species");
        String species = input.nextLine();

        return new Animal(name, species);
    }

    //MODIFIES: toBeFoundAnimal
    //EFFECTS: process the user's input for editing the toBeFoundAnimal's profile
    private void doEdit(Animal toBeFoundAnimal) {
        //String optionStr = input.nextLine();
        int option = 0;

        while (option != 4) {
            displayEditMenu();
            String optionStr = input.nextLine();
            option = Integer.parseInt(optionStr);
            processEditOption(option, toBeFoundAnimal);
        }
    }

    //MODIFIES: toBeFoundAnimal
    //EFFECTS: process the user's options to edit the animal's profile
    private void processEditOption(int option, Animal toBeFoundAnimal) {
        if (option == 1) {
            doSetAge(toBeFoundAnimal);
        } else if (option == 2) {
            doSetHealthStatus(toBeFoundAnimal);
        } else if (option == 3) {
            toBeFoundAnimal.setVaccinated();
            System.out.println("now " + toBeFoundAnimal.getName() + " is recorded as already-vaccinated");
        } else if (option == 4) {
            System.out.println("Leaving the editing page now");
            //isValidOption = false;
        } else {
            System.out.println("Please make a valid selection...");
        }

    }

    //MODIFIES: toBeFoundAnimal
    //EFFECTS: set the animal's age as the user input
    private void doSetAge(Animal foundAnimal) {
        System.out.println("Enter the age");
        String ageStr = input.nextLine();
        int age = Integer.parseInt(ageStr);
        foundAnimal.setAge(age);
        System.out.println("Age of " + foundAnimal.getName() + " is set to " + foundAnimal.getAge());
    }

    //MODIFIES: toBeFoundAnimal
    //EFFECTS: set the animal's health status as the user input
    private void doSetHealthStatus(Animal foundAnimal) {
        System.out.println("Enter the health status");
        String healthStatus = input.nextLine();
        foundAnimal.setHealthStatus(healthStatus);
        System.out.println("Health status of " + foundAnimal.getName()
                         + " is set to " + foundAnimal.getHealthStatus());
    }

    //EFFECTS: display the menu in Editing page
    //         user can choose to edit animal's age or health status
    //         or its vaccination status or leave the editing page
    private void displayEditMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> edit age");
        System.out.println("\t2 -> edit health status");
        System.out.println("\t3 -> set the animal as already-vaccinated");
        System.out.println("\t4 -> leave the editing page");
    }

    //MODIFIES: this, manipulatingAnimal
    //EFFECT: assign an owner to the animal
    //        if the animal is NOT currently in our shelter, print out the fail message saying we don't
    //        have such animal
    //        if the animal is currently in our shelter, ask the user to input the new owner's name
    //         - if the animal is eligible for adoption, assign the animal to the new owner, and print out
    //           the success message
    //         - otherwise, print out a fail message saying the animal is not vaccinated
    private void doAdoptionService() {
        manipulatingAnimal = getAnimalFromInput();
        ArrayList<Animal> currentList = myShelter.getCurrentAnimalsList();
        Animal toBeAdopted = myShelter.find(manipulatingAnimal.getName(),manipulatingAnimal.getSpecies());
        if (toBeAdopted == null || !currentList.contains(toBeAdopted)) {
            System.out.println("Adoption cannot be done... We don't have such animal in our shelter");
            return;
        }

        System.out.println("Please enter the lucky owner's name");
        String ownerName = input.nextLine();

        String animalName = toBeAdopted.getName();
        String animalSpecies = toBeAdopted.getSpecies();

        boolean isDone = myShelter.makeAdoption(animalName, animalSpecies, ownerName);
        System.out.println(toBeAdopted.getOwnerName());
        if (isDone) {
            System.out.println("Adoption is done");
            System.out.println(animalName + "'s new owner is " + toBeAdopted.getOwnerName());
        } else {
            System.out.println("Adoption cannot be done..." + animalName + " is not vaccinated");
        }
    }

    //EFFECTS: process the user's input for viewing the animals' information in different categories
    private void doView() {
        String viewCommand = "";
        while (!viewCommand.equalsIgnoreCase("LEAVE")) {
            displayViewMenu();
            viewCommand = input.nextLine();
            doViewCommand(viewCommand);
        }
    }

    //EFFECTS: display the menu in Viewing page
    //         user can choose to view a list of to-be-vaccinated animals' name and species
    //         or a list of eligible-to-be-adopted animals' name and species
    //         or a list of already-adopted animals' name, species and owner's name
    //         or the detailed profile of all animals currently in the shelter
    //         or leave the viewing page
    private void displayViewMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tTBV    -> view the list of to-be-vaccinated animals");
        System.out.println("\tTBA    -> view the list of to-be-adopted animals");
        System.out.println("\tAA     -> view the list of already-adopted animals with their owner's name");
        System.out.println("\tPC     -> view the list of all animals currently in the shelter with details");
        System.out.println("\tLEAVE  -> leave the view page");
    }

    //EFFECTS: process the user's options to view the animals in our shelter in different categories
    //         users can choose to see a list only with the animals that need to get vaccination
    //         or a list only with the animals that are eligible for adoption
    //         or a list only with the animals that are already adopted
    //         or the list of profiles of all the animals that currently live in our shelter
    //         or ask the user to input a valid command
    private void doViewCommand(String viewCommand) {
        if (viewCommand.equalsIgnoreCase("TBV")) {
            System.out.println(myShelter.printToBeVaccinatedList());
        } else if (viewCommand.equalsIgnoreCase("TBA")) {
            System.out.println(myShelter.printToBeAdoptedList());
        } else if (viewCommand.equalsIgnoreCase("AA")) {
            System.out.println(myShelter.printAlreadyAdoptedList());
        } else if (viewCommand.equalsIgnoreCase("PC")) {
            System.out.println(myShelter.printCurrentAnimalList());
        } else if (viewCommand.equalsIgnoreCase("LEAVE")) {
            System.out.println("Leaving the view page now.");
        } else {
            System.out.println("Please make a valid selection.");
        }
    }


    private void loadAnimalShelter() {
        try {
            myShelter = jsonReader.read();
            System.out.println("Loaded my shelter from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


    private void saveAnimalShelter() {
        try {
            jsonWriter.open();
            jsonWriter.write(myShelter);
            jsonWriter.close();
            System.out.println("Saved my shelter to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}

