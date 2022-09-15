package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This JsonTest references code from this repo
 * Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */
public class JsonTest {
    protected void checkAnimal(String name, String species, Animal animal) {
        assertEquals(name, animal.getName());
        assertEquals(species, animal.getSpecies());
    }
}

