package me.redtea.prayersandstatues.services;

import me.redtea.prayersandstatues.types.User;

import java.util.HashMap;
import java.util.Map;

public interface IUserFactory {
    User createUser(String uuid, HashMap<String, Map<String, Integer>> data);

}
