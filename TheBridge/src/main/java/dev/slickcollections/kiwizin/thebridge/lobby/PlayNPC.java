package dev.slickcollections.kiwizin.thebridge.lobby;

import dev.slickcollections.kiwizin.libraries.holograms.HologramLibrary;
import dev.slickcollections.kiwizin.libraries.holograms.api.Hologram;
import dev.slickcollections.kiwizin.libraries.npclib.NPCLibrary;
import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeModeType;
import dev.slickcollections.kiwizin.thebridge.lobby.trait.NPCSkinTrait;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PlayNPC {
  
  private static final KConfig CONFIG = Main.getInstance().getConfig("npcs");
  private static final List<PlayNPC> NPCS = new ArrayList<>();
  private String id;
  private TheBridgeModeType mode;
  private Location location;
  private NPC npc;
  private Hologram hologram;
  
  public PlayNPC(Location location, String id, TheBridgeModeType mode) {
    this.location = location;
    this.id = id;
    this.mode = mode;
    if (!this.location.getChunk().isLoaded()) {
      this.location.getChunk().load(true);
    }
    
    this.spawn();
  }
  
  public static void setupNPCs() {
    if (!CONFIG.contains("play")) {
      CONFIG.set("play", new ArrayList<>());
    }
    
    for (String serialized : CONFIG.getStringList("play")) {
      if (serialized.split("; ").length > 6) {
        String id = serialized.split("; ")[6];
        TheBridgeModeType mode = TheBridgeModeType.findByName(serialized.split("; ")[7]);
        if (mode == null) {
          continue;
        }
        
        NPCS.add(new PlayNPC(BukkitUtils.deserializeLocation(serialized), id, mode));
      }
    }
    
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> listNPCs().forEach(PlayNPC::update), 20, 20);
  }
  
  public static void add(String id, Location location, TheBridgeModeType mode) {
    NPCS.add(new PlayNPC(location, id, mode));
    List<String> list = CONFIG.getStringList("play");
    list.add(BukkitUtils.serializeLocation(location) + "; " + id + "; " + mode);
    CONFIG.set("play", list);
  }
  
  public static void remove(PlayNPC npc) {
    NPCS.remove(npc);
    List<String> list = CONFIG.getStringList("play");
    list.remove(BukkitUtils.serializeLocation(npc.getLocation()) + "; " + npc.getId() + "; " + npc.getMode());
    CONFIG.set("play", list);
    
    npc.destroy();
  }
  
  public static PlayNPC getById(String id) {
    return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
  }
  
  public static Collection<PlayNPC> listNPCs() {
    return NPCS;
  }

  public void spawn() {
    if (this.npc != null) {
      this.npc.destroy();
      this.npc = null;
    }
    
    if (this.hologram != null) {
      HologramLibrary.removeHologram(this.hologram);
      this.hologram = null;
    }
    
    this.hologram = HologramLibrary.createHologram(this.location.clone().add(0, 0.5, 0));

    for (int index = (this.mode == TheBridgeModeType.CASUAL ? Language.lobby$npc$play$casual$hologram.size() : Language.lobby$npc$play$comp$hologram.size()); index > 0; index--) {
      this.hologram.withLine((this.mode == TheBridgeModeType.CASUAL ? Language.lobby$npc$play$casual$hologram : Language.lobby$npc$play$comp$hologram).get(index - 1).replace("{players}",
          StringUtils.formatNumber(getOnline().get())));
    }
    
    this.npc = NPCLibrary.createNPC(EntityType.PLAYER, "§8[NPC] ");
    this.npc.data().set("play-npc", this.mode.getName());
    this.npc.data().set(NPC.HIDE_BY_TEAMS_KEY, true);
    if (this.mode == TheBridgeModeType.CASUAL) {
      this.npc.addTrait(new NPCSkinTrait(this.npc, Language.lobby$npc$play$casual$skin$value, Language.lobby$npc$play$casual$skin$signature));
    } else {
      this.npc.addTrait(new NPCSkinTrait(this.npc, Language.lobby$npc$play$comp$skin$value, Language.lobby$npc$play$comp$skin$signature));
    }

    this.npc.spawn(this.location);
  }

  private AtomicInteger getOnline() {
    AtomicInteger online = new AtomicInteger();
    if (this.mode.equals(TheBridgeModeType.CASUAL)) {
      Arrays.stream(TheBridgeMode.values()).filter(theBridgeModeType -> theBridgeModeType.equals(TheBridgeMode.SOLO) || theBridgeModeType.equals(TheBridgeMode.TRIO) || theBridgeModeType.equals(TheBridgeMode.DUPLA) || theBridgeModeType.equals(TheBridgeMode.QUARTETO)).collect(Collectors.toList()).forEach(mode1 -> {
        online.addAndGet((TheBridge.getWaiting(mode1) + TheBridge.getPlaying(mode1)));
      });
    } else {
      Arrays.stream(TheBridgeMode.values()).filter(theBridgeModeType -> theBridgeModeType.equals(TheBridgeMode.COMPSOLO) || theBridgeModeType.equals(TheBridgeMode.COMPDUPLA) || theBridgeModeType.equals(TheBridgeMode.COMPTRIO) || theBridgeModeType.equals(TheBridgeMode.COMPQUARTETO)).collect(Collectors.toList()).forEach(mode1 -> {
        online.addAndGet((TheBridge.getWaiting(mode1) + TheBridge.getPlaying(mode1)));
      });
    }
    return online;
  }

  public void update() {
    int size = this.mode == TheBridgeModeType.CASUAL ? Language.lobby$npc$play$casual$hologram.size() : Language.lobby$npc$play$comp$hologram.size();
    for (int index = size; index > 0; index--) {
      this.hologram.updateLine(size - (index - 1), (this.mode == TheBridgeModeType.CASUAL ? Language.lobby$npc$play$casual$hologram : Language.lobby$npc$play$comp$hologram).get(index - 1)
              .replace("{players}", StringUtils.formatNumber( getOnline().get())));
    }
  }
  
  public void destroy() {
    this.id = null;
    this.mode = null;
    this.location = null;
    
    this.npc.destroy();
    this.npc = null;
    HologramLibrary.removeHologram(this.hologram);
    this.hologram = null;
  }
  
  public String getId() {
    return id;
  }
  
  public TheBridgeModeType getMode() {
    return this.mode;
  }
  
  public Location getLocation() {
    return this.location;
  }
}
