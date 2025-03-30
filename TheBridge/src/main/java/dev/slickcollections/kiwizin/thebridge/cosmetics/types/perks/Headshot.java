package dev.slickcollections.kiwizin.thebridge.cosmetics.types.perks;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.Perk;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class Headshot extends Perk {
  
  private final int percentage;
  private final int level;
  private final int time;
  
  public Headshot(ConfigurationSection section) {
    super(2, "headshot", section.getDouble("coins"), section.getString("permission"), section.getString("name"), section.getString("icon"));
    this.percentage = section.getInt("percentage");
    this.level = section.getInt("level");
    this.time = section.getInt("time");
    this.register();
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    if (evt.getEntity() instanceof Player && evt.getDamager() instanceof Arrow) {
      Player damaged = (Player) evt.getEntity();
      Arrow arrow = (Arrow) evt.getDamager();
      
      if (arrow.getShooter() instanceof Player) {
        Player player = (Player) arrow.getShooter();
        
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
          TheBridge game = profile.getGame(TheBridge.class);
          if (game != null && this.isSelected(profile)) {
            if (isHeadShot(damaged, arrow) && ThreadLocalRandom.current().nextInt(100) < this.percentage) {
              player.sendMessage("§6[Headshot] §aVocê aplicou naúsea no seu oponente!");
              damaged.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, this.time, this.level - 1));
            }
          }
        }
      }
    }
  }
  
  private boolean isHeadShot(Player player, Arrow arrow) {
    double arrowY = arrow.getLocation().getY();
    double damagedY = player.getLocation().getY();
    return arrowY - damagedY > 1.4D;
  }
}
