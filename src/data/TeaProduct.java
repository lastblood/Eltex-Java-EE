package data;

import logic.utils.ConsoleInput;

public class TeaProduct extends Product {
    private String packageType;

    @Override
    public void create() {
        super.create();

        packageType = random.nextString(3,8);
    }

    @Override
    public void delete() {
        packageType = null;
        super.delete();
    }

    private static final String[] hints = {"type of package"};
    @Override
    public void update() {
        packageType = new ConsoleInput(hints).input()[0];

        super.update();
    }

    @Override
    public String toString() {
        return "data.TeaProduct{" +
                "packageType='" + packageType + '\'' +
                "} " + super.toString();
    }

    public String getPackageType() {
        return packageType;
    }
}
