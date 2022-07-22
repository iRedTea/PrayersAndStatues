package me.redtea.prayersandstatues;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;
import me.redtea.prayersandstatues.commands.PrayerCommand;
import me.redtea.prayersandstatues.listeners.PlayerHandler;
import me.redtea.prayersandstatues.services.DatabaseManager;
import me.redtea.prayersandstatues.services.EffectScheduler;
import me.redtea.prayersandstatues.tools.Config;
import me.redtea.prayersandstatues.tools.Message;
import me.redtea.prayersandstatues.types.User;
import me.redtea.prayersandstatues.services.TimeUtil;

@AllArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class PrayersCore {
    private final PrayerCommand prayerCommand;

    private final EffectScheduler effectsManager;

    private final PlayerHandler playerHandler;

    private final Config config;

    @Getter
    private final PrayersAndStatues plugin;

    @Inject
    private final DatabaseManager databaseManager;

    private final TimeUtil timeUtil;

    public void init() {
        databaseManager.init();

        plugin.getServer().getPluginManager().registerEvents(playerHandler, plugin);

        Message.load(plugin.getConfig(), false);

        effectsManager.init();

        //Command setup
        CommandManager commandManager = new CommandManager(plugin);
        commandManager.getParameterHandler().register(User.class, argument -> {
            final User user = databaseManager.getUserData(String.valueOf(argument));
            if (user == null) return new TypeResult(argument);
            return new TypeResult(user, argument);
        });
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> {
            sender.sendMessage(Message.noPermissions.toString());
        });
        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> {
            sender.sendMessage(Message.usage.toString());
        });
        commandManager.register(prayerCommand);
    }

    public void close() {
        databaseManager.save();
        config.load(plugin, timeUtil);
    }
}
