package me.redtea.prayersandstatues.db;

import lombok.NonNull;
import me.redtea.prayersandstatues.types.User;

public interface IDatabase {
    User getData(@NonNull String uuid);

    void saveData(@NonNull String uuid, User value);

    void saveData();
}
