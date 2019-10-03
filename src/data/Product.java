package data;

import logic.ConsoleInput;
import logic.UtilityRandom;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.UUID;

abstract public class Product implements ICrudAction {
    public static final HashSet<UUID> identifiers = new HashSet<>();

    private static long object_counter = 0;

    public final UUID id;

    private String name;
    private BigDecimal price;
    private String firmSupplier;
    private String countryManufacture;

    public Product() {
        id = UUID.randomUUID();
        identifiers.add(id);
        object_counter++;
    }

    final UtilityRandom random = new UtilityRandom();
    public void create() {
        name = random.nextString(5, 12);
        firmSupplier = random.nextString(4, 6).toUpperCase();
        countryManufacture = random.nextString(3, 4).toUpperCase();

        //TODO: создать корректную генерацию BigDecimal по диапазону
        price = new BigDecimal(random.nextDouble() * 1000).setScale(2, RoundingMode.FLOOR);
    }

    @Override
    public String read() {
        return toString();
    }


    private static final String[] hints = {"name", "price", "supplier company", "country of manufacture"};

    @Override
    public void update() {
        String[] params = new ConsoleInput(hints).input();

        name = params[0];
        price = new BigDecimal(params[1]);
        firmSupplier = params[2];
        countryManufacture = params[3];
    }

    @Override
    public void delete() {
        name = null;
        price = null;
        countryManufacture = null;
        firmSupplier = null;
        object_counter--;
    }

    public UUID getId() {
        return id;
    }

    public static long getCounter() {
        return object_counter;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getFirmSupplier() {
        return firmSupplier;
    }

    public String getCountryManufacture() {
        return countryManufacture;
    }

    @Override
    public String toString() {
        return "data.Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", firmSupplier='" + firmSupplier + '\'' +
                ", countryManufacture='" + countryManufacture + '\'' +
            '}';
    }
}
