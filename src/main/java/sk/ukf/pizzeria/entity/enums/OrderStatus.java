package sk.ukf.pizzeria.entity.enums;

public enum OrderStatus {
    CAKAJUCA("Cakajuca"),
    PRIPRAVUJE_SA("Pripravuje sa"),
    HOTOVA("Hotova"),
    DORUCUJE_SA("Dorucuje sa"),
    DORUCENA("Dorucena"),
    ZRUSENA("Zrusena");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isCancellable() {
        return this == CAKAJUCA;
    }

    public boolean isActive() {
        return this != DORUCENA && this != ZRUSENA;
    }
}
