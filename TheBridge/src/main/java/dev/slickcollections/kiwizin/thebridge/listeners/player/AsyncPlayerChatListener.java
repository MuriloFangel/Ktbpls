package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AsyncPlayerChatListener implements Listener {
  
  private static final Map<String, Long> flood = new HashMap<>();
  
  private static final DecimalFormat df = new DecimalFormat("###.#");

  private static final Map<String, TheBridge> ENDING_CACHE = new HashMap<>();

  public static void addCacheGaming(String key, TheBridge game) {
    ENDING_CACHE.put(key, game);
  }

  public static String findKeyByGame(TheBridge game) {
    return ENDING_CACHE.keySet().stream().filter(s -> ENDING_CACHE.get(s).equals(game)).findFirst().orElse(null);
  }

  public static TheBridge findGameByKey(String key) {
    return ENDING_CACHE.get(key);
  }

  public static void clearCache(TheBridge game) {
    String key = findKeyByGame(game);
    if (key != null) {
      ENDING_CACHE.remove(key);
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    flood.remove(evt.getPlayer().getName());
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void AsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    if (!player.hasPermission("kthebridge.chat.delay")) {
      long start = flood.containsKey(player.getName()) ? flood.get(player.getName()) : 0;
      if (start > System.currentTimeMillis()) {
        double time = (start - System.currentTimeMillis()) / 1000.0;
        if (time > 0.1) {
          evt.setCancelled(true);
          String timeString = df.format(time).replace(",", ".");
          if (timeString.endsWith("0")) {
            timeString = timeString.substring(0, timeString.lastIndexOf("."));
          }
          
          player.sendMessage(Language.chat$delay.replace("{time}", timeString));
          return;
        }
      }
      
      flood.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3));
    }
    
    Role role = Profile.getProfile(player.getName()).getTagContainer().getSelectedRole();
    if (player.hasPermission("kthebridge.chat.color")) {
      evt.setMessage(StringUtils.formatColors(evt.getMessage()));
    }

    TheBridge game = profile.getGame(TheBridge.class);
    String suffix = "";
    DataContainer container = profile.getDataContainer("kCoreProfile", "clan");
    if (container != null) {
      suffix = container.getAsString().replace(" ", "") + " ";
      if (suffix.contains("§8")) {
        suffix = "";
      }
    }
    if (game == null || !game.isSpectator(player)) {
      evt.setFormat(Language.chat$format$lobby.replace("{player}", role.getPrefix() + "%s")
          .replace("{color}", role.isDefault() ? Language.chat$color$default : Language.chat$color$custom).replace("{message}", "%s"));
    } else {
      evt.setFormat(Language.chat$format$spectator.replace("{player}", role.getPrefix() + "%s")
          .replace("{color}", role.isDefault() ? Language.chat$color$default : Language.chat$color$custom).replace("{message}", "%s"));
    }
    evt.getRecipients().clear();
    evt.setFormat((suffix.equals(" ") ? "" : suffix) + "§r" + evt.getFormat());
    for (Player players : player.getWorld().getPlayers()) {
      evt.getRecipients().add(players);
    }
  }
}