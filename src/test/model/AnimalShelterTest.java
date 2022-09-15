package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class AnimalShelterTest {
    private Animal a1;
    private Animal a2;
    private Animal a3;
    private Animal a4;
    private Animal a5;

    private AnimalShelter testShelter;

    @BeforeEach
    void runBefore() {
        a1 = new Animal("Donut","dog");
        a2 = new Animal("Cupcake","cat");
        a3 = new Animal("Pawpaw","dog");
        a4 = new Animal("Snoopy","dog");
        a5 = new Animal("Luna","cat");

        a1.setVaccinated();
        a2.setVaccinated();
        a3.setVaccinated();

        testShelter = new AnimalShelter();
        testShelter.receiveAnimal(a1);
        testShelter.receiveAnimal(a2);
        testShelter.receiveAnimal(a3);
    }

    @Test
    void testConstructor() {
        AnimalShelter anotherShelter = new AnimalShelter();
        int sizeOfReadyForAdoptionList = anotherShelter.getReadyForAdoptionList().size();
        int sizeOfToBeVaccinatedList = anotherShelter.getToBeVaccinated().size();
        assertTrue(anotherShelter.isOkToReceive());
        assertEquals(0, sizeOfReadyForAdoptionList);
        assertEquals(0, sizeOfToBeVaccinatedList);
    }

    @Test
    void testReceiveAnimals() {
        assertTrue(testShelter.isOkToReceive());
        testShelter.receiveAnimal(a4);
        assertEquals(4, testShelter.getCurrentAnimalsList().size());
        assertEquals(4, testShelter.getAllAnimalsList().size());

        assertTrue(testShelter.isOkToReceive());
        testShelter.receiveAnimal(a5);
        assertEquals(5, testShelter.getCurrentAnimalsList().size());
        assertEquals(5, testShelter.getAllAnimalsList().size());

        assertFalse(testShelter.isOkToReceive());
    }

    @Test
    void testReceiveAnimalsFull() {
        testShelter.receiveAnimal(a4);
        testShelter.receiveAnimal(a5);

        Animal a6 = new Animal("Mimi", "cat");
        assertFalse(testShelter.isOkToReceive());
        testShelter.receiveAnimal(a6);
        assertEquals(5,testShelter.getCurrentAnimalsList().size());
        assertEquals(5,testShelter.getAllAnimalsList().size());
    }

    @Test
    void testFindIfExists() {
        Animal found1 = testShelter.find("Cupcake", "cat");
        assertEquals(a2, found1);

        Animal found2 = testShelter.find("doNUT", "DOG");
        assertEquals(a1, found2);
    }

    @Test
    void testFindIfNotExists() {
        Animal found1 = testShelter.find("Grumpy", "cat");
        assertNull(found1);

        Animal found2 = testShelter.find("Pawpaw", "Cat");
        assertNull(found2);
    }

    @Test
    void testGetToBeVaccinatedList() {
        ArrayList<Animal> list = testShelter.getToBeVaccinated();
        assertEquals(0, list.size());

        testShelter.receiveAnimal(a4);
        assertFalse(a4.checkVaccination());
        list = testShelter.getToBeVaccinated();
        assertEquals(1, list.size());
    }

    @Test
    void testReadyForAdoptionList() {
        ArrayList<Animal> list = testShelter.getReadyForAdoptionList();
        assertEquals(3, list.size());
    }

    @Test
    void testMakeAdoptionTypical() {
        assertTrue(testShelter.makeAdoption("Pawpaw","dog","Peter Parker"));
        String ownerName = testShelter.find("Pawpaw","dog").getOwnerName();
        assertEquals("Peter Parker", ownerName);
    }

    @Test
    void testMakeAdoptionNotReady() {
        testShelter.receiveAnimal(a5);
        assertFalse(testShelter.getReadyForAdoptionList().contains(a5));
        assertFalse(testShelter.makeAdoption("Luna","cat","Tony Stark"));
        String ownerName = testShelter.find("Luna","cat").getOwnerName();
        assertEquals(0, ownerName.length());
    }

    @Test
    void testPrintToBeVaccinatedListEmpty() {
        String expectedEmptyList = "To-be-vaccinated list: \n" + "No one needs to get vaccination!\n";
        assertEquals(expectedEmptyList, testShelter.printToBeVaccinatedList());
    }
    
    @Test
    void testPrintToBeVaccinatedListTypical() {
        testShelter.receiveAnimal(a4);
        testShelter.receiveAnimal(a5);
        String toBeVaccinatedList = "To-be-vaccinated list: \n" + "Snoopy, dog\n" + "Luna, cat\n";
        assertEquals(toBeVaccinatedList, testShelter.printToBeVaccinatedList());
    }

    @Test
    void testPrintToAdoptedListEmpty() {
        AnimalShelter anotherShelter = new AnimalShelter();
        anotherShelter.receiveAnimal(a4);
        anotherShelter.receiveAnimal(a5);
        String expectedEmptyList = "To-be-adopted list: \n" + "No one is eligible to be adopted :(\n";
        assertEquals(expectedEmptyList, anotherShelter.printToBeAdoptedList());
    }

    @Test
    void testPrintToAdoptedListTypical() {
        testShelter.receiveAnimal(a4);
        testShelter.receiveAnimal(a5);
        String toBeAdoptedList = "To-be-adopted list: \n"
                               + "Donut, dog\n" + "Cupcake, cat\n" + "Pawpaw, dog\n";
        assertEquals(toBeAdoptedList, testShelter.printToBeAdoptedList());
    }

    @Test
    void testPrintAlreadyAdoptedList() {
        testShelter.makeAdoption("Cupcake","cat","Tony Stark");
        testShelter.makeAdoption("Pawpaw","dog","Peter Parker");
        String AlreadyAdoptedList = "Already-adopted list: \n"
                                  + "Cupcake, cat; Owner name: Tony Stark\n"
                                  + "Pawpaw, dog; Owner name: Peter Parker\n";
        assertEquals(AlreadyAdoptedList, testShelter.printAlreadyAdoptedList());
    }

    @Test
    void testPrintCurrentAnimals() {
        String currentListStr = "Currently we have: "
                                + "\n============================\n"
                                + "Name: Donut"
                                + "\nSpecies: dog"
                                + "\nAge: 0"
                                + "\nHealth Status: "
                                + "\nIs vaccinated: true"
                                + "\nOwner's name: "
                                + "\n============================\n"
                                + "Name: Cupcake"
                                + "\nSpecies: cat"
                                + "\nAge: 0"
                                + "\nHealth Status: "
                                + "\nIs vaccinated: true"
                                + "\nOwner's name: "
                                +"\n============================\n"
                                + "Name: Pawpaw"
                                + "\nSpecies: dog"
                                + "\nAge: 0"
                                + "\nHealth Status: "
                                + "\nIs vaccinated: true"
                                + "\nOwner's name: ";
        assertEquals(currentListStr, testShelter.printCurrentAnimalList());
    }
}