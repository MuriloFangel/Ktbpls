package dev.slickcollections.kiwizin.thebridge.listeners.entity;

import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.enums.BloodAndGore;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.api.CacheManager;
import dev.slickcollections.kiwizin.thebridge.api.cache.RemoveProtectionCache;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.TheBridgeTeam;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityListener implements Listener {
  
  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    if (CacheManager.getCache(evt.getEntity().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    if (evt.getEntity() instanceof Player) {
      Player player = (Player) evt.getEntity();
      
      TheBridge game;
      Profile profile = Profile.getProfile(player.getName());
      if (profile == null || (game = profile.getGame(TheBridge.class)) == null || game.getState() != GameState.EMJOGO || game.isSpectator(player)) {
        evt.setCancelled(true);
      } else {
        TheBridgeTeam team = game.getTeam(player);
        
        Player damager = null;
        Profile profile2;
        if (evt.getDamager() instanceof Player) {
          damager = (Player) evt.getDamager();
          profile2 = Profile.getProfile(damager.getName());
          if (profile2 == null || profile2.getGame() == null || !profile2.getGame().equals(game) || game.isSpectator(damager) || damager.equals(player) || (team != null && team
              .hasMember(damager))) {
            evt.setCancelled(true);
          } else {
            if (profile.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
              player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
            if (profile2.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
              damager.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
          }
        }
        
        if (evt.getDamager() instanceof Projectile) {
          Projectile proj = (Projectile) evt.getDamager();
          if (proj.getShooter() instanceof Player) {
            damager = (Player) proj.getShooter();
            profile2 = Profile.getProfile(damager.getName());
            if (profile2 == null || profile2.getGame() == null || !profile2.getGame().equals(game) || game.isSpectator(damager) || damager.equals(player) || (team != null && team
                .hasMember(damager))) {
              evt.setCancelled(true);
            } else {
              if (profile.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
                player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
              }
              if (profile2.getPreferencesContainer().getBloodAndGore() == BloodAndGore.ATIVADO) {
                damager.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
              }
              
              if (proj instanceof Arrow) {
                Player finalDamager = damager;
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> finalDamager.sendMessage(
                    Language.ingame$messages$bow$hit.replace("{name}", team != null ? (team.getColor() + player.getName()) : player.getName())
                        .replace("{hp}", StringUtils.formatNumber(player.getHealth()))), 5L);
              }
            }
          }
        }
        
        if (!evt.isCancelled() && damager != null) {
          profile.setHit(damager.getName());
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityDamage(EntityDamageEvent evt) {
    if (evt.getEntity() instanceof Player) {
      if (CacheManager.getCache(evt.getEntity().getName(), RemoveProtectionCache.class) != null) {
        return;
      }

      Player player = (Player) evt.getEntity();
      
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        TheBridge game = profile.getGame(TheBridge.class);
        if (game == null) {
          evt.setCancelled(true);
        } else {
          if (game.getState() != GameState.EMJOGO) {
            evt.setCancelled(true);
          } else if (game.isSpectator(player)) {
            evt.setCancelled(true);
          } else if (evt.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && evt.getCause() != EntityDamageEvent.DamageCause.PROJECTILE && evt.getCause() != EntityDamageEvent.DamageCause.VOID) {
            evt.setCancelled(true);
          }
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityShotBow(EntityShootBowEvent evt) {
    if (evt.getEntity() instanceof Player) {
      if (CacheManager.getCache(evt.getEntity().getName(), RemoveProtectionCache.class) != null) {
        return;
      }

      Player player = (Player) evt.getEntity();
      
      Profile profile = Profile.getProfile(player.getName());
      if (profile != null) {
        TheBridgeTeam team;
        TheBridge game = profile.getGame(TheBridge.class);
        if (game != null && (team = game.getTeam(player)) != null) {
          team.setArrow(player);
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCreatureSpawn(CreatureSpawnEvent evt) {
    evt.setCancelled(evt.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onProjectileHitEvent(ProjectileHitEvent evt) {
    if (CacheManager.getCache(evt.getEntity().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    if (evt.getEntity() instanceof Arrow) {
      TheBridge game = TheBridge.getByWorldName(evt.getEntity().getWorld().getName());
      if (game != null) {
        evt.getEntity().remove();
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFoodLevelChange(FoodLevelChangeEvent evt) {
    if (CacheManager.getCache(evt.getEntity().getName(), RemoveProtectionCache.class) != null) {
      return;
    }

    evt.setCancelled(true);
  }
}
