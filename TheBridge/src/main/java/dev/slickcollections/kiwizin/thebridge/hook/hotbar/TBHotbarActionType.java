package dev.slickcollections.kiwizin.thebridge.hook.hotbar;

import dev.slickcollections.kiwizin.bukkit.BukkitPartyManager;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.menus.MenuLobbies;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlay;
import dev.slickcollections.kiwizin.thebridge.menus.MenuShop;

public class TBHotbarActionType extends HotbarActionType {
  
  @Override
  public void execute(Profile profile, String action) {
    if (action.equalsIgnoreCase("loja")) {
      new MenuShop(profile);
    } else if (action.equalsIgnoreCase("lobbies")) {
      new MenuLobbies(profile);
    } else if (action.equalsIgnoreCase("jogar")) {
      new MenuPlay(profile, profile.getGame(TheBridge.class).getMode(), false, BukkitPartyManager.getMemberParty(profile.getName()) != null, null);
    } else if (action.equalsIgnoreCase("sair")) {
      profile.getGame(TheBridge.class).leave(profile, null);
    }
  }
}
