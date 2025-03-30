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
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.inventory.ItemStack;

public class Block extends Cosmetic {
  
  private final String name;
  private final String material;
  
  public Block(long id, EnumRarity rarity, double coins, long cash, String permission, String name, String material) {
    super(id, CosmeticType.BLOCK, coins, permission);
    this.name = name;
    this.material = material;
    this.rarity = rarity;
    this.cash = cash;
  }
  
  public static void setupBlocks() {
    KConfig config = Main.getInstance().getConfig("cosmetics", "blocks");
    
    for (String key : config.getKeys(false)) {
      long id = config.getInt(key + ".id");
      double coins = config.getDouble(key + ".coins");
      if (!config.contains(key + ".cash")) {
        config.set(key + ".cash", getAbsentProperty("blocks", key + ".cash"));
      }
      long cash = config.getInt(key + ".cash");
      String permission = config.getString(key + ".permission");
      String name = config.getString(key + ".name");
      String material = config.getString(key + ".material");
      if (!config.contains(key + ".rarity")) {
        config.set(key + ".rarity", getAbsentProperty("blocks", key + ".rarity"));
      }
      
      new Block(id, EnumRarity.fromName(config.getString(key + ".rarity")), coins, cash, permission, name, material);
    }
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  public String getMaterial() {
    return this.material;
  }
  
  @Override
  public ItemStack getIcon(Profile profile) {
    double coins = profile.getCoins("kCoreTheBridge");
    long cash = profile.getStats("kCoreProfile", "cash");
    boolean has = this.has(profile);
    boolean canBuy = this.canBuy(profile.getPlayer());
    boolean isSelected = isSelected(profile);
    if (isSelected && !canBuy) {
      isSelected = false;
      profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).setSelected(getType(), 0);
    }
    
    Role role = Role.getRoleByPermission(this.getPermission());
    String color = has ?
        (isSelected ? Language.cosmetics$color$selected : Language.cosmetics$color$unlocked) :
        (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) && canBuy ? Language.cosmetics$color$canbuy : Language.cosmetics$color$locked;
    String desc = (has && canBuy ?
        Language.cosmetics$block$icon$has_desc$start.replace("{has_desc_status}", isSelected ? Language.cosmetics$icon$has_desc$selected : Language.cosmetics$icon$has_desc$select) :
        canBuy ?
            Language.cosmetics$block$icon$buy_desc$start
                .replace("{buy_desc_status}", (coins >= this.getCoins() || (CashManager.CASH && cash >= this.getCash())) ? Language.cosmetics$icon$buy_desc$click_to_buy : Language.cosmetics$icon$buy_desc$enough) :
            Language.cosmetics$block$icon$perm_desc$start
                .replace("{perm_desc_status}", (role == null ? Language.cosmetics$icon$perm_desc$common : Language.cosmetics$icon$perm_desc$role.replace("{role}", role.getName()))))
        .replace("{name}", this.name).replace("{rarity}", this.getRarity().getName()).replace("{coins}", StringUtils.formatNumber(this.getCoins())).replace("{cash}", StringUtils.formatNumber(this.getCash()));
    ItemStack item = BukkitUtils.deserializeItemStack(this.material + " : 1 : nome>" + color + this.name + " : desc>" + desc);
    if (isSelected) {
      BukkitUtils.putGlowEnchantment(item);
    }
    
    return item;
  }
}
