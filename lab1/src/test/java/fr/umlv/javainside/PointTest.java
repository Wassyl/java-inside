package fr.umlv.javainside;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    public void testCoordinates() {
        var point = new Point( 5,3);
        assertEquals( 5, point.x() );
        assertEquals( 3, point.y() );
    }
}