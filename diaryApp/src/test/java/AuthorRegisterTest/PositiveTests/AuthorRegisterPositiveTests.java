package AuthorRegisterTest.PositiveTests;

import edu.ntnu.iir.bidata.models.Author;
import edu.ntnu.iir.bidata.registers.AuthorRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorRegisterPositiveTests {

    private AuthorRegister register;

    private Author authorFull;
    private Author authorNick;

    private static final String FIRST_NAME_1 = "Robert";
    private static final String FIRST_NAME_2 = "Ola";
    private static final String FIRST_NAME_3 = "Rob";
    private static final String FIRST_NAME_4 = "Another";

    private static final String LAST_NAME_1 = "Larsen";
    private static final String LAST_NAME_2 = "Nordmann";
    private static final String LAST_NAME_3 = "Lars";

    private static final String NICKNAME_1 = "robelar";
    private static final String NICKNAME_2 = "olar";
    private static final String NICKNAME_3 = "nick3";
    private static final String NICKNAME_4 = "nick4";
    private static final String NICKNAME_5 = "Robo";
    private static final String NICKNAME_6 = "Robster";

    private static final String FULL_NAME_1 = FIRST_NAME_1 + " " + LAST_NAME_1;
    private static final String FULL_NAME_2 = FIRST_NAME_1 + " " + LAST_NAME_1 + "(" + NICKNAME_1 + ")";
    private static final String FULL_NAME_3 = FIRST_NAME_1 + " " + LAST_NAME_1 + "(" + NICKNAME_5 + ")";
    private static final String FULL_NAME_4 = FIRST_NAME_3 + " " + LAST_NAME_3 + "(" + NICKNAME_1 + ")";
    private static final String FULL_NAME_5 = FIRST_NAME_3 + " " + LAST_NAME_3 + "(" + NICKNAME_6 + ")";

    @BeforeEach
    void setUp() {
        register = new AuthorRegister();
        authorFull = new Author(FIRST_NAME_1, LAST_NAME_1, NICKNAME_1);
        authorNick = new Author(NICKNAME_2);
    }

    @Test
    void testAddAuthorWithFullNameAndNickname() {
        register.addAuthor(authorFull);
        assertTrue(register.hasAuthor(FULL_NAME_2));
        Optional<Author> author = register.getAuthor(FULL_NAME_2);
        assertTrue(author.isPresent());
        assertEquals(FIRST_NAME_1, author.get().getFirstName());
        assertEquals(LAST_NAME_1, author.get().getLastName());
        assertEquals(NICKNAME_1, author.get().getNickname());
    }

    @Test
    void testAddAuthorWithNicknameOnly() {
        register.addAuthor(authorNick);
        assertTrue(register.hasAuthor(NICKNAME_2));
        Optional<Author> author = register.getAuthor(NICKNAME_2);
        assertTrue(author.isPresent());
        assertEquals(NICKNAME_2, author.get().getNickname());
    }

    @Test
    void testHasAuthorFullNameOnly() {
        Author author = new Author(FIRST_NAME_1, LAST_NAME_1);
        register.addAuthor(author);
        assertTrue(register.hasAuthor(FULL_NAME_1));
        Optional<Author> retrieved = register.getAuthor(FULL_NAME_1);
        assertTrue(retrieved.isPresent());
        assertEquals(FIRST_NAME_1, retrieved.get().getFirstName());
        assertEquals(LAST_NAME_1, retrieved.get().getLastName());
        assertNull(retrieved.get().getNickname());
    }

    @Test
    void testUpdateNameFirstAndLastOnly() {
        register.addAuthor(authorFull);
        Optional<Author> author = register.updateName(FULL_NAME_2, FIRST_NAME_3, LAST_NAME_3);
        assertTrue(author.isPresent());
        assertEquals(FIRST_NAME_3, author.get().getFirstName());
        assertEquals(LAST_NAME_3, author.get().getLastName());
        assertEquals(NICKNAME_1, author.get().getNickname());
        assertTrue(register.hasAuthor(FULL_NAME_4));
    }

    @Test
    void testUpdateNameNicknameOnly() {
        register.addAuthor(authorFull);
        Optional<Author> author = register.updateName(FULL_NAME_2, NICKNAME_5);
        assertTrue(author.isPresent());
        assertEquals(FIRST_NAME_1, author.get().getFirstName());
        assertEquals(LAST_NAME_1, author.get().getLastName());
        assertEquals(NICKNAME_5, author.get().getNickname());
        assertTrue(register.hasAuthor(FULL_NAME_3));
    }

    @Test
    void testUpdateNameAllFields() {
        register.addAuthor(authorFull);
        Optional<Author> author = register.updateName(FULL_NAME_2, FIRST_NAME_3, LAST_NAME_3, NICKNAME_6);
        assertTrue(author.isPresent());
        assertEquals(FIRST_NAME_3, author.get().getFirstName());
        assertEquals(LAST_NAME_3, author.get().getLastName());
        assertEquals(NICKNAME_6, author.get().getNickname());
        assertTrue(register.hasAuthor(FULL_NAME_5));
    }

    @Test
    void testRemoveAuthor() {
        register.addAuthor(authorFull);
        Optional<Author> author = register.removeAuthor(FULL_NAME_2);
        assertTrue(author.isPresent());
        assertEquals(FULL_NAME_2, author.get().getName());
        assertFalse(register.hasAuthor(FULL_NAME_2));
    }

    @Test
    void testFindByFirstName() {
        register.addAuthor(authorFull);
        register.addAuthor(new Author(FIRST_NAME_1, LAST_NAME_3, NICKNAME_4));
        register.addAuthor(new Author(FIRST_NAME_2, LAST_NAME_2, NICKNAME_3));
        Iterator<Author> it = register.findByFirstName(FIRST_NAME_1,false);
        int count = 0;
        while (it.hasNext()) {
            Author author = it.next();
            assertEquals(FIRST_NAME_1, author.getFirstName());
            count++;
        }
        assertEquals(2, count);
    }
}
