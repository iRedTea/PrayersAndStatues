package me.redtea.prayersandstatues.services;

import com.google.inject.Singleton;
import lombok.NonNull;

@Singleton
public class TimeUtil {

    public long getTicksFromString(@NonNull String value) {
        long output = 0;
        for(String v : value.split(" ")) {
            if(v.contains("s")) {
                output += Integer.parseInt(v.replace("s", "")) * 20L;
            } else if (v.contains("m")) {
                output += Integer.parseInt(v.replace("m", "")) * 60 * 20L;
            } else if (v.contains("h")) {
                output += Integer.parseInt(v.replace("h", "")) * 60 * 60 * 20L;
            } else if (v.contains("d")) {
                output += Integer.parseInt(v.replace("d", "")) * 24 * 60 * 60 * 20L;
            } else throw new RuntimeException("Illegal format of time");
        }
        return output;
    }
}
