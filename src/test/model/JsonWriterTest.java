package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This JsonWriterTest references code from this repo
 * Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            AnimalShelter as = new AnimalShelter();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyAnimalShelter() {
        try {
            AnimalShelter as = new AnimalShelter();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyAnimalShelter.json");
            writer.open();
            writer.write(as);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyAnimalShelter.json");
            as = reader.read();
            assertEquals(0, as.getAllAnimalsList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterAnimalShelter() {
        try {
            AnimalShelter as = new AnimalShelter();
            as.receiveAnimal(new Animal("Luna","cat"));
            as.receiveAnimal(new Animal("Coffee","dog"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAnimalShelter.json");
            writer.open();
            writer.write(as);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralAnimalShelter.json");
            as = reader.read();
            ArrayList<Animal> allAnimals = as.getAllAnimalsList();
            ArrayList<Animal> currentAnimals = as.getCurrentAnimalsList();
            assertEquals(2, allAnimals.size());
            checkAnimal("Luna", "cat", allAnimals.get(0));
            checkAnimal("Coffee", "dog", allAnimals.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
