package fr.umlv.javainsidelab2;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSONPrinter {

    private static String escape(Object o) {
        return o instanceof String ? "\"" + o +"\"" : String.valueOf(o);
    }

    private static class Cache extends ClassValue<List<Function<Record,String>>>{
        @Override
        protected List<Function<Record,String>> computeValue(Class<?> type) {
            return Arrays.stream( type.getRecordComponents() )
                    .<Function<Record,String>> map( component -> {
                        var prefix = "\""+checkAnnotation(component)+"\" : ";
                        return record -> prefix+escape(invokeAccessor(component.getAccessor(), record));
                    })
                    .collect( Collectors.toList());
        }
    }
    private static final Cache CACHE = new Cache();

    private static Object invokeAccessor(Method accessor, Record record ){
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

    private static String checkAnnotation(RecordComponent record){
        return record.isAnnotationPresent( JSONProperty.class) ? record.getAccessor().getName().replace('_','-') : record.getAccessor().getName();
    }

    public static String toJSON(Record record) {
        var components = CACHE.get( record.getClass() );

        return components.stream()
                .map( line -> line.apply(record) )
                .collect(Collectors.joining(", ","{","}"));
    }

}
