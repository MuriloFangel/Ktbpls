package dev.slickcollections.kiwizin.thebridge.cmd;

import dev.slickcollections.kiwizin.bukkit.BukkitParty;
import dev.slickcollections.kiwizin.bukkit.BukkitPartyManager;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.thebridge.game.TheBridge;
import dev.slickcollections.kiwizin.thebridge.game.enums.TheBridgeMode;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlay;
import dev.slickcollections.kiwizin.thebridge.menus.MenuPlaySelectMode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DuelsCommand extends Commands {

    public DuelsCommand() {
        super("duelar");
    }

    private static final Map<String, String> DUELS_CACHE = new HashMap<>();

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando é exclusivo para jogadores!");
            return;
        }

        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());

        if (profile != null) {
            if (profile.getGame(TheBridge.class) != null) {
                player.sendMessage("§cNão é possível usar esse comando em partida!");
                return;
            }

            if (args.length < 2) {
                player.sendMessage("§cUso incorreto! Tente /duelar [desafiar/aceitar/recusar] [JOGADOR]");
                return;
            }

            String action = args[0];
            String target = args[1];
            Player playerTarget = null;

            if (player.getName().equalsIgnoreCase(target)) {
                player.sendMessage("§cNão é possível utilizar este comando consigo mesmo.");
                return;
            }

            try {
                playerTarget = Bukkit.getPlayer(target);
            } catch (Exception e) {
                player.sendMessage("§cEste jogador não está online no momento!");
            }

            BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
            BukkitParty partyTarget = BukkitPartyManager.getMemberParty(target);

            if (party != null && partyTarget == null || partyTarget != null && party == null ) {
                player.sendMessage("§cSó é possível duelar caso ambos jogadores estejam em uma party.");
                return;
            }

            if (party != null && !party.isLeader(player.getName())) {
                if (party.listMembers().size() < 2) {
                    player.sendMessage("§cSó é possível mandar um convite de duelos caso sua party tiver mais que 2 jogadores.");
                    return;
                }

                player.sendMessage("§cSó é possível usar este comando sendo o líder da party!");
                return;
            }

            if (partyTarget != null && !partyTarget.isLeader(target)) {
                playerTarget = Bukkit.getPlayer(partyTarget.getLeader());
                target = playerTarget.getName();
            }

            switch (action) {
                case "desafiar": {
                    if (playerTarget != null) {
                        if (DUELS_CACHE.containsKey(target) && DUELS_CACHE.get(target).equalsIgnoreCase(player.getName())) {
                            player.sendMessage("§cVocê já convidou esse jogador para um duelo!");
                            return;
                        }

                        if (DUELS_CACHE.containsKey(target)) {
                            player.sendMessage("§cEste jogador já possui um pedido pentende!");
                            return;
                        }

                        TextComponent inicio = new TextComponent("§aVocê recebeu um convite de duelo do jogador " + player.getName() + ". §aPara §aaceitar§a, §abastar §aclicar ");
                        TextComponent aquiGreen = new TextComponent("AQUI");
                        aquiGreen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duelar aceitar " + player.getName()));
                        aquiGreen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Clique aqui para aceitar!")));
                        aquiGreen.setColor(ChatColor.DARK_GREEN);
                        aquiGreen.setBold(true);
                        inicio.addExtra(aquiGreen);
                        inicio.addExtra(" §a. Para recusar, basta clicar ");
                        TextComponent aquiRed = new TextComponent("AQUI");
                        aquiRed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duelar recusar " + player.getName()));
                        aquiRed.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Clique aqui para recusar!")));
                        aquiRed.setColor(ChatColor.DARK_RED);
                        aquiRed.setBold(true);
                        inicio.addExtra(aquiRed);
                        inicio.addExtra("§a!");

                        playerTarget.spigot().sendMessage(inicio);
                        player.sendMessage("§aConvite de duelo enviado com sucesso!");
                        runDuelsRemover(target, player.getName());
                        return;
                    }
                    return;
                }

                case "aceitar": {
                    if (playerTarget != null) {
                        if (!DUELS_CACHE.containsKey(player.getName())) {
                            player.sendMessage("§cVocê não possui nenhum pedido de duelo pendente!");
                            return;
                        }

                        if (!DUELS_CACHE.get(player.getName()).equalsIgnoreCase(target)) {
                            player.sendMessage("§cVocê não possui nenhum pedido pendente desse jogador!");
                            return;
                        }

                        playerTarget.sendMessage("§aO jogador " + player.getName() + " aceitou o seu pedido de duelo!");
                        player.sendMessage("§aVocê acaba de aceitar o pedido de duelo do jogador " + target + "!");
                        if (BukkitPartyManager.getMemberParty(playerTarget.getName()) != null) {
                            if (BukkitPartyManager.getMemberParty(playerTarget.getName()).listMembers().size() < 2) {
                                player.sendMessage("§cSó é possível aceitar o convite caso a party do desafiante tenha mais que 2 jogadores.");
                                return;
                            }
                        }

                        DUELS_CACHE.remove(playerTarget.getName());
                        new MenuPlaySelectMode(Profile.getProfile(playerTarget.getName()), profile);
                        return;
                    }
                    return;
                }

                case "recusar": {
                    if (playerTarget != null) {
                        if (!DUELS_CACHE.containsKey(player.getName())) {
                            player.sendMessage("§cVocê não possui nenhum pedido de duelo pendente!");
                            return;
                        }
                        if (!DUELS_CACHE.get(player.getName()).equalsIgnoreCase(target)) {
                            player.sendMessage("§cVocê não possui nenhum pedido pendente desse jogador!");
                            return;
                        }

                        player.sendMessage("§cVocê acaba de recusar o pedido de duelos do jogador " + target + "! Que arregão você é!");
                        playerTarget.sendMessage("§cO jogador " + player.getName() + " acaba de recusar o seu pedido de duelo! Que oponente indigno!");
                        DUELS_CACHE.remove(player.getName());
                        return;
                    }
                    return;
                }
            }

            player.sendMessage("§cUso incorreto! Tente /duelar [desafiar/aceitar/recusar] [JOGADOR]");
        }
    }


    public void runDuelsRemover(String target, String player) {
        new Thread(() -> {
            DUELS_CACHE.put(target, player);
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DUELS_CACHE.remove(target);
        }).start();
    }
}
