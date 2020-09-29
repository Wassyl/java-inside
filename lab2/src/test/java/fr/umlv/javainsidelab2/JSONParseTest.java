package fr.umlv.javainsidelab2;

import org.junit.jupiter.api.Test;

import static fr.umlv.javainsidelab2.JSONParse.parse;
import static fr.umlv.javainsidelab2.JSONPrinter.toJSON;
import static org.junit.jupiter.api.Assertions.*;

public class JSONParseTest {

    @Test
    public void failingPerson() {
        assertThrows( NullPointerException.class, () -> new Person(null, "Doe") );
        assertThrows( NullPointerException.class, () -> new Person("John", null) );
    }

    @Test
    public void failingAlien() {
        assertThrows( NullPointerException.class, () -> new Alien(100, null) );
        assertThrows( IllegalArgumentException.class, () -> new Alien(-15, "Saturn") );
    }

    @Test
    public void isRecordPerson() {
        assertTrue( Person.class.isRecord() );
    }

    @Test
    public void isRecordAlien() {
        assertTrue( Alien.class.isRecord() );
    }

    @Test
    public void testingPerson() {
        assertEquals( parse("{ \"first-name\": \"John\", \"last-name\": \"Doe\" }").toString(), parse(toJSON(new Person("John", "Doe"))).toString() );
    }

    @Test
    public void testingAlien() {
        assertEquals( parse("{ \"age\": 100, \"planet\": \"Saturn\" }").toString(), parse(toJSON(new Alien(100, "Saturn"))).toString() );
    }
}