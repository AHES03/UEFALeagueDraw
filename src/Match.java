import java.util.Objects;

public class Match {
    private final Team homeTeam;
    private final Team awayTeam;

    public Match(Team home, Team away){
        homeTeam = home;
        awayTeam = away;
    }
    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }


}
