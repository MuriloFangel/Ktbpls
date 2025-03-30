package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class PlayerDeathListener implements Listener {
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent evt) {
    Player player = evt.getEntity();
    evt.setDeathMessage(null);

    if (CacheManager.getCache(evt.getEntity().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      evt.setDroppedExp(0);
      evt.getDrops().clear();
      player.setHealth(20.0);
      
      TheBridge game = profile.getGame(TheBridge.class);
      if (game == null) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), profile::refresh, 3);
      } else {
        List<Profile> hitters = profile.getLastHitters();
        game.kill(profile, hitters.size() > 0 ? hitters.get(0) : null);
        hitters.clear();
      }
    }
  }
}
