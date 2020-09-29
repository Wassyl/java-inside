package fr.umlv.javainsidelab2;

import static java.util.Objects.requireNonNull;

public record Person( @JSONProperty(field = "first-name") String firstName, @JSONProperty(field = "last-name") String lastName) {
    public Person {
        requireNonNull(firstName);
        requireNonNull(lastName);
    }
}
   