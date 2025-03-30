package dev.slickcollections.kiwizin.thebridge.game.enums;

public enum TheBridgeMode {
  SOLO("1v1", 1),
  DUPLA("2v2", 2),
  TRIO("3v3", 3),
  QUARTETO("4v4", 4),
  COMPSOLO("comp1v1", 1),
  COMPDUPLA("comp2v2", 2),
  COMPTRIO("comp3v3", 3),
  COMPQUARTETO("comp4v4", 4),
  PARTY_DUELS("Party Duels", 100),
  COMPPARTY_DUELS("Comp Party Duels", 100);
  
  private static final TheBridgeMode[] VALUES = values();
  private final int size;
  private final String name;
  
  TheBridgeMode(String name, int size) {
    this.name = name;
    this.size = size;
  }
  
  public static TheBridgeMode fromName(String name) {
    for (TheBridgeMode mode : VALUES) {
      if (name.equalsIgnoreCase(mode.name())) {
        return mode;
      }
    }
    
    return null;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public String getName() {
    return this.name;
  }
}