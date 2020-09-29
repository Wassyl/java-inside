package fr.umlv.javainsidelab2;

import org.junit.jupiter.api.Test;

import static fr.umlv.javainsidelab2.JSONParse.parse;
import static fr.umlv.javainsidelab2.JSONPrinter.toJSON;
import static org.junit.jupiter.api.Assertions.*;

public class JSONParseTest {

    @Test
    public void testingOftoJSON() {
        assertEquals( parse("{ \"firstName\": \"John\", \"lastName\": \"Doe\" }").toString(), parse(toJSON(new Person("John", "Doe"))).toString() );
        assertEquals( parse("{ \"age\": 100, \"planet\": \"Saturn\" }").toString(), parse(toJSON(new Alien(100, "Saturn"))).toString() );
    }
}