package me.redtea.prayersandstatues;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import lombok.AllArgsConstructor;
import me.redtea.prayersandstatues.db.IDatabase;
import me.redtea.prayersandstatues.db.IDatabaseFactory;
import me.redtea.prayersandstatues.db.impl.json.JsonData;
import me.redtea.prayersandstatues.services.*;
import me.redtea.prayersandstatues.types.User;

@AllArgsConstructor
public class PrayersModule extends AbstractModule {
    private final PrayersAndStatues plugin;

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(User.class, User.class).build(IUserFactory.class));
        install(new FactoryModuleBuilder().implement(IDatabase.class, JsonData.class).build(IDatabaseFactory.class));
        bind(PrayersAndStatues.class).toInstance(plugin);
    }
}
