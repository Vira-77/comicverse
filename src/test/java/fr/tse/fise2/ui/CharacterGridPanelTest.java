package fr.tse.fise2.ui;

import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import fr.tse.fise2.model.ComicsCharacter;
import fr.tse.fise2.ui.panels.CharacterGridPanel;

class CharacterGridPanelTest {

    // Test de la création de la grille et de l'affichage des personnages
    @Test
    void testGridPanelCreation() {
        // Crée une instance de CharacterGridPanel avec des personnages
        List<ComicsCharacter> characters = Arrays.asList(
            new ComicsCharacter("Spider-Man", "Peter Parker", "Spidey", "http://photo-url.com", "12/08/1962", 
                "A friendly neighborhood Spider-Man.", "Bitten by a radioactive spider, Peter Parker now has spider-like abilities.", 
                List.of("Amazing Spider-Man #1", "Spider-Man #200"),null, "Marvel Comics", "http://publisher-image.com",
                "New York", "http://spiderman-api.com", 1),
        
            new ComicsCharacter("Captain America", "Steve Rogers", "Cap", "http://cap-photo.com", "04/07/1941", 
                "Super soldier with a shield.", "Enhanced strength, speed, and agility", 
                List.of("Captain America #100", "Avengers #1"), null, "Marvel Comics", "http://cap-publisher.com", 
                "America", "http://captain-america-api.com", 2),
        
            new ComicsCharacter("Thor", "Thor Odinson", "God of Thunder", "http://thor-photo.com", "01/08/1962", 
                "The Asgardian god of thunder.", "Control over thunder, immortality, super strength", 
                List.of("Thor #1", "Avengers #4"), null, "Marvel Comics", "http://thor-publisher.com", 
                "Asgard", "http://thor-api.com", 3),
        
            new ComicsCharacter("Hulk", "Bruce Banner", "Green Giant", "http://hulk-photo.com", "01/12/1962", 
                "The scientist who transforms into a raging green giant.", "Superhuman strength, regeneration", 
                List.of("The Incredible Hulk #1", "Hulk #100"), null, "Marvel Comics", "http://hulk-publisher.com", 
                 "Earth", "http://hulk-api.com", 4),
        
            new ComicsCharacter("Black Widow", "Natasha Romanoff", "Nat", "http://bw-photo.com", "09/11/1984", 
                "A deadly spy and Avenger.", "Expert martial artist, espionage", 
                List.of("Avengers #1", "Black Widow #1"),null,  "Marvel Comics", "http://bw-publisher.com", 
                 "Russia", "http://bw-api.com", 5),
        
            new ComicsCharacter("Hawkeye", "Clint Barton", "Clint", "http://hawkeye-photo.com", "07/01/1985", 
                "Master archer and Avenger.", "Expert marksmanship", 
                List.of("Avengers #16", "Hawkeye #1"), null, "Marvel Comics", "http://hawkeye-publisher.com", 
             "Earth", "http://hawkeye-api.com", 6),
        
            new ComicsCharacter("Black Panther", "T'Challa", "King of Wakanda", "http://bp-photo.com", "01/05/1966", 
                "The king of Wakanda and protector of the realm.", "Enhanced senses, agility, and strength", 
                List.of("Black Panther #1", "Avengers #52"), null, "Marvel Comics", "http://bp-publisher.com", 
                 "Wakanda", "http://blackpanther-api.com", 7),
        
            new ComicsCharacter("Doctor Strange", "Stephen Strange", "Sorcerer Supreme", "http://strange-photo.com", "10/11/1963", 
                "The Sorcerer Supreme, master of the mystic arts.", "Master of magic and mystic arts", 
                List.of("Doctor Strange #169", "Avengers #61"), null, "Marvel Comics", "http://strange-publisher.com", 
                 "Kamar-Taj", "http://doctor-strange-api.com", 8),
        
            new ComicsCharacter("Ant-Man", "Scott Lang", "Ant-Man", "http://antman-photo.com", "23/07/1989", 
                "The shrinking hero with a big heart.", "Ability to shrink and communicate with ants", 
                List.of("Ant-Man #1", "Avengers #47"), null, "Marvel Comics", "http://antman-publisher.com", 
                 "Earth", "http://antman-api.com", 9),
        
            new ComicsCharacter("Wolverine", "Logan", "James Howlett", "http://wolverine-photo.com", "20/06/1886", 
                "The mutant with regenerative healing and claws.", "Regenerative healing, retractable claws", 
                List.of("X-Men #1", "Wolverine #1"), null, "Marvel Comics", "http://wolverine-publisher.com", 
                 "Canada", "http://wolverine-api.com", 10),
        
            new ComicsCharacter("Deadpool", "Wade Wilson", "Merc with a Mouth", "http://deadpool-photo.com", "12/02/1991", 
                "The unpredictable anti-hero.", "Regeneration, skilled hand-to-hand combat", 
                List.of("Deadpool #1", "X-Force #1"), null, "Marvel Comics", "http://deadpool-publisher.com", 
                 "Canada", "http://deadpool-api.com", 11),
        
            new ComicsCharacter("Vision", "Victor Shade", "Synthetic Man", "http://vision-photo.com", "20/03/1968", 
                "The android Avenger with human emotions.", "Density manipulation, flight", 
                List.of("Avengers #57", "Vision #1"), null, "Marvel Comics", "http://vision-publisher.com", 
                 "Earth", "http://vision-api.com", 12)
        );
        
        CharacterGridPanel gridPanel = new CharacterGridPanel(characters);

        // Vérifie que le gridPanel a bien été initialisé
        assertNotNull(gridPanel);

        gridPanel.updateGrid(); // Met à jour la grille pour afficher les personnages
    
         // Affiche le nombre de composants dans gridPanel pour déboguer
        System.out.println("Nombre de composants dans gridPanel : " + gridPanel.getComponentCount());

        // Récupère directement le gridPanel
        JPanel grid = gridPanel.getGridPanel();
        assertNotNull(grid);  // Vérifie que le gridPanel contient un JPanel valide

        // Affiche les informations pour déboguer sur grid
        System.out.println("Nombre de composants dans le grid (directement depuis gridPanel) : " + grid.getComponentCount());

        // Vérifie que la grille contient des personnages (les ComicCard)
        assertTrue(grid.getComponentCount() > 0, "Le gridPanel devrait contenir au moins un personnage.");
}

    // Test de la pagination
    @Test
    void testPagination() throws Exception {
        // Créer une liste de 30 personnages (plus de 25 pour tester la pagination)
        List<ComicsCharacter> characters = Arrays.asList(
        new ComicsCharacter("Spider-Man", "Peter Parker", "Spidey", "http://photo-url.com", "12/08/1962", 
            "A friendly neighborhood Spider-Man.", "Bitten by a radioactive spider, Peter Parker now has spider-like abilities.", 
            List.of("Amazing Spider-Man #1", "Spider-Man #200"), null, "Marvel Comics", "http://publisher-image.com", 
           "New York", "http://spiderman-api.com", 1),

        new ComicsCharacter("Iron Man", "Tony Stark", "Shellhead", "http://ironman-photo.com", "29/05/1963", 
            "Genius billionaire philanthropist.", "Inventor of the Iron Man suit, a powerful weapon for peace.", 
            List.of("Iron Man #1", "Avengers #4"), null, "Marvel Comics", "http://ironman-publisher.com", 
           "New York", "http://ironman-api.com", 2),

        new ComicsCharacter("Captain America", "Steve Rogers", "Cap", "http://cap-photo.com", "04/07/1941", 
            "Super soldier with a shield.", "Enhanced strength, speed, and durability from the super soldier serum.", 
            List.of("Captain America #100", "Avengers #1"), null, "Marvel Comics", "http://cap-publisher.com", 
             "America", "http://captain-america-api.com", 3),

        new ComicsCharacter("Thor", "Thor Odinson", "God of Thunder", "http://thor-photo.com", "01/08/1962", 
            "The Asgardian god of thunder.", "Wields Mjolnir, the enchanted hammer, with the power to control thunder and lightning.", 
            List.of("Thor #1", "Avengers #4"), null, "Marvel Comics", "http://thor-publisher.com", 
             "Asgard", "http://thor-api.com", 4),

        new ComicsCharacter("Hulk", "Bruce Banner", "Green Giant", "http://hulk-photo.com", "01/12/1962", 
            "The scientist who transforms into a raging green giant.", "Regenerative healing and superhuman strength that increases with anger.", 
            List.of("The Incredible Hulk #1", "Hulk #100"), null, "Marvel Comics", "http://hulk-publisher.com", 
             "Earth", "http://hulk-api.com", 5),

        new ComicsCharacter("Black Widow", "Natasha Romanoff", "Nat", "http://bw-photo.com", "09/11/1984", 
            "A deadly spy and Avenger.", "Expert martial artist, hand-to-hand combat, and espionage.", 
            List.of("Avengers #1", "Black Widow #1"), null, "Marvel Comics", "http://bw-publisher.com", 
             "Russia", "http://bw-api.com", 6),

        new ComicsCharacter("Hawkeye", "Clint Barton", "Clint", "http://hawkeye-photo.com", "07/01/1985", 
            "Master archer and Avenger.", "Unmatched skill with a bow and arrow, expert marksman.", 
            List.of("Avengers #16", "Hawkeye #1"), null, "Marvel Comics", "http://hawkeye-publisher.com", 
           "Earth", "http://hawkeye-api.com", 7),

        new ComicsCharacter("Black Panther", "T'Challa", "King of Wakanda", "http://bp-photo.com", "01/05/1966", 
            "The king of Wakanda and protector of the realm.", "Enhanced senses, strength, agility, and a suit made of vibranium.", 
            List.of("Black Panther #1", "Avengers #52"), null, "Marvel Comics", "http://bp-publisher.com", 
           "Wakanda", "http://blackpanther-api.com", 8),

        new ComicsCharacter("Doctor Strange", "Stephen Strange", "Sorcerer Supreme", "http://strange-photo.com", "10/11/1963", 
            "The Sorcerer Supreme, master of the mystic arts.", "Mastery of magic, can manipulate mystical forces, teleport, and time-manipulation.", 
            List.of("Doctor Strange #169", "Avengers #61"), null, "Marvel Comics", "http://strange-publisher.com", 
             "Kamar-Taj", "http://doctor-strange-api.com", 9),

        new ComicsCharacter("Ant-Man", "Scott Lang", "Ant-Man", "http://antman-photo.com", "23/07/1989", 
            "The shrinking hero with a big heart.", "Can shrink to the size of an ant, communicate with ants, and alter his density.", 
            List.of("Ant-Man #1", "Avengers #47"), null, "Marvel Comics", "http://antman-publisher.com", 
            "Earth", "http://antman-api.com", 10),

        new ComicsCharacter("Wolverine", "Logan", "James Howlett", "http://wolverine-photo.com", "20/06/1886", 
            "The mutant with regenerative healing and claws.", "Regenerative healing factor, adamantium claws and skeleton.", 
            List.of("X-Men #1", "Wolverine #1"), null, "Marvel Comics", "http://wolverine-publisher.com", 
          "Canada", "http://wolverine-api.com", 11),

        new ComicsCharacter("Deadpool", "Wade Wilson", "Merc with a Mouth", "http://deadpool-photo.com", "12/02/1991", 
            "The unpredictable anti-hero.", "Regenerative healing, master of weapons, and skilled in hand-to-hand combat.", 
            List.of("Deadpool #1", "X-Force #1"), null, "Marvel Comics", "http://deadpool-publisher.com", 
            "Canada", "http://deadpool-api.com", 12),

        new ComicsCharacter("Vision", "Victor Shade", "Synthetic Man", "http://vision-photo.com", "20/03/1968", 
            "The android Avenger with human emotions.", "Density manipulation, flight, and superhuman strength.", 
            List.of("Avengers #57", "Vision #1"), null, "Marvel Comics", "http://vision-publisher.com", 
            "Earth", "http://vision-api.com", 13),

        new ComicsCharacter("Scarlet Witch", "Wanda Maximoff", "Wanda", "http://scarletwitch-photo.com", "16/09/1968", 
            "The mutant with reality-bending powers.", "Reality manipulation, chaos magic, telekinesis.", 
            List.of("Avengers #4", "Scarlet Witch #1"), null, "Marvel Comics", "http://scarletwitch-publisher.com", 
             "Sokovia", "http://scarletwitch-api.com", 14),

        new ComicsCharacter("Falcon", "Sam Wilson", "Captain America", "http://falcon-photo.com", "23/10/1982", 
            "The flying Avenger, new Captain America.", "Flight with mechanical wings, expert fighter.", 
            List.of("Captain America #100", "Avengers #4"), null, "Marvel Comics", "http://falcon-publisher.com", 
             "USA", "http://falcon-api.com", 15),

        new ComicsCharacter("Silver Surfer", "Norrin Radd", "Surfer", "http://silversurfer-photo.com", "05/08/1966", 
            "The cosmic hero with the power cosmic.", "Super strength, flight, energy projection.", 
            List.of("Fantastic Four #48", "Silver Surfer #1"), null, "Marvel Comics", "http://silversurfer-publisher.com", 
             "Zenn-La", "http://silversurfer-api.com", 16),

        new ComicsCharacter("Gamora", "Gamora Zen Whoberi Ben Titan", "Most Dangerous Woman in the Universe", "http://gamora-photo.com", "05/06/1969", 
            "The deadliest woman in the universe, member of the Guardians.", "Superhuman strength, combat skills, and healing factor.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://gamora-publisher.com", 
             "Zen-Whoberi", "http://gamora-api.com", 17),

        new ComicsCharacter("Drax", "Drax the Destroyer", "Drax", "http://drax-photo.com", "02/12/1975", 
            "The destroyer, member of the Guardians.", "Super strength, near invulnerability, and skilled hand-to-hand fighter.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://drax-publisher.com", 
             "Kree Empire", "http://drax-api.com", 18),

        new ComicsCharacter("Rocket", "Rocket Raccoon", "Rocket", "http://rocket-photo.com", "01/08/1977", 
            "The smart-talking raccoon and weapons expert.", "Highly intelligent, skilled with various weapons, enhanced durability.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://rocket-publisher.com", 
            "Halfworld", "http://rocket-api.com", 19),

        new ComicsCharacter("Groot", "Groot", "Groot", "http://groot-photo.com", "23/05/1973", 
            "The tree-like hero with a simple vocabulary.", "Can grow and regenerate limbs, super strength, and limited telepathy.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://groot-publisher.com", 
            "Planet X", "http://groot-api.com", 20),

        new ComicsCharacter("Star-Lord", "Peter Quill", "Star-Lord", "http://star-lord-photo.com", "24/02/1979", 
            "The leader of the Guardians of the Galaxy.", "Expert pilot and marksman, enhanced strength.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://star-lord-publisher.com", 
             "Earth", "http://starlord-api.com", 21),

        new ComicsCharacter("Shang-Chi", "Shang-Chi", "Master of Kung Fu", "http://shang-chi-photo.com", "01/01/1973", 
            "The martial arts master with the ten rings.", "Master of martial arts and equipped with mystical Ten Rings of Power.", 
            List.of("Special Marvel Edition #15", "Shang-Chi #1"), null, "Marvel Comics", "http://shang-chi-publisher.com", 
             "China", "http://shang-chi-api.com", 22),

        new ComicsCharacter("Elektra", "Elektra Natchios", "Elektra", "http://elektra-photo.com", "09/03/1980", 
            "The deadly assassin and Daredevil's ally.", "Expert in martial arts, armed with twin sais.", 
            List.of("Daredevil #190", "Elektra #1"), null, "Marvel Comics", "http://elektra-publisher.com", 
            "Greece", "http://elektra-api.com", 23),

        new ComicsCharacter("Luke Cage", "Carl Lucas", "Power Man", "http://lukecage-photo.com", "11/09/1972", 
            "The bulletproof hero of Harlem.", "Superhuman strength and unbreakable skin.", 
            List.of("Luke Cage #1", "Avengers #123"), null, "Marvel Comics", "http://lukecage-publisher.com", 
             "Harlem, New York", "http://lukecage-api.com", 24),

        new ComicsCharacter("Iron Fist", "Danny Rand", "Iron Fist", "http://ironfist-photo.com", "01/06/1985", 
            "The martial artist with the power of the Iron Fist.", "Mastery of martial arts and the ability to focus his chi into an indestructible fist.", 
            List.of("Iron Fist #1", "Marvel Premiere #15"), null, "Marvel Comics", "http://ironfist-publisher.com", 
             "K'un-Lun", "http://ironfist-api.com", 25),

        new ComicsCharacter("Jessica Jones", "Jessica Campbell Jones", "Alias", "http://jessicajones-photo.com", "07/11/1982", 
            "The private investigator with super strength.", "Super strength, limited invulnerability.", 
            List.of("Alias #1", "Avengers #222"), null, "Marvel Comics", "http://jessicajones-publisher.com", 
             "New York", "http://jessicajones-api.com", 26),

        new ComicsCharacter("Punisher", "Frank Castle", "Punisher", "http://punisher-photo.com", "04/08/1970", 
            "The vigilante who punishes criminals.", "Expert in weapons, tactics, and hand-to-hand combat.", 
            List.of("The Punisher #1", "Daredevil #183"), null, "Marvel Comics", "http://punisher-publisher.com", 
             "New York", "http://punisher-api.com", 27)
    );


        // Créer une instance de CharacterGridPanel
        CharacterGridPanel gridPanel = new CharacterGridPanel(characters);

        // Récupérer les boutons de pagination "Suivant" et "Précédent"
        JPanel navigationPanel = (JPanel) gridPanel.getComponent(1); // Le panneau de navigation se trouve en bas
        JButton prevButton = (JButton) navigationPanel.getComponent(0); // Le bouton "Précédent"
        JButton nextButton = (JButton) navigationPanel.getComponent(2); // Le bouton "Suivant"

        // Vérifier la page initiale (doit être Page 1 / 2)
        JLabel pageLabel = (JLabel) navigationPanel.getComponent(1); // Le label de la page
        assertEquals("Page 1 / 2", pageLabel.getText()); // Au départ, on est sur la page 1

        // Cliquer sur le bouton "Suivant" pour aller à la page suivante
        nextButton.doClick(); // Simuler un clic sur le bouton "Suivant"

        // Attendre que l'interface soit mise à jour
        Thread.sleep(3000); // Attendre 1 seconde pour que l'interface se mette à jour

        // Vérifier que la page est mise à jour
        assertEquals("Page 2 / 2", pageLabel.getText()); // Vérifier que la page affichée est la 2ème page

        // Cliquer sur le bouton "Précédent" pour revenir à la page précédente
        prevButton.doClick(); // Simuler un clic sur le bouton "Précédent"

        // Attendre que l'interface soit mise à jour
        Thread.sleep(3000); // Attendre 1 seconde pour que l'interface se mette à jour

        // Vérifier que la page est revenue à la première
        assertEquals("Page 1 / 2", pageLabel.getText()); // Vérifier que la page affichée est à nouveau la 1ère
    }

    // Test de l'affichage correct des personnages lorsque la page est mise à jour
    @Test
    void testUpdateGridOnPageChange() throws InterruptedException {
        // Créer un CharacterGridPanel avec un certain nombre de personnages
        List<ComicsCharacter> characters = Arrays.asList(
    new ComicsCharacter(
        "Spider-Man", 
        "Peter Parker", 
        "Spidey", 
        "http://photo-url.com", 
        "12/08/1962", 
        "A friendly neighborhood Spider-Man.", 
        "Bitten by a radioactive spider, Peter Parker now has spider-like abilities, including wall-crawling, enhanced strength, and the famous 'spider-sense'.", 
        List.of("Amazing Spider-Man #1", "Spider-Man #200"), 
        null, "Marvel Comics", 
        "http://publisher-image.com",  
        "New York", 
        "http://spiderman-api.com", 
        1
    ),
    new ComicsCharacter(
        "Iron Man", 
        "Tony Stark", 
        "Shellhead", 
        "http://ironman-photo.com", 
        "29/05/1963", 
        "Genius billionaire philanthropist.", 
        "Tony Stark built a high-tech suit of armor to escape captivity, and now uses it to fight injustice as Iron Man.", 
        List.of("Tales of Suspense #39", "Iron Man #1"), 
        null, "Marvel Comics", 
        "http://ironman-publisher.com", 
        "Long Island, New York", 
        "http://ironman-api.com", 
        2
    ),
    new ComicsCharacter("Iron Man", "Tony Stark", "Shellhead", "http://ironman-photo.com", "29/05/1963", 
            "Genius billionaire philanthropist.", "Inventor of the Iron Man suit, a powerful weapon for peace.", 
            List.of("Iron Man #1", "Avengers #4"), null, "Marvel Comics", "http://ironman-publisher.com", 
           "New York", "http://ironman-api.com", 2),

        new ComicsCharacter("Captain America", "Steve Rogers", "Cap", "http://cap-photo.com", "04/07/1941", 
            "Super soldier with a shield.", "Enhanced strength, speed, and durability from the super soldier serum.", 
            List.of("Captain America #100", "Avengers #1"), null, "Marvel Comics", "http://cap-publisher.com", 
             "America", "http://captain-america-api.com", 3),

        new ComicsCharacter("Thor", "Thor Odinson", "God of Thunder", "http://thor-photo.com", "01/08/1962", 
            "The Asgardian god of thunder.", "Wields Mjolnir, the enchanted hammer, with the power to control thunder and lightning.", 
            List.of("Thor #1", "Avengers #4"), null, "Marvel Comics", "http://thor-publisher.com", 
             "Asgard", "http://thor-api.com", 4),

        new ComicsCharacter("Hulk", "Bruce Banner", "Green Giant", "http://hulk-photo.com", "01/12/1962", 
            "The scientist who transforms into a raging green giant.", "Regenerative healing and superhuman strength that increases with anger.", 
            List.of("The Incredible Hulk #1", "Hulk #100"), null, "Marvel Comics", "http://hulk-publisher.com", 
             "Earth", "http://hulk-api.com", 5),

        new ComicsCharacter("Black Widow", "Natasha Romanoff", "Nat", "http://bw-photo.com", "09/11/1984", 
            "A deadly spy and Avenger.", "Expert martial artist, hand-to-hand combat, and espionage.", 
            List.of("Avengers #1", "Black Widow #1"), null, "Marvel Comics", "http://bw-publisher.com", 
             "Russia", "http://bw-api.com", 6),

        new ComicsCharacter("Hawkeye", "Clint Barton", "Clint", "http://hawkeye-photo.com", "07/01/1985", 
            "Master archer and Avenger.", "Unmatched skill with a bow and arrow, expert marksman.", 
            List.of("Avengers #16", "Hawkeye #1"), null, "Marvel Comics", "http://hawkeye-publisher.com", 
           "Earth", "http://hawkeye-api.com", 7),

        new ComicsCharacter("Black Panther", "T'Challa", "King of Wakanda", "http://bp-photo.com", "01/05/1966", 
            "The king of Wakanda and protector of the realm.", "Enhanced senses, strength, agility, and a suit made of vibranium.", 
            List.of("Black Panther #1", "Avengers #52"), null, "Marvel Comics", "http://bp-publisher.com", 
           "Wakanda", "http://blackpanther-api.com", 8),

        new ComicsCharacter("Doctor Strange", "Stephen Strange", "Sorcerer Supreme", "http://strange-photo.com", "10/11/1963", 
            "The Sorcerer Supreme, master of the mystic arts.", "Mastery of magic, can manipulate mystical forces, teleport, and time-manipulation.", 
            List.of("Doctor Strange #169", "Avengers #61"), null, "Marvel Comics", "http://strange-publisher.com", 
             "Kamar-Taj", "http://doctor-strange-api.com", 9),

        new ComicsCharacter("Ant-Man", "Scott Lang", "Ant-Man", "http://antman-photo.com", "23/07/1989", 
            "The shrinking hero with a big heart.", "Can shrink to the size of an ant, communicate with ants, and alter his density.", 
            List.of("Ant-Man #1", "Avengers #47"), null, "Marvel Comics", "http://antman-publisher.com", 
            "Earth", "http://antman-api.com", 10),

        new ComicsCharacter("Wolverine", "Logan", "James Howlett", "http://wolverine-photo.com", "20/06/1886", 
            "The mutant with regenerative healing and claws.", "Regenerative healing factor, adamantium claws and skeleton.", 
            List.of("X-Men #1", "Wolverine #1"), null, "Marvel Comics", "http://wolverine-publisher.com", 
          "Canada", "http://wolverine-api.com", 11),

        new ComicsCharacter("Deadpool", "Wade Wilson", "Merc with a Mouth", "http://deadpool-photo.com", "12/02/1991", 
            "The unpredictable anti-hero.", "Regenerative healing, master of weapons, and skilled in hand-to-hand combat.", 
            List.of("Deadpool #1", "X-Force #1"), null, "Marvel Comics", "http://deadpool-publisher.com", 
            "Canada", "http://deadpool-api.com", 12),

        new ComicsCharacter("Vision", "Victor Shade", "Synthetic Man", "http://vision-photo.com", "20/03/1968", 
            "The android Avenger with human emotions.", "Density manipulation, flight, and superhuman strength.", 
            List.of("Avengers #57", "Vision #1"), null, "Marvel Comics", "http://vision-publisher.com", 
            "Earth", "http://vision-api.com", 13),

        new ComicsCharacter("Scarlet Witch", "Wanda Maximoff", "Wanda", "http://scarletwitch-photo.com", "16/09/1968", 
            "The mutant with reality-bending powers.", "Reality manipulation, chaos magic, telekinesis.", 
            List.of("Avengers #4", "Scarlet Witch #1"), null, "Marvel Comics", "http://scarletwitch-publisher.com", 
             "Sokovia", "http://scarletwitch-api.com", 14),

        new ComicsCharacter("Falcon", "Sam Wilson", "Captain America", "http://falcon-photo.com", "23/10/1982", 
            "The flying Avenger, new Captain America.", "Flight with mechanical wings, expert fighter.", 
            List.of("Captain America #100", "Avengers #4"), null, "Marvel Comics", "http://falcon-publisher.com", 
             "USA", "http://falcon-api.com", 15),

        new ComicsCharacter("Silver Surfer", "Norrin Radd", "Surfer", "http://silversurfer-photo.com", "05/08/1966", 
            "The cosmic hero with the power cosmic.", "Super strength, flight, energy projection.", 
            List.of("Fantastic Four #48", "Silver Surfer #1"), null, "Marvel Comics", "http://silversurfer-publisher.com", 
             "Zenn-La", "http://silversurfer-api.com", 16),

        new ComicsCharacter("Gamora", "Gamora Zen Whoberi Ben Titan", "Most Dangerous Woman in the Universe", "http://gamora-photo.com", "05/06/1969", 
            "The deadliest woman in the universe, member of the Guardians.", "Superhuman strength, combat skills, and healing factor.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://gamora-publisher.com", 
             "Zen-Whoberi", "http://gamora-api.com", 17),

        new ComicsCharacter("Drax", "Drax the Destroyer", "Drax", "http://drax-photo.com", "02/12/1975", 
            "The destroyer, member of the Guardians.", "Super strength, near invulnerability, and skilled hand-to-hand fighter.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://drax-publisher.com", 
             "Kree Empire", "http://drax-api.com", 18),

        new ComicsCharacter("Rocket", "Rocket Raccoon", "Rocket", "http://rocket-photo.com", "01/08/1977", 
            "The smart-talking raccoon and weapons expert.", "Highly intelligent, skilled with various weapons, enhanced durability.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://rocket-publisher.com", 
            "Halfworld", "http://rocket-api.com", 19),

        new ComicsCharacter("Groot", "Groot", "Groot", "http://groot-photo.com", "23/05/1973", 
            "The tree-like hero with a simple vocabulary.", "Can grow and regenerate limbs, super strength, and limited telepathy.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://groot-publisher.com", 
            "Planet X", "http://groot-api.com", 20),

        new ComicsCharacter("Star-Lord", "Peter Quill", "Star-Lord", "http://star-lord-photo.com", "24/02/1979", 
            "The leader of the Guardians of the Galaxy.", "Expert pilot and marksman, enhanced strength.", 
            List.of("Guardians of the Galaxy #1", "Infinity Gauntlet #1"), null, "Marvel Comics", "http://star-lord-publisher.com", 
             "Earth", "http://starlord-api.com", 21),

        new ComicsCharacter("Shang-Chi", "Shang-Chi", "Master of Kung Fu", "http://shang-chi-photo.com", "01/01/1973", 
            "The martial arts master with the ten rings.", "Master of martial arts and equipped with mystical Ten Rings of Power.", 
            List.of("Special Marvel Edition #15", "Shang-Chi #1"), null, "Marvel Comics", "http://shang-chi-publisher.com", 
             "China", "http://shang-chi-api.com", 22),

        new ComicsCharacter("Elektra", "Elektra Natchios", "Elektra", "http://elektra-photo.com", "09/03/1980", 
            "The deadly assassin and Daredevil's ally.", "Expert in martial arts, armed with twin sais.", 
            List.of("Daredevil #190", "Elektra #1"), null, "Marvel Comics", "http://elektra-publisher.com", 
            "Greece", "http://elektra-api.com", 23),

        new ComicsCharacter("Luke Cage", "Carl Lucas", "Power Man", "http://lukecage-photo.com", "11/09/1972", 
            "The bulletproof hero of Harlem.", "Superhuman strength and unbreakable skin.", 
            List.of("Luke Cage #1", "Avengers #123"), null, "Marvel Comics", "http://lukecage-publisher.com", 
             "Harlem, New York", "http://lukecage-api.com", 24),

        new ComicsCharacter("Iron Fist", "Danny Rand", "Iron Fist", "http://ironfist-photo.com", "01/06/1985", 
            "The martial artist with the power of the Iron Fist.", "Mastery of martial arts and the ability to focus his chi into an indestructible fist.", 
            List.of("Iron Fist #1", "Marvel Premiere #15"), null, "Marvel Comics", "http://ironfist-publisher.com", 
             "K'un-Lun", "http://ironfist-api.com", 25),

        new ComicsCharacter("Jessica Jones", "Jessica Campbell Jones", "Alias", "http://jessicajones-photo.com", "07/11/1982", 
            "The private investigator with super strength.", "Super strength, limited invulnerability.", 
            List.of("Alias #1", "Avengers #222"), null, "Marvel Comics", "http://jessicajones-publisher.com", 
             "New York", "http://jessicajones-api.com", 26),

        new ComicsCharacter("Punisher", "Frank Castle", "Punisher", "http://punisher-photo.com", "04/08/1970", 
            "The vigilante who punishes criminals.", "Expert in weapons, tactics, and hand-to-hand combat.", 
            List.of("The Punisher #1", "Daredevil #183"), null, "Marvel Comics", "http://punisher-publisher.com", 
             "New York", "http://punisher-api.com", 27)
);


        CharacterGridPanel gridPanel = new CharacterGridPanel(characters);

        /**
            // Accéder au JScrollPane qui contient le gridPanel
            JScrollPane scrollPane = (JScrollPane) gridPanel.getComponent(0);
    
            // Accéder au JPanel qui est contenu dans le JScrollPane (le gridPanel)
            JPanel grid = (JPanel) scrollPane.getViewport().getView();
        */

        // On clique sur "Suivant" et on vérifie que la page change
        JButton nextButton = (JButton) ((JPanel) gridPanel.getComponent(1)).getComponent(2); // Le bouton "Suivant" se trouve dans le panneau de navigation
        nextButton.doClick(); // On clique sur le bouton "Suivant"

        
    
         // Affiche le nombre de composants dans gridPanel pour déboguer
        System.out.println("Nombre de composants dans gridPanel : " + gridPanel.getComponentCount());

        // Récupère directement le gridPanel
        JPanel grid = gridPanel.getGridPanel();
        assertNotNull(grid);  // Vérifie que le gridPanel contient un JPanel valide

        // Affiche les informations pour déboguer sur grid
        System.out.println("Nombre de composants dans le grid (directement depuis gridPanel) : " + grid.getComponentCount());

        // Attendre que l'interface soit mise à jour
        Thread.sleep(3000); // Attendre 1 seconde pour que l'interface se mette à jour

        // Vérifie que la grille contient des personnages (les ComicCard)
        assertTrue(grid.getComponentCount() > 0, "Le gridPanel devrait contenir au moins un personnage.");

        // Après le clic, la page doit être avancée et la méthode updateGrid() doit être appelée
        // Vérifier qu'un composant supplémentaire a été ajouté dans la grille
        assertTrue(grid.getComponentCount() > 0); // Assurez-vous que des personnages supplémentaires sont affichés après le changement de page
    }
}
