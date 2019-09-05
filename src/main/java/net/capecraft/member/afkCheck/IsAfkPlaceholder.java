package net.capecraft.member.afkCheck;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.capecraft.member.MemberConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class IsAfkPlaceholder extends PlaceholderExpansion {

    private Plugin plugin;

    public IsAfkPlaceholder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "CapeCraft";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("afk")) {
            MemberConfig memberconfig = new MemberConfig(plugin);
            String uuid = player.getUniqueId().toString();
            if (Boolean.parseBoolean(memberconfig.readConfig("isAfk", uuid).toString())) {
                return "AFK";
            }
            return null;
        }
        return null;
    }
}

