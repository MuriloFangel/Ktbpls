package dev.slickcollections.kiwizin.thebridge.cosmetics.types.perks;

import dev.slickcollections.kiwizin.thebridge.cosmetics.types.Perk;
import org.bukkit.configuration.ConfigurationSection;

public class VidaExtra extends Perk {
  
  private final int health;
  
  public VidaExtra(ConfigurationSection section) {
    super(1, "vida_extra", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
    this.health = section.getInt("health");
  }
  
  public int getHealth() {
    return this.health * 2;
  }
}
