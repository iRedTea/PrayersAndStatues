package me.redtea.prayersandstatues.types;


import lombok.*;

import java.util.ArrayList;

@Data
public class UpgradeTree {
    private final String tag;
    private final String name;
    private final String icon;
    private final int slot;
    private final ArrayList<Prayer> prayers;
}
