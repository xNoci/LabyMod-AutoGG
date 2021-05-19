package me.noci.labyaddon.languages.language;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class LanguageGerman implements Language {

    //DEUTSCH
    private static List<String> ROUND_END_INDICATOR = ImmutableList.of(
            "-= Statistiken dieser Runde =-",
            "-\\= Statistiken dieser Runde \\=-",
            "[Kit1vs1] --------- Match-Statistiken ---------",
            "[Game1vs1] --------- Match-Statistiken ---------");

    @Override
    public List<String> getRoundEndIndicator() {
        return ROUND_END_INDICATOR;
    }
}
