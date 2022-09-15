package ui;

import java.io.FileNotFoundException;

/**
 * This class represents a viewer of animal shelter application
 * run the application
 */
public class AnimalShelterViewer {
    public static void main(String[] args) {
        try {
            new AnimalShelterViewerFrame();
        } catch (FileNotFoundException fne) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
