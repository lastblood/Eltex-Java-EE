package data;

import logic.UtilityRandom;

import java.util.UUID;

public class Credentials {
    public final UUID id;

    public Credentials(UUID id) {
        this.id = id;
    }

    public Credentials() {
        id = UUID.randomUUID();
    }


    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Credentials getRandomInstance() {
        UtilityRandom random = new UtilityRandom();
        Credentials c = new Credentials();
        c.firstName = random.nextString(2,6);
        c.lastName = random.nextString(3,9);
        c.patronymic = random.nextString(5,7);
        c.email = random.nextString(5,8) + "@" + random.nextString(3,5) + "." + random.nextString(2,5);
        return c;
    }
}
