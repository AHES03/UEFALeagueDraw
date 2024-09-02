import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pot {
    private final int potNo;
    private List<Team> teams;
    Map<String, Map<String, List<String>>> fixtures = new HashMap<>();
    public Pot(int potNo){
        this.potNo = potNo;
        this.teams = new ArrayList<>();
    }

    public List<Team> getTeams() {
        return teams;
    }

    public int getPotNo() {
        return potNo;
    }
    public void addTeam(Team team){
        this.teams.add(team);
    }
    public void addTeamFixtures(Team team,List<Match> matches){

    }
}
