import java.util.*;

public class Team {
    private final String teamName;
    private final String nationalAssoc;
    private final int potNo;
    private List<Match> matches;
    public Team(String teamName, String nationalAssoc, int potNo){
        this.teamName= teamName;
        this.nationalAssoc = nationalAssoc;
        this.potNo = potNo;
        this.matches = new ArrayList<>();
    }
    public String getTeamName(){
        return teamName;
    }
    public String getNationalAssoc(){
        return nationalAssoc;
    }
    public int getPotNo(){
        return potNo;
    }
    public void addMatch(List<Match> matchList) {
        for (Match match : matchList){
            matches.add(match);
            if (match.getHomeTeam() != this){
                match.getHomeTeam().addOpponentMatch(match);
            } else if (match.getAwayTeam() != this) {
                match.getAwayTeam().addOpponentMatch(match);
            }
        }

    }
    public void addOpponentMatch(Match match) {
        matches.add(match);
    }
}