package me.redtea.prayersandstatues.commands;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.redtea.prayersandstatues.PrayersAndStatues;
import me.redtea.prayersandstatues.tools.Message;
import me.redtea.prayersandstatues.types.User;
import org.bukkit.command.CommandSender;

@AllArgsConstructor(onConstructor_ = @Inject)
@Command("prayer")
public class PrayerCommand extends CommandBase {

    private final PrayersAndStatues plugin;

    @Default
    public void defaultCommand(final CommandSender sender) {
        Message.usage.send(sender);
    }

    @SubCommand("set")
    @Permission("prayer.set")
    public void setSubCommand(final CommandSender sender, final User player,
                              final String god, String tree, final String level) {
        try {
            player.setLevel(god, tree, Integer.parseInt(level));
            Message.commands_set_success.send(sender);
        } catch (Throwable e) {
            Message.commands_set_error.send(sender);
        }
    }

    @SubCommand("reload")
    @Permission("prayer.reload")
    public void reloadSubCommand(final CommandSender sender) {
        try {
            long timeInitStart = System.currentTimeMillis();
            plugin.restart();
            long timeReload = (System.currentTimeMillis() - timeInitStart);
            Message.commands_reload_success.replace("%time%", String.valueOf(timeReload)).send(sender);
        } catch (Throwable e) {
            Message.commands_reload_error.send(sender);
        }
    }

}
