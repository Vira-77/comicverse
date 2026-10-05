package fr.tse.fise2.db;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import java.util.*;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import fr.tse.fise2.model.Comic;
import fr.tse.fise2.model.User;

/**
 * Classe DatabaseManager
 * Cette classe permet de gérer la connexion à la base de données
 * Elle permet de gérer les requêtes SQL et de récupérer les données de la base de données
 * 
 * @version 1.0
 * @autor Samuel PLET
 */
public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_NAME = "comicvine.db";
    private static final Path DB_PATH = Paths.get("data", DB_NAME);
    private static final String SCHEMA_PATH = "/sql/schema.sql";
    private static Connection connection;
    private static final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();

    // Initialisation du logger
    static {
        try {
            // Créez le dossier log s'il n'existe pas
            File logDir = new File("./logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            // Configurez le FileHandler pour enregistrer les logs dans un fichier
            FileHandler fileHandler = new FileHandler("./logs/databasemanager.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.config("Logger initialisé avec succès.");
        } catch (IOException e) {
            LOGGER.severe("Erreur lors de la configuration du FileHandler : " + e.getMessage());
        }
    }

    /**
     * Initialise la structure de la base de données en exécutant le script schema.sql
     * 
     * @throws SQLException si l'exécution du script échoue
     */
    private static void initializeDatabase() throws SQLException {
        try {
            // Utiliser getResourceAsStream pour lire le fichier schema.sql
            try (InputStream is = DatabaseManager.class.getResourceAsStream(SCHEMA_PATH)) {
                if (is == null) {
                    throw new IOException("Schema SQL non trouvé dans les ressources");
                }
                String schema = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(schema);
                }
            }
            LOGGER.info("Structure de la base de données initialisée");
        } catch (IOException e) {
            LOGGER.severe("Erreur lecture schema.sql : " + e.getMessage());
            throw new SQLException("Erreur initialisation BDD", e);
        }
    }

    /**
     * Vérifie les permissions d'écriture sur le dossier data
     * 
     * @throws SecurityException
     */
    private static void checkPermissions() throws SecurityException {
        Path dataDir = DB_PATH.getParent();
        if (!Files.isWritable(dataDir)) {
            LOGGER.severe("Permissions insuffisantes sur " + dataDir);
            throw new SecurityException("Droit d'écriture requis sur " + dataDir);
        }
    }

    /**
     * Initialise la connexion à la base de données SQLite
     * 
     * @throws SQLException si la connexion échoue
     */
    public static void initConnection() throws SQLException {
        try {
            // Créer le dossier data s'il n'existe pas
            Files.createDirectories(DB_PATH.getParent());

            // Vérifier les permissions d'écriture
            checkPermissions();
            
            // Charger le driver
            Class.forName(DB_DRIVER);
            
            // Établir la connexion
            String dbUrl = "jdbc:sqlite:" + DB_PATH.toString();
            connection = DriverManager.getConnection(dbUrl);
            
            // Initialiser la base de données
            initializeDatabase();
            
            LOGGER.info("Connexion à la base de données établie");
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Driver SQLite non trouvé : " + e.getMessage());
            throw new SQLException("Driver non trouvé", e);
        } catch (IOException e) {
            LOGGER.severe("Erreur création dossier data : " + e.getMessage());
            throw new SQLException("Erreur création dossier", e);
        }
    }

    /**
     * Ferme la connexion à la base de données
     * Si la connexion est déjà fermée, aucune action n'est effectuée
     * 
     * @throws SQLException si la fermeture de la connexion échoue
     */
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Connexion à la base de données fermée");
            } catch (SQLException e) {
                LOGGER.severe("Erreur fermeture connexion : " + e.getMessage());
            }
        }
    }

    /**
     * Récupère la connexion active
     * Si la connexion est fermée, elle est rétablie
     * 
     * @return la connexion à la base de données
     * @throws SQLException si la connexion n'est pas établie
     */
    public static Connection getConnection() throws SQLException {
        // Si on est dans une transaction, utiliser la connexion courante
        Connection conn = transactionConnection.get();
        if (conn != null && !conn.isClosed()) {
            return conn;
        }

        // Sinon utiliser/créer une connexion standard
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        return connection;
    }

    /**
     * Exécute une opération dans une transaction
     * 
     * @param operation l'opération à exécuter
     * @throws SQLException si l'opération échoue
     */
    public static void executeInTransaction(SqlOperation operation) throws SQLException {
        // Vérifier que la connexion existe et est ouverte
        if (connection == null || connection.isClosed()) {
            initConnection();
        }
        boolean previousAutoCommit = connection.getAutoCommit();
        try {
            // Désactiver l'autocommit
            connection.setAutoCommit(false);
            transactionConnection.set(connection);
            
            // Exécuter l'opération
            operation.execute(connection);
            
            // Valider la transaction
            connection.commit();
        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            connection.rollback();
            LOGGER.severe("Erreur transaction : " + e.getMessage());
            throw e;
        } finally {
            // Rétablir l'autocommit
            connection.setAutoCommit(previousAutoCommit);
            transactionConnection.remove();
        }
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * 
     * @param username le nom d'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws SQLException si la requête échoue
     */
    public User createUser(Connection conn, User user) throws SQLException {
        // Vérfier si l'utilisateur existe déjà
        if (getUserByUsername(conn, user.getUsername()) != null) {
            String message = "Utilisateur déjà existant : " + user.getUsername();
            LOGGER.warning(message);
            throw new SQLException(message);
        }
        String sql = "INSERT INTO users (username, password, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setObject(3, user.getCreatedAt().withNano(0).toString());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user = new User(
                        rs.getInt(1),
                        user.getUsername(),
                        user.getPassword(),
                        user.getCreatedAt()
                    );
                }
            }
            LOGGER.info("Utilisateur créé : " + user.getUsername());
            return user;
        } catch (SQLException e) {
            LOGGER.severe("Erreur création utilisateur : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * 
     * @param username le nom d'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws SQLException si la requête échoue
     */
    public User getUserByUsername(Connection conn,String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        LocalDateTime.parse(rs.getString("created_at")).withNano(0)
                    );
                }
            }
            LOGGER.info("Utilisateur introuvable : " + username);
            return null;
        } catch (SQLException e) {
            LOGGER.severe("Erreur récupération utilisateur : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Vérifie si un utilisateur existe et si le mot de passe est correct
     * 
     * @param username le nom d'utilisateur
     * @param password le mot de passe
     * @return true si l'utilisateur existe et le mot de passe est correct, false sinon
     * @throws SQLException si la requête échoue
     */
    public boolean validateUser(Connection conn, String username, String password) throws SQLException {
        User user = getUserByUsername(conn, username);
        return user != null && user.getPassword().equals(password);
    }

     /**
     * Récupère un utilisateur par son nom d'utilisateur
     * 
     * @param username le nom d'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws SQLException si la requête échoue
     */
    public void addComicToDatabase(Connection conn, Comic comic, User user) throws SQLException {
        
        String sql = "INSERT INTO user_libraries (user_id,comic_id,comic_title,in_library,in_favorite,in_wish_list,comic_read_status,comic_detail_url,comic_image_url,rating) VALUES (?,?,?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, comic.getId());
            pstmt.setString(3,comic.getName());
            pstmt.setInt(4,comic.getInLibrary()? 1:0);
            pstmt.setInt(5,comic.getInFavorite()? 1:0);
            pstmt.setInt(6,comic.getInPurchase()? 1:0);
            pstmt.setInt(7,comic.getReadingStatus());
            pstmt.setString(8, comic.getApiDetailUrl());
            pstmt.setString(9, comic.getImageUrl());
            pstmt.setInt(10,comic.getStarsRating());
            pstmt.executeUpdate();

           
            LOGGER.info("Comic ajouté : " + comic.getName());
            
        } catch (SQLException e) {
            LOGGER.severe("Erreur ajout comic : " + e.getMessage());
            throw e;
        }
    }

    public void deleteComicInDatabase(Connection conn, Comic comic, User user) throws SQLException {
        String sql = "DELETE FROM user_libraries WHERE user_id = ? AND comic_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, comic.getId());
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Comic deleted from database: " + comic.getName());
            } else {
                LOGGER.warning("No comic found to delete for user: " + user.getUsername());
            }
            
        } catch (SQLException e) {
            LOGGER.severe("Error deleting comic from database: " + e.getMessage());
            throw e;
        }
    }

    // Method to modify the record (e.g., updating values)
    public void modifyComicInDatabase(Connection conn,Comic comic ,User user) throws SQLException {
        String updateQuery = "UPDATE user_libraries SET comic_read_status = ?, in_library = ?, in_wish_list = ?, in_favorite = ?,rating = ? WHERE user_id = ? and comic_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            // Modify with new values as needed
            stmt.setInt(1, comic.getReadingStatus());
            stmt.setInt(2, comic.getInLibrary() ? 1:0);   // Example: Setting the library flag to 0 (false)
            stmt.setInt(3, comic.getInPurchase() ? 1:0);   // Example: Setting the wishlist flag to 0 (false)
            stmt.setInt(4, comic.getInFavorite() ? 1:0);   // Example: Setting the favorite flag to 0 (false)
            stmt.setInt(5,comic.getStarsRating());
            stmt.setInt(6,user.getId());
            stmt.setInt(7,comic.getId());
            stmt.executeUpdate();
        }
    }

    // Method to get the list of comics from the database
    public List <Comic> getComicFromDatabase(Connection conn,User user) throws SQLException{
        List<Comic> comicsList = new ArrayList<>();
        String sql = "select comic_id,comic_read_status,comic_detail_url,comic_title,comic_image_url,in_library,in_favorite,in_wish_list,rating from user_libraries WHERE user_id = ? ";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idComic = rs.getInt("comic_id");  // Get the comic ID
                    int readStatus = rs.getInt("comic_read_status"); // Get the read status
                    String detail_url=rs.getString("comic_detail_url");
                    String name =rs.getString("comic_title");
                    String image =rs.getString("comic_image_url");
                    boolean in_library=rs.getInt("in_library")==1;
                    boolean in_favorite=rs.getInt("in_favorite")==1;
                    boolean in_wish_list=rs.getInt("in_wish_list")==1; 
                    int rating=rs.getInt("rating");

                    Comic comic=new Comic(detail_url, idComic,in_library,in_wish_list,in_favorite,readStatus,name,image,rating);
                    comicsList.add(comic);
                    
                    // Create a new ComicStatus object and add it to the list
                }
            }
            return comicsList;
            
            
        } catch (SQLException e) {
            LOGGER.severe("Error deleting comic from library: " + e.getMessage());
            throw e;
        }
    }

    // Interface fonctionnelle pour exécuter une opération SQL
    @FunctionalInterface
    public interface SqlOperation {
        void execute(Connection conn) throws SQLException;
    }
}