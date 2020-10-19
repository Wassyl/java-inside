package fr.umlv.javainsidelab4;

import fr.umlv.javainside.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTests {

    @Test
    public void of(){
        class Foo{}
        var logger = Logger.of(Foo.class, __ -> {});
        assertNotNull(logger);
    }

    @Test
    public void ofError(){
        class Foo{}
        // TODO
        var logger = Logger.of(Foo.class, __ -> {});
    }

    @Test
    public void log(){
        class Foo{}
        var logger = Logger.of(Foo.class, message -> {
            assertEquals("test",message);
        });
        logger.log("test");
    }

    @Test
    public void logWithNullAsValue(){
        class Foo{}
        var logger = Logger.of(Foo.class, Assertions::assertNull);
        logger.log(null);
    }

}
