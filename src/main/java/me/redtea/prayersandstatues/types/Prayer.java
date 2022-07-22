package me.redtea.prayersandstatues.types;

import lombok.*;

import java.util.HashMap;

@Data
public class Prayer {
    private final int level;
    private final HashMap<String, Integer> items;
    private final String effect;
}