package fr.tse.fise2.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import fr.tse.fise2.db.DatabaseManager;
import fr.tse.fise2.ui.LoginFrame;
import fr.tse.fise2.ui.panels.FavoritePanel;
import fr.tse.fise2.ui.panels.LibraryPanel;
import fr.tse.fise2.ui.panels.ShoppingPanel;

public class ComicManager {

    private static List<Comic> comicsList=new ArrayList<>() ;//liste des comics qui stocke tous les comics qui sont soient dans la 
    //bibliothèque,soit dans la liste d'achat,soit dans les favoris
    private static DatabaseManager dbManager=new DatabaseManager();
    private static final Logger LOGGER = Logger.getLogger(LoginFrame.class.getName());


    public static void addComic(Comic comic){
        comicsList.add(comic);
    }

    public static void setComicsList(List<Comic> comics){
        comicsList=comics;

    }

    public static List<Comic> getComicsList(){
        return comicsList;
    }

    
    public static List<Integer> getComicsID(){
        List<Integer> listId=new ArrayList<>();
        for(Comic comic :ComicManager.getComicsList()){
            listId.add(comic.getId());
        }
        return listId;

    }

    // Method to check if any comic is in the library
    public static boolean isComicInLibrary(Comic libraryComic) {
        for (Comic comic : comicsList) {
            if (comic.getId()==libraryComic.getId() && comic.getInLibrary()) {
                return true; // Return true if any comic is in the library
            }
        }
        return false; // Return false if none are in the library
    }

    // Method to check if any comic is in the wishlist
    public static boolean isComicInWishList(Comic wishListComic) {
        for (Comic comic :comicsList) {
            if (comic.getId()==wishListComic.getId() && comic.getInPurchase()) {
                return true; // Return true if any comic is in the wishlist
            }
        }
        return false; // Return false if none are in the wishlist
    }

    // Method to check if any comic is a favorite
    public static boolean isComicInFavorite(Comic favoriteComic) {
        for (Comic comic : comicsList) {
            if (comic.getId()==favoriteComic.getId() && comic.getInFavorite()) {
                return true; // Return true if any comic is a favorite
            }
        }
        return false; // Return false if none are a favorite
    }

    // Retourne une liste de tous les comics favoris
    public static List<Comic> getFavoriteComics() {
        List<Comic> favoriteComics = new ArrayList<>();
        for (Comic comic : comicsList) {
            if (comic.getInFavorite()) {
                favoriteComics.add(comic);
            }
        }
        System.out.println("Order of favorites: " + favoriteComics.stream()
                                                        .map(Comic::getName) // Utilise le nom au lieu de l'ID
                                                        .collect(Collectors.toList()));

        return favoriteComics;
    }
        

     // Méthode pour ajouter un comic aux favoris
    public static void addComicToLibrary(Comic comic) {
        if (!isComicInLibrary(comic)) {
            //comicsLibrary.add(comic);
            comic.setInLibrary(true);
            System.out.println("reading statu du comic : " +comic.getReadingStatus());
            if (!ComicManager.getComicsID().contains(comic.getId())){
                ComicManager.addComic(comic);
            }
            
        
           
            LibraryPanel.addComicCard(comic);
            boolean isInAnyCategory = comic.getInPurchase() || comic.getInFavorite();
            

            try {
			DatabaseManager.executeInTransaction(conn -> {
                if(!isInAnyCategory){

				dbManager.addComicToDatabase(conn, comic,LoginFrame.getLoggedInUser());
                }
                else{
                    dbManager.modifyComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                }
				
				
			});
		} catch (SQLException ex) {
			LOGGER.severe("Erreur d'ajout " + ex.getMessage());// pas necessaire
		}

            
        }
    }

    // Méthode pour retirer un comic des favoris
    public static void removeComicFromLibrary(Comic comic) {
        if(comic.getInLibrary())
            {
            if (ComicManager.getComicsID().contains(comic.getId())) {
            comic.setInLibrary(false);
            LibraryPanel.removeComicCard(comic);
            comic.setReadingStatus(-1);
            boolean isInAnyCategory = comic.getInPurchase() || comic.getInFavorite();
            
            try {
                DatabaseManager.executeInTransaction(conn -> {
                    if(!isInAnyCategory){ 
                        dbManager.deleteComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                        
                        }
                        else{
                            dbManager.modifyComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                        }
                    
                });
            } catch (SQLException ex) {
                LOGGER.severe("Erreur pour supprimer " + ex.getMessage());// pas necessaire
            }
            
            
            }
        }
    }

      // Méthode pour ajouter un comic aux favoris
      public static void addComicToWishList(Comic comic) {
        if (!isComicInWishList(comic)) {
            //comicsLibrary.add(comic);
            comic.setInPurchase(true);
            if (!ComicManager.getComicsID().contains(comic.getId())){
                ComicManager.addComic(comic);
            }
            
           
            ShoppingPanel.addComicCard(comic);
            boolean isInAnyCategory = comic.getInLibrary() || comic.getInFavorite(); 

            try {
			DatabaseManager.executeInTransaction(conn -> {
                if(!isInAnyCategory){
                    System.out.println("erreur 1");
                    dbManager.addComicToDatabase(conn, comic,LoginFrame.getLoggedInUser());
                    
                    }
                    else{
                        System.out.println("erreur 2");
                        dbManager.modifyComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                    }
				
				
			});
		} catch (SQLException ex) {
			LOGGER.severe("Erreur d'ajout " + ex.getMessage());// pas necessaire
		}

            
        }
    }

        // Méthode pour retirer un comic des favoris
    public static void removeComicFromWishList(Comic comic) {
        // Check if the comic is in the purchase state
        if (comic.getInPurchase()) {
            // Remove the comic from the list
            if (ComicManager.getComicsID().contains(comic.getId())) {
                // Set the comic as not in purchase state
                comic.setInPurchase(false);

                // Remove the comic card from the panel
                ShoppingPanel.removeComicCard(comic);

                // Optionally, handle the database update
                boolean isInAnyCategory = comic.getInLibrary() || comic.getInFavorite(); 
                
                try {
                    DatabaseManager.executeInTransaction(conn -> {
                        if(!isInAnyCategory){
                            
                          
                            dbManager.deleteComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                            
                            }
                            else{
                                
                                dbManager.modifyComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                            }
                    });
                } catch (SQLException ex) {
                    LOGGER.severe("Erreur pour supprimer " + ex.getMessage()); // Logging error if necessary
                }
                
            }
        } else {
            System.out.println("Le comic n'est pas en état d'achat et ne peut pas être retiré.");
        }
    }

    public static void addComicToFavorites(Comic comic) {
        if (!isComicInFavorite(comic)) {
            comic.setInFavorite(true);
            System.out.println("reading statu du comic : " +comic.getReadingStatus());
            if (!ComicManager.getComicsID().contains(comic.getId())){
                ComicManager.addComic(comic);
            }
            
            FavoritePanel.addComicCard(comic);
            boolean isInAnyCategory = comic.getInLibrary() || comic.getInPurchase(); 

            try {
                DatabaseManager.executeInTransaction(conn -> {
                    if(!isInAnyCategory){
                        System.out.println("erreur 1");
                        dbManager.addComicToDatabase(conn, comic,LoginFrame.getLoggedInUser());
                        
                        }
                        else{
                            System.out.println("erreur 2");
                            dbManager.modifyComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                        }
                    });
                } catch (SQLException ex) {
                    LOGGER.severe("Erreur d'ajout " + ex.getMessage());// pas necessaire
            }
        }
    }

    public static void removeComicFromFavorites(Comic comic) {
        // Check if the comic is in the purchase state
        if (comic.getInFavorite()) {
            // Remove the comic from the list
            if (ComicManager.getComicsID().contains(comic.getId())) {
                // Set the comic as not in purchase state
                comic.setInFavorite(false);

                // Remove the comic card from the panel
                FavoritePanel.removeComicCard(comic);
                //FavoritePanel.loadFavorites();

                // Optionally, handle the database update
                boolean isInAnyCategory = comic.getInLibrary() || comic.getInPurchase(); 
                
                try {
                    DatabaseManager.executeInTransaction(conn -> {
                        if(!isInAnyCategory){
                            
                          
                            dbManager.deleteComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                            
                            }
                            else{
                                
                                dbManager.modifyComicInDatabase(conn, comic,LoginFrame.getLoggedInUser());
                            }
                    });
                } catch (SQLException ex) {
                    LOGGER.severe("Erreur pour supprimer " + ex.getMessage()); // Logging error if necessary
                }
                
            }
        } else {
            System.out.println("Le comic n'est pas un favoris et ne peut pas être retiré.");
        }
    }

   


    public static void resetAllLists() {
        if (comicsList != null) {
            comicsList.clear();
        }
        Library.resetLibrary();
        Favorite.resetFavorites();
    }
}
