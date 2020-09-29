package fr.umlv.javainsidelab2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
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

    private static String getAnnotation(RecordComponent record){
        return record.isAnnotationPresent( JSONProperty.class) ? record.getAccessor().getName().replace('_','-') : record.getAccessor().getName();
    }

    public static String toJSON(Record record) {
        var components = record.getClass().getRecordComponents();

        return Arrays.stream(components)
                .map( line ->
                            """
                            "%s" : "%s"
                            """.formatted( getAnnotation(line)  , checkException( line.getAccessor(), record ).toString() )
                )
                .collect(Collectors.joining(", ","{","}"));
    }

}
