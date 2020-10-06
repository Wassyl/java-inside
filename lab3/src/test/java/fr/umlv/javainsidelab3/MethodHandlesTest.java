package fr.umlv.javainsidelab3;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.junit.jupiter.api.Assertions.*;

public class MethodHandlesTest {

    @Test
    public void  findStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        assertEquals( MethodType.methodType(int.class, String.class) , parseInt.type() );
    }

    @Test
    public void findVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var toUpperCase = lookup.findVirtual(String.class,"toUpperCase", MethodType.methodType(String.class));
        assertEquals( MethodType.methodType(String.class, String.class) , toUpperCase.type() );
    }

    @Test
    public void invokeExactStaticTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        assertEquals( 555 , (int) parseInt.invokeExact( "555") );
    }

    @Test
    public void invokeExactStaticWrongArgumentTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        assertThrows( Throwable.class , () -> parseInt.invokeExact( ) );
    }

    @Test
    public void invokeExactVirtualTest() {

    }
}