package persistence;

import model.Animal;
import model.AnimalShelter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

/**
 * This JsonReader references code from this repo
 * Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 *
 * This class represents a reader that reads animal shelter from JSON data stored in file
 */
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads animal shelter from file and returns it;
    // throws IOException if an error occurs reading data from file
    public AnimalShelter read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAnimalShelter(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }



     //EFFECTS: parses animal shelter from JSON object and returns it
    private AnimalShelter parseAnimalShelter(JSONObject jsonObject) {
        AnimalShelter as = new AnimalShelter();
        addAllAnimals(as, jsonObject);
        return as;
    }


    // MODIFIES: as
    // EFFECTS: parses allAnimals from JSON object and adds them to animal shelter
    private void addAllAnimals(AnimalShelter as, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("allAnimals");
        for (Object json : jsonArray) {
            JSONObject nextInAllAnimals = (JSONObject) json;
            addAnimal(as, nextInAllAnimals);
        }
    }

    // MODIFIES: as
    // EFFECTS: parses animal from JSON object and adds it to animal shelter's allAnimal list
    private void addAnimal(AnimalShelter as, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String species = jsonObject.getString("species");
        int age = jsonObject.getInt("age");
        String healthStatus = jsonObject.getString("healthStatus");
        boolean isVaccinated = jsonObject.getBoolean("isVaccinated");
        boolean isAdopted = jsonObject.getBoolean("isAdopted");
        String ownerName = jsonObject.getString("ownerName");

        Animal animal = new Animal(name,species);
        as.receiveAnimal(animal);
        animal.setAge(age);
        animal.setHealthStatus(healthStatus);
        animal.setVaccinated(isVaccinated);
        animal.setAdopted(isAdopted);
        animal.setOwnerName(ownerName);
    }
}
