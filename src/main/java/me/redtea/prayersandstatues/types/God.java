package me.redtea.prayersandstatues.types;

import lombok.*;

import java.util.ArrayList;

@Data
public class God {

    private final String name;

    private final Statue statue;

    private final ArrayList<UpgradeTree> trees;

    private final long cooldown;


}
