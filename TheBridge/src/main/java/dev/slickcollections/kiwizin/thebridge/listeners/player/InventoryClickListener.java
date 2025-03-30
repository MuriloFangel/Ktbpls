package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.cmd.tb.BuildCommand;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      if (CacheManager.getCache(evt.getWhoClicked().getName(), RemoveProtectionCache.class) != null) {
        return;
      }

      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.getProfile(player.getName());
      
      if (profile != null) {
        TheBridge game = profile.getGame(TheBridge.class);
        evt.setCancelled(game == null && !BuildCommand.hasBuilder(player));
      }
    }
  }
}
