package PageTests.PositiveTests;

import edu.ntnu.iir.bidata.models.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Positive tests for the Page class.
 *
 * <ul>
 *   <li>Constructor
 *     <ul>
 *       <li>testValidPage: Tests constructor with valid title and text</li>
 *     </ul>
 *   </li>
 *
 *   <li>Getters
 *     <ul>
 *       <li>testGetTitle: Tests getTitle returns current title</li>
 *       <li>testGetText: Tests getText returns current text</li>
 *     </ul>
 *   </li>
 *
 *   <li>Setters
 *     <ul>
 *       <li>testSetTitle: Tests setTitle updates title correctly</li>
 *       <li>testSetText: Tests setText updates text correctly</li>
 *     </ul>
 *   </li>
 * </ul>
 */
class PagePositiveTest {

    private Page page;

    private final String TITLE_1 = "Went out with a friend";
    private final String TITLE_2 = "Went to the university";
    private final String TITLE_3 = "Ate with a friend at a restaurant";

    private final String TEXT_1 = "I went out today and had to hang out with someone I don't really like...";
    private final String TEXT_2 = "Today I actually went to university. It's boring, I'd rather hang out in the city.";
    private final String TEXT_3 = "I actually went on a date with someone, but she didn't like me, so we agreed to just be friends.";

    @BeforeEach
    void setUp() {
        page = new Page(TITLE_1, TEXT_1);
    }

    /**
     * Tests constructor with valid title and text.
     */
    @Test
    void testValidPage() {
        assertEquals(TITLE_1, page.getTitle());
        assertEquals(TEXT_1, page.getText());
    }

    /**
     * Tests getTitle returns the current title.
     */
    @Test
    void testGetTitle() {
        assertEquals(TITLE_1, page.getTitle());
    }

    /**
     * Tests getText returns the current text.
     */
    @Test
    void testGetText() {
        assertEquals(TEXT_1, page.getText());
    }

    /**
     * Tests setTitle updates the title correctly.
     */
    @Test
    void testSetTitle() {
        page.setTitle(TITLE_2);
        assertEquals(TITLE_2, page.getTitle());
    }

    /**
     * Tests setText updates the text correctly.
     */
    @Test
    void testSetText() {
        page.setText(TEXT_2);
        assertEquals(TEXT_2, page.getText());
    }


}
