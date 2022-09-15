package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private Animal testAnimal;

    @BeforeEach
    void setUp() {
        testAnimal = new Animal("Stitch", "Dog");
    }

    @Test
    void testConstructor() {
        assertEquals("Stitch", testAnimal.getName());
        assertEquals("Dog", testAnimal.getSpecies());
        assertFalse(testAnimal.checkVaccination());
        assertFalse(testAnimal.checkAdoption());
    }

    @Test
    void testSetAge() {
        testAnimal.setAge(3);
        assertEquals(3, testAnimal.getAge());
    }

    @Test
    void testSetHealthStatus() {
        testAnimal.setHealthStatus("I'm healthy!");
        assertEquals("I'm healthy!", testAnimal.getHealthStatus());
    }

    @Test
    void testSetVaccination() {
        assertFalse(testAnimal.checkVaccination());
        testAnimal.setVaccinated();
        assertTrue(testAnimal.checkVaccination());
    }

    @Test
    void testAdopted() {
        assertFalse(testAnimal.checkAdoption());
        testAnimal.adoptedBy("Steve Rogers");
        assertTrue(testAnimal.checkAdoption());
        assertEquals("Steve Rogers", testAnimal.getOwnerName());
    }

    @Test
    void testToString() {
        assertEquals("\n============================\n"
                + "Name: Stitch"
                + "\nSpecies: Dog"
                + "\nAge: 0"
                + "\nHealth Status: "
                + "\nIs vaccinated: false"
                + "\nOwner's name: ", testAnimal.toString());
    }
}
