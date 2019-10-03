package data;

import logic.ConsoleInput;

public class CoffeeProduct extends Product {
    public enum BEAN_TYPES {
        ARABICA,
        ROBUSTA
    }

    private BEAN_TYPES beans;

    @Override
    public void create() {
        super.create();
        beans = BEAN_TYPES.values()[random.nextInt(2)];
    }

    @Override
    public void delete() {
        beans = null;
        super.delete();
    }

    private static final String[] hints = {"beans type (Arabica, Robusta)"};
    @Override
    public void update() {
        String t = new ConsoleInput(hints).input()[0];

        switch(t.toLowerCase()) {
            case "a":
            case "arabica":
                beans = BEAN_TYPES.ARABICA;
                break;
            case "r":
            case "robusta":
                beans = BEAN_TYPES.ROBUSTA;
                break;
            default:
                throw new IllegalArgumentException("Unknown sort of beans: " + t);
        }

        super.update();
    }

    @Override
    public String toString() {
        return "data.CoffeeProduct{" +
                "beans=" + beans +
                "} " + super.toString();
    }

    public BEAN_TYPES getBeans() {
        return beans;
    }
}
