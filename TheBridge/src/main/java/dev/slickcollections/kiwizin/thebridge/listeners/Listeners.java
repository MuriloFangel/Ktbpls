package dev.slickcollections.kiwizin.thebridge.listeners;

import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.listeners.entity.EntityListener;
import dev.slickcollections.kiwizin.thebridge.listeners.player.*;
import dev.slickcollections.kiwizin.thebridge.listeners.server.ServerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Listeners {
  
  public static void setupListeners() {
    try {
      PluginManager pm = Bukkit.getPluginManager();
      
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new EntityListener(), Main.getInstance());
      
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new AsyncPlayerChatListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new InventoryClickListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerDeathListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerInteractListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerJoinListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerQuitListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerRestListener(), Main.getInstance());
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new PlayerTagChangeListeners(), Main.getInstance());
      
      pm.getClass().getDeclaredMethod("registerEvents", Listener.class, Plugin.class).invoke(pm, new ServerListener(), Main.getInstance());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
