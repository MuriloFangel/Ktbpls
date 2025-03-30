package dev.slickcollections.kiwizin.thebridge.game.enums;

import java.util.Arrays;

public enum TheBridgeModeType {

    CASUAL("Casual"),
    COMPETITIVO("Competitivo");

    public static TheBridgeModeType findByName(String name) {
        return Arrays.stream(TheBridgeModeType.values()).filter(theBridgeModeType -> theBridgeModeType.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private final String name;

    TheBridgeModeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
