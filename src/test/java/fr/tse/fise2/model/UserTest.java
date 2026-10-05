package fr.tse.fise2.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class UserTest {
    // Constantes pour éviter la duplication
    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_PASSWORD = "password123";
    private static final Integer VALID_ID = 1;
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    @Test
    void testConstructorNouvelUtilisateur() {
        User user = new User(VALID_USERNAME, VALID_PASSWORD);
        
        assertNull(user.getId());
        assertEquals(VALID_USERNAME, user.getUsername());
        assertEquals(VALID_PASSWORD, user.getPassword());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testConstructorUtilisateurExistant() {
        User user = new User(VALID_ID, VALID_USERNAME, VALID_PASSWORD, CREATED_AT);
        
        assertEquals(VALID_ID, user.getId());
        assertEquals(VALID_USERNAME, user.getUsername());
        assertEquals(VALID_PASSWORD, user.getPassword());
        assertEquals(CREATED_AT, user.getCreatedAt());
    }

    @Test
    void testIsValidAvecDonneesValides() {
        User user = new User(VALID_USERNAME, VALID_PASSWORD);
        assertTrue(user.isValid());
    }

    @Test
    void testIsValidAvecUsernameNull() {
        User user = new User(null, VALID_PASSWORD);
        assertFalse(user.isValid());
    }

    @Test
    void testIsValidAvecUsernameTropCourt() {
        User user = new User("ab", VALID_PASSWORD);
        assertFalse(user.isValid());
    }

    @Test
    void testIsValidAvecPasswordNull() {
        User user = new User(VALID_USERNAME, null);
        assertFalse(user.isValid());
    }

    @Test
    void testIsValidAvecPasswordTropCourt() {
        User user = new User(VALID_USERNAME, "1234567");
        assertFalse(user.isValid());
    }

    @Test
    void testEquals() {
        User user1 = new User(VALID_USERNAME, VALID_PASSWORD);
        User user2 = new User(VALID_USERNAME, "differentpassword");
        User user3 = new User("differentuser", VALID_PASSWORD);
        
        assertEquals(user1, user2); // Même username
        assertNotEquals(user1, user3); // Username différent
        assertNotEquals(user1, null);
        assertNotEquals(user1, VALID_USERNAME);
    }

    @Test
    void testHashCode() {
        User user1 = new User(VALID_USERNAME, VALID_PASSWORD);
        User user2 = new User(VALID_USERNAME, "differentpassword");
        
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}