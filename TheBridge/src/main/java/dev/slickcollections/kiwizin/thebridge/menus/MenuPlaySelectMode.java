package dev.slickcollections.kiwizin.thebridge.menus;

import dev.slickcollections.kiwizin.bukkit.BukkitPartyManager;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeModeType;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuPlaySelectMode extends PlayerMenu {

    private Profile profileDuels;

    public MenuPlaySelectMode(Profile profile, Profile profileDuels) {
        super(profile.getPlayer(), "The Bridge Duels", 3);
        this.profileDuels = profileDuels;

        this.setItem(12, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge Casual : desc>\n&eClique para jogar!"));
        this.setItem(14, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge Competitivo : desc>\n&eClique para jogar!"));

        this.register(Main.getInstance());
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
                        int slot = evt.getSlot();
                        evt.setCancelled(true);
                        switch (slot) {
                            case 12: {
                                if (BukkitPartyManager.getMemberParty(profileDuels.getName()) != null) {
                                    new MenuPlay(profile, TheBridgeMode.PARTY_DUELS, true, true, profileDuels);
                                } else {
                                    new MenuPlaySelect(profile, TheBridgeModeType.CASUAL, true, profileDuels);
                                }
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                break;
                            }

                            case 14: {
                                if (BukkitPartyManager.getMemberParty(profileDuels.getName()) != null) {
                                    new MenuPlay(profile, TheBridgeMode.COMPPARTY_DUELS, true, true, profileDuels);
                                } else {
                                    new MenuPlaySelect(profile, TheBridgeModeType.COMPETITIVO, true, profileDuels);
                                }
                                EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                                break;
                            }
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
