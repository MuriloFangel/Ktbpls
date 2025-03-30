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

public class MenuPlaySelect extends PlayerMenu {

    private final TheBridgeModeType mode;
    private final boolean isDuels;
    private final Profile profileDuels;

    public MenuPlaySelect(Profile profile, TheBridgeModeType mode, boolean isDuels, Profile duelsProfile) {
        super(profile.getPlayer(), "The Bridge " + mode.getName(), 3);
        this.mode = mode;
        this.isDuels = isDuels;
        this.profileDuels = duelsProfile;

        if (mode == TheBridgeModeType.CASUAL) {
            this.setItem(13, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge casual solo  : desc>\n&eClique para jogar!"));
/*            this.setItem(12, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge casual dupla  : desc>\n&eClique para jogar!"));
            this.setItem(14, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge casual trio  : desc>\n&eClique para jogar!"));
            this.setItem(16, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge casual quarteto  : desc>\n&eClique para jogar!"));*/
        } else if (mode == TheBridgeModeType.COMPETITIVO) {
            this.setItem(13, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge competitivo solo  : desc>\n&eClique para jogar!"));
/*            this.setItem(12, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge competitivo dupla  : desc>\n&eClique para jogar!"));
            this.setItem(14, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge competitivo trio  : desc>\n&eClique para jogar!"));
            this.setItem(16, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>&aThe bridge competitivo quarteto  : desc>\n&eClique para jogar!"));*/
        }

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
                        EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                        switch (this.mode) {
                            case CASUAL: {
                                switch (evt.getSlot()) {
                                    case 13: {
                                        new MenuPlay(profile, TheBridgeMode.SOLO, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }

                                    case 12: {
                                        new MenuPlay(profile, TheBridgeMode.DUPLA, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }

                                    case 14: {
                                        new MenuPlay(profile, TheBridgeMode.TRIO, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }

                                    case 16: {
                                        new MenuPlay(profile, TheBridgeMode.QUARTETO, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }
                                }
                                break;
                            }

                            case COMPETITIVO: {
                                switch (evt.getSlot()) {
                                    case 13: {
                                        new MenuPlay(profile, TheBridgeMode.COMPSOLO, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }

                                    case 12: {
                                        new MenuPlay(profile, TheBridgeMode.COMPDUPLA, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }

                                    case 14: {
                                        new MenuPlay(profile, TheBridgeMode.COMPTRIO, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }

                                    case 16: {
                                        new MenuPlay(profile, TheBridgeMode.COMPQUARTETO, isDuels, BukkitPartyManager.getMemberParty(profile.getName()) != null, profileDuels);
                                        break;
                                    }
                                }
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
