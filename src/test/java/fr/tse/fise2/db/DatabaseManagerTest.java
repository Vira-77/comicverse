package fr.tse.fise2.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.sql.*;

import fr.tse.fise2.model.User;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DatabaseManagerTest {
    private static final Path DB_PATH = Paths.get("data", "comicvine.db");
    private DatabaseManager dbManager;

    @BeforeAll 
    static void setUpClass() throws Exception {
        // S'assurer que la base est supprimée avant tous les tests
        DatabaseManager.closeConnection();
        Files.deleteIfExists(DB_PATH);
    }

    @BeforeEach
    void setUp() throws Exception {
        // Réinitialiser la connexion pour chaque test
        if (DatabaseManager.getConnection() != null) {
            DatabaseManager.closeConnection();
        }
        dbManager = new DatabaseManager();
        DatabaseManager.initConnection();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Fermer la connexion après chaque test
        DatabaseManager.closeConnection();
    }

    @Test
    void testInitConnection() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initConnection();
            assertTrue(Files.exists(DB_PATH));
        });
    }

    @Test
    void testGetConnection() {
        assertDoesNotThrow(() -> {
            Connection conn = DatabaseManager.getConnection();
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        });
    }

    @Test
    void testCloseConnection() {
        assertDoesNotThrow(() -> {
            final Connection conn = DatabaseManager.getConnection();
            DatabaseManager.closeConnection();
            assertTrue(conn.isClosed());
        });
    }

    @Test
    void testCheckPermissions() {
        Path dataDir = DB_PATH.getParent();
        assertDoesNotThrow(() -> {
            Files.createDirectories(dataDir);
            DatabaseManager.initConnection();
        });
    }

    @Test
    void testExecuteInTransaction() {
        assertDoesNotThrow(() -> {
            DatabaseManager.executeInTransaction(connection -> {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (id INTEGER)");
                stmt.executeUpdate("INSERT INTO test (id) VALUES (1)");
                stmt.close();
            });
        });
    }

    @Test
    void testTransactionRollback() {
        assertThrows(SQLException.class, () -> {
            DatabaseManager.executeInTransaction(connection -> {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (id INTEGER)");
                stmt.executeUpdate("INSERT INTO invalid_table (id) VALUES (1)");
                stmt.close();
            });
        });
    }

    @Test
    void testCreateAndGetUser() {
        assertDoesNotThrow(() -> {
            DatabaseManager.executeInTransaction(connection -> {
                User newUser = new User("testuser", "password123");
                User createdUser = dbManager.createUser(connection, newUser);

                assertNotNull(createdUser.getId());
                assertEquals("testuser", createdUser.getUsername());

                User foundUser = dbManager.getUserByUsername(connection, "testuser");
                assertNotNull(foundUser);
                assertEquals("testuser", foundUser.getUsername());
            });
        });
    }

    @Test
    void testCreateDuplicateUser() {
        assertThrows(SQLException.class, () -> {
            DatabaseManager.executeInTransaction(connection -> {
                dbManager.createUser(connection, new User("testuser", "password123"));
                dbManager.createUser(connection, new User("testuser", "differentpassword"));
            });
        });
    }

    @Test
    void testGetNonExistentUser() {
        assertDoesNotThrow(() -> {
            DatabaseManager.executeInTransaction(connection -> {
                User user = dbManager.getUserByUsername(connection, "nonexistent");
                assertNull(user);
            });
        });
    }

    @Test
    void testValidateUser() {
        assertDoesNotThrow(() -> {
            DatabaseManager.executeInTransaction(connection -> {
                // Nettoyer d'abord la table users
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate("DELETE FROM users");
                }

                // Utiliser un nom d'utilisateur unique pour ce test
                String uniqueUsername = "testValidateUser_" + System.currentTimeMillis();
                User newUser = new User(uniqueUsername, "password123");
                dbManager.createUser(connection, newUser);

                // Vérifier la validation
                assertTrue(dbManager.validateUser(connection, uniqueUsername, "password123"),
                    "L'utilisateur devrait être validé avec le bon mot de passe");
                assertFalse(dbManager.validateUser(connection, uniqueUsername, "wrongpassword"),
                    "L'utilisateur ne devrait pas être validé avec un mauvais mot de passe");
                assertFalse(dbManager.validateUser(connection, "nonexistent", "password123"),
                    "Un utilisateur non existant ne devrait pas être validé");
            });
        });
    }

    @Test
    void testTransactionRollbackWithUserOperations() {
        // Vérifier qu'aucun utilisateur n'existe au début
        assertDoesNotThrow(() -> {
            Connection initialConn = DatabaseManager.getConnection();
            assertNull(dbManager.getUserByUsername(initialConn, "user1"), 
                "L'utilisateur ne devrait pas exister avant le test");

            try {
                DatabaseManager.executeInTransaction(connection -> {
                    // Crée le premier utilisateur
                    User user1 = new User("user1", "password123");
                    User created = dbManager.createUser(connection, user1);
                    assertNotNull(created, "L'utilisateur devrait être créé");
                    
                    // Tente de créer un doublon
                    User user2 = new User("user1", "password456");
                    dbManager.createUser(connection, user2);
                    
                    fail("Une SQLException aurait dû être levée");
                });
            } catch (SQLException expected) {
                // Vérifie que le rollback a bien fonctionné
                Connection conn = DatabaseManager.getConnection();
                User result = dbManager.getUserByUsername(conn, "user1");
                assertNull(result, "L'utilisateur ne devrait pas exister après le rollback");
            }
        });
    }
}