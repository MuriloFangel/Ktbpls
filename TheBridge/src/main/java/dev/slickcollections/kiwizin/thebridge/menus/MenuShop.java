package dev.slickcollections.kiwizin.thebridge.menus;

import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.cosmetics.Cosmetic;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.*;
import dev.slickcollections.kiwizin.thebridge.menus.cosmetics.MenuCosmetics;
import dev.slickcollections.kiwizin.thebridge.menus.cosmetics.MenuHotbarConfig;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuShop extends PlayerMenu {

  public MenuShop(Profile profile) {
    super(profile.getPlayer(), "Loja - The Bridge", 5);
    this.setItem(12, BukkitUtils.deserializeItemStack(
        "DIAMOND_SWORD : 1 : esconder>tudo : nome>&aOrdem dos Itens : desc>&7Configure como você irá receber\n&7o seu equipamento na partida.\n \n&eClique para configurar!"));
    
    List<Perk> perks = Cosmetic.listByType(Perk.class);
    long max = perks.size();
    long owned = perks.stream().filter(perk -> perk.has(profile)).count();
    long percentage = max == 0 ? 100 : (owned * 100) / max;
    String color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    perks.clear();
    this.setItem(14, BukkitUtils.deserializeItemStack(
        "EXP_BOTTLE : 1 : nome>&aHabilidades : desc>&7Aproveite de vantagens únicas\n&7para lhe auxiliar nas partidas.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
    List<DeathMessage> deathmessages = Cosmetic.listByType(DeathMessage.class);
    max = deathmessages.size();
    owned = deathmessages.stream().filter(cage -> cage.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned > max / 2) ? "&7" : "&c";
    deathmessages.clear();
    this.setItem(10, BukkitUtils.deserializeItemStack(
        "BOOK_AND_QUILL : 1 : nome>&aMensagens de Morte : desc>&7Anuncie o abate do seu inimigo de\n&7uma forma estilosa com mensagens de morte.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
    List<KillEffect> killEffects = Cosmetic.listByType(KillEffect.class);
    max = killEffects.size();
    owned = killEffects.stream().filter(killEffect -> killEffect.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    killEffects.clear();
    this.setItem(16, BukkitUtils.deserializeItemStack(
        "BONE : 1 : nome>&aEfeitos de Abate : desc>&7Deixa a sua marca quando abater\n&7os seus oponentes.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
    List<DeathCry> deathcries = Cosmetic.listByType(DeathCry.class);
    max = deathcries.size();
    owned = deathcries.stream().filter(deathcry -> deathcry.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    deathcries.clear();
    this.setItem(29, BukkitUtils.deserializeItemStack(
        "GHAST_TEAR : 1 : nome>&aGritos de Morte : desc>&7Gritos de mortes são sons que\n&7irão ser reproduzidos toda vez\n&7que você morrer.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
    List<Block> blocks = Cosmetic.listByType(Block.class);
    max = blocks.size();
    owned = blocks.stream().filter(block -> block.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    blocks.clear();
    this.setItem(30, BukkitUtils.deserializeItemStack(
        "STAINED_CLAY:11 : 1 : nome>&aBlocos : desc>&7Altere o tipo de bloco que você irá\n&7receber para fazer sua ponte ao\n&7iniciar a partida.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
    List<Balloon> balloons = Cosmetic.listByType(Balloon.class);
    max = balloons.size();
    owned = balloons.stream().filter(balloon -> balloon.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    balloons.clear();
    this.setItem(32, BukkitUtils.deserializeItemStack(
        "LEASH : 1 : nome>&aBalões : desc>&7Escolha um balão decorativo para\n&7ser colocado em sua ilha.\n \n&8Em modos de times maiores o balão\n&8será escolhido aleatoriamente pelo\n&8sistema. Um pré-requesito é o jogador\n&8ter um balão selecionado. Sendo assim,\n&8você ou seu companheiro poderá ter o\n&8balão spawnado em sua ilha.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
    List<WinAnimation> animations = Cosmetic.listByType(WinAnimation.class);
    max = animations.size();
    owned = animations.stream().filter(animation -> animation.has(profile)).count();
    percentage = max == 0 ? 100 : (owned * 100) / max;
    color = (owned == max) ? "&a" : (owned >= max / 2) ? "&7" : "&c";
    animations.clear();
    this.setItem(33, BukkitUtils.deserializeItemStack(
        "DRAGON_EGG : 1 : nome>&aComemorações de Vitória : desc>&7Esbanje estilo nas suas vitórias\n&7com comemorações exclusivas.\n \n&fDesbloqueados: " + color + owned + "/" + max + " &8(" + percentage + "%)\n \n&eClique para comprar ou selecionar!"));
    
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
            if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Mensagens de Morte", DeathMessage.class);
            } else if (evt.getSlot() == 12) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuHotbarConfig(profile);
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Habilidades", Perk.class);
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Efeitos de Abate", KillEffect.class);
            } else if (evt.getSlot() == 29) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Gritos de Morte", DeathCry.class);
            } else if (evt.getSlot() == 30) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Blocos", Block.class);
            } else if (evt.getSlot() == 32) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Balões", Balloon.class);
            } else if (evt.getSlot() == 33) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuCosmetics<>(profile, "Comemorações", WinAnimation.class);
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