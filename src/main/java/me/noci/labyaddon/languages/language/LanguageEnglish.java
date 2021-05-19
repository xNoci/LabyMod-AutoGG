package me.noci.labyaddon.languages.language;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class LanguageEnglish implements Language {

    //ENGLISCH
    private static List<String> ROUND_END_INDICATOR = ImmutableList.of(
            "-= Statistics of this game =-",
            "-\\= Statistics of this game \\=-",
            "[Kit1vs1] --------- Match statistics ---------",
            "[Game1vs1] --------- Match statistics ---------");

    @Override
    public List<String> getRoundEndIndicator() {
        return ROUND_END_INDICATOR;
    }
}
