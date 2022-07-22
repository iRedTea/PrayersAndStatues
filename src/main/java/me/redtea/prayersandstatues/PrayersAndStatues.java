package me.redtea.prayersandstatues;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;


public final class PrayersAndStatues extends JavaPlugin {

    private PrayersCore core;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new PrayersModule(this));
        core = injector.getInstance(PrayersCore.class);
        core.init();
    }

    @Override
    public void onDisable() {
        core.close();
    }

    public void restart() {
        core.close();
        core.init();
    }
}
