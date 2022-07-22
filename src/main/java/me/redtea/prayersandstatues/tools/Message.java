package me.redtea.prayersandstatues.tools;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.redtea.prayersandstatues.PrayersAndStatues;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Message {
    usage, noPermissions,
    notEnoughItems, prayerSuccess, prayerError,

    prayerOpenLore, prayerGotLore, prayerLockedLore,

    commands_set_error, commands_set_success,
    commands_reload_success, commands_reload_error;


    private List<String> msg;

    private static LangProperties langProperties;

    private static boolean PAPI;

    @SuppressWarnings("unchecked")
    public static void load(FileConfiguration c, boolean PAPIEnabled) {
        for(Message message : Message.values()) {
            PAPI = PAPIEnabled;
            try {
                Object obj = c.get("messages." + message.name().replace("_", "."));
                if(obj instanceof List) {
                    message.msg = (((List<String>) obj)).stream().map(m -> ChatColor.translateAlternateColorCodes('&', m)).collect(Collectors.toList());
                } else {
                    message.msg = Lists.newArrayList(obj == null ? "" : ChatColor.translateAlternateColorCodes('&', obj.toString()));
                }
            } catch (NullPointerException e) {
            }
        }
    }



    public Sender replace(String from, String to) {
        Sender sender = new Sender();
        return sender.replace(from, to);
    }

    public void send(CommandSender player) {
        new Sender().send(player);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(String s : Message.this.msg) {
            str.append(" ").append(s);
        }
        return ChatColor.translateAlternateColorCodes('&', str.toString());
    }

    public List<String> toList() {
        ArrayList<String> result = new ArrayList<>();
        for(String m : msg) {
            result.add(ChatColor.translateAlternateColorCodes('&',m));
        }
        return result;
    }

    public class Sender {
        private final Map<String, String> placeholders = new HashMap<>();

        public void send(CommandSender player) {
            for(String message : Message.this.msg) {
                sendMessage(player, replacePlaceholders(message));
            }

        }

        public Sender replace(String from, String to) {
            placeholders.put(from, ChatColor.translateAlternateColorCodes('&', to));
            return this;
        }

        private void sendMessage(CommandSender player, String message) {

            player.sendMessage(message);

        }

        private String replacePlaceholders(String message) {
            for(Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return message;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for(String s : Message.this.msg) {
                str.append(" ").append(replacePlaceholders(s));
            }
            return ChatColor.translateAlternateColorCodes('&', str.toString());
        }

        public List<String> toList() {
            ArrayList<String> list = new ArrayList<>();
            for(String s : Message.this.msg) {
                list.add(ChatColor.translateAlternateColorCodes('&', replacePlaceholders(s)));
            }
            return list;
        }

    }

    public static LangProperties getLangProperties() {
        return langProperties;
    }

    @AllArgsConstructor
    @Getter
    @NonNull
    public static class LangProperties {
        private final String language;

        private final String author;

        private final String version;

        private final String pluginVersion;
    }
}
