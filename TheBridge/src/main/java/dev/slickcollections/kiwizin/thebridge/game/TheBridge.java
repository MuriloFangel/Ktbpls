package dev.slickcollections.kiwizin.thebridge.game;

import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.bukkit.BukkitParty;
import dev.slickcollections.kiwizin.bukkit.BukkitPartyManager;
import dev.slickcollections.kiwizin.game.FakeGame;
import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.game.GameState;
import dev.slickcollections.kiwizin.game.GameTeam;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.party.PartyPlayer;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.thebridge.Language;
import dev.slickcollections.kiwizin.thebridge.Main;
import dev.slickcollections.kiwizin.thebridge.container.RejoinContainer;
import dev.slickcollections.kiwizin.thebridge.container.SelectedContainer;
import dev.slickcollections.kiwizin.thebridge.cosmetics.CosmeticType;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.DeathCry;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.DeathMessage;
import dev.slickcollections.kiwizin.thebridge.cosmetics.types.KillEffect;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.listeners.player.AsyncPlayerChatListener;
import dev.slickcollections.kiwizin.thebridge.utils.tagger.TagUtils;
import dev.slickcollections.kiwizin.utils.CubeID;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static dev.slickcollections.kiwizin.thebridge.hook.TBCoreHook.hotbarGame;
import static dev.slickcollections.kiwizin.thebridge.hook.TBCoreHook.reloadScoreboard;

public class TheBridge implements Game<TheBridgeTeam> {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("GAME");
  public static final List<TheBridge> QUEUE = new ArrayList<>();
  private static final Map<String, TheBridge> GAMES = new HashMap<>();
  private String name;
  private TheBridgeConfig config;
  private int timer;
  private int time;
  private GameState state;
  private TheBridgeTask task;
  private List<UUID> players;
  private List<UUID> spectators;
  private Map<String, Integer> kills;
  protected boolean ambiguos;
  private boolean isPartyDuels1v1;
  
  public TheBridge(String name, LoadCallback callback) {
    this.name = name;
    this.timer = 11;
    this.time = 0;
    this.task = new TheBridgeTask(this);
    this.config = new TheBridgeConfig(this);
    this.config.setup(this);
    this.state = GameState.AGUARDANDO;
    this.players = new ArrayList<>();
    this.spectators = new ArrayList<>();
    this.kills = new HashMap<>();
    this.ambiguos = false;
    this.isPartyDuels1v1 = false;
    if (callback != null) {
      callback.finish();
    }
  }

  public static List<TheBridge> getGamesAvaliable(TheBridgeMode mode) {
    return GAMES.values().stream().filter(theBridge -> theBridge.getState().equals(GameState.AGUARDANDO) && theBridge.getOnline() == 0 && theBridge.getMode().equals(mode) && theBridge.listPlayers().size() == 0).collect(Collectors.toList());
  }

  public static void addToQueue(TheBridge game) {
    if (QUEUE.contains(game)) {
      return;
    }
    
    QUEUE.add(game);
  }
  
  public static void setupGames() {
    new ArenaRollbackerTask().runTaskTimer(Main.getInstance(), 0, 100);
    
    File ymlFolder = new File("plugins/kTheBridge/arenas");
    File mapFolder = new File("plugins/kTheBridge/mundos");
    
    if (!ymlFolder.exists() || !mapFolder.exists()) {
      if (!ymlFolder.exists()) {
        ymlFolder.mkdirs();
      }
      if (!mapFolder.exists()) {
        mapFolder.mkdirs();
      }
    }
    
    for (File file : ymlFolder.listFiles()) {
      load(file, null);
    }
    
    LOGGER.info("Foram carregadas " + GAMES.size() + " salas.");
  }
  
  public static void load(File yamlFile, LoadCallback callback) {
    String arenaName = yamlFile.getName().split("\\.")[0];
    
    try {
      File backup = new File("plugins/kTheBridge/mundos", arenaName);
      if (!backup.exists() || !backup.isDirectory()) {
        throw new IllegalArgumentException("Backup do mapa nao encontrado para a arena \"" + yamlFile.getName() + "\"");
      }
      
      GAMES.put(arenaName, new TheBridge(arenaName, callback));
    } catch (Exception ex) {
      LOGGER.log(Level.WARNING, "load(\"" + yamlFile.getName() + "\"): ", ex);
    }
  }
  
  public static TheBridge getByWorldName(String worldName) {
    return GAMES.get(worldName);
  }
  
  public static int getWaiting(TheBridgeMode mode) {
    int waiting = 0;
    List<TheBridge> games = listByMode(mode);
    for (TheBridge game : games) {
      if (game.getState() != GameState.EMJOGO) {
        waiting += game.getOnline();
      }
    }
    
    return waiting;
  }
  
  public static int getPlaying(TheBridgeMode mode) {
    int playing = 0;
    List<TheBridge> games = listByMode(mode);
    for (TheBridge game : games) {
      if (game.getState() == GameState.EMJOGO) {
        playing += game.getOnline();
      }
    }
    
    return playing;
  }
  
  public static TheBridge findRandom(TheBridgeMode mode) {
    List<TheBridge> games = GAMES.values().stream().filter(game -> game.getMode().equals(mode) && game.getState().canJoin() && game.getOnline() < game.getMaxPlayers())
        .sorted((g1, g2) -> Integer.compare(g2.getOnline(), g1.getOnline())).collect(Collectors.toList());
    TheBridge game = games.stream().findFirst().orElse(null);
    if (game != null && game.getOnline() == 0) {
      game = games.get(ThreadLocalRandom.current().nextInt(games.size()));
    }
    
    return game;
  }
  
  public static Map<String, List<TheBridge>> getAsMap(TheBridgeMode mode) {
    Map<String, List<TheBridge>> result = new HashMap<>();
    GAMES.values().stream().filter(game -> game.getMode().equals(mode) && game.getState().canJoin() && game.getOnline() < game.getMaxPlayers()).forEach(game -> {
      List<TheBridge> list = result.computeIfAbsent(game.getMapName(), k -> new ArrayList<>());
      
      if (game.getState().canJoin() && game.getOnline() < game.getMaxPlayers()) {
        list.add(game);
      }
    });
    
    return result;
  }
  
  public static List<TheBridge> listByMode(TheBridgeMode mode) {
    return GAMES.values().stream().filter(tb -> tb.getMode().equals(mode)).collect(Collectors.toList());
  }
  
  public void destroy() {
    this.name = null;
    this.config.destroy();
    this.config = null;
    this.timer = 0;
    this.time = 0;
    this.state = null;
    this.task.cancel();
    this.task = null;
    this.players.clear();
    this.players = null;
    this.spectators.clear();
    this.spectators = null;
    this.kills.clear();
    this.kills = null;
  }
  
  public void point(Profile profile, TheBridgeTeam team) {
    team.increaseScore();
    if (!this.getMode().name().contains("PARTY_DUELS")) {
      team.listPlayers().forEach(player -> Profile.getProfile(player.getName()).addCoinsWM("kCoreTheBridge", Language.options$coins$score));
      profile.addStats("kCoreTheBridge", this.getMode().getName() + "points");
      profile.addStats("kCoreTheBridge", "monthlypoints");
    }

    DecimalFormat df = new DecimalFormat("##.#");
    this.broadcastMessage(Language.ingame$broadcast$point.replace("{name}", team.getColor() + profile.getName()).replace("{player_life}", df.format(profile.getPlayer().getHealth())).replace("{red_points}", getTeamByName("RED").getPontuation()).replace("{blue_points}", getTeamByName("BLUE").getPontuation()));
    this.check();
    if (this.state == GameState.EMJOGO) {
      this.cage();
    }
  }
  
  public void spectate(Player player, Player target) {
    if (this.getState() == GameState.AGUARDANDO) {
      player.sendMessage("§cA partida ainda não começou.");
      return;
    }
    
    Profile profile = Profile.getProfile(player.getName());
    if (profile.playingGame()) {
      if (profile.getGame().equals(this)) {
        return;
      }
      
      profile.getGame().leave(profile, this);
    }
    
    profile.setGame(this);
    spectators.add(player.getUniqueId());
    
    player.teleport(target.getLocation());
    reloadScoreboard(profile);
    for (Player players : Bukkit.getOnlinePlayers()) {
      if (!players.getWorld().equals(player.getWorld())) {
        player.hidePlayer(players);
        players.hidePlayer(player);
        continue;
      }
      
      if (isSpectator(players)) {
        players.showPlayer(player);
      } else {
        players.hidePlayer(player);
      }
      player.showPlayer(players);
    }
    
    profile.setHotbar(Hotbar.getHotbarById("spectator"));
    profile.refresh();
    player.setGameMode(GameMode.ADVENTURE);
    player.spigot().setCollidesWithEntities(false);
    player.setAllowFlight(true);
    player.setFlying(true);
    this.updateTags();
  }
  
  @Override
  public void broadcastMessage(String message) {
    this.broadcastMessage(message, true);
  }
  
  @Override
  public void broadcastMessage(String message, boolean spectators) {
    this.listPlayers().forEach(player -> player.sendMessage(message));
  }

  public void disconnectPlayer(Player player) {
    this.listTeams().stream().filter(theBridgeTeam -> theBridgeTeam.listPlayers().contains(player)).findFirst().ifPresent(theBridgeTeam -> {
      theBridgeTeam.removeMember(player);
    });

    if (isPartyDuels1v1 || this.getMode().name().contains("SOLO")) {
      this.listTeams().stream().filter(team -> !team.listPlayers().contains(player)).findFirst().ifPresent(this::stop);
    }
  }

  public void joinParty(Profile profile, boolean ignoreLeader, boolean isPartyDuels) {
    Player player = profile.getPlayer();
    if (player == null || !this.state.canJoin() || this.players.size() >= this.getMaxPlayers()) {
      return;
    }
    
    if (profile.getGame() != null && profile.getGame().equals(this)) {
      return;
    }
    
    TheBridgeTeam team = null;
    boolean fullSize = false;
    BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
    RejoinContainer container = profile.getAbstractContainer("kCoreTheBridge", "rejoin", RejoinContainer.class);
    TheBridge game = container.getRejoinGame(player);
    if (game != null) {
      game.disconnectPlayer(player);
      return;
    }

    if (party != null) {
      if (!ignoreLeader) {
        if (!party.isLeader(player.getName())) {
          player.sendMessage("§cApenas o líder da Party pode buscar por partidas.");
          return;
        }
        
        if (party.onlineCount() + players.size() > getMaxPlayers()) {
          return;
        }
        
        fullSize = true;
        List<Profile> newPartyList = party.listMembers().stream().filter(PartyPlayer::isOnline).map(pp -> Profile.getProfile(pp.getName())).filter(pp -> pp != null && pp.getGame(FakeGame.class) == null).collect(Collectors.toList());
        for (int i = 0; i < 3; i++) {
          Collections.shuffle(newPartyList);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> newPartyList.forEach(pp -> joinParty(pp, true, isPartyDuels)), 5);
      } else {
        List<Profile> newPartyList = party.listMembers().stream().filter(PartyPlayer::isOnline).map(pp -> Profile.getProfile(pp.getName())).filter(pp -> pp != null && pp.getGame(FakeGame.class) == null).collect(Collectors.toList());
        List<TheBridgeTeam> teans = listTeams();
        for (int i = 0; i < 3; i++) {
          Collections.shuffle(newPartyList);
          Collections.shuffle(teans, new Random(listTeams().size()));
        }

        if (!isPartyDuels) {
          team = teans.stream().filter(tt -> tt.canJoin() && (newPartyList.stream().anyMatch(pp -> pp.isOnline() && tt.hasMember((Player) Manager.getPlayer(pp.getName())))))
                  .findAny().orElse(null);
        } else {
          int teamSize = Math.round((float) party.listMembers().size() / 2);
          team = teans.stream().filter(theBridgeTeam -> theBridgeTeam.listPlayers().size() < teamSize).findAny().orElse(null);
        }
      }
    }
    
    team = team == null ? getAvailableTeam(fullSize ? this.getMode().getSize() : 1) : team;
    if (team == null) {
      return;
    }
    
    team.addMember(player);
    if (profile.getGame() != null) {
      profile.getGame().leave(profile, profile.getGame());
    }
    
    this.players.add(player.getUniqueId());
    profile.setGame(this);
    
    player.teleport(team.getRespawn());
    reloadScoreboard(profile);
    
    profile.setHotbar(Hotbar.getHotbarById("waiting"));
    profile.refresh();
    for (Player players : Bukkit.getOnlinePlayers()) {
      if (!players.getWorld().equals(player.getWorld())) {
        player.hidePlayer(players);
        players.hidePlayer(player);
        continue;
      }
      
      if (isSpectator(players)) {
        player.hidePlayer(players);
      } else {
        player.showPlayer(players);
      }
      players.showPlayer(player);
    }
    
    this.broadcastMessage(Language.ingame$broadcast$join.replace("{player}", Role.getColored(player.getName())).replace("{players}", String.valueOf(this.getOnline()))
        .replace("{max_players}", String.valueOf(this.getMaxPlayers())));
  }

  @Override
  public void join(Profile profile) {}

  
  @Override
  public void leave(Profile profile, Game<?> game) {
    Player player = profile.getPlayer();
    if (player == null || profile.getGame() != this) {
      return;
    }
    
    TheBridgeTeam team = this.getTeam(player);
    boolean alive = this.players.contains(player.getUniqueId());
    this.players.remove(player.getUniqueId());
    this.spectators.remove(player.getUniqueId());

    if (team != null) {
      if (!this.state.equals(GameState.EMJOGO)) team.removeMember(player);
    }

    if (game != null) {
      if (alive) {
        if (this.state == GameState.EMJOGO) {
          List<Profile> hitters = profile.getLastHitters();
          this.killLeave(profile, hitters.size() > 0 ? hitters.get(0) : null);
          hitters.clear();
        }

        if (!this.getMode().name().contains("PARTY_DUELS")) profile.updateDailyStats("kCoreTheBridge", "laststreak", 0, "winstreak");
        profile.getAbstractContainer("kCoreTheBridge", "rejoin", RejoinContainer.class).setRejoinGame(this);
        if (getMode().name().contains("SOLO") || isPartyDuels1v1) {
          String oldKey = AsyncPlayerChatListener.findKeyByGame(this);
          String code = oldKey == null ? generateRandomKey(10) : oldKey;
          TextComponent principal = new TextComponent("§aSeu oponente saiu do servidor. Você pode esperar ele voltar ou ");
          TextComponent aqui = new TextComponent("§e§oclicar aqui para reinvindicar a vitória.");
          AsyncPlayerChatListener.addCacheGaming(code, this);
          aqui.setBold(true);
          aqui.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reinvidicar " + code));
          aqui.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClique aqui para ganhar")));
          principal.addExtra(aqui);

          if (this.players.size() == 1) {
            this.listTeams().stream().filter(theBridgeTeam -> !theBridgeTeam.listPlayers().contains(player)).findFirst().ifPresent(theBridgeTeam -> {
              theBridgeTeam.listPlayers().get(0).spigot().sendMessage(principal);
            });
          }

          new BukkitRunnable() {
            private final UUID backup = player.getUniqueId();

            @Override
            public void run() {
              if (!players.contains(backup)) {
                if (players.size() == 1) {
                  listTeams().stream().filter(theBridgeTeam -> !theBridgeTeam.listPlayers().contains(player)).findFirst().ifPresent(theBridgeTeam -> {
                    theBridgeTeam.listPlayers().get(0).spigot().sendMessage(principal);
                  });
                }
              }
            }
          }.runTaskLater(Main.getInstance(), 20 * 3L);
        }
      }

      if (Profile.isOnline(player.getName())) {
        profile.setGame(null);
        if (isPartyDuels1v1 || this.getMode().name().contains("SOLO")) {
          this.listTeams().stream().filter(teamb -> !teamb.listPlayers().contains(player)).findFirst().ifPresent(this::stop);
        }

        getTeam(player).removeMember(player);
        TagUtils.setTag(player, profile.getTagContainer().getSelectedRole());
      }

      if (this.state != GameState.ENCERRADO && alive) {
        this.broadcastMessage(Language.ingame$broadcast$leave.replace("{player}", Role.getColored(player.getName())).replace("{players}", String.valueOf(this.getOnline()))
            .replace("{max_players}", String.valueOf(this.getMaxPlayers())));
      }

      this.check();
      return;
    }

    profile.setGame(null);
    TagUtils.setTag(player, profile.getTagContainer().getSelectedRole());
    reloadScoreboard(profile);
    profile.setHotbar(Hotbar.getHotbarById("lobby"));
    profile.refresh();
    if (this.state != GameState.ENCERRADO && alive) {
      this.broadcastMessage(Language.ingame$broadcast$leave.replace("{player}", Role.getColored(player.getName())).replace("{players}", String.valueOf(this.getOnline()))
          .replace("{max_players}", String.valueOf(this.getMaxPlayers())));
    }

    this.check();
  }

  public void rejoin(Profile profile) {
    Player player = profile.getPlayer();

    this.players.add(player.getUniqueId());
    profile.setGame(this);
    reloadScoreboard(profile);
    profile.setHotbar(null);
    profile.getAbstractContainer("kCoreTheBridge", "rejoin", RejoinContainer.class).clearRejoinGame();
    player.setGameMode(GameMode.SURVIVAL);
    profile.update();
    kill(profile, null);
    Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()-> {
      for (Player players : Bukkit.getOnlinePlayers()) {
        if (!players.getWorld().equals(player.getWorld())) {
          player.hidePlayer(players);
          players.hidePlayer(player);
          continue;
        }

        if (isSpectator(players)) {
          player.hidePlayer(players);
        } else {
          player.showPlayer(players);
        }

        players.showPlayer(player);
      }

      broadcastMessage("§eO jogador " + player.getName() + " acabou de se reconectar.");
    }, 3L);
    updateTags();
  }

  private void cage() {
    GameState oldState = this.state;
    this.state = GameState.INICIANDO;
    this.listTeams().forEach(TheBridgeTeam::buildCage);
    if (oldState == GameState.AGUARDANDO) {
      this.listTeams().forEach(TheBridgeTeam::spawnBalloon);
    }
    this.listPlayers(false).forEach(player -> {
      Profile profile = Profile.getProfile(player.getName());
      if (oldState == GameState.AGUARDANDO) {
        reloadScoreboard(profile);
        profile.setHotbar(null);
        if (!this.getMode().name().contains("PARTY_DUELS")) profile.addStats("kCoreTheBridge", this.getMode().getName() + "games");
      }

      profile.update();
      profile.refresh();
      player.getInventory().clear();
      player.getInventory().setArmorContents(null);
      player.updateInventory();
      player.setGameMode(GameMode.ADVENTURE);
      player.teleport(this.getTeam(player).getLocation());
      hotbarGame(profile, this.getTeam(player));
    });

    this.task.swap(null);
  }

  @Override
  public void start() {
    if (this.state == GameState.AGUARDANDO) {
      this.cage();
      return;
    }
    
    this.state = GameState.EMJOGO;
    this.task.swap(null);
    if (this.time == 0) {
      this.time = 60 * 15;
    }
    
    this.listTeams().forEach(TheBridgeTeam::breakCage);
    
    for (Player player : this.listPlayers(false)) {
      Profile profile = Profile.getProfile(player.getName());
      reloadScoreboard(profile);
      player.setGameMode(GameMode.SURVIVAL);
    }

    this.isPartyDuels1v1 = this.players.size() == 2;
    this.updateTags();
    this.check();
  }
  
  @Override
  public void kill(Profile profile, Profile killer) {
    Player player = profile.getPlayer();
    TheBridgeTeam team = this.getTeam(player);
    
    Player pk = killer != null ? killer.getPlayer() : null;
    if (player.equals(pk)) {
      pk = null;
    }
    
    if (pk == null) {
      this.broadcastMessage(Language.ingame$broadcast$suicide.replace("{name}", team.getColor() + player.getName()));
    } else {
      if (player.getLastDamageCause() == null || player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.VOID) {
        KillEffect ke = killer.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).getSelected(CosmeticType.KILL_EFFECT, KillEffect.class);
        if (ke != null) {
          ke.execute(player.getLocation());
        }
      }
      
      this.addKills(pk);
      EnumSound.ORB_PICKUP.play(pk, 1.0F, 1.0F);
      if (!this.getMode().name().contains("PARTY_DUELS")) {
        killer.addCoinsWM("kCoreTheBridge", Language.options$coins$kills);
        killer.addStats("kCoreTheBridge", this.getMode().getName() + "kills");
        killer.addStats("kCoreTheBridge", "monthlykills");
      }
      profile.refresh();
      player.teleport(team.getRespawn());
  
      DeathMessage dm = killer.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).getSelected(CosmeticType.DEATH_MESSAGE, DeathMessage.class);
      if (dm != null) {
        this.broadcastMessage(dm.getRandomMessage().replace("{name}", team.getColor() + player.getName()).replace("{killer}", this.getTeam(pk).getColor() + pk.getName()));
      } else {
        this.broadcastMessage(Language.ingame$broadcast$killed.replace("{name}", team.getColor() + player.getName()).replace("{killer}", this.getTeam(pk).getColor() + pk.getName()));
      }
    }
    
    DeathCry dc = profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class)
        .getSelected(CosmeticType.DEATH_CRY, DeathCry.class);
    if (dc != null) {
      dc.getSound().play(player.getWorld(), player.getLocation(), dc.getVolume(), dc.getSpeed());
    }
    
    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      if (player.isOnline()) {
        profile.refresh();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        hotbarGame(profile, team);
        player.teleport(team.getRespawn());
      }
    }, 2);
    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      if (player.isOnline()) {
        profile.refresh();
        player.teleport(team.getRespawn());
      }
    }, 1);

    if (!this.getMode().name().contains("PARTY_DUELS")) profile.addStats("kCoreTheBridge", this.getMode().getName() + "deaths");
    this.check();
  }
  
  @Override
  public void killLeave(Profile profile, Profile killer) {
    Player player = profile.getPlayer();
    TheBridgeTeam team = this.getTeam(player);
    
    Player pk = killer != null ? killer.getPlayer() : null;
    if (player.equals(pk)) {
      pk = null;
    }
    
    if (pk == null) {
      this.broadcastMessage(Language.ingame$broadcast$suicide.replace("{name}", team.getColor() + player.getName()));
    } else {
      this.addKills(pk);
      EnumSound.ORB_PICKUP.play(pk, 1.0F, 1.0F);
      if (!this.getMode().name().contains("PARTY_DUELS")) {
        killer.addCoinsWM("kCoreTheBridge", Language.options$coins$kills);
        killer.addStats("kCoreTheBridge", this.getMode().getName() + "kills");
        killer.addStats("kCoreTheBridge", "monthlykills");
      }
  
      DeathMessage dm = killer.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).getSelected(CosmeticType.DEATH_MESSAGE, DeathMessage.class);
      if (dm != null) {
        this.broadcastMessage(dm.getRandomMessage().replace("{name}", team.getColor() + player.getName()).replace("{killer}", this.getTeam(pk).getColor() + pk.getName()));
      } else {
        this.broadcastMessage(Language.ingame$broadcast$killed.replace("{name}", team.getColor() + player.getName()).replace("{killer}", this.getTeam(pk).getColor() + pk.getName()));
      }
    }
    
    DeathCry dc = profile.getAbstractContainer("kCoreTheBridge", "selected", SelectedContainer.class).getSelected(CosmeticType.DEATH_CRY, DeathCry.class);
    if (dc != null) {
      dc.getSound().play(player.getWorld(), player.getLocation(), dc.getVolume(), dc.getSpeed());
    }

    if (!this.getMode().name().contains("PARTY_DUELS")) {
      profile.updateDailyStats("kCoreTheBridge", "laststreak", 0, "winstreak");
      profile.addStats("kCoreTheBridge", this.getMode().getName() + "deaths");
    }
    this.check();
  }
  
  private void check() {
    if (this.state != GameState.EMJOGO) {
      return;
    }
    
    TheBridgeTeam winner = null;
    long count = this.listTeams().stream().filter(theBridgeTeam -> theBridgeTeam.listPlayers().stream().anyMatch(player -> this.players.contains(player.getUniqueId()))).count();
    if (count == 0 || (winner = this.listTeams().stream().filter(team -> team.getScore() >= 5).findFirst().orElse(null)) != null) {
      if (count == 0) {
        this.stop(null);
        return;
      }
      
      this.stop(winner);
    }
  }
  
  @Override
  public void stop(TheBridgeTeam winners) {
    this.state = GameState.ENCERRADO;
    AsyncPlayerChatListener.clearCache(this);

    StringBuilder name = new StringBuilder("§cNinguém");
    List<Player> players;
    if (ambiguos) {
      players = listPlayers();
    } else {
      players = winners != null ? winners.listPlayers() : null;
    }
    if (winners != null || ambiguos) {
      for (Player player : players) {
        TheBridgeTeam team = getTeam(player);
        if (!name.toString().equals("§cNinguém")) {
          if (ambiguos) {
            name.append(" §ae ").append(team.getColor() ).append(player.getName());
          } else {
            name.append(" §ae ").append(winners.getColor() ).append(player.getName());
          }
        } else {
          name = new StringBuilder(ambiguos ? team.getColor() + player.getName() : winners.getColor() + player.getName());
        }
      }

      players.clear();
    }
    this.broadcastMessage((this.getMode() == TheBridgeMode.SOLO ? Language.ingame$broadcast$win$solo : Language.ingame$broadcast$win$dupla).replace("{name}", name.toString()));
    for (Player player : this.listPlayers(false)) {
      Profile profile = Profile.getProfile(player.getName());
      profile.update();
      TheBridgeTeam team = this.getTeam(player);
      if (!this.getMode().name().contains("PARTY_DUELS")) {
        if (team != null) {
          int coinsWin = (int) (ambiguos ? profile.calculateWM(Language.options$coins$wins) : team.equals(winners) ? profile.calculateWM(Language.options$coins$wins) : 0);
          int coinsKill = (int) profile.calculateWM(this.getKills(player) * Language.options$coins$kills);
          int coinsScore = (int) profile.calculateWM(team.getScore() * Language.options$coins$score);
          int totalCoins = coinsWin + coinsKill + coinsScore;

          if (totalCoins > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> player.sendMessage(
                    Language.ingame$messages$coins$base.replace("{coins}", StringUtils.formatNumber(totalCoins))
                            .replace("{coins_win}", coinsWin > 0 ? Language.ingame$messages$coins$win.replace("{coins}", StringUtils.formatNumber(coinsWin)) : "").replace("{coins_points}",
                                    coinsScore > 0 ?
                                            Language.ingame$messages$coins$points.replace("{coins}", StringUtils.formatNumber(coinsScore)).replace("{points}", StringUtils.formatNumber(team.getScore())) :
                                            "").replace("{coins_kills}", coinsKill > 0 ?
                                    Language.ingame$messages$coins$kills.replace("{coins}", StringUtils.formatNumber(coinsKill)).replace("{kills}", StringUtils.formatNumber(this.getKills(player))) :
                                    "")), 30);
          }
        }

        if (ambiguos && team != null || team != null && team.equals(winners)) {
          profile.addCoinsWM("kCoreTheBridge", Language.options$coins$wins);
          profile.addStats("kCoreTheBridge", this.getMode().getName() + "wins");
          profile.addStats("kCoreTheBridge", "monthlywins");
          profile.updateDailyStats("kCoreTheBridge", "laststreak", 1, "winstreak");
        } else {
          profile.updateDailyStats("kCoreTheBridge", "laststreak", 0, "winstreak");
        }
      }

      String header = ambiguos ? Language.ingame$titles$end$header.replace("{winner}", team.getColor() + "" + team.getColorD().toUpperCase(Locale.ROOT)) :  winners != null ? Language.ingame$titles$end$header.replace("{winner}", winners.getColor() + "" + winners.getColorD().toUpperCase(Locale.ROOT)) : Language.ingame$titles$end$header.replace("{winner}", "§7NINGUÉM");
      String footer = Language.ingame$titles$end$footer.replace("{red_points}", getTeamByName("RED").getPontuation()).replace("{blue_points}", getTeamByName("BLUE").getPontuation());
      NMS.sendTitle(player, header, footer, 10, 80, 10);

      this.spectators.add(player.getUniqueId());
      profile.setHotbar(Hotbar.getHotbarById("spectator"));
      profile.refresh();
      player.setGameMode(GameMode.ADVENTURE);
      player.setAllowFlight(true);
      player.setFlying(true);
      if (team != null && ambiguos || team != null && team.equals(winners)) {
        player.teleport(team.getLocation());
      }
    }

    this.updateTags();
    this.task.swap(winners);
  }
  
  @Override
  public void reset() {
    this.kills.clear();
    this.players.clear();
    this.spectators.clear();
    this.task.cancel();
    this.listTeams().forEach(TheBridgeTeam::reset);
    addToQueue(this);
    this.time = 0;
  }
  
  private void updateTags() {
    for (Player player : this.listPlayers()) {
      Scoreboard scoreboard = player.getScoreboard();
      
      for (Player players : this.listPlayers()) {
        TheBridgeTeam gt;
        
        if (this.isSpectator(players)) {
          Team team = scoreboard.getEntryTeam(players.getName());
          if (team != null && !team.getName().equals("spectators")) {
            team.unregister();
            team = null;
          }
          
          if (team == null) {
            team = scoreboard.getTeam("spectators");
            if (team == null) {
              team = scoreboard.registerNewTeam("spectators");
              team.setPrefix("§8");
              team.setAllowFriendlyFire(true);
            }
            
            if (!team.hasEntry(players.getName())) {
              team.addEntry(players.getName());
            }
          }
        } else if ((gt = this.getTeam(players)) != null) {
          Team team = scoreboard.getTeam(gt.getName());
          if (team == null) {
            team = scoreboard.registerNewTeam(gt.getName());
            team.setPrefix(gt.getColor());
          }
          
          if (!team.hasEntry(players.getName())) {
            team.addEntry(players.getName());
          }
        }
      }
    }
  }
  
  @Override
  public String getGameName() {
    return this.name;
  }
  
  public int getTimer() {
    return this.timer;
  }
  
  public void setTimer(int timer) {
    this.timer = timer;
  }
  
  public TheBridgeConfig getConfig() {
    return this.config;
  }
  
  public TheBridgeTask getTask() {
    return this.task;
  }
  
  public CubeID getBridgeCube() {
    return this.config.getBridgeCube();
  }
  
  public String getMapName() {
    return this.config.getMapName();
  }
  
  public TheBridgeMode getMode() {
    return this.config.getMode();
  }
  
  @Override
  public GameState getState() {
    return this.state;
  }
  
  public void setState(GameState state) {
    this.state = state;
  }

  @Override
  public boolean isSpectator(Player player) {
    return this.spectators.contains(player.getUniqueId());
  }

  public void addKills(Player player) {
    this.kills.put(player.getName(), this.getKills(player) + 1);
  }
  
  public int getKills(Player player) {
    return this.kills.get(player.getName()) != null ? kills.get(player.getName()) : 0;
  }
  
  @Override
  public int getOnline() {
    return this.players.size();
  }
  
  @Override
  public int getMaxPlayers() {
    return this.listTeams().size() * this.getMode().getSize();
  }

  public TheBridgeTeam getAvailableTeam(int teamSize) {
    List<TheBridgeTeam> teans = this.listTeams().stream().filter(team -> team.canJoin(teamSize)).collect(Collectors.toList());
    return teans.get(ThreadLocalRandom.current().nextInt(teans.size()));
  }
  
  @Override
  public TheBridgeTeam getTeam(Player player) {
    return this.listTeams().stream().filter(team -> team.hasMember(player)).findAny().orElse(null);
  }
  
  public TheBridgeTeam getTeamNear(Location location) {
    return this.listTeams().stream().min(Comparator.comparingInt(t -> (int) location.distance(t.getRespawn()))).orElse(null);
  }
  
  @Override
  public List<TheBridgeTeam> listTeams() {
    return this.config.listTeams();
  }
  
  @Override
  public List<Player> listPlayers() {
    return this.listPlayers(true);
  }
  
  @Override
  public List<Player> listPlayers(boolean spectators) {
    List<Player> players = new ArrayList<>(spectators ? this.spectators.size() + this.players.size() : this.players.size());
    this.players.forEach(id -> players.add(Bukkit.getPlayer(id)));
    if (spectators) {
      this.spectators.stream().filter(name -> !this.players.contains(name)).forEach(id -> players.add(Bukkit.getPlayer(id)));
    }
    
    return players.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getTime() {
    return this.time;
  }

  public TheBridgeTeam getTeamByName(String typeCollor) {
    return listTeams().stream().filter(theBridgeTeam -> theBridgeTeam.getColorD().equalsIgnoreCase(typeCollor)).findFirst().orElse(null);
  }

  private String generateRandomKey(int length) {
    StringBuilder key = new StringBuilder();
    for (int i = 0; i < length; i++) {
      key.append((char) (Math.random() * 24) + 'a');
    }

    return key.toString();
  }
}