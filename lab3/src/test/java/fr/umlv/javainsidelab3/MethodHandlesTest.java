package fr.umlv.javainsidelab3;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;

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
        assertThrows( WrongMethodTypeException.class , () -> parseInt.invokeExact( ) );
    }

    @Test
    public void invokeExactVirtualTest() throws Throwable {
        var lookup = MethodHandles.lookup();
        var toUpperCase = lookup.findVirtual(String.class,"toUpperCase", MethodType.methodType(String.class));
        assertEquals( "BONJOUR" , (String) toUpperCase.invokeExact( "Bonjour") );
    }

    @Test
    public void invokeExactVirtualWrongArgumentTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var toUpperCase = lookup.findVirtual(String.class,"toUpperCase", MethodType.methodType(String.class));
        assertThrows( WrongMethodTypeException.class , () -> toUpperCase.invokeExact( ) );
    }

    /////////////////////

    @Test
    public void invokeStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        assertAll(
                () -> assertEquals( 555 , (Integer) parseInt.invoke("555")),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = (String) parseInt.invoke("555");
                })
        );
    }

    @Test
    public void invokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var toUpperCase = lookup.findVirtual(String.class,"toUpperCase", MethodType.methodType(String.class));
        assertAll(
                () -> assertEquals( "BONJOUR" , (String) toUpperCase.invoke("Bonjour")),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = (double) toUpperCase.invoke("Bonjour");
                })
        );
    }

    @Test
    public void insertAndInvokeStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        var meth = MethodHandles.insertArguments( parseInt, 0 , "123" );
        assertAll(
                () -> assertEquals( 123 , meth.invoke() ),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = meth.invoke("abc");
                })
        );
    }

    @Test
    public void bindToAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        var meth = parseInt.bindTo( "123" );
        assertAll(
                () -> assertEquals( 123 , meth.invoke() ),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = meth.invoke("abc");
                })
        );
    }

    @Test
    public void dropAndInvokeStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        var meth1 = MethodHandles.dropArguments( parseInt, 0 , int.class );
        var meth2 = MethodHandles.dropArguments( parseInt, 0 , int.class );
        assertAll(
                () -> assertEquals( 123 , parseInt.invoke("123") ),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = meth1.invoke("123");
                }),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = meth2.invoke("abc");
                })
        );
    }

    @Test
    public void dropAndInvokeVirtualTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var toUpperCase = lookup.findVirtual(String.class,"toUpperCase", MethodType.methodType(String.class));
        var meth2 = MethodHandles.dropArguments( toUpperCase, 0 , int.class );
        assertAll(
                () -> assertEquals( "BONJOUR" , toUpperCase.invoke("Bonjour") ),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = meth2.invoke("abc");
                })
        );
    }
}