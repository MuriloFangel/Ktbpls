package dev.slickcollections.kiwizin.thebridge.cosmetics;


public enum CosmeticType {
  PERK("Habilidades"),
  KILL_EFFECT("Efeitos de Abate"),
  DEATH_CRY("Gritos de Mortre"),
  BLOCK("Blocos"),
  DEATH_MESSAGE("Mensagens de Morte"),
  CAGE("Jaulas"),
  FALL_EFFECT("Efeitos de Queda"),
  PROJECTILE_EFFECT("Efeitos de Projétil"),
  BALLOON("Balões"),
  WIN_ANIMATION("Comemorações de Vitória");
  
  private final String[] names;
  
  CosmeticType(String... names) {
    this.names = names;
  }
  
  public String getName(long index) {
    return this.names[(int) (index - 1)];
  }
}
