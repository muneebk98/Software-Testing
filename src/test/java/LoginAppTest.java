import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class LoginAppTest {
    private LoginApp loginApp;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/softwaretesting";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345";

    @BeforeEach
    void setup() {
        loginApp = new LoginApp();
    }

    @Test
    void testAuthenticateUserWithValidEmail() {

        String validEmail = "johndoe@example.com";
        String expectedName = "John Doe";

        String result = loginApp.authenticateUser(validEmail);


        assertNotNull(result, "The result should not be null for a valid email");
        assertEquals(expectedName, result, "The username should match the expected name");
        assertTrue(result.length() > 0, "The username should not be an empty string");
    }

    @Test
    void testAuthenticateUserWithInvalidEmail() {

        String invalidEmail = "notfound@example.com";

        String result = loginApp.authenticateUser(invalidEmail);


        assertNull(result, "The result should be null for an invalid email");
        assertTrue(result == null || result.isEmpty(), "The result should be null or an empty string");
    }

    @Test
    void testAuthenticateUserWithEmptyAndNullEmail() {

        String emptyEmail = "";

        String emptyResult = loginApp.authenticateUser(emptyEmail);


        assertNull(emptyResult, "The result should be null for an empty email");


        String nullResult = loginApp.authenticateUser(null);


        assertNull(nullResult, "The result should be null for a null email");
    }

    @Test
    void testAuthenticateUserWithSQLInjectionAttempt() {

        String maliciousInput = "' OR '1'='1";

        String result = loginApp.authenticateUser(maliciousInput);


        assertNull(result, "The result should be null for an SQL injection attempt");
        assertTrue(result == null || result.isEmpty(), "The result should be null or an empty string");
    }

    @Test
    void testDatabaseConnection() {

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            assertNotNull(conn, "Connection should not be null");
            assertTrue(conn.isValid(2), "Connection should be valid");
        } catch (Exception e) {
            fail("Database connection test failed: " + e.getMessage());
        }
    }
}
