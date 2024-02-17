package teamb.w4e.entities;

public enum AdvantageType {
    COCKTAIL("Cocktail"),
    VIP("VIP"),
    LOCAL_SPECIALITY("Local speciality"),
    REDUCTION("Reduction");
    private String name;

    private AdvantageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
