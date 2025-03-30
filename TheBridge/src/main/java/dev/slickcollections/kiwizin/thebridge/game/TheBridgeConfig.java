package dev.slickcollections.kiwizin.thebridge.game;

import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.CubeID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static dev.slickcollections.kiwizin.thebridge.utils.VoidChunkGenerator.VOID_CHUNK_GENERATOR;

public class TheBridgeConfig {
  
  private KConfig config;
  
  private String yaml;
  private World world;
  private String name;
  private TheBridgeMode mode;
  private List<TheBridgeTeam> teams;
  private CubeID bridgeCube;
  
  public TheBridgeConfig(TheBridge game) {
    this.yaml = game.getGameName();
    this.config = Main.getInstance().getConfig("arenas", this.yaml);
    this.name = this.config.getString("name");
    this.mode = TheBridgeMode.fromName(this.config.getString("mode"));
    this.teams = new ArrayList<>();
    this.bridgeCube = new CubeID(config.getString("bridge-cube"));
    this.reload(null);
  }
  
  public void setBalloon(int index, Location location) {
    this.config.set((index == 0 ? "red" : "blue") + ".balloon", BukkitUtils.serializeLocation(location));
    this.teams.get(index).setBalloon(BukkitUtils.serializeLocation(location));
  }
  
  protected void setup(TheBridge game) {
    this.teams.add(new TheBridgeTeam(game, this.config.getString("red.spawn"), this.config.getString("red.respawn"), this.config.getString("red.balloon"), this.mode.getSize()));
    this.teams.add(new TheBridgeTeam(game, this.config.getString("blue.spawn"), this.config.getString("blue.respawn"), this.config.getString("blue.balloon"), this.mode.getSize()));
  }
  
  public void destroy() {
    if ((this.world = Bukkit.getWorld(this.yaml)) != null) {
      Bukkit.unloadWorld(this.world, false);
    }
    
    Main.getInstance().getFileUtils().deleteFile(new File(this.yaml));
    this.yaml = null;
    this.name = null;
    this.mode = null;
    this.teams.clear();
    this.teams = null;
    this.bridgeCube = null;
    this.world = null;
    this.config = null;
  }
  
  public void reload(final Runnable async) {
    File file = new File("plugins/kTheBridge/mundos/" + this.yaml);
    if ((this.world = Bukkit.getWorld(file.getName())) != null) {
      Bukkit.unloadWorld(this.world, false);
    }
    
    Runnable delete = () -> {
      Main.getInstance().getFileUtils().deleteFile(new File(file.getName()));
      Main.getInstance().getFileUtils().copyFiles(file, new File(file.getName()));
      
      Runnable newWorld = () -> {
        WorldCreator wc = WorldCreator.name(file.getName());
        wc.generator(VOID_CHUNK_GENERATOR);
        wc.generateStructures(false);
        this.world = wc.createWorld();
        this.world.setTime(0L);
        this.world.setStorm(false);
        this.world.setThundering(false);
        this.world.setAutoSave(false);
        this.world.setAnimalSpawnLimit(0);
        this.world.setWaterAnimalSpawnLimit(0);
        this.world.setKeepSpawnInMemory(false);
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("mobGriefing", "false");
        this.world.getEntities().stream().filter(entity -> !(entity instanceof Player)).forEach(Entity::remove);
        if (async != null) {
          async.run();
        }
      };
      
      if (async == null) {
        newWorld.run();
        return;
      }
      
      Bukkit.getScheduler().runTask(Main.getInstance(), newWorld);
    };
    
    if (async == null) {
      delete.run();
      return;
    }
    
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), delete);
  }
  
  public KConfig getConfig() {
    return this.config;
  }
  
  public String getMapName() {
    return this.name;
  }
  
  public TheBridgeMode getMode() {
    return this.mode;
  }
  
  public List<TheBridgeTeam> listTeams() {
    return this.teams;
  }
  
  public CubeID getBridgeCube() {
    return this.bridgeCube;
  }
}
