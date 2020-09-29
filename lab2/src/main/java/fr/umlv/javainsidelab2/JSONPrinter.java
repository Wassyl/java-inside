package fr.umlv.javainsidelab2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JSONPrinter {

    private static Object checkException( Method accessor, Record record ){
        try {
            return accessor.invoke(record);
        } catch ( IllegalAccessException e){
            throw (IllegalAccessError) new IllegalAccessError().initCause(e);
        } catch ( InvocationTargetException e){
            var cause = e.getCause();
            if ( cause instanceof RuntimeException re){
                throw re;
            }
            if( cause instanceof Error error){
                throw error;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    public static String toJSON(Record record) {
        var foo = record.getClass();
        var components = foo.getRecordComponents();

        return Arrays.stream(components)
                .map( line -> checkException( line.getAccessor(), record ) )
                .map( Object::toString )
                .collect(Collectors.joining("; ","{","}"));
    }

    public static void main(String[] args) {
        var person = new Person("John", "Doe");
        System.out.println(toJSON(person));
        var alien = new Alien(100, "Saturn");
        System.out.println(toJSON(alien));

    }
}
