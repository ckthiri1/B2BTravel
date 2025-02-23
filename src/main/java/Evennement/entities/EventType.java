package Evennement.entities;
public enum EventType {
    CONFERENCE,
    WEBINAR,
    TRADE_SHOW,
    WORKSHOP,
    DEFAULT;

    @Override
    public String toString() {
        switch (this) {
            case CONFERENCE:
                return "Conference";
            case WEBINAR:
                return "Webinar";
            case TRADE_SHOW:
                return "Trade Show";
            case WORKSHOP:
                return "Workshop";
            case DEFAULT:
                return "Unknown";
            default:
                return "Unknown";
        }
    }
}