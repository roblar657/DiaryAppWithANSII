package AuthorTests.NegativeTests;

import edu.ntnu.iir.bidata.models.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Negative tests for the Author class.
 *
 * <ul>
 *   <li>Constructor
 *     <ul>
 *       <li>Have first name
 *         <ul>
 *           <li>testConstructorWithFirstNameNullAndLastNameBlank: Tests constructor throws exception when first name is null and last name is blank</li>
 *           <li>testConstructorWithFirstNameBlankAndLastNameNull: Tests constructor throws exception when first name is blank and last name is null</li>
 *         </ul>
 *       </li>
 *       <li>Have last name
 *         <ul>
 *           <li>testConstructorWithFirstNameNullAndLastNameBlank: Tests constructor throws exception when first name is null and last name is blank</li>
 *           <li>testConstructorWithFirstNameBlankAndLastNameNull: Tests constructor throws exception when first name is blank and last name is null</li>
 *         </ul>
 *       </li>
 *       <li>Have full name
 *         <ul>
 *           <li>testConstructorWithBothNull: Tests constructor throws exception when both first name and last name are null</li>
 *           <li>testConstructorWithBothBlank: Tests constructor throws exception when both first name and last name are blank</li>
 *           <li>testConstructorWithBothEmpty: Tests constructor throws exception when both first name and last name are empty</li>
 *         </ul>
 *       </li>
 *       <li>Have only nickname
 *         <ul>
 *           <li>testConstructorWithNicknameNull: Tests constructor throws exception when nickname is null</li>
 *           <li>testConstructorWithNicknameBlank: Tests constructor throws exception when nickname is blank</li>
 *           <li>testConstructorWithNicknameEmpty: Tests constructor throws exception when nickname is empty</li>
 *         </ul>
 *       </li>
 *       <li>Have all fields
 *         <ul>
 *           <li>testConstructorWithAllNull: Tests constructor throws exception when all parameters are null</li>
 *           <li>testConstructorWithAllBlank: Tests constructor throws exception when all parameters are blank</li>
 *           <li>testConstructorWithAllEmpty: Tests constructor throws exception when all parameters are empty</li>
 *         </ul>
 *       </li>
 *     </ul>
 *   </li>
 *
 *   <li>Setters
 *     <ul>
 *       <li>testSetFirstNameNull: Tests setFirstName throws exception when first name is null</li>
 *       <li>testSetFirstNameBlank: Tests setFirstName throws exception when first name is blank</li>
 *       <li>testSetFirstNameEmpty: Tests setFirstName throws exception when first name is empty</li>
 *       <li>testSetLastNameNull: Tests setLastName throws exception when last name is null</li>
 *       <li>testSetLastNameBlank: Tests setLastName throws exception when last name is blank</li>
 *       <li>testSetLastNameEmpty: Tests setLastName throws exception when last name is empty</li>
 *       <li>testSetNicknameNull: Tests setNickname throws exception when nickname is null</li>
 *       <li>testSetNicknameBlank: Tests setNickname throws exception when nickname is blank</li>
 *       <li>testSetNicknameEmpty: Tests setNickname throws exception when nickname is empty</li>
 *     </ul>
 *   </li>
 * </ul>
 */
class AuthorNegativeTest {

    private Author author;

    private final String FIRST_NAME = "Robert";
    private final String LAST_NAME = "Larsen";
    private final String NICKNAME = "robelar";

    private final String ERROR_NULL_VALUE = null;
    private final String ERROR_BLANK_VALUE = "   ";
    private final String ERROR_EMPTY_VALUE = "";

    @BeforeEach
    void setUp() {
        author = new Author(FIRST_NAME, LAST_NAME);
    }

    /**
     * Tests constructor throws exception when all parameters are null.
     */
    @Test
    void testConstructorWithAllNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_NULL_VALUE, ERROR_NULL_VALUE, ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when all parameters are blank.
     */
    @Test
    void testConstructorWithAllBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_BLANK_VALUE, ERROR_BLANK_VALUE, ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when all parameters are empty.
     */
    @Test
    void testConstructorWithAllEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_EMPTY_VALUE, ERROR_EMPTY_VALUE, ERROR_EMPTY_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when both parameters are null.
     */
    @Test
    void testConstructorWithBothNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_NULL_VALUE, ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when both parameters are blank.
     */
    @Test
    void testConstructorWithBothBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_BLANK_VALUE, ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when both parameters are empty.
     */
    @Test
    void testConstructorWithBothEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_EMPTY_VALUE, ERROR_EMPTY_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when first name is null and last name is blank.
     */
    @Test
    void testConstructorWithFirstNameNullAndLastNameBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_NULL_VALUE, ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when first name is blank and last name is null.
     */
    @Test
    void testConstructorWithFirstNameBlankAndLastNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_BLANK_VALUE, ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when nickname is null.
     */
    @Test
    void testConstructorWithNicknameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when nickname is blank.
     */
    @Test
    void testConstructorWithNicknameBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests constructor throws exception when nickname is empty.
     */
    @Test
    void testConstructorWithNicknameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Author(ERROR_EMPTY_VALUE);
        });
    }

    /**
     * Tests setFirstName throws exception when first name is null.
     */
    @Test
    void testSetFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            author.setFirstName(ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests setFirstName throws exception when first name is blank.
     */
    @Test
    void testSetFirstNameBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            author.setFirstName(ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests setFirstName throws exception when first name is empty.
     */
    @Test
    void testSetFirstNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            author.setFirstName(ERROR_EMPTY_VALUE);
        });
    }

    /**
     * Tests setLastName throws exception when last name is null.
     */
    @Test
    void testSetLastNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            author.setLastName(ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests setLastName throws exception when last name is blank.
     */
    @Test
    void testSetLastNameBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            author.setLastName(ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests setLastName throws exception when last name is empty.
     */
    @Test
    void testSetLastNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            author.setLastName(ERROR_EMPTY_VALUE);
        });
    }

    /**
     * Tests setNickname throws exception when nickname is null.
     */
    @Test
    void testSetNicknameNull() {
        Author authorWithNickname = new Author(NICKNAME);
        assertThrows(IllegalArgumentException.class, () -> {
            authorWithNickname.setNickname(ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests setNickname throws exception when nickname is blank.
     */
    @Test
    void testSetNicknameBlank() {
        Author authorWithNickname = new Author(NICKNAME);
        assertThrows(IllegalArgumentException.class, () -> {
            authorWithNickname.setNickname(ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests setNickname throws exception when nickname is empty.
     */
    @Test
    void testSetNicknameEmpty() {
        Author authorWithNickname = new Author(NICKNAME);
        assertThrows(IllegalArgumentException.class, () -> {
            authorWithNickname.setNickname(ERROR_EMPTY_VALUE);
        });
    }
}