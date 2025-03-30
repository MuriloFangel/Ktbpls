package dev.slickcollections.kiwizin.thebridge.cosmetics.object;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.cosmetics.Cosmetic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractPreview<T extends Cosmetic> extends BukkitRunnable {
  
  public static final KConfig CONFIG = Main.getInstance().getConfig("previewLocations");
  protected Player player;
  protected T cosmetic;
  
  public AbstractPreview(Profile profile, T cosmetic) {
    this.player = profile.getPlayer();
    this.cosmetic = cosmetic;
  }
  
  public static boolean canDoKillEffect() {
    return CONFIG.getSection("killeffect") != null && CONFIG.getSection("killeffect").getKeys(false).size() > 2;
  }
  
  public abstract void returnToMenu();
}
