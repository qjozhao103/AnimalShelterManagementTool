package ui;

import model.Animal;
import model.AnimalShelter;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class represents a window frame and content of animal shelter application
 * it builds the content of the application and makes it visible
 * all the images are from http://xhslink.com/ypUgNe
 */
public class AnimalShelterViewerFrame extends JFrame {
    private JPanel userInputPanel;
    private JPanel buttonPanel;
    private JPanel imagePanel;
    private JButton editButton;
    private JComboBox displayedOptionCombo;
    private JTextArea contentArea;
    private JTextField nameField;
    private JTextField speciesField;
    private JLabel imageAsLabel;

    private static final String JSON_STORE = "./data/myShelter.json";
    private AnimalShelter myShelter;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 600;
    private static final int FIELD_WIDTH = 10;

    //EFFECT: construct the visual parts of animal shelter and make them visible
    public AnimalShelterViewerFrame() throws FileNotFoundException {
        super("Animal Shelter");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        processLoadingAndStart();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                processSavingAndExit();
                //printLog();
                System.exit(0);
            }
        });

        JPanel displayPanel = createDisplayPanel();
        buttonPanel = createButtonPanel();
        userInputPanel = createUserInputPanel();
        createImagePanel();

        this.add(displayPanel, BorderLayout.CENTER);
        this.add(imagePanel, BorderLayout.EAST);
        this.add(userInputPanel, BorderLayout.SOUTH);

        this.centreOnScreen();
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setVisible(true);
    }

    //EFFECTS:  do the printing of logging events
    private void printLog() {
        for (Event next : EventLog.getInstance()) {
            System.out.println(next);
        }
    }

    //MODIFIES: this
    //EFFECTS: create a panel for displaying images
    //This references code from this repo:
    //Link: https://github.students.cs.ubc.ca/CPSC210/C3-LectureLabStarter
    private void createImagePanel() {
        imagePanel = new JPanel();
        imageAsLabel = new JLabel(new ImageIcon("./data/newCat.png"));
        imagePanel.add(imageAsLabel);
    }

    //MODIFIES: this
    //EFFECTS: if there is an image is displaying in the image panel, remove it
    //         otherwise, do nothing
    //This references code from this repo:
    //Link: https://github.students.cs.ubc.ca/CPSC210/C3-LectureLabStarter
    private void removeExistingImage() {
        if (imageAsLabel != null) {
            imagePanel.remove(imageAsLabel);
        }
    }

    //MODIFIES: this
    //EFFECTS: change the currently displaying image
    private void changeImage(ImageIcon imageIcon) {
        removeExistingImage();
        imageAsLabel = new JLabel(imageIcon);
        imagePanel.add(imageAsLabel);
        repaint();
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: pop out a window to ask if the user wants to save data to the file
    //         if the user click "yes", save the data, otherwise do nothing
    private void processSavingAndExit() {
        printLog();
        int option = JOptionPane.showConfirmDialog(null,
                "Would you like to save data to the file?",
                "Please confirm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                jsonWriter.open();
                jsonWriter.write(myShelter);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,
                        "Successfully Saved my shelter to " + JSON_STORE,
                        "Successfully saved!", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "Unable to write to file" + JSON_STORE,
                        "Oops", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: construct animal shelter
    //         pop up a window to ask if the user wants to load data from file
    //         if the user click "yes" , load data. otherwise do nothing
    private void processLoadingAndStart() {
        myShelter = new AnimalShelter();
        int option = JOptionPane.showConfirmDialog(null,
                "Would you like to load the animal shelter data from the file?",
                "Please confirm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                myShelter = jsonReader.read();
                JOptionPane.showMessageDialog(null,
                        "Successfully loaded my shelter from " + JSON_STORE,
                        "Successfully loaded!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Unable to read from file: " + JSON_STORE,
                        "Oops", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: create a panel for displaying all the buttons in a grid layout
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,1));
        JButton receiveButton = createReceiveButton();
        editButton = createEditButton();
        JButton setVaccinationButton = createVaccinationButton();
        JButton adoptionButton = createAdoptionButton();

        buttonPanel.add(receiveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(setVaccinationButton);
        buttonPanel.add(adoptionButton);
        return buttonPanel;
    }

    //MODIFIES: this, animal
    //EFFECTS: create a button for doing "set vaccination" function
    //         click this button will set the chosen animal's status as "already vaccinated"
    //         and change currently displaying image
    private JButton createVaccinationButton() {
        JButton vb = new JButton("Vaccination");
        vb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Animal animal = myShelter.find(nameField.getText(), speciesField.getText());
                animal.setVaccinated();
                contentArea.setText(myShelter.printCurrentAnimalList());
                displayedOptionCombo.setSelectedItem("All Animals");
                changeImage(new ImageIcon("./data/cuteCat2.png"));

            }
        });
        return vb;
    }

    //MODIFIES: this, myShelter, animal
    //EFFECTS: create a button for doing "make adoption" function
    //         click this button will change the currently displaying image
    //         if the animal is not vaccinated, click this button will pop up a window telling the user
    //         that this animal needs to be vaccinated
    //         if the animal is not in the shelter, click this button will pop up a window telling the user
    //         that this animal is not in our shelter
    //         otherwise, the animal is considered as eligible to be adopted, click this button will pop up
    //         a window asking the user to input the owner's name, and set the chosen animal's ownerName as
    //         the user input,
    private JButton createAdoptionButton() {
        JButton ab = new JButton("Adopt");
        ab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeImage(new ImageIcon("./data/cutecat3.jpg"));
                Animal toBeAdopted = myShelter.find(nameField.getText(), speciesField.getText());
                if (!toBeAdopted.checkVaccination()) {
                    JOptionPane.showMessageDialog(null,"This animal needs to be vaccinated first!",
                            "Adoption cannot be done...", JOptionPane.INFORMATION_MESSAGE);
                } else if (!myShelter.getCurrentAnimalsList().contains(toBeAdopted)) {
                    JOptionPane.showMessageDialog(null,"We don't have this animal",
                            "Adoption cannot be done...", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String ownerName = JOptionPane.showInputDialog(null,
                            "Please enter the owner's name:", "Who's the lucky one?",
                            JOptionPane.QUESTION_MESSAGE);
                    myShelter.makeAdoption(nameField.getText(), speciesField.getText(), ownerName);
                    contentArea.setText(myShelter.printCurrentAnimalList());
                    displayedOptionCombo.setSelectedItem("All Animals");
                }
            }
        });
        return ab;
    }

    //MODIFIES: this, animal
    //EFFECTS: create a button for doing "edit" function
    //         click this button will edit the chosen animal's profile
    //         and change currently displaying image
    private JButton createEditButton() {
        JButton eb = new JButton("Edit");
        eb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeImage(new ImageIcon("./data/newCat.png"));

                String name = nameField.getText();
                String species = speciesField.getText();
                Animal foundAnimal = myShelter.find(name, species);
                if (foundAnimal == null || !myShelter.getCurrentAnimalsList().contains(foundAnimal)) {
                    JOptionPane.showMessageDialog(null,"We don't have this animal",
                            "OOPS", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    doEditing(foundAnimal);
                }
            }
        });
        return eb;
    }

    //REQUIRES: user input for age must be an integer
    //MODIFIES: this, foundAnimal
    //EFFECTS: pop up a window for the user to input the animal's age and health status.
    //         if the user click "ok", then edit the animal's profile as user input
    //This references code from this website tutorial:
    //https://www.tutorialspoint.com/what-are-the-different-types-of-joptionpane-dialogs-in-java
    private void doEditing(Animal foundAnimal) {
        String message = "Editing the animal's profile:";
        JTextField ageField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField healthStatusField = new JTextField();
        JLabel healthStatusLabel = new JLabel("Health status:");

        int result = JOptionPane.showOptionDialog(null,
                new Object[] {message, ageLabel, ageField, healthStatusLabel,
                              healthStatusField},
                "Editing...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String ageStr = ageField.getText();
            foundAnimal.setAge(Integer.parseInt(ageStr));
            foundAnimal.setHealthStatus(healthStatusField.getText());

            contentArea.setText(myShelter.printCurrentAnimalList());
            displayedOptionCombo.setSelectedItem("All Animals");
        }
    }

    //MODIFIES: this
    //EFFECTS: create a button for doing "receiving" function
    //         click this button will do the receiving function of the chosen animal
    //         and change currently displaying image
    private JButton createReceiveButton() {
        JButton rb = new JButton("Receive");
        rb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeImage(new ImageIcon("./data/cutecat4.png"));
                Animal animal = new Animal(nameField.getText(), speciesField.getText());
                doReceiving(animal);
            }
        });
        return rb;
    }

    //MODIFIES: this, myShelter
    //EFFECTS: if myShelter is ok to receive the new animal:
    //            - if the new animal's name is not the same as an animal(same species) that is already
    //              in the shelter, receive the animal and pop up a window telling user that it's done,
    //              updates the content displaying in the content panel
    //            - otherwise, pop up a window asking user to think up another name for this animal
    private void doReceiving(Animal animal) {
        if (myShelter.isOkToReceive()) {
            if (myShelter.find(animal.getName(), animal.getSpecies()) == null) {
                myShelter.receiveAnimal(animal);
                JOptionPane.showMessageDialog(null, "Successfully received!",
                        "YAY!", JOptionPane.INFORMATION_MESSAGE);
                contentArea.setText(myShelter.printCurrentAnimalList());
                displayedOptionCombo.setSelectedItem("All Animals");
            } else {
                JOptionPane.showMessageDialog(null,"Please think of another name!",
                        "OOPS", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,"Shelter is full!",
                    "OOPS", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //EFFECTS: create a panel that contains all the small panels functioning for user to manipulate
    //         user can input the animal's name and species
    private JPanel createUserInputPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300,125));

        JPanel panel1 = new JPanel();
        JLabel nameLabel = new JLabel("Name: ");
        nameField = new JTextField(FIELD_WIDTH);

        JPanel panel2 = new JPanel();
        JLabel speciesLabel = new JLabel("Species: ");
        speciesField = new JTextField(FIELD_WIDTH);

        panel1.add(nameLabel);
        panel1.add(nameField);
        panel2.add(speciesLabel);
        panel2.add(speciesField);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(buttonPanel);

        return panel;
    }

    //EFFECTS: return a panel for displaying the comboBox and textArea
    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(165, 229, 227));
        panel.setLayout(new BorderLayout());
        panel.add(createComboBox(), BorderLayout.NORTH);
        panel.add(createTextArea(), BorderLayout.CENTER);

        return panel;
    }

    //EFFECTS: return a JScrollPane that displaying the content of
    //         a list of all animals that currently live ing myShelter (by default)
    //         or a list of to-be-vaccinated animals' name and species
    //         or a list of eligible-to-be-adopted animals' name and species
    //         or a list of already-adopted animals' name, species and owner's name
    private JScrollPane createTextArea() {
        contentArea = new JTextArea(3,3);
        contentArea.setBackground(new Color(165, 229, 227));
        JScrollPane sp = new JScrollPane(contentArea);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentArea.setText(myShelter.printCurrentAnimalList());

        return sp;
    }

    //MODIFIES: this
    //EFFECTS: return a comboBox of choices to displaying the content of all animals, to-be-vaccinated animals,
    //         to-be-adopted animals, and already-adopted animals
    //         user click which option then set the displaying content to that option
    public JPanel createComboBox() {
        displayedOptionCombo = new JComboBox();
        displayedOptionCombo.addItem("All Animals");
        displayedOptionCombo.addItem("To Be Vaccinated");
        displayedOptionCombo.addItem("To Be Adopted");
        displayedOptionCombo.addItem("Already Adopted");
        displayedOptionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDisplayedContent();
            }
        });

        JPanel panel = new JPanel();
        panel.add(displayedOptionCombo);
        return panel;
    }

    //MODIFIES: this
    //EFFECTS: set the displaying text based on the user's option
    private void setDisplayedContent() {
        String displayChoice = (String) displayedOptionCombo.getSelectedItem();
        if (displayChoice.equalsIgnoreCase("All Animals")) {
            contentArea.setText(myShelter.printCurrentAnimalList());
        } else if (displayChoice.equalsIgnoreCase("To Be Vaccinated")) {
            contentArea.setText(myShelter.printToBeVaccinatedList());
        } else if (displayChoice.equalsIgnoreCase("To Be Adopted")) {
            contentArea.setText(myShelter.printToBeAdoptedList());
        } else if (displayChoice.equalsIgnoreCase("Already Adopted")) {
            contentArea.setText(myShelter.printAlreadyAdoptedList());
        }
    }


    //MODIFIES: this
    //EFFECTS: location of frame is set so frame is centred on desktop
    //This references code from this repo:
    //Link: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - FRAME_WIDTH) / 2, (scrn.height - FRAME_HEIGHT) / 2);
    }

}
