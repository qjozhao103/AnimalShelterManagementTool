package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

/**
 * Represents an animal shelter that stores the information of rescued animals
 * The shelter has a limited capacity, once it's full, we cannot accept a new animal
 * The shelter has 2 lists, one of them keeps track of all the animals that have been
 * received in the shelter, the other one only focus on the animals currently live in the shelter
 * NOTE: The animal must be vaccinated to be eligible to be adopted.
 *       Once the animal is adopted, it will be removed from the currentAnimals list
 */
public class AnimalShelter implements Writable {
    private ArrayList<Animal> allAnimals;

    //the shelter could be able to receive more animals
    // I set the maximum capacity as 5 for simplicity on testing
    public static final int MAX_CAPACITY = 5;

    //EFFECTS: construct an empty animal shelter
    public AnimalShelter() {
        allAnimals = new ArrayList<>();
    }

    //EFFECTS: check if my shelter has positions for new animals
    public boolean isOkToReceive() {
        ArrayList<Animal> currentAnimals = getCurrentAnimalsList();
        return currentAnimals.size() < MAX_CAPACITY;
    }

    //MODIFIES: this
    //EFFECTS: receive an animal if the shelter still has positions
    public void receiveAnimal(Animal animal) {
        if (this.isOkToReceive()) {
            EventLog.getInstance().logEvent(new Event("Receive animal " + animal.getName()
                    + " " + animal.getSpecies() + "\n"));
            allAnimals.add(animal);
        }
    }

    //REQUIRES: all animal list is not empty
    //EFFECTS: find an animal with its name and species
    //         if there's no such animal in my shelter, return null
    public Animal find(String name, String species) {
        for (Animal animal : allAnimals) {
            if (animal.getName().equalsIgnoreCase(name) && animal.getSpecies().equalsIgnoreCase(species)) {
                return animal;
            }
        }
        return null;
    }

    //EFFECT: return the list of all the animals being received in my shelter
    public ArrayList<Animal> getAllAnimalsList() {
        return allAnimals;
    }

    //EFFECT: return the list of current animals in my shelter
    public ArrayList<Animal> getCurrentAnimalsList() {
        ArrayList<Animal> currentAnimals = new ArrayList<>();
        for (Animal next : allAnimals) {
            if (!next.checkAdoption()) {
                currentAnimals.add(next);
            }
        }
        return currentAnimals;
    }

    //EFFECT: if the animal is vaccinated, add the animal to the to-be-adopted list, and return the list
    public ArrayList<Animal> getReadyForAdoptionList() {
        ArrayList<Animal> currentAnimals = getCurrentAnimalsList();
        ArrayList<Animal> readyForAdoption = new ArrayList<>();
        for (Animal animal : currentAnimals) {
            if (animal.checkVaccination()) {
                readyForAdoption.add(animal);
            }
        }
        return readyForAdoption;
    }

    //EFFECT: if the animal is NOT vaccinated, add the animal to the toBeVaccinated list, and return the list
    public ArrayList<Animal> getToBeVaccinated() {
        ArrayList<Animal> currentAnimals = getCurrentAnimalsList();
        ArrayList<Animal> toBeVaccinatedList = new ArrayList<>();
        for (Animal animal : currentAnimals) {
            if (!animal.checkVaccination()) {
                toBeVaccinatedList.add(animal);
            }
        }
        return toBeVaccinatedList;
    }

    //MODIFIES: animal, this
    //EFFECT: if the animal is in the ready-for-adoption list, set the animal as is adopted
    //        and record the new owner's name, remove the animal from the current animal list
    //        and return true
    //        otherwise, do nothing and return false
    public boolean makeAdoption(String animalName, String species, String ownerName) {
        ArrayList<Animal> currentAnimals = getCurrentAnimalsList();
        Animal luckyAnimal = find(animalName, species);
        boolean isReady = this.getReadyForAdoptionList().contains(luckyAnimal);
        if (isReady) {
            luckyAnimal.adoptedBy(ownerName);
            currentAnimals.remove(luckyAnimal);
            EventLog.getInstance().logEvent(new Event("This animal: " + animalName
                    + " " + species + " has been removed from the list of current animals\n"));
            return true;
        }
        return false;
    }

    //EFFECT:  if all the animals are already vaccinated, return "No one needs to get vaccination"
    //         otherwise, return the list of animals that need to be vaccinated
    //         (only shows the animal's name and species)
    public String printToBeVaccinatedList() {
        StringBuilder toBeVaccinatedListStr = new StringBuilder();
        if (this.getToBeVaccinated().isEmpty()) {
            toBeVaccinatedListStr.append("No one needs to get vaccination!\n");
        } else {
            for (Animal animal : this.getToBeVaccinated()) {
                toBeVaccinatedListStr.append(animal.getName()).append(", ").append(animal.getSpecies()).append("\n");
            }
        }
        return "To-be-vaccinated list: \n" + toBeVaccinatedListStr;
    }

    //EFFECT: if all the animals are NOT vaccinated, return "No one is eligible to be adopted"
    //        otherwise, return the list of animals that are eligible to be adopted
    //        (only shows the animal's name and species)
    public String printToBeAdoptedList() {
        StringBuilder toBeAdoptedListStr = new StringBuilder();
        if (this.getReadyForAdoptionList().isEmpty()) {
            toBeAdoptedListStr.append("No one is eligible to be adopted :(\n");
        } else {
            for (Animal animal : this.getReadyForAdoptionList()) {
                toBeAdoptedListStr.append(animal.getName()).append(", ").append(animal.getSpecies()).append("\n");
            }
        }
        return "To-be-adopted list: \n" + toBeAdoptedListStr;
    }

    //EFFECT: return the list of animals that are already adopted
    //        (only shows the animal's name, species and the new owner's name)
    public String printAlreadyAdoptedList() {
        StringBuilder toAlreadyAdoptedListStr = new StringBuilder();
        for (Animal animal : allAnimals) {
            if (animal.checkAdoption()) {
                toAlreadyAdoptedListStr.append(animal.getName()).append(", ").append(animal.getSpecies())
                                       .append("; Owner name: ").append(animal.getOwnerName()).append("\n");
            }
        }
        return "Already-adopted list: \n" + toAlreadyAdoptedListStr;
    }

    //EFFECT: return the list of all animals currently live in the shelter
    //        with the detailed profile of each animal
    public String printCurrentAnimalList() {
        ArrayList<Animal> currentAnimals = getCurrentAnimalsList();
        StringBuilder currentAnimalListStr = new StringBuilder();
        for (Animal animal : currentAnimals) {
            currentAnimalListStr.append(animal.toString());
        }
        return "Currently we have: " + currentAnimalListStr;
    }

    //EFFECT: return this as Json object
    //This references code from this repo:
    //Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("allAnimals", allAnimalsToJson());
        return json;
    }

    //EFFECT: return this.allAnimals as JsonArray
    //This references code from this repo:
    //Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    private JSONArray allAnimalsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Animal animal : allAnimals) {
            jsonArray.put(animal.toJson());
        }
        return jsonArray;
    }

}


