package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.cmd.tb.BuildCommand;
import dev.slickcollections.kiwizin.thebridge.cmd.tb.CreateCommand;
import dev.slickcollections.kiwizin.thebridge.utils.tagger.TagUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    evt.setQuitMessage(null);
    BuildCommand.remove(evt.getPlayer());
    TagUtils.reset(evt.getPlayer().getName());
    CreateCommand.CREATING.remove(evt.getPlayer());
    CacheManager.clearCache(evt.getPlayer().getName(), RemoveProtectionCache.class);
  }
}
