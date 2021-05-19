package me.noci.labyaddon.languages.language;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class LanguageVlaams implements Language {

    //Vlaams (belgische Niederländisch)
    private static List<String> ROUND_END_INDICATOR = ImmutableList.of(
            "-= Statistieken van deze ronde =-",
            "-\\= Statistieken van deze ronde \\=-",
            "[Kit1vs1] --------- Match statistieken ---------",
            "[Game1vs1] --------- Match statistieken ---------");

    @Override
    public List<String> getRoundEndIndicator() {
        return ROUND_END_INDICATOR;
    }
}
