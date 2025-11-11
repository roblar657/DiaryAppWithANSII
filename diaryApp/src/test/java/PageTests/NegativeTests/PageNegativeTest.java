package PageTests.NegativeTests;

import edu.ntnu.iir.bidata.models.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Negative tests for the Page class.
 *
 * <ul>
 *   <li>Constructor
 *     <ul>
 *       <li>testConstructorWithNullTitle: Tests constructor throws when title is null</li>
 *       <li>testConstructorWithEmptyTitle: Tests constructor throws when title is empty</li>
 *       <li>testConstructorWithBlankTitle: Tests constructor throws when title is blank</li>
 *       <li>testConstructorWithNullText: Tests constructor throws when text is null</li>
 *     </ul>
 *   </li>
 *
 *   <li>Setters
 *     <ul>
 *       <li>testSetTitleNull: Tests setTitle throws when title is null</li>
 *       <li>testSetTitleEmpty: Tests setTitle throws when title is empty</li>
 *       <li>testSetTitleBlank: Tests setTitle throws when title is blank</li>
 *       <li>testSetTextNull: Tests setText throws when text is null</li>
 *     </ul>
 *   </li>
 * </ul>
 */
class PageNegativeTest {

    private Page page;

    private final String TITLE_1 = "Went out with a friend";
    private final String TITLE_2 = "Went to the university";
    private final String TITLE_3 = "Ate with a friend at a restaurant";

    private final String TEXT_1 = "I went out today and had to hang out with someone I don't really like...";
    private final String TEXT_2 = "Today I actually went to university. It's boring, I'd rather hang out in the city.";
    private final String TEXT_3 = "I actually went on a date with someone, but she didn't like me, so we agreed to just be friends.";

    // Error values for negative tests
    private final String ERROR_NULL_VALUE = null;
    private final String ERROR_BLANK_VALUE = "   ";
    private final String ERROR_EMPTY_VALUE = "";

    @BeforeEach
    void setUp() {
        page = new Page(TITLE_1, TEXT_1);
    }

    /**
     * Tests constructor throws when title is null.
     */
    @Test
    void testConstructorWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Page(ERROR_NULL_VALUE, TEXT_1);
        });
    }

    /**
     * Tests constructor throws when title is empty.
     */
    @Test
    void testConstructorWithEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Page(ERROR_EMPTY_VALUE, TEXT_1);
        });
    }

    /**
     * Tests constructor throws when title is blank.
     */
    @Test
    void testConstructorWithBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Page(ERROR_BLANK_VALUE, TEXT_1);
        });
    }

    /**
     * Tests constructor throws when text is null.
     */
    @Test
    void testConstructorWithNullText() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Page(TITLE_1, ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests setTitle throws when title is null.
     */
    @Test
    void testSetTitleNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            page.setTitle(ERROR_NULL_VALUE);
        });
    }

    /**
     * Tests setTitle throws when title is empty.
     */
    @Test
    void testSetTitleEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            page.setTitle(ERROR_EMPTY_VALUE);
        });
    }

    /**
     * Tests setTitle throws when title is blank.
     */
    @Test
    void testSetTitleBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            page.setTitle(ERROR_BLANK_VALUE);
        });
    }

    /**
     * Tests setText throws when text is null.
     */
    @Test
    void testSetTextNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            page.setText(ERROR_NULL_VALUE);
        });
    }
}
