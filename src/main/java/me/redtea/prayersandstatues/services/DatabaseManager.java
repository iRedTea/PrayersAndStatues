package me.redtea.prayersandstatues.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.redtea.prayersandstatues.PrayersAndStatues;
import me.redtea.prayersandstatues.db.IDatabase;
import me.redtea.prayersandstatues.db.IDatabaseFactory;
import me.redtea.prayersandstatues.db.impl.json.JsonData;
import me.redtea.prayersandstatues.types.User;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class DatabaseManager {
    private final IDatabaseFactory databaseFactory;

    private IDatabase db;

    public void init() {
        db = databaseFactory.createJsonDatabase();
    }

    public User getUserData(String uuid) {
        return db.getData(uuid);
    }

    public void insertUserData(User user) {
        db.saveData(user.getUuid(), user);
    }

    public void save() {
        db.saveData();
    }

}
