import java.util.*;

public class Team {
    private final String teamName;
    private final String nationalAssoc;
    private final Map<Integer,Pot> pot =new HashMap<>(1);
    private final HashMap<Pot, List < Map<String, Match>>> fixtureMap = new HashMap<>();
    private final HashMap<Pot, Map<String, List<Team>>>validTeams= new HashMap<>();
    public Team(String teamName, String nationalAssoc, List<Pot> pots, int potNo){
        this.teamName= teamName;
        this.nationalAssoc = nationalAssoc;
        this.pot.put(potNo,pots.get(potNo-1));
        for (Pot pot : pots ){
            fixtureMap.put(pot, new ArrayList<>());
            validTeams.put(pot, new HashMap<>());
            Map<String, List<Team>> emptyMapValidTeams = new HashMap<>();
            Map<String,Match> emptyMapFixtures = new HashMap<>();
            emptyMapValidTeams.put("Home", new ArrayList<>());
            emptyMapValidTeams.put("Away", new ArrayList<>());
            emptyMapFixtures.put("Home",null);
            emptyMapFixtures.put("Away",null);
            fixtureMap.get(pot).add(emptyMapFixtures);
            validTeams.put(pot, emptyMapValidTeams);
        }
    }
    public String getTeamName(){
        return this.teamName;
    }
    public String getNationalAssoc(){
        return this.nationalAssoc;
    }
    public Map<Integer,Pot> getPotMap(){
        return this.pot;
    }
    public Pot getPot(){
        int key = (this.pot.keySet().hashCode());
        return this.pot.get(key);
    }

    public void addMatch(List<Match> matchList,Pot pot) {
        for (Match match : matchList){
            if (match == null){
                continue;
            }
            if (match.getHomeTeam() != this){
                this.fixtureMap.get(pot).getFirst().replace("Away",match);
                match.getHomeTeam().addOpponentMatch(match,"Home",this.pot);
            } else if (match.getAwayTeam() != this) {
                this.fixtureMap.get(pot).getFirst().replace("Home",match);
                match.getAwayTeam().addOpponentMatch(match,"Away",this.pot);
            }
        }
    }
    public HashMap<Pot, List < Map<String, Match>>> getFixtureMap(){
        // when requested if match == null replace with -
        return this.fixtureMap;
    }
    public void addOpponentMatch(Match match, String type,Map<Integer,Pot> pot) {
        int key = (pot.keySet().hashCode());
        this.fixtureMap.get(pot.get(key)).getFirst().replace(type,match);
    }
    public List<Match> getMatchList() {
        List<Match> matchList = new ArrayList<>();

        // Correctly typed Map.Entry
        for (Map.Entry<Pot, List<Map<String, Match>>> entry : this.fixtureMap.entrySet()) {
            List<Map<String, Match>> matches = entry.getValue();  // Get the list of matches for this pot
            // Iterate over the list of fixture maps for each pot
            for (Map<String, Match> matchMap : matches) {
                // Check if "Away" match exists and add it to the matchList
                if (matchMap.get("Away") != null) {
                    matchList.add(matchMap.get("Away"));
                }
                // Check if "Home" match exists and add it to the matchList
                if (matchMap.get("Home") != null) {
                    matchList.add(matchMap.get("Home"));
                }
            }
        }

        return matchList;
    }
    public Match getFixture(Pot pot, String type){
        return (this.fixtureMap.get(pot).getFirst().get(type));

    }
    public void clearFixtureList(List<Pot> pots,int potNo){
        this.pot.put(potNo,pots.get(potNo-1));
        for (Pot pot : pots ){
            fixtureMap.put(pot, new ArrayList<>());
            Map<String,Match> emptyMap = new HashMap<>();
            emptyMap.put("Home",null);
            emptyMap.put("Away",null);
            fixtureMap.get(pot).add(emptyMap);
        }
    }
    public void addValidTeams(Pot pot){
        List<Team> potTeams =pot.getTeams();
        List<Team> validTeams =new ArrayList<>();
        for (Team team : potTeams){
            if (team == this){
                continue;
            }
            if (!Objects.equals(team.getNationalAssoc(), this.nationalAssoc)){
                validTeams.add(team);
            }
        }
        this.validTeams.get(pot).put("Home",validTeams);
        this.validTeams.get(pot).put("Away",validTeams);
    }
}