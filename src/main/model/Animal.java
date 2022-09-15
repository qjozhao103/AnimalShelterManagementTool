package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * This class represents an animal
 * Each animal has a profile, documenting its name, species
 * age, health status, vaccination record, and its owner's name if being adopted
 */
public class Animal implements Writable {
    private String name;
    private String species;
    private int age;
    private String healthStatus;
    private boolean isVaccinated;
    private boolean isAdopted;
    private String ownerName;

    //EFFECTS: construct an animal object with given name and species,
    //         this animal is neither vaccinated nor adopted by default
    //         other information about the animal is to-be-edited.
    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
        age = 0;
        healthStatus = "";
        isVaccinated = false;
        isAdopted = false;
        ownerName = "";
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public void setAge(int age) {
        this.age = age;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName() + " "
                + getSpecies() + "'s age is set to: " + age + "\n"));
    }

    public int getAge() {
        return age;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName() + " "
                                            + getSpecies() + "'s health status is set to: " + healthStatus + "\n"));
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setVaccinated() {
        isVaccinated = true;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName() + " "
                + getSpecies() + "'s vaccination status is set to: " + isVaccinated + "\n"));
    }

    public void setVaccinated(boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName() + " "
                + getSpecies() + "'s vaccination status is set to: " + isVaccinated + "\n"));
    }

    //EFFECTS: returns true if the animal is vaccinated, false otherwise
    public boolean checkVaccination() {
        return isVaccinated;
    }

    //MODIFIES: this
    //EFFECTS: once the animal is adopted, change isAdopted to true, and the owner's name is documented
    public void adoptedBy(String ownerName) {
        isAdopted = true;
        this.ownerName = ownerName;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName()
                                        + " " + getSpecies() + " is adopted by: " + ownerName + "\n"));
    }

    //EFFECTS: returns true if the animal is adopted, false otherwise
    public boolean checkAdoption() {
        return isAdopted;
    }

    public void setAdopted(boolean isAdopted) {
        this.isAdopted = isAdopted;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName() + " "
                + getSpecies() + "'s adoption status is set to: " + isAdopted + "\n"));
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        EventLog.getInstance().logEvent(new Event("This animal: " + getName() + " "
                + getSpecies() + "'s owner's name is set to: " + ownerName + "\n"));
    }

    public String getOwnerName() {
        return ownerName;
    }

    //EFFECTS: return an animal's profile in detail
    public String toString() {
        String profile = "Name: " + this.getName()
                       + "\nSpecies: " + this.getSpecies()
                       + "\nAge: " + this.getAge()
                       + "\nHealth Status: " + this.getHealthStatus()
                       + "\nIs vaccinated: " + this.checkVaccination()
                       + "\nOwner's name: " + this.getOwnerName();
        return "\n============================\n" + profile;
    }

    //EFFECT: return this as Json object
    //This references code from this repo:
    //Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("species", species);
        json.put("age", age);
        json.put("healthStatus", healthStatus);
        json.put("isVaccinated", isVaccinated);
        json.put("isAdopted",isAdopted);
        json.put("ownerName", ownerName);
        return json;
    }
}

