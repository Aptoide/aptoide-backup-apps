package pt.aptoide.backupapps.download;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 01-08-2013
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCategory {

    COMICS(1),
    COMMUNICATION(2),
    ENTERTAINMENT(3),
    FINANCE(4),
    HEALTH(5),
    LIFESTYLE(6),
    MULTIMEDIA(7),
    NEWS_WEATHER(8),
    PRODUCTIVITY(9),
    REFERENCE(10),
    SHOPPING(11),
    SOCIAL(12),
    SPORTS(13),
    THEMES(14),
    TOOLS(15),
    TRAVEL(16),
    DEMO(17),
    SOFTWARE_LIBRARIES(18),
    ARCADE_ACTION_GAMES(19),
    BRAIN_PUZZLE_GAMES(20),
    CARDS_CASINO_GAMES(21),
    CASUAL_GAMES(22),
    NEWS_MAGAZINES(24),
    MUSIC_AUDIO(29),
    PHOTOGRAPHY(37),
    PERSONALIZATION(38),
    RACING_GAMES(45),
    BOOKS_REFERENCE(76),
    HEALTH_FITNESS(84),
    MEDIA_VIDEO(87),
    EDUCATION(93),
    BUSINESS(147),
    SPORTS_GAMES(291),
    WEATHER(308),
    TRAVEL_LOCAL(413),
    TRANSPORTATION(416),
    MEDICAL(457),
    LIBRARIES_DEMO(734),
    TRANSPORT(848), NONE(0);

    private int numVal;

    EnumCategory(int numVal) {
        this.numVal = numVal;
    }


    public int getNumVal() {
        return numVal;
    }
}
