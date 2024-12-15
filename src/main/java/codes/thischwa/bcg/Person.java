package codes.thischwa.bcg;

import java.time.LocalDate;

public record Person(String firstName, String lastName, String displayName, LocalDate birthday) {}
