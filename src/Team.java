import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<Match> getMatches(){
        return Collections.unmodifiableList(matches);
    }
    public Boolean addMatch(Match match){
        if (match.isValid()){
            matches.add(match);
            return true;
        }else{
            return false;
        }
    }
}
