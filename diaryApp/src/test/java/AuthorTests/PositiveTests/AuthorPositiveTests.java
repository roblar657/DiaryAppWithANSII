package AuthorTests.PositiveTests;

import edu.ntnu.iir.bidata.models.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Positive tests for the Author class.
 *
 * <ul>
 *   <li>Constructor
 *     <ul>
 *       <li>testValidAuthorWithAllFields: Tests constructor with first name, last name, and nickname</li>
 *       <li>testValidAuthorWithFirstAndLastName: Tests constructor with first name and last name</li>
 *       <li>testValidAuthorWithNickname: Tests constructor with only nickname</li>
 *     </ul>
 *   </li>
 *
 *   <li>Getters
 *     <ul>
 *       <li>testGetFirstName: Tests getFirstName returns current first name</li>
 *       <li>testGetLastName: Tests getLastName returns current last name</li>
 *       <li>testGetNickname: Tests getNickname returns current nickname</li>
 *       <li>testGetNameWithFullName: Tests getName returns full name when both first and last name are set</li>
 *       <li>testGetNameWithFirstNameOnly: Tests getName returns first name when only first name is set</li>
 *       <li>testGetNameWithLastNameOnly: Tests getName returns last name when only last name is set</li>
 *       <li>testGetNameWithNicknameOnly: Tests getName returns nickname when only nickname is set</li>
 *     </ul>
 *   </li>
 * </ul>
 */
class AuthorPositiveTest {

    private Author authorFullName;
    private Author authorNickname;

    private final String FIRST_NAME_1 = "Robert";
    private final String FIRST_NAME_2 = "Ola";
    private final String LAST_NAME_1 = "Larsen";
    private final String LAST_NAME_2 = "Nordmann";
    private final String NICKNAME_1 = "robelar";
    private final String NICKNAME_2 = "lar";

    @BeforeEach
    void setUp() {
        authorFullName = new Author(FIRST_NAME_1, LAST_NAME_1, NICKNAME_1);
        authorNickname = new Author(NICKNAME_2);
    }

    /**
     * Tests constructor with first name, last name, and nickname.
     */
    @Test
    void testValidAuthorWithAllFields() {
        assertEquals(FIRST_NAME_1, authorFullName.getFirstName());
        assertEquals(LAST_NAME_1, authorFullName.getLastName());
        assertEquals(NICKNAME_1, authorFullName.getNickname());
    }

    /**
     * Tests constructor with first name and last name.
     */
    @Test
    void testValidAuthorWithFirstAndLastName() {
        Author author = new Author(FIRST_NAME_2, LAST_NAME_2);
        assertEquals(FIRST_NAME_2, author.getFirstName());
        assertEquals(LAST_NAME_2, author.getLastName());
        assertNull(author.getNickname());
    }

    /**
     * Tests constructor with only nickname.
     */
    @Test
    void testValidAuthorWithNickname() {
        assertEquals(NICKNAME_2, authorNickname.getNickname());
        assertNull(authorNickname.getFirstName());
        assertNull(authorNickname.getLastName());
    }

    /**
     * Tests getFirstName returns the current first name.
     */
    @Test
    void testGetFirstName() {
        assertEquals(FIRST_NAME_1, authorFullName.getFirstName());
    }

    /**
     * Tests getLastName returns the current last name.
     */
    @Test
    void testGetLastName() {
        assertEquals(LAST_NAME_1, authorFullName.getLastName());
    }

    /**
     * Tests getNickname returns the current nickname.
     */
    @Test
    void testGetNickname() {
        assertEquals(NICKNAME_1, authorFullName.getNickname());
    }

    /**
     * Tests getName returns full name when both first and last name are set.
     */
    @Test
    void testGetNameWithFullName() {
        Author author = new Author(FIRST_NAME_1, LAST_NAME_1);
        assertEquals(FIRST_NAME_1 + " " + LAST_NAME_1, author.getName());
    }

    /**
     * Tests getName returns first name when only first name is set.
     */
    @Test
    void testGetNameWithFirstNameOnly() {
        Author author = new Author(FIRST_NAME_1, null, null);
        assertEquals(FIRST_NAME_1, author.getName());
    }

    /**
     * Tests getName returns last name when only last name is set.
     */
    @Test
    void testGetNameWithLastNameOnly() {
        Author author = new Author(null, LAST_NAME_1, null);
        assertEquals(LAST_NAME_1, author.getName());
    }

    /**
     * Tests getName returns nickname when only nickname is set.
     */
    @Test
    void testGetNameWithNicknameOnly() {
        assertEquals(NICKNAME_2, authorNickname.getName());
    }
}