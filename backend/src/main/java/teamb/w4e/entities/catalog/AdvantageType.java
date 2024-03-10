package teamb.w4e.entities.catalog;

public enum AdvantageType {
    COCKTAIL("Cocktail"),
    VIP("VIP"),
    LOCAL_SPECIALITY("Local speciality"),
    REDUCTION("Reduction");
    private final String name;

    AdvantageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
