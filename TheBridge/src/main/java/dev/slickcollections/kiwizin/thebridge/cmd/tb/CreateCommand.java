package dev.slickcollections.kiwizin.thebridge.cmd.tb;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.CubeID;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CreateCommand extends SubCommand {
  
  public static final Map<Player, Object[]> CREATING = new HashMap<>();
  
  public CreateCommand() {
    super("criar", "criar [solo/dupla/trio/quarteto/party_duels] [nome]", "Criar uma sala.", true);
  }
  
  public static void handleClick(Profile profile, String display, PlayerInteractEvent evt) {
    Player player = profile.getPlayer();
    
    switch (display) {
      case "§9Spawn": {
        evt.setCancelled(true);
        Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());
        CREATING.get(player)[3] = location;
        player.sendMessage("§aSpawn azul setado.");
        break;
      }
      case "§9Respawn": {
        evt.setCancelled(true);
        Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());
        CREATING.get(player)[4] = location;
        player.sendMessage("§aRespawn azul setado.");
        break;
      }
      case "§aCuboID da Ponte": {
        evt.setCancelled(true);
        if (evt.getAction() == Action.LEFT_CLICK_BLOCK) {
          CREATING.get(player)[5] = evt.getClickedBlock().getLocation();
          player.sendMessage("§aBorda da Ponte 1 setada.");
        } else if (evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
          CREATING.get(player)[6] = evt.getClickedBlock().getLocation();
          player.sendMessage("§aBorda da Ponte 2 setada.");
        } else {
          player.sendMessage("§cClique em um bloco.");
        }
        break;
      }
      case "§aConfirmar": {
        evt.setCancelled(true);
        if (CREATING.get(player)[3] == null) {
          player.sendMessage("§cSpawn azul não setado.");
          return;
        }
        
        if (CREATING.get(player)[4] == null) {
          player.sendMessage("§cRespawn azul não setado.");
          return;
        }
        
        if (CREATING.get(player)[5] == null) {
          player.sendMessage("§cBorda da Ponte 1 não setada.");
          return;
        }
        
        if (CREATING.get(player)[6] == null) {
          player.sendMessage("§cBorda da Ponte 2 não setada.");
          return;
        }
        
        if (CREATING.get(player)[7] == null) {
          player.sendMessage("§cSpawn vermelho não setado.");
          return;
        }
        
        if (CREATING.get(player)[8] == null) {
          player.sendMessage("§cRespawn vermelho não setado.");
          return;
        }
        
        Object[] array = CREATING.get(player);
        World world = player.getWorld();
        KConfig config = Main.getInstance().getConfig("arenas", world.getName());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.updateInventory();
        CREATING.remove(player);
        player.sendMessage("§aCriando sala...");
        
        CubeID cube = new CubeID((Location) array[5],
            (Location) array[6]);
        
        config.set("name", array[1]);
        config.set("mode", array[2]);
        config.set("blue.spawn", BukkitUtils.serializeLocation((Location) array[3]));
        config.set("blue.respawn", BukkitUtils.serializeLocation((Location) array[4]));
        config.set("red.spawn", BukkitUtils.serializeLocation((Location) array[7]));
        config.set("red.respawn", BukkitUtils.serializeLocation((Location) array[8]));
        config.set("bridge-cube", cube.toString());
        world.save();
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
          player.sendMessage("§aCriando backup do mapa...");
          Main.getInstance().getFileUtils().copyFiles(new File(world.getName()), new File("plugins/kTheBridge/mundos/" + world.getName()), "playerdata", "stats", "uid.dat");
          
          profile.setHotbar(Hotbar.getHotbarById("lobby"));
          profile.refresh();
          TheBridge.load(config.getFile(), () ->
              player.sendMessage("§aSala criada com sucesso."));
        }, 60);
        break;
      }
      case "§cSpawn": {
        evt.setCancelled(true);
        Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());
        CREATING.get(player)[7] = location;
        player.sendMessage("§aSpawn vermelho setado.");
        break;
      }
      case "§cRespawn": {
        evt.setCancelled(true);
        Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
        location.setYaw(player.getLocation().getYaw());
        location.setPitch(player.getLocation().getPitch());
        CREATING.get(player)[8] = location;
        player.sendMessage("§aRespawn vermelho setado.");
        break;
      }
    }
  }
  
  @Override
  public void perform(Player player, String[] args) {
    TheBridge tb = TheBridge.getByWorldName(player.getWorld().getName());
    if (tb != null) {
      player.sendMessage("§cJá existe uma sala neste mundo.");
      return;
    }
    
    if (args.length <= 1) {
      player.sendMessage("§cUtilize /tb " + this.getUsage());
      return;
    }
    
    TheBridgeMode mode = TheBridgeMode.fromName(args[0]);
    if (mode == null) {
      player.sendMessage("§cUtilize /tb " + this.getUsage());
      return;
    }
    
    String name = StringUtils.join(args, 1, " ");
    Object[] array = new Object[9];
    array[0] = player.getWorld();
    array[1] = name;
    array[2] = mode.name();
    CREATING.put(player, array);
    
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    
    player.getInventory()
        .setItem(0, BukkitUtils.deserializeItemStack("BEACON : 1 : nome>&9Spawn"));
    player.getInventory()
        .setItem(1, BukkitUtils.deserializeItemStack("COMPASS : 1 : nome>&9Respawn"));
    player.getInventory()
        .setItem(2, BukkitUtils.deserializeItemStack("STAINED_CLAY:11 : 1 : nome>&9« Time Azul"));
    
    player.getInventory().setItem(4, BukkitUtils.deserializeItemStack("BLAZE_ROD : 1 : nome>&aCuboID da Ponte"));
    player.getInventory().setItem(5, BukkitUtils.deserializeItemStack("STAINED_CLAY:13 : 1 : nome>&aConfirmar"));
    
    player.getInventory().setItem(6, BukkitUtils.deserializeItemStack("STAINED_CLAY:14 : 1 : nome>&cTime Vermelho »"));
    player.getInventory().setItem(7, BukkitUtils.deserializeItemStack("COMPASS : 1 : nome>&cRespawn"));
    player.getInventory().setItem(8, BukkitUtils.deserializeItemStack("BEACON : 1 : nome>&cSpawn"));
    
    player.updateInventory();
    
    Profile.getProfile(player.getName()).setHotbar(null);
  }
}