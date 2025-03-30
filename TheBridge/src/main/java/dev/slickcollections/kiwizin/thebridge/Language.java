package dev.slickcollections.kiwizin.thebridge;

import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.plugin.config.KWriter;
import dev.slickcollections.kiwizin.plugin.config.KWriter.YamlEntry;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@SuppressWarnings("rawtypes")
public class Language {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger())
      .getModule("LANGUAGE");
  private static final KConfig CONFIG = Main.getInstance().getConfig("language");
  public static int options$coins$wins = 50;
  public static int options$coins$kills = 5;
  public static int options$coins$score = 10;
  public static long scoreboards$scroller$every_tick = 1;
  public static List<String> scoreboards$scroller$titles = Arrays
      .asList("§a§lTHE BRIDGE", "§f§l§6§lT§a§lHE BRIDGE", "§f§lT§6§lH§a§lE BRIDGE",
          "§f§lTH§6§lE §a§lBRIDGE", "§f§lTHE §6§lB§a§lRIDGE", "§f§lTHE B§6§lR§a§lIDGE",
          "§f§lTHE BR§6§lI§a§lDGE", "§f§lTHE BRI§6§lD§a§lGE", "§f§lTHE BRID§6§lG§a§lE",
          "§f§lTHE BRIDG§6§lE", "§f§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§f§lTHE BRIDGE", "§f§lTHE BRIDGE",
          "§f§lTHE BRIDGE", "§f§lTHE BRIDGE", "§f§lTHE BRIDGE", "§f§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§f§lTHE BRIDGE", "§f§lTHE BRIDGE", "§f§lTHE BRIDGE",
          "§f§lTHE BRIDGE", "§f§lTHE BRIDGE", "§f§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE",
          "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE", "§a§lTHE BRIDGE");
  public static String scoreboards$time$waiting = "Aguardando...";
  public static String scoreboards$time$starting = "Iniciando em §a{time}s";
  public static List<String> scoreboards$lobby = Arrays
      .asList("", "  Vitórias: §a%kCore_TheBridge_wins%", "  Abates: §a%kCore_TheBridge_kills%",
          "  Pontos: §a%kCore_TheBridge_points%", "  Winstreak: §a%kCore_TheBridge_winstreak%", "",
          "  Cash: §b%kCore_cash%", "  Coins: §6%kCore_TheBridge_coins%", "", "  §7www.redeslick.com", "");
  public static List<String> scoreboards$waiting =
      Arrays
          .asList("", "  Mapa: §a{map}", "  Modo: §a{mode}", "", "  Jogadores: §a{players}/{max_players}",
              "", "  {time}", "", "  §7www.redeslick.com", "");
  public static List<String> scoreboards$ingame = Arrays
      .asList("", "  §c§lV §fVermelho §7(§a{red}/5§7)", "  §9§lA §fAzul §7(§a{blue}/5§7)", "",
          "  Abates: §a{kills}", "  Pontos: §a{points}", "", "  Modo: §a{mode}", "  Mapa: §a{map}", "",
          "  §7www.redeslick.com", "");
  public static String cosmetics$color$locked = "§a";
  public static String cosmetics$color$canbuy = "§e";
  public static String cosmetics$color$unlocked = "§a";
  public static String cosmetics$color$selected = "§6";
  public static String cosmetics$icon$perm_desc$common = "§cVocê não possui permissão.";
  public static String cosmetics$icon$perm_desc$role = "§7Exclusivo para {role} §7ou superior.";
  public static String cosmetics$icon$buy_desc$enough = "§cVocê não possui saldo suficiente.";
  public static String cosmetics$icon$buy_desc$click_to_buy = "§eClique para comprar!";
  public static String cosmetics$icon$has_desc$select = "§eClique para selecionar!";
  public static String cosmetics$icon$has_desc$selected = "§eClique para deselecionar!";
  public static String cosmetics$perk$icon$perm_desc$start = "\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$perk$icon$buy_desc$start = "\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$perk$icon$has_desc$start = "\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$kill_effect$icon$perm_desc$start = "\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$kill_effect$icon$buy_desc$start =
      "\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$kill_effect$icon$has_desc$start = "\n \n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$deathcry$icon$perm_desc$start =
      "§7Toca o grito de morte {name}\n§7quando você morre.\n \n§6Clique direito para escutar!\n  \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$deathcry$icon$buy_desc$start =
      "§7Toca o grito de morte {name}\n§7quando você morre.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n§6Clique direito para escutar!\n \n{buy_desc_status}";
  public static String cosmetics$deathcry$icon$has_desc$start =
      "§7Toca o grito de morte {name}\n§7quando você morre.\n \n§6Clique direito para escutar!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$block$icon$perm_desc$start = "§7Altera seu bloco de construção\n§7para {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$block$icon$buy_desc$start =
      "§7Altera seu bloco de construção\n§7para {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$block$icon$has_desc$start = "§7Altera seu bloco de construção\n§7para {name}.\n \n§fRaridade: {rarity}\n  \n{has_desc_status}";
  public static String cosmetics$deathmessage$icon$perm_desc$start =
      "\n§6Clique direito para ver!\n \n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$deathmessage$icon$buy_desc$start =
      "\n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$deathmessage$icon$has_desc$start =
      "\n§6Clique direito para ver!\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$balloon$icon$perm_desc$start = "§7Altera o balão decorativo da ilha\n§7para {name}.\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$balloon$icon$buy_desc$start =
      "§7Altera o balão decorativo da ilha\n§7para {name}.\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$balloon$icon$has_desc$start = "§7Altera o balão decorativo da ilha\n§7para {name}.\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String cosmetics$win_animation$icon$perm_desc$start = "\n \n§fRaridade: {rarity}\n \n{perm_desc_status}";
  public static String cosmetics$win_animation$icon$buy_desc$start = "\n \n§fRaridade: {rarity}\n§fCusto: §6{coins} Coins §7ou §b{cash} Cash\n \n{buy_desc_status}";
  public static String cosmetics$win_animation$icon$has_desc$start = "\n \n§fRaridade: {rarity}\n \n{has_desc_status}";
  public static String chat$delay = "§cAguarde mais {time}s para falar novamente.";
  public static String chat$color$default = "§7";
  public static String chat$color$custom = "§f";
  public static String chat$format$lobby = "{player}{color}: {message}";
  public static String chat$format$spectator = "§8[Espectador] {player}{color}: {message}";
  public static String lobby$achievement = " \n§aVocê concluiu o desafio: §f{name}\n ";
  public static String lobby$broadcast = "{player} §6entrou no lobby!";
  public static boolean lobby$tab$enabled = true;
  public static String lobby$tab$header = " \n§b§lREDE SLICK\n  §fredeslick.com\n ";
  public static String lobby$tab$footer =
      " \n \n§aForúm: §fredeslick.com/forum\n§aTwitter: §f@RedeSlick\n§aDiscord: §fredeslick.com/discord\n \n                                          §bAdquira VIP acessando: §floja.redeslick.com                                          \n ";
  public static long lobby$leaderboard$minutes = 30;
  public static String lobby$leaderboard$empty = "§7Ninguém";
  public static List<String> lobby$leaderboard$wins$holograms = Arrays
      .asList("{monthly_color}Mensal {total_color}Total", "§6§lClique para alternar!", "§a10. {name_10} §7- §a{stats_10}", "§a9. {name_9} §7- §a{stats_9}",
          "§a8. {name_8} §7- §a{stats_8}", "§a7. {name_7} §7- §a{stats_7}",
          "§a6. {name_6} §7- §a{stats_6}",
          "§a5. {name_5} §7- §a{stats_5}", "§a4. {name_4} §7- §a{stats_4}",
          "§a3. {name_3} §7- §a{stats_3}", "§a2. {name_2} §7- §a{stats_2}",
          "§a1. {name_1} §7- §a{stats_1}", "",
          "§7Ranking de Vitórias", "§f§lTodos os Modos");
  public static List<String> lobby$leaderboard$kills$holograms = Arrays
      .asList("{monthly_color}Mensal {total_color}Total", "§6§lClique para alternar!", "§a10. {name_10} §7- §a{stats_10}", "§a9. {name_9} §7- §a{stats_9}",
          "§a8. {name_8} §7- §a{stats_8}", "§a7. {name_7} §7- §a{stats_7}",
          "§a6. {name_6} §7- §a{stats_6}",
          "§a5. {name_5} §7- §a{stats_5}", "§a4. {name_4} §7- §a{stats_4}",
          "§a3. {name_3} §7- §a{stats_3}", "§a2. {name_2} §7- §a{stats_2}",
          "§a1. {name_1} §7- §a{stats_1}", "",
          "§7Ranking de Abates", "§f§lTodos os Modos");
  public static List<String> lobby$leaderboard$points$holograms = Arrays
      .asList("{monthly_color}Mensal {total_color}Total", "§6§lClique para alternar!", "§a10. {name_10} §7- §a{stats_10}", "§a9. {name_9} §7- §a{stats_9}",
          "§a8. {name_8} §7- §a{stats_8}", "§a7. {name_7} §7- §a{stats_7}",
          "§a6. {name_6} §7- §a{stats_6}",
          "§a5. {name_5} §7- §a{stats_5}", "§a4. {name_4} §7- §a{stats_4}",
          "§a3. {name_3} §7- §a{stats_3}", "§a2. {name_2} §7- §a{stats_2}",
          "§a1. {name_1} §7- §a{stats_1}", "",
          "§7Ranking de Pontuação", "§f§lTodos os Modos");
  public static List<String> lobby$leaderboard$daily_winstreak$holograms = Arrays
      .asList("§a10. {name_10} §7- §a{stats_10}", "§a9. {name_9} §7- §a{stats_9}",
          "§a8. {name_8} §7- §a{stats_8}", "§a7. {name_7} §7- §a{stats_7}",
          "§a6. {name_6} §7- §a{stats_6}",
          "§a5. {name_5} §7- §a{stats_5}", "§a4. {name_4} §7- §a{stats_4}",
          "§a3. {name_3} §7- §a{stats_3}", "§a2. {name_2} §7- §a{stats_2}",
          "§a1. {name_1} §7- §a{stats_1}", "",
          "§7Winstreak Diário", "§f§lTodos os Modos");
  public static String lobby$npc$play$connect = "§aConectando...";
  public static String lobby$npc$play$menu$info$item = "PAPER : 1 : nome>§aInformações : desc>{desc}";
  public static String lobby$npc$play$menu$info$desc_limit =
      "§fLimite Diário: §7{limit}\n \n§7Jogadores que possuem o grupo §aVIP §7ou\n§7superior, podem escolher o mapa sem\n§7limite algum.\n \n&7www.redeslick.com/loja";
  public static String lobby$npc$play$menu$info$desc_not_limit = "§7Você não possui limite diário de seleções.";
  public static String lobby$npc$deliveries$deliveries = "§c{deliveries} Entrega{s}";
  public static List<String> lobby$npc$deliveries$hologram = Arrays
      .asList("{deliveries}", "§bEntregador", "§e§lCLIQUE DIREITO");
  public static List<String> lobby$npc$stats$hologram = Arrays
      .asList("§6Estatísticas", "Total de Eliminações: §7%kCore_TheBridge_kills%", "Total de Vitórias: §7%kCore_TheBridge_wins%", "§e§lCLIQUE DIREITO");
  public static List<String> lobby$npc$play$casual$hologram = Arrays
      .asList("§bCasual", "§a{players} Jogadores");
  public static List<String> lobby$npc$play$comp$hologram = Arrays
      .asList("§bCompetitivo", "§a{players} Jogadores");
  public static String lobby$npc$deliveries$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1ODM0NTc4OTkzMTksInByb2ZpbGVJZCI6IjIxMWNhN2E4ZWFkYzQ5ZTVhYjBhZjMzMTBlODY0M2NjIiwicHJvZmlsZU5hbWUiOiJNYXh0ZWVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85MWU0NTc3OTgzZjEzZGI2YTRiMWMwNzQ1MGUyNzQ2MTVkMDMyOGUyNmI0MGQ3ZDMyMjA3MjYwOWJmZGQ0YTA4IiwibWV0YWRhdGEiOnsibW9kZWwiOiJzbGltIn19fX0=";
  public static String lobby$npc$deliveries$skin$signature =
      "SXnMF3f9x90fa+FdP2rLk/V6/zvMNuZ0sC4RQpPHF9JxdVWYRZm/+DhxkfjCHWKXV/4FSTN8LPPsxXd0XlYSElpi5OaT9/LGhITSK6BbeBfaYhLZnoD0cf9jG9nl9av38KipnkNXI+cO3wttB27J7KHznAmfrJd5bxdO/M0aGQYtwpckchYUBG6pDzaxN7tr4bFxDdxGit8Tx+aow/YtYSQn4VilBIy2y/c2a4PzWEpWyZQ94ypF5ZojvhaSPVl88Fbh+StdgfJUWNN3hNWt31P68KT4Jhx+SkT2LTuDj0jcYsiuxHP6AzZXtOtPPARqM0/xd53CUHCK+TEF5mkbJsG/PZYz/JRR1B1STk4D2cgbhunF87V4NLmCBtF5WDQYid11eO0OnROSUbFduCLj0uJ6QhNRRdhSh54oES7vTi0ja3DftTjdFhPovDAXQxCn+ROhTeSxjW5ZvP6MpmJERCSSihv/11VGIrVRfj2lo9MaxRogQE3tnyMNKWm71IRZQf806hwSgHp+5m2mhfnjYeGRZr44j21zqnSKudDHErPyEavLF83ojuMhNqTTO43ri3MVbMGix4TbIOgB2WDwqlcYLezENBIIkRsYO/Y1r5BWCA7DJ5IlpxIr9TCu39ppVmOGReDWA/Znyox5GP6JIM53kQoTOFBM3QWIQcmXll4=";
  public static String lobby$npc$play$casual$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1ODQyNDcwOTU5MDksInByb2ZpbGVJZCI6Ijc0NDE2MjQxNjAzZTRkYzRiYTVmZjU2NGE5YjIwNjZiIiwicHJvZmlsZU5hbWUiOiJUYWtpUGFuMzU0MiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRiZTQ3NDhlZTY4N2MxMDRkYTBlMzUxMzkxNDFlMWRlNTU4ZGQ1MDBmYTNhYWMyZjZhNWFkNDFjNTI0MmFjZSJ9fX0=";
  public static String lobby$npc$play$casual$skin$signature =
      "h7oIZHFsV4xTpzegVKb0am7DSps6oDu/HkUaP87ptuABdAjECT//eKIXJt8RRYNWUJhnpGOAycxv6KETZuezfBA7yGKnP+IEyPJXMqM0KMRlMFYIoLyDnjqoeScjgVzNV0/amXF+IZrzIB/c0htsM3CdBGDsJqZBn4M7FRijlDxMrdrOJOVnx1ZA5PB07sxcg+svCqrEHMYXrIzFPRZaZqOiszV/L6g4nLijSYl8gyM3AgV7NDTV4vQddGgQMy8PRxoMzF7Wr9QMgJuCGzHh+9mJKXyCWrVk3exf6mK8miDdQim+Y+iAz+7dEbj2LHR8ij9RmAMQgtKfn6zQMQowo0rdoWLkBYj6Owr8R8+f7OsWMBzop2qXzfkaO2FBgBIHaBVlGt/h5qCYPeLsyF6PgjDLxsmmZo85necDKpJWtQf196psLD3hI6eJU6ycFjJ9HEJKXiC7gkAnvOajOjG4ozYKQxGBU8oW+2MCgE9zmNGh4Om6FQzwc4x6hsu9uNKy0HDbpen29VAm2gQAtAovbjlNrz3djTaVXHIO44I7GDLr3uphvYr/Lf1/pj3zU0OqQLPdnDhQJF8+5kvACyolzPAT1xwt1Kl4M+6BMvxKCs3ZZqBH9+cH4UQza43nJVdSTJUX6BhW34zmiEbIBVzZmIBDZXRHzKpx4EsqEgMPGPA=";
  public static String lobby$npc$play$comp$skin$value =
      "eyJ0aW1lc3RhbXAiOjE1ODQyNDcyNDQwMTksInByb2ZpbGVJZCI6IjUyZTZlMWI1YTczYzQ1OWRiZmIwOGE0MjEyNTVjZWEwIiwicHJvZmlsZU5hbWUiOiJCdWlsZF9WaWxsYWluIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jNDMyYTEzMWQxNWI1M2UzYTUyMDU5YjM1ZmIzNDMyMjUxZWViYWM4ZjZmZGE5ZDIyYWEwM2IxYTliNGE4MTBhIn19fQ==";
  public static String lobby$npc$play$comp$skin$signature =
      "omvzBV7bhA9KuwbGC9qH5pCu1xmN4Pw5bJpmF11WT40R3y4285Jf2PI4IjlUmZy4J92+Ot9+VrVdMXoaR4AD8i9EL07OwneSqBzAB/t/qeLI3/mtXeUp7bzQs6F7xgoGByMvcq1chQlwPvLAVmRuwCGIuBoSyf6XOfAme2RiS3YMi0hmUrYPRWL7SvcExB/U8tACJ8xO+Ts7xvag7VtY+RWj3xVC+imZ1rSeXJ26QBk/pDxC2ni5CinC6M/iNHxTQ12TOP/TXMcLK1ms0pttOQbIJ5RDDes65jckLBCkrf8zjabjN7NzdIGgaRUj2dDbDT3DfMiJafWb9shsh7iSCRUni0DO2oHzCXZjCATnBXvoGN5+ym9aryywErgL3GfcEdT50ankUhxXLEfxSEHsqu3d9/HLF5V5m9eSgQi0Cpnb5q9eZ90nSNt2TEYRrjV7o/eaoaJyfCtzIqkKnfBZ0TqqAF4yBer/M7cdo0JrOXzhMlRkAdK2nWNgaBsOAnpx+XvfWW5Hk+zq+Gjsg6IKXPJBoulWuEsDlcO7ZwObleXl9eU/Z8mKZMdn9qVx37zz1aE/ygwP+ost7Rzn+MA3nMiBzmy5SSUU0PDgUix3OUozosyO8ANq+qbq/eQ28ToPKtb0Z8IThHycdwoTeCwPCzZydmVBg0nHgGIQSxsWMF4=";
  public static String ingame$broadcast$join = "{player} §eentrou na partida! §a({players}/{max_players})";
  public static String ingame$broadcast$leave = "{player} §csaiu da partida! §a({players}/{max_players})";
  public static String ingame$broadcast$starting = "§aO jogo começa em §f{time} §asegundo{s}.";
  public static String ingame$broadcast$suicide = "{name} §amorreu sozinho.";
  public static String ingame$broadcast$killed = "{name} §afoi abatido por {killer}";
  public static String ingame$broadcast$point = "{name} §7(§a{player_life}§c❤§7) §7marcou um ponto!\n{red_points} §7- {blue_points}\n";
  public static String ingame$broadcast$win$solo = " \n{name} §avenceu a partida!\n ";
  public static String ingame$broadcast$win$dupla = " \n{name} §avenceram a partida!\n ";
  public static String ingame$titles$cage$header = "{time}";
  public static String ingame$titles$cage$footer = "§7Liberando jogadores...";
  public static String ingame$titles$end$header = "{winner} VENCEU!";
  public static String ingame$titles$end$footer = "{red_points} §7- {blue_points}";
  public static String ingame$messages$bow$hit = "{name} §aestá com §c{hp} §ade HP.";
  public static String ingame$messages$coins$base = " \n  §a{coins} coins ganhos nesta partida:\n {coins_win}{coins_points}{coins_kills}\n ";
  public static String ingame$messages$coins$win = "\n       §a+{coins} §fpor vencer o jogo";
  public static String ingame$messages$coins$points = "\n       §a+{coins} §fpor pontuar §a{points} §fvezes";
  public static String ingame$messages$coins$kills = "\n       §a+{coins} §fpor realizar §c{kills} §fabates";
  
  public static void setupLanguage() {
    boolean save = false;
    KWriter writer =
        Main.getInstance().getWriter(CONFIG.getFile(), "kTheBridge - Criado por Kiwizin\nVersão da configuração: " + Main.getInstance()
                .getDescription().getVersion());
    for (Field field : Language.class.getDeclaredFields()) {
      if (field.getName().contains("$") && !Modifier.isFinal(field.getModifiers())) {
        String nativeName = field.getName().replace("$", ".").replace("_", "-");
        
        try {
          Object value;
          
          if (CONFIG.contains(nativeName)) {
            value = CONFIG.get(nativeName);
            if (value instanceof String) {
              value = StringUtils.formatColors((String) value).replace("\\n", "\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.formatColors((String) v).replace("\\n", "\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            field.set(null, value);
            writer.set(nativeName, new YamlEntry(new Object[]{"", CONFIG.get(nativeName)}));
          } else {
            value = field.get(null);
            if (value instanceof String) {
              value = StringUtils.deformatColors((String) value).replace("\n", "\\n");
            } else if (value instanceof List) {
              List l = (List) value;
              List<Object> list = new ArrayList<>(l.size());
              for (Object v : l) {
                if (v instanceof String) {
                  list.add(StringUtils.deformatColors((String) v).replace("\n", "\\n"));
                } else {
                  list.add(v);
                }
              }
              
              value = list;
            }
            
            save = true;
            writer.set(nativeName, new YamlEntry(new Object[]{"", value}));
          }
        } catch (ReflectiveOperationException e) {
          LOGGER.log(Level.WARNING, "Unexpected error on settings file: ", e);
        }
      }
    }
    
    if (save) {
      writer.write();
      CONFIG.reload();
      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
          () -> LOGGER.info("A config §6language.yml §afoi modificada ou criada."));
    }
  }
}
