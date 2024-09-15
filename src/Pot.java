import java.util.*;

public class Pot {
    private final int potNo;
    private final List<Team> teams;
    public Pot(int potNo) {
        this.potNo = potNo;
        this.teams = new ArrayList<>();
    }
    public List<Team> getTeams() {
        return teams;
    }

    public int getPotNo() {
        return potNo;
    }
    public void addTeam(Team team) {
        this.teams.add(team);
    }

}