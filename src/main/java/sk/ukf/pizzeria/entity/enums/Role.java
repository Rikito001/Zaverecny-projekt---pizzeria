package sk.ukf.pizzeria.entity.enums;

public enum Role {
    ZAKAZNIK("Zakaznik"),
    KUCHAR("Kuchar"),
    KURIER("Kurier"),
    ADMIN("Administrator");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
