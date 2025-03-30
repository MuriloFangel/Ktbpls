package dev.slickcollections.kiwizin.thebridge.cmd;

import dev.slickcollections.kiwizin.bukkit.BukkitPartyManager;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeModeType;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlay;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlaySelect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCompCmd extends Commands{

    public MenuCompCmd() {
        super("menucomp");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Profile profile = Profile.getProfile(sender.getName());
            if (profile != null) {
                if (BukkitPartyManager.getMemberParty(sender.getName()) != null) {
                    new MenuPlay(profile, TheBridgeMode.COMPPARTY_DUELS, false, true, profile);
                    return;
                }

                new MenuPlaySelect(profile, TheBridgeModeType.COMPETITIVO, false, null);
            }
        }
    }
}
