package model;

import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This JsonReaderTest references code from this repo
 * Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            AnimalShelter as = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAnimalShelter() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAnimalShelter.json");
        try {
            AnimalShelter as = reader.read();
            assertTrue(as.getAllAnimalsList().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderAnimalShelter() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAnimalShelter.json");
        try {
            AnimalShelter as = reader.read();
            ArrayList<Animal> allAnimals = as.getAllAnimalsList();
            assertEquals(2, allAnimals.size());

            checkAnimal("Cola", "dog", allAnimals.get(0));
            assertEquals(0,allAnimals.get(0).getAge());
            assertEquals("",allAnimals.get(0).getHealthStatus());
            assertFalse(allAnimals.get(0).checkVaccination());
            assertFalse(allAnimals.get(0).checkAdoption());
            assertEquals("",allAnimals.get(0).getOwnerName());

            checkAnimal("Pudding", "cat", allAnimals.get(1));
            assertEquals(2,allAnimals.get(1).getAge());
            assertEquals("all good",allAnimals.get(1).getHealthStatus());
            assertTrue(allAnimals.get(1).checkVaccination());
            assertTrue(allAnimals.get(1).checkAdoption());
            assertEquals("Harry Potter",allAnimals.get(1).getOwnerName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


}
