package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.container.RejoinContainer;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.utils.tagger.TagUtils;
import dev.slickcollections.kiwizin.titles.TitleManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

import static dev.slickcollections.kiwizin.thebridge.hook.TBCoreHook.reloadScoreboard;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent evt) {
    evt.setJoinMessage(null);

    Player player = evt.getPlayer();
    TagUtils.sendTeams(player);

    Profile profile = Profile.getProfile(player.getName());
    profile.setHotbar(Hotbar.getHotbarById("lobby"));
    profile.refresh();

    Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
      TitleManager.joinLobby(profile);
      reloadScoreboard(profile);
      RejoinContainer container = profile.getAbstractContainer("kCoreTheBridge", "rejoin", RejoinContainer.class);
      TheBridge game = container.getRejoinGame(player);
      if (game != null) {
        TextComponent principal = new TextComponent("§eVocê recentemente esteve em um jogo. clique ");
        TextComponent aqui = new TextComponent("§aAQUI");
        aqui.setBold(true);
        aqui.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rejoin"));
        principal.addExtra(aqui);
        principal.addExtra(" §epara se reconectar.");
        player.spigot().sendMessage(principal);
      }
    }, 10);

    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      TagUtils.setTag(evt.getPlayer(), profile.getTagContainer().getSelectedRole());
      if (Role.getPlayerRole(player).isBroadcast()) {
        String broadcast = Language.lobby$broadcast.replace("{player}", profile.getTagContainer().getSelectedRole().getPrefix() + player.getName());
        Profile.listProfiles().forEach(pf -> {
          if (!pf.playingGame()) {
            Player players = pf.getPlayer();
            if (players != null) {
              players.sendMessage(broadcast);
            }
          }
        });
      }
    }, 5);

    NMS.sendTitle(player, "", "", 0, 1, 0);
    if (Language.lobby$tab$enabled) {
      NMS.sendTabHeaderFooter(player, Language.lobby$tab$header, Language.lobby$tab$footer);
    }
  }
}