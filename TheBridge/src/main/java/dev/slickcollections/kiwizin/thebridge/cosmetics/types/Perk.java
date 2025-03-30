package dev.slickcollections.kiwizin.thebridge.cosmetics.types;

import dev.slickcollections.kiwizin.cash.CashManager;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.container.SelectedContainer;
import dev.slickcollections.kiwizin.thebridge.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.thebridge.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.perks.Headshot;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.perks.VidaExtra;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Perk extends Cosmetic implements Listener {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("cosmetics", "perks");
  private final String name;
  private final String icon;
  
  public Perk(long id, String key, double coins, String permission, String name, String icon) {
    super(id, CosmeticType.PERK, coins, permission);
    this.name = name;
    this.icon = icon;
    this.cash = this.getCash(key);
    this.rarity = this.getRarity(key);
  }
  
  public static void setupPerks() {
    checkIfAbsent("vida_extra");
    checkIfAbsent("headshot");
    
    new VidaExtra(CONFIG.getSection("vida_extra"));
    new Headshot(CONFIG.getSection("headshot"));
  }
  
  private static void checkIfAbsent(String key) {
    if (CONFIG.contains(key)) {
      return;
    }
    
    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource("perks.yml"), StandardCharsets.UTF_8));
    for (String dataKey : config.getConfigurationSection(key).getKeys(false)) {
      CONFIG.set(key + "." + dataKey, config.get(key + "." + dataKey));
    }
  }
  
  protected long getCash(String key) {
    if (!CONFIG.contains(key + ".cash")) {
      CONFIG.set(key + ".cash", getAbsentProperty("perks", key + ".cash"));
    }
    
    return CONFIG.getInt(key + ".cash");
  }
  
  protected EnumRarity getRarity(String key) {
    if (!CONFIG.contains(key + ".rarity")) {
      CONFIG.set(key + ".rarity", getAbsentProperty("perks", key + ".rarity"));
    }
    
    return EnumRarity.fromName(CONFIG.getString(key + ".rarity"));
  }
  
  protected void register() {
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public ItemStack getIcon(Profile profile) {
    double coins = profile.getCoins("kCoreTheBridge");
    long cash = profile.getStats("kCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = this.isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
        (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked) :
        (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$perk$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$perk$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$perk$icon$perm_desc$start
                .replace("{perm_desc_status}", (role == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{role}", role.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.icon + desc + " : nome>" + color + this.name);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
}
