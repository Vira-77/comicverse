package fr.tse.fise2.ui.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.tse.fise2.db.DatabaseManager;
import fr.tse.fise2.model.Comic;
import fr.tse.fise2.ui.LoginFrame;

public class EvaluationBar extends JPanel{
    
    private static final int STAR_COUNT = 5;
    private final JLabel[] stars;
    private final ImageIcon yellowStarIcon;
    private final ImageIcon whiteStarIcon;
    private Comic comic;
    private static DatabaseManager dbManager=new DatabaseManager();
    
    // Constructeur avec le nombre d'étoiles jaunes initiales en paramètre
    public EvaluationBar(Comic comic) {
        this.comic=comic;
        stars = new JLabel[STAR_COUNT];
        yellowStarIcon = loadIcon("yellow_star.png");
        whiteStarIcon =  loadIcon("white_star.png");

        setLayout(new FlowLayout());
        //étoiles
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new JLabel(i <comic.getStarsRating() ? yellowStarIcon : whiteStarIcon);
            stars[i].setPreferredSize(new Dimension(40, 40));
            stars[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateStars((JLabel) e.getSource());
                }
            });
            add(stars[i]);
        }
    }

    // Met à jour les étoiles après un clic
    private void updateStars(JLabel clickedStar) {
        int clickedIndex = -1;
        for (int i = 0; i < STAR_COUNT; i++) {
            if (stars[i] == clickedStar) {
                clickedIndex = i;
                break;
            }
        }

        // Vérifier si l'étoile cliquée est la dernière étoile jaune
        int yellowStarsCount =-1;
        for (int i = 0; i < STAR_COUNT; i++) {
            if (stars[i].getIcon() == yellowStarIcon) {
                yellowStarsCount++;
            }
        }

        // Si l'étoile cliquée est déjà jaune, tout réinitialiser à blanc
        if (clickedIndex==yellowStarsCount) {
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i].setIcon(whiteStarIcon);
            }
            comic.setStarsRating(0);
            try {
			DatabaseManager.executeInTransaction(conn -> {
                dbManager.modifyComicInDatabase(conn, comic, LoginFrame.getLoggedInUser());
				
			});
	    	} catch (SQLException ex) {
			    System.out.println("erreur modification du rating dans la bdd");
		    }

				
				
			
        }  else {
            // Sinon, mettre à jour les étoiles jusqu'à l'étoile cliquée en jaune, le reste en blanc
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i].setIcon(i <= clickedIndex ? yellowStarIcon : whiteStarIcon);
            }
            comic.setStarsRating(clickedIndex+1);
            try {
                DatabaseManager.executeInTransaction(conn -> {
                    dbManager.modifyComicInDatabase(conn, comic, LoginFrame.getLoggedInUser());
                    
                });
                } catch (SQLException ex) {
                    System.out.println("erreur modification du rating dans la bdd");
                }
        }
    }

    // Charge une icône et la redimensionne
    private ImageIcon loadIcon(String iconName) {
        try {
            URL iconUrl = getClass().getClassLoader().getResource("images/" + iconName);
            if (iconUrl != null) {
                ImageIcon icon = new ImageIcon(iconUrl);
                return new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    

   
}
