package dev.slickcollections.kiwizin.thebridge.hook;

import com.comphenix.protocol.ProtocolLibrary;
import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.achievements.Achievement;
import dev.slickcollections.kiwizin.achievements.types.TheBridgeAchievement;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.hotbar.HotbarAction;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;
import dev.slickcollections.kiwizin.player.hotbar.HotbarButton;
import dev.slickcollections.kiwizin.player.scoreboard.KScoreboard;
import dev.slickcollections.kiwizin.player.scoreboard.scroller.ScoreboardScroller;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.container.HotbarContainer;
import dev.slickcollections.kiwizin.thebridge.container.SelectedContainer;
import dev.slickcollections.kiwizin.thebridge.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.Block;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.Perk;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.perks.VidaExtra;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.TheBridgeTeam;
import dev.slickcollections.kiwizin.thebridge.hook.hotbar.TBHotbarActionType;
import dev.slickcollections.kiwizin.thebridge.hook.protocollib.HologramAdapter;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;

public class TBCoreHook {
  
  public static void setupHook() {
    Core.minigame = "The Bridge";
    
    setupHotbars();
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (profile.getScoreboard() != null) {
            profile.getScoreboard().scroll();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, Language.scoreboards$scroller$every_tick);
    
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (!profile.playingGame() && profile.getScoreboard() != null) {
            profile.update();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);

    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
  }
  
  public static void checkAchievements(Profile profile) {
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> Achievement.listAchievements(TheBridgeAchievement.class).stream().filter(tba -> tba.canComplete(profile)).forEach(tba -> {
      tba.complete(profile);
      profile.getPlayer().sendMessage(Language.lobby$achievement.replace("{name}", tba.getName()));
    }));
  }
  
  public static void hotbarGame(Profile profile, TheBridgeTeam team) {
    Player player = profile.getPlayer();
    Perk perk = profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).getSelected(CosmeticType.PERK, Perk.class);
    if (perk != null && perk.getId() == 1) {
      player.setMaxHealth(20.0 + ((VidaExtra) perk).getHealth());
      player.setHealth(player.getMaxHealth());
    }
    
    player.getInventory().setArmorContents(new ItemStack[]{BukkitUtils.deserializeItemStack("LEATHER_BOOTS : 1 : pintar>" + team.getColorD()),
        BukkitUtils.deserializeItemStack("LEATHER_LEGGINGS : 1 : pintar>" + team.getColorD()),
        BukkitUtils.deserializeItemStack("LEATHER_CHESTPLATE : 1 : pintar>" + team.getColorD()),
        BukkitUtils.deserializeItemStack("LEATHER_HELMET : 1 : pintar>" + team.getColorD())});
    
    HotbarContainer config = profile.getAbstractContainer("kCoreTheBridge", "hotbar", HotbarContainer.class);
    
    player.getInventory().setItem(config.get("IR0", 0), BukkitUtils.deserializeItemStack("IRON_SWORD : 1"));
    player.getInventory().setItem(config.get("BO0", 1), BukkitUtils.deserializeItemStack("BOW : 1"));
    player.getInventory().setItem(config.get("DI0", 2), BukkitUtils.deserializeItemStack("DIAMOND_PICKAXE : 1 : encantar>DIG_SPEED:4\nSILK_TOUCH:1"));
    player.getInventory().setItem(config.get("GO0", 5), BukkitUtils.deserializeItemStack("GOLDEN_APPLE : 8"));
    Block block = profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).getSelected(CosmeticType.BLOCK, Block.class);
    if (block != null) {
      player.getInventory().setItem(config.get("ST11", 3), BukkitUtils.deserializeItemStack(block.getMaterial() + " : 64"));
      player.getInventory().setItem(config.get("ST14", 4), BukkitUtils.deserializeItemStack(block.getMaterial() + " : 64"));
      
    } else {
      player.getInventory().setItem(config.get("ST11", 3), BukkitUtils.deserializeItemStack("STAINED_CLAY:" + team.getColorS() + " : 64"));
      player.getInventory().setItem(config.get("ST14", 4), BukkitUtils.deserializeItemStack("STAINED_CLAY:" + team.getColorS() + " : 64"));
    }
    player.getInventory().setItem(config.get("AR0", 7), BukkitUtils.deserializeItemStack("ARROW : 1"));
    player.getInventory().setItem(config.get("EM0", 8), BukkitUtils.deserializeItemStack("EMERALD : 1 : nome>&bCampainha"));

    player.updateInventory();
  }
  
  public static void reloadScoreboard(Profile profile) {
    if (!profile.playingGame()) {
      checkAchievements(profile);
    }
    Player player = profile.getPlayer();
    TheBridge game = profile.getGame(TheBridge.class);
    List<String> lines = game == null ? Language.scoreboards$lobby : game.getState() == GameState.AGUARDANDO ? Language.scoreboards$waiting : Language.scoreboards$ingame;
    profile.setScoreboard(new KScoreboard() {
      @Override
      public void update() {
        for (int index = 0; index < Math.min(lines.size(), 15); index++) {
          String line = lines.get(index);

          if (game != null) {
            TheBridgeTeam team = game.getTeam(player);
            line = line.replace("{map}", game.getMapName());
            line = line.replace("{server}", game.getGameName());
            line = line.replace("{mode}", game.getMode().getName());
            line = line.replace("{players}", StringUtils.formatNumber(game.getOnline()));
            line = line.replace("{max_players}", StringUtils.formatNumber(game.getMaxPlayers()));
            line = line.replace("{time}", game.getTimer() == 11 ? Language.scoreboards$time$waiting : Language.scoreboards$time$starting.replace("{time}", StringUtils.formatNumber(game.getTimer())));
            line = line.replace("{timer}", formatedTime((long) game.getTime()));
            line = line.replace("{pontuation_red}", game.getTeamByName("RED").getPontuation());
            line = line.replace("{pontuation_blue}", game.getTeamByName("BLUE").getPontuation());
            line = line.replace("{red}", StringUtils.formatNumber(game.listTeams().get(0).getScore()));
            line = line.replace("{blue}", StringUtils.formatNumber(game.listTeams().get(1).getScore()));
            line = line.replace("{points}", StringUtils.formatNumber(team == null ? 0 : team.getScore()));
            line = line.replace("{kills}", StringUtils.formatNumber(game.getKills(player)));
          }
          line = PlaceholderAPI.setPlaceholders(player, line);

          this.add(15 - index, line);
        }
      }
    }.scroller(new ScoreboardScroller(Language.scoreboards$scroller$titles)).to(profile.getPlayer()).build());
    if (game != null && game.getState() != GameState.AGUARDANDO) {
      profile.getScoreboard().health().updateHealth();
    }
    profile.update();
    profile.getScoreboard().scroll();
  }
  
  private static void setupHotbars() {
    HotbarActionType.addActionType("thebridge", new TBHotbarActionType());
    
    KConfig config = Main.getInstance().getConfig("hotbar");
    for (String id : new String[]{"lobby", "waiting", "spectator"}) {
      Hotbar hotbar = new Hotbar(id);
      
      ConfigurationSection hb = config.getSection(id);
      for (String button : hb.getKeys(false)) {
        try {
          hotbar.getButtons().add(new HotbarButton(hb.getInt(button + ".slot"), new HotbarAction(hb.getString(button + ".execute")), hb.getString(button + ".icon")));
        } catch (Exception ex) {
          Main.getInstance().getLogger().log(Level.WARNING, "Falha ao carregar o botao \"" + button + "\" da hotbar \"" + id + "\": ", ex);
        }
      }
      
      Hotbar.addHotbar(hotbar);
    }
  }

  public static String formatedTime(Long time) {
    String segundos;
    String minutos;
    DecimalFormat df = new DecimalFormat("00");
    Long finalTime = time;
    if (finalTime < 60) {
      segundos = df.format(finalTime);
      minutos = "00";
    } else {
      Long min = 0L;
      while (finalTime > 60) {
        finalTime = finalTime - 60;
        min++;
      }
      minutos = df.format(min);
      segundos = df.format(finalTime);
    }

    return minutos + ":" + (segundos.equals("60") ? "59" : segundos);
  }
}
