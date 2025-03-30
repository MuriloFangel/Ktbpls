package dev.slickcollections.kiwizin.thebridge.menus.cosmetics;

import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.container.HotbarContainer;
import dev.slickcollections.kiwizin.thebridge.menus.MenuShop;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuHotbarConfig extends PlayerMenu {
  
  protected static final List<ItemStack> ITEMS = Arrays
      .asList(new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BOW), BukkitUtils.deserializeItemStack("DIAMOND_PICKAXE : 1 : encantar>DIG_SPEED:4\nSILK_TOUCH:1"),
          new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.STAINED_CLAY, 1, (byte) 11), new ItemStack(Material.STAINED_CLAY, 1, (byte) 14),
          new ItemStack(Material.ARROW, 1), new ItemStack(Material.EMERALD));
  protected static final Map<ItemStack, Integer> DEFAULT = new HashMap<>();

  static {
    DEFAULT.put(ITEMS.get(0), 0);
    DEFAULT.put(ITEMS.get(1), 1);
    DEFAULT.put(ITEMS.get(2), 2);
    DEFAULT.put(ITEMS.get(3), 5);
    DEFAULT.put(ITEMS.get(4), 3);
    DEFAULT.put(ITEMS.get(5), 4);
    DEFAULT.put(ITEMS.get(6), 7);
    DEFAULT.put(ITEMS.get(7), 8);
  }
  
  private HotbarContainer config;
  private Map<ItemStack, String> itemIndex;
  
  public MenuHotbarConfig(Profile profile) {
    super(profile.getPlayer(), "Customizar itens", 6);
    this.config = profile.getAbstractContainer("kCoreTheBridge", "hotbar", HotbarContainer.class);
    this.itemIndex = new HashMap<>();
    
    for (ItemStack item : ITEMS) {
      String name = item.getType().name().substring(0, 2) + item.getDurability();
      int slot = config.get(name, DEFAULT.get(item));
      this.setItem(HotbarContainer.convertConfigSlot(slot), item);
      this.itemIndex.put(item, name);
    }
    
    for (int glass = 27; glass < 36; glass++) {
      this.setItem(glass, BukkitUtils.deserializeItemStack("STAINED_GLASS_PANE:14 : 1 : nome>&8↑ Inventário : desc>&8↓ Hotbar"));
    }
    
    this.setItem(49, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar : desc>&7Para a Loja."));
    
    this.register(Main.getInstance());
    this.open();
  }
  
  @EventHandler(priority = EventPriority.LOW)
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
            if (evt.getSlot() == 49) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuShop(profile);
            } else if (this.itemIndex.containsKey(item)) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuHotbarConfigSlot(profile, this.itemIndex.get(item));
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
    this.config = null;
    this.itemIndex.clear();
    this.itemIndex = null;
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
