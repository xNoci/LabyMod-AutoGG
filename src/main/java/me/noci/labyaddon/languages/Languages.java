package me.noci.labyaddon.languages;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.noci.labyaddon.languages.language.Language;
import me.noci.labyaddon.languages.language.LanguageEnglish;
import me.noci.labyaddon.languages.language.LanguageGerman;
import me.noci.labyaddon.languages.language.LanguageVlaams;

import java.util.List;
import java.util.Set;

public class Languages {

    private static Set<Language> LANGUAGES = Sets.newHashSet();


    static {
        LANGUAGES.add(new LanguageGerman());
        LANGUAGES.add(new LanguageEnglish());
        LANGUAGES.add(new LanguageVlaams());
    }

    public static List<String> getRoundEndIndicators() {
        List<String> indicators = Lists.newArrayList();
        LANGUAGES.stream().forEach(language -> indicators.addAll(language.getRoundEndIndicator()));
        return indicators;
    }

}
