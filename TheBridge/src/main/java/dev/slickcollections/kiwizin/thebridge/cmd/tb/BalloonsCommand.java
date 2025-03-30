package dev.slickcollections.kiwizin.thebridge.cmd.tb;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.thebridge.cmd.SubCommand;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class BalloonsCommand extends SubCommand {
  
  public static final Map<Player, TheBridge> BALLOONS = new HashMap<>();
  
  public BalloonsCommand() {
    super("baloes", "baloes", "Setar localização dos balões.", true);
  }
  
  public static void handleClick(Profile profile, String display, PlayerInteractEvent evt) {
    Player player = profile.getPlayer();
    
    switch (display) {
      case "§cBalão do Time Vermelho": {
        evt.setCancelled(true);
        if (evt.getClickedBlock() != null) {
          BALLOONS.get(player).getConfig().setBalloon(0, evt.getClickedBlock().getLocation().add(0.5, 0, 0.5));
          player.sendMessage("§aBalão do time vermelho setado.");
        } else {
          player.sendMessage("§cClique em um bloco.");
        }
        break;
      }
      case "§9Balão do Time Azul": {
        evt.setCancelled(true);
        if (evt.getClickedBlock() != null) {
          BALLOONS.get(player).getConfig().setBalloon(1, evt.getClickedBlock().getLocation().add(0.5, 0, 0.5));
          player.sendMessage("§aBalão do time azul setado.");
        } else {
          player.sendMessage("§cClique em um bloco.");
        }
        break;
      }
      case "§cCancelar": {
        evt.setCancelled(true);
        BALLOONS.remove(player);
        profile.setHotbar(Hotbar.getHotbarById("lobby"));
        profile.refresh();
        break;
      }
    }
  }
  
  @Override
  public void perform(Player player, String[] args) {
    TheBridge tb = TheBridge.getByWorldName(player.getWorld().getName());
    if (tb == null) {
      player.sendMessage("§cNão existe uma sala neste mundo.");
      return;
    }
    
    BALLOONS.put(player, tb);
    
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    
    player.getInventory().setItem(0, BukkitUtils.deserializeItemStack("STAINED_CLAY:14 : 1 : nome>&cBalão do Time Vermelho"));
    player.getInventory().setItem(1, BukkitUtils.deserializeItemStack("STAINED_CLAY:11 : 1 : nome>&9Balão do Time Azul"));
    
    player.getInventory().setItem(8, BukkitUtils.deserializeItemStack("BED : 1 : nome>&cCancelar"));
    
    player.updateInventory();
    
    Profile.getProfile(player.getName()).setHotbar(null);
  }
}
