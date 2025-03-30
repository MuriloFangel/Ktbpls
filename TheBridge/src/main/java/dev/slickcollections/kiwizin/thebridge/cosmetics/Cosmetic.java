package dev.slickcollections.kiwizin.thebridge.cosmetics;

import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.container.CosmeticsContainer;
import dev.slickcollections.kiwizin.thebridge.container.SelectedContainer;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.*;
import dev.slickcollections.kiwizin.utils.enums.EnumRarity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class Cosmetic {
  
  private static final List<Cosmetic> COSMETICS = new ArrayList<>();
  protected long cash;
  protected EnumRarity rarity;
  private final long id;
  private final double coins;
  private final String permission;
  private final CosmeticType type;
  
  public Cosmetic(long id, CosmeticType type, double coins, String permission) {
    this.id = id;
    this.coins = coins;
    this.permission = permission;
    this.type = type;
    COSMETICS.add(this);
  }
  
  public static void setupCosmetics() {
    Block.setupBlocks();
    DeathCry.setupDeathCries();
    Perk.setupPerks();
    KillEffect.setupEffects();
    WinAnimation.setupAnimations();
    Balloon.setupBalloons();
    DeathMessage.setupDeathMessages();
  }
  
  public static <T extends Cosmetic> T findById(Class<T> cosmeticClass, long id) {
    return COSMETICS.stream()
        .filter(cosmetic -> (cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass)) && cosmetic.getId() == id)
        .map(cosmetic -> (T) cosmetic).findFirst().orElse(null);
  }
  
  public static Cosmetic findById(String lootChestID) {
    return COSMETICS.stream().filter(cosmetic -> cosmetic.getLootChestsID().equals(lootChestID)).findFirst().orElse(null);
  }
  
  public static List<Cosmetic> listCosmetics() {
    return COSMETICS;
  }
  
  public static <T extends Cosmetic> List<T> listByType(Class<T> cosmeticClass) {
    return COSMETICS.stream().filter(cosmetic -> cosmetic.getClass().isAssignableFrom(cosmeticClass) || cosmetic.getClass().getSuperclass().equals(cosmeticClass))
        .map(cosmetic -> (T) cosmetic).collect(Collectors.toList());
  }
  
  protected static Object getAbsentProperty(String file, String property) {
    return YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource(file + ".yml"), StandardCharsets.UTF_8)).get(property);
  }
  
  public void give(Profile profile) {
    profile.getAbstractContainer("kCoreTheBridge", "cosmetics", CosmeticsContainer.class).addCosmetic(this);
  }
  
  public boolean has(Profile profile) {
    return profile.getAbstractContainer("kCoreTheBridge", "cosmetics", CosmeticsContainer.class).hasCosmetic(this);
  }
  
  public boolean isSelected(Profile profile) {
    return profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).isSelected(this);
  }
  
  public long getId() {
    return this.id;
  }
  
  public long getIndex() {
    return 1;
  }
  
  public String getLootChestsID() {
    return "tb" + this.type.ordinal() + "-" + this.id;
  }
  
  public EnumRarity getRarity() {
    return this.rarity;
  }
  
  public double getCoins() {
    return this.coins;
  }
  
  public long getCash() {
    return this.cash;
  }
  
  public String getPermission() {
    return this.permission;
  }
  
  public CosmeticType getType() {
    return this.type;
  }
  
  public boolean canBuy(Player player) {
    return this.permission.isEmpty() || player.hasPermission(this.permission);
  }
  
  public abstract String getName();
  
  public abstract ItemStack getIcon(Profile profile);
}
