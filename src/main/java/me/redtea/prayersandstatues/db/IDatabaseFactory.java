package me.redtea.prayersandstatues.db;

import me.redtea.prayersandstatues.db.impl.json.JsonData;

public interface IDatabaseFactory {
    JsonData createJsonDatabase();
}
