package com.gmail.nossr50.core.runnables.party;

import com.gmail.nossr50.core.config.MainConfig;
import com.gmail.nossr50.core.datatypes.party.Party;
import com.gmail.nossr50.core.locale.LocaleLoader;
import com.gmail.nossr50.core.mcmmo.colors.ChatColor;
import com.gmail.nossr50.core.mcmmo.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartyChatTask extends BukkitRunnable {
    private Plugin plugin;

    private Party party;
    private String senderName;
    private String displayName;
    private String message;

    public PartyChatTask(Plugin plugin, Party party, String senderName, String displayName, String message) {
        this.plugin = plugin;

        this.party = party;
        this.senderName = senderName;
        this.displayName = displayName;
        this.message = message;
    }

    @Override
    public void run() {
        if (MainConfig.getInstance().getPartyChatColorLeaderName() && senderName.equalsIgnoreCase(party.getLeader().getPlayerName())) {
            message = message.replaceFirst(Pattern.quote(displayName), ChatColor.GOLD + Matcher.quoteReplacement(displayName) + ChatColor.RESET);
        }

        for (Player member : party.getOnlineMembers()) {
            member.sendMessage(message);
        }

        if (party.getAlly() != null) {
            for (Player member : party.getAlly().getOnlineMembers()) {
                String allyPrefix = LocaleLoader.formatString(MainConfig.getInstance().getPartyChatPrefixAlly());
                member.sendMessage(allyPrefix + message);
            }
        }

        plugin.getServer().getConsoleSender().sendMessage(ChatColor.stripColor("[mcMMO] [P]<" + party.getName() + ">" + message));
    }
}