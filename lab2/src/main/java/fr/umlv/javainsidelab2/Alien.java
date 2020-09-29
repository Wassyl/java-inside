package fr.umlv.javainsidelab2;

import static java.util.Objects.requireNonNull;

public record Alien(int age, String planet) {
    public Alien {
        if (age < 0) {
            throw new IllegalArgumentException("negative age");
        }
        requireNonNull(planet);
    }
}
