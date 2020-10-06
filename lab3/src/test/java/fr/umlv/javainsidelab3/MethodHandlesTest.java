package fr.umlv.javainsidelab3;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;

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

    @Test
    public void asTypeAndInvokeExactStaticTest() throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var parseInt = lookup.findStatic(Integer.class,"parseInt", MethodType.methodType(int.class,String.class));
        var asType = parseInt.asType( MethodType.methodType( Integer.class, String.class ) );
        assertAll(
                () -> assertEquals( 555 , (Integer) asType.invokeExact("555")),
                () -> assertThrows( WrongMethodTypeException.class , () -> {
                    var s = asType.invokeExact("555");
                })
        );
    }

    @Test
    public void invokeExactConstantTest() throws Throwable {
        assertEquals( 42 , (Integer) MethodHandles.constant(Integer.class,42).invokeExact());
    }

    private static MethodHandle match(String str) throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var equals = lookup.findVirtual(String.class,"equals", MethodType.methodType(boolean.class, Object.class));
        var test = MethodHandles.insertArguments(equals, 1, str  );
        var target = MethodHandles.dropArguments( MethodHandles.constant(int.class,1), 0 , String.class );
        var fallback = MethodHandles.dropArguments( MethodHandles.constant(int.class,-1), 0 , String.class );
        return MethodHandles.guardWithTest( test, target, fallback );
    }

    @Test
    public void matchTest() throws NoSuchMethodException, IllegalAccessException {
        var testMatch = match("Bonjour");
        assertAll(
                () -> assertEquals( 1 , (int) testMatch.invokeExact("Bonjour")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("Echec hehe"))
        );
    }

    private MethodHandle matchAll( List<String> strings ) throws NoSuchMethodException, IllegalAccessException {
        var lookup = MethodHandles.lookup();
        var equals = lookup.findVirtual(String.class,"equals", MethodType.methodType(boolean.class, Object.class));
        var target = MethodHandles.dropArguments( MethodHandles.constant(int.class,-1), 0 , String.class );
        int i = 0;
        for(var str:strings) {
            var test = MethodHandles.insertArguments(equals, 1, str );
            var ok= MethodHandles.dropArguments(MethodHandles.constant(int.class, i),0,String.class);
            target = MethodHandles.guardWithTest( test,ok,target );
            i++;
        }
        return target;
    }

    @Test
    public void matchAllTest() throws NoSuchMethodException, IllegalAccessException {
        var testMatch = matchAll(List.of("Bonjour","hello","kurwa mac"));
        assertAll(
                () -> assertEquals( 0 , (int) testMatch.invokeExact("Bonjour")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("yes")),
                () -> assertEquals( 1 , (int) testMatch.invokeExact("hello")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("Non")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("Rebonjour")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("Vu du ciel")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("l'océan")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("ça fait moins peur")),
                () -> assertEquals( -1 , (int) testMatch.invokeExact("Ah okay hehe, faut pas toucher")),
                () -> assertEquals( 2 , (int) testMatch.invokeExact("kurwa mac"))
        );
    }
}