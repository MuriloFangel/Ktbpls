package dev.slickcollections.kiwizin.thebridge.menus;

import dev.slickcollections.kiwizin.libraries.menu.UpdatablePlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuPlay extends UpdatablePlayerMenu {
  
  private final TheBridgeMode mode;
  private final boolean isDuels;
  private final Profile duelPlayer;
  private final boolean isPartyDuels;

  public MenuPlay(Profile profile, TheBridgeMode mode, boolean isDuels, boolean isPartyDuels, Profile duelPlayer) {
    super(profile.getPlayer(), "The Bridge " + mode.getName(), 3);
    this.mode = mode;
    this.isDuels = isDuels;
    this.duelPlayer = duelPlayer;
    this.isPartyDuels = isPartyDuels;
    
    this.update();
    this.register(Main.getInstance(), 20);
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
            if (evt.getSlot() == 12) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              if (isDuels) {
                if (getRandomGame() == null) {
                  player.sendMessage("§cNo momento não há nenhuma sala disponível.");
                  return;
                }

                this.player.sendMessage(Language.lobby$npc$play$connect);
                duelPlayer.getPlayer().sendMessage(Language.lobby$npc$play$connect);
                connectGameDuels(player, duelPlayer.getPlayer());
                return;
              }

              TheBridge game = TheBridge.findRandom(this.mode);
              if (game != null) {
                this.player.sendMessage(Language.lobby$npc$play$connect);
                game.joinParty(profile, false, isPartyDuels);
              }
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuMapSelector(profile, this.mode, isPartyDuels, isDuels, duelPlayer);
            }
          }
        }
      }
    }
  }
  
  @Override
  public void update() {
    int players = this.mode.getSize() * 2;
    int waiting = TheBridge.getWaiting(this.mode);
    int playing = TheBridge.getPlaying(this.mode);
    
    this.setItem(12, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe Bridge " + this.mode.getName() + " : desc>&72 ilhas, " + (this.mode.name().contains("PARTY_DUELS") ? "∞" : players)
        + " jogadores.\n \n&fEm espera: &7" + StringUtils.formatNumber(waiting) + "\n&fJogando: &7" + StringUtils.formatNumber(playing) + "\n \n&eClique para jogar!"));
    
    this.setItem(14, BukkitUtils.deserializeItemStack("MAP : 1 : nome>&aSelecione um Mapa : desc>&eClique para jogar em um mapa específico."));
  }
  
  public void cancel() {
    super.cancel();
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

  private TheBridge getRandomGame() {
    if (TheBridge.getGamesAvaliable(this.mode) == null) {
      return null;
    }

    List<TheBridge> games = TheBridge.getGamesAvaliable(this.mode);
    if (games.isEmpty()) {
      return null;
    }

    if (games.size() == 1) {
      return games.get(0);
    }

    return games.get((int) (Math.random() * (games.size() - 1)));
  }

  public void connectGameDuels(Player player, Player target) {
    TheBridge gameSorted = getRandomGame();
    if (gameSorted != null) {
      gameSorted.joinParty(Profile.getProfile(player.getName()), false, isPartyDuels);
      gameSorted.joinParty(Profile.getProfile(target.getName()), false, isPartyDuels);
    }
  }
}
