package dev.slickcollections.kiwizin.thebridge.cosmetics.types.winanimations;

import dev.slickcollections.kiwizin.thebridge.cosmetics.object.AbstractExecutor;
import dev.slickcollections.kiwizin.thebridge.cosmetics.object.winanimations.CartExecutor;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.WinAnimation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Cart extends WinAnimation {
  
  public Cart(ConfigurationSection section) {
    super(section.getLong("id"), "cowboy", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
  }
  
  public AbstractExecutor execute(Player player) {
    return new CartExecutor(player);
  }
}
