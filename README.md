# Animal Shelter Management System

- This application will keep track of the animals in the shelter. Newly accepted animals' profiles could be created 
easily, the profile of any animal that has been in the shelter could be easily look up and edit if needed.
- The shelter staff will use this application. 
- I'm interested in this project as I would like to give some support to animal shelter staff. I hope that with this 
application, their workload could be reduced a bit, as the digital way to keep track of the animals is easier and 
clearer comparing to the traditional paper-based filing system.

## User stories

- As a user, I want to be able to add an animal to my shelter and create a profile of it
- As a user, I want to be able to select an animal in my shelter and edit its profile
- As a user, I want to be view the list of to-be-adopted animals, the list of already-adopted animals with their owner's
name, and the list of animals that need to be vaccinated
- As a user, I want to be able to provide adoption service. Once the adoption is made, the animal would be **removed**  
from the *current animal* list to already-adopted list, and the new owner's name is added to the animal's profile
- As a user, I want to be asked if I want to load data of the animal shelter from a file when I run the application
- As a user, I want to be asked if I want to save data of the animal shelter to a file when leaving the application


## Phase 4: Task 2


Mon Nov 22 23:12:38 PST 2021
Receive animal cat cat

Mon Nov 22 23:12:38 PST 2021
This animal: cat cat's age is set to: 2

Mon Nov 22 23:12:38 PST 2021
This animal: cat cat's health status is set to: good

Mon Nov 22 23:12:38 PST 2021
This animal: cat cat's vaccination status is set to: true

Mon Nov 22 23:12:38 PST 2021
This animal: cat cat's adoption status is set to: true

Mon Nov 22 23:12:38 PST 2021
This animal: cat cat's owner's name is set to: me!

Mon Nov 22 23:12:38 PST 2021
Receive animal dog dog

Mon Nov 22 23:12:38 PST 2021
This animal: dog dog's age is set to: 3

Mon Nov 22 23:12:38 PST 2021
This animal: dog dog's health status is set to: great

Mon Nov 22 23:12:38 PST 2021
This animal: dog dog's vaccination status is set to: true

Mon Nov 22 23:12:38 PST 2021
This animal: dog dog's adoption status is set to: false

Mon Nov 22 23:12:38 PST 2021
This animal: dog dog's owner's name is set to:

Mon Nov 22 23:12:38 PST 2021
Receive animal wolf wolf

Mon Nov 22 23:12:38 PST 2021
This animal: wolf wolf's age is set to: 0

Mon Nov 22 23:12:38 PST 2021
This animal: wolf wolf's health status is set to:

Mon Nov 22 23:12:38 PST 2021
This animal: wolf wolf's vaccination status is set to: false

Mon Nov 22 23:12:38 PST 2021
This animal: wolf wolf's adoption status is set to: false

Mon Nov 22 23:12:38 PST 2021
This animal: wolf wolf's owner's name is set to:

Mon Nov 22 23:13:07 PST 2021
This animal: wolf wolf's age is set to: 2

Mon Nov 22 23:13:07 PST 2021
This animal: wolf wolf's health status is set to: too thin

Mon Nov 22 23:13:09 PST 2021
This animal: wolf wolf's vaccination status is set to: true

Mon Nov 22 23:13:28 PST 2021
This animal: dog dog is adopted by: me!

Mon Nov 22 23:13:28 PST 2021
This animal: dog dog has been removed from the list of current animals

Mon Nov 22 23:13:44 PST 2021
Receive animal lion lion


## Phase 4: Task 3


Now I put everything inside one class: AnimalShelterViewerFrame, so this class
serve multiple functions now, as creating different function areas and putting them
together as a whole part.

- I should refactor class AnimalShelterViewerFrame into 3 classes, each functions for 
displaying animal shelter's content, displaying images, and managing user input respectively
- Then I can have a class to put those three classes in to one window frame in desirable layout
