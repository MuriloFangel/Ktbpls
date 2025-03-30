package dev.slickcollections.kiwizin.thebridge;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.libraries.MinecraftVersion;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.thebridge.cmd.Commands;
import dev.slickcollections.kiwizin.thebridge.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.hook.TBCoreHook;
import dev.slickcollections.kiwizin.thebridge.listeners.Listeners;
import dev.slickcollections.kiwizin.thebridge.lobby.*;
import dev.slickcollections.kiwizin.thebridge.utils.tagger.TagUtils;
import dev.slickcollections.kiwizin.utils.BukkitUtils;

import java.io.File;
import java.io.FileInputStream;

public class Main extends KPlugin {

  public static String currentServerName;
  private static Main instance;
  private static boolean validInit;
  
  public static Main getInstance() {
    return instance;
  }
  
  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {
  }
  
  @Override
  public void enable() {
    if (MinecraftVersion.getCurrentVersion().getCompareId() != 183) {
      this.setEnabled(false);
      this.getLogger().warning("O plugin apenas funciona na versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }
    
    saveDefaultConfig();
    currentServerName = getConfig().getString("lobby");
    if (getConfig().getString("spawn") != null) {
      Core.setLobby(BukkitUtils.deserializeLocation(getConfig().getString("spawn")));
    }
    
    TheBridge.setupGames();
    
    Language.setupLanguage();
    Listeners.setupListeners();
  
    TBCoreHook.setupHook();
    Lobby.setupLobbies();
    Cosmetic.setupCosmetics();
    
    PlayNPC.setupNPCs();
    StatsNPC.setupNPCs();
    DeliveryNPC.setupNPCs();
    Leaderboard.setupLeaderboards();
    
    Commands.setupCommands();
    
    validInit = true;
  }
  
  @Override
  public void disable() {
    if (validInit) {
      PlayNPC.listNPCs().forEach(PlayNPC::destroy);
      DeliveryNPC.listNPCs().forEach(DeliveryNPC::destroy);
      Leaderboard.listLeaderboards().forEach(Leaderboard::destroy);
      
      TagUtils.reset();
    }
    
    File update = new File("plugins/kTheBridge/update", "kTheBridge.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/" + update.getName()));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/" + update.getName()));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do kTheBridge aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }
}