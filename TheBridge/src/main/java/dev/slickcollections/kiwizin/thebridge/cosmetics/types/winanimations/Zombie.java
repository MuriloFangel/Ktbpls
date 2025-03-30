package dev.slickcollections.kiwizin.thebridge.cosmetics.types.winanimations;

import dev.slickcollections.kiwizin.thebridge.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.thebridge.cosmetics.object.winanimations.ZombieExecutor;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Zombie extends WinAnimation {
  
  public Zombie(ConfigurationSection section) {
    super(section.getLong("id"), "zombie", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new ZombieExecutor(player);
  }
}
