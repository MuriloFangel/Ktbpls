package dev.slickcollections.kiwizin.thebridge.menus;


import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Maxter
 */
public class MenuStatsNPC extends PlayerMenu {
  
  public MenuStatsNPC(Profile profile) {
    super(profile.getPlayer(), "Estatísticas - The Bridge", 5);
    
    this.setItem(4, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "PAPER : 1 : nome>&aTodos os Modos : desc>&fAbates: &7%kCore_TheBridge_kills%\n&fMortes: &7%kCore_TheBridge_deaths%\n&fPontos: &7%kCore_TheBridge_points%\n&fVitórias: &7%kCore_TheBridge_wins%\n&fPartidas: &7%kCore_TheBridge_games%")));
    
    this.setItem(21, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "PAPER : 1 : nome>&aSolo : desc>&fAbates: &7%kCore_TheBridge_1v1kills%\n&fMortes: &7%kCore_TheBridge_1v1deaths%\n&fPontos: &7%kCore_TheBridge_1v1points%\n&fVitórias: &7%kCore_TheBridge_1v1wins%\n&fPartidas: &7%kCore_TheBridge_1v1games%")));
    
    this.setItem(23, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "PAPER : 1 : nome>&aDupla : desc>&fAbates: &7%kCore_TheBridge_2v2kills%\n&fMortes: &7%kCore_TheBridge_2v2deaths%\n&fPontos: &7%kCore_TheBridge_2v2points%\n&fVitórias: &7%kCore_TheBridge_2v2wins%\n&fPartidas: &7%kCore_TheBridge_2v2games%")));
    
    this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cFechar"));
    
    this.register(Core.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.player.closeInventory();
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
