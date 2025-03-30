package dev.slickcollections.kiwizin.thebridge.listeners.player;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.bukkit.BukkitPartyManager;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.libraries.npclib.api.event.NPCLeftClickEvent;
import dev.slickcollections.kiwizin.libraries.npclib.api.event.NPCRightClickEvent;
import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import dev.slickcollections.kiwizin.menus.MenuDeliveries;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.cmd.tb.BalloonsCommand;
import dev.slickcollections.kiwizin.thebridge.cmd.tb.BuildCommand;
import dev.slickcollections.kiwizin.thebridge.cmd.tb.CreateCommand;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.TheBridgeTeam;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeModeType;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlay;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlaySelect;
import dev.slickcollections.kiwizin.thebridge.menus.MenuStatsNPC;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import static dev.slickcollections.kiwizin.thebridge.cmd.tb.BalloonsCommand.BALLOONS;
import static dev.slickcollections.kiwizin.thebridge.cmd.tb.CreateCommand.CREATING;

public class PlayerInteractListener implements Listener {
  
  @EventHandler
  public void onNPCLeftClick(NPCLeftClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (CacheManager.getCache(evt.getPlayer().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        new MenuPlaySelect(profile, TheBridgeModeType.findByName(npc.data().get("play-npc")), false, null);
      }
    }
  }
  
  @EventHandler
  public void onNPCRightClick(NPCRightClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (CacheManager.getCache(evt.getPlayer().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        TheBridgeModeType modeType = TheBridgeModeType.findByName(npc.data().get("play-npc"));
        if (BukkitPartyManager.getMemberParty(player.getName()) != null) {
          new MenuPlay(profile, modeType == TheBridgeModeType.CASUAL ? TheBridgeMode.PARTY_DUELS : TheBridgeMode.COMPPARTY_DUELS, false, true, profile);
          return;
        }

        new MenuPlaySelect(profile, modeType, false, null);
      } else if (npc.data().has("delivery-npc")) {
        new MenuDeliveries(profile);
      } else if (npc.data().has("stats-npc")) {
        new MenuStatsNPC(profile);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (CacheManager.getCache(evt.getPlayer().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    if (profile != null) {
      if (CREATING.containsKey(player) && CREATING.get(player)[0].equals(player.getWorld())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          CreateCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      } else if (BALLOONS.containsKey(player) && BALLOONS.get(player).getGameName().equals(player.getWorld().getName())) {
        ItemStack item = player.getItemInHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
          BalloonsCommand.handleClick(profile, item.getItemMeta().getDisplayName(), evt);
        }
      }
      
      TheBridge game = profile.getGame(TheBridge.class);
      if (game == null && !BuildCommand.hasBuilder(player)) {
        evt.setCancelled(true);
      } else  {
        if (game != null && game.getState() == GameState.INICIANDO || game != null &&  game.getState() == GameState.EMJOGO) {
          return;
        }
        if (game != null && (game.getState() != GameState.EMJOGO || game.isSpectator(player) || (evt.getAction() == Action.PHYSICAL && evt.getClickedBlock() != null && evt.getClickedBlock().getType() == Material.SOIL))) {
          player.updateInventory();
          evt.setCancelled(true);
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent evt) {
    if (CacheManager.getCache(evt.getPlayer().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    if (evt.getTo().getBlock().getRelative(BlockFace.DOWN).getLocation().getBlock().getType() == Material.ENDER_PORTAL || evt.getTo().getBlock().getType() == Material.ENDER_PORTAL) {
      Player player = evt.getPlayer();
      Profile profile = Profile.getProfile(player.getName());
      
      if (profile != null && profile.playingGame()) {
        TheBridge game = profile.getGame(TheBridge.class);
        if (!game.isSpectator(player) && game.getState() == GameState.EMJOGO) {
          TheBridgeTeam team = game.getTeam(player);
          TheBridgeTeam target;
          if (team != null && (target = game.getTeamNear(evt.getFrom())) != null) {
            if (!team.equals(target)) {
              game.point(profile, team);
            }
          }
        }
      }
    } else if (evt.getTo().getBlockY() != evt.getFrom().getBlockY() && evt.getTo().getBlockY() < 0) {
      Player player = evt.getPlayer();
      Profile profile = Profile.getProfile(player.getName());
      
      if (profile != null) {
        TheBridge game = profile.getGame(TheBridge.class);
        if (game == null) {
          player.teleport(Core.getLobby());
        }
      }
    } else if (evt.getTo().getBlockY() != evt.getFrom().getBlockY() && evt.getTo().getBlockY() < 82) {
      Player player = evt.getPlayer();
      Profile profile = Profile.getProfile(player.getName());

      if (profile != null) {
        TheBridge game = profile.getGame(TheBridge.class);
        if (game != null && game.getState() != GameState.EMJOGO || game != null &&  game.isSpectator(player)) {
          player.teleport(game.listTeams().get(0).getRespawn());
        } else {
          ((CraftPlayer) player).getHandle().damageEntity(DamageSource.OUT_OF_WORLD, (float) player.getMaxHealth() * 2);
        }
      }
    }
  }
}
