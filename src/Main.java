import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static Pot pot1 = new Pot(1);
    private static Pot pot2 = new Pot(2);
    private static Pot pot3 = new Pot(3);
    private static Pot pot4 = new Pot(4);
    private static List<Team> teamExludeList = new ArrayList<>();
    private static List<Pot> potList= new ArrayList<>();
    public static void main(String[] args) {
        Collections.addAll(potList,pot1,pot2,pot3,pot4);
        Main mainInstance = new Main();
        try {
            mainInstance.getAllTeams("UEFA_Champions_League_2024_2025_Teams_with_Pots.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Pot> potExludeList = new ArrayList<>();
        for (int i = 0; i < potList.size(); i++){
            Pot pot = potList.get(i);  // Get the current Pot
            List<Team> potTeam = pot.getTeams();
            Random rand = new Random();
            for (Iterator<Team> it = potTeam.iterator(); it.hasNext();) {
                Team teamDrawn;
                if (potTeam.size() !=1){
                    int teamSelector = rand.nextInt(potTeam.size());
                    teamDrawn = potTeam.get(teamSelector);
                }
                else{
                    teamDrawn = potTeam.getFirst();
                }

                for (Pot teamPotDraw : potList) {
                    if (potExludeList.contains(teamPotDraw)){
                        continue;
                    }
                    List<Team> validTeams = getPossibleOpponents(teamDrawn,teamPotDraw.getPotNo());
                    int opposition;
                    Match HomeDraw;
                    Match AwayDraw;
                    if (!validTeams.isEmpty()){
                        opposition = rand.nextInt(validTeams.size());
                        HomeDraw =  new Match(teamDrawn,validTeams.get(opposition));
                        validTeams.remove(opposition);
                    }else{
                        continue;
                    }
                    if (!validTeams.isEmpty()){
                        opposition = rand.nextInt(validTeams.size());
                        AwayDraw = new Match(validTeams.get(opposition),teamDrawn);
                    } else{
                        continue;
                    }
                    List<Match> draws = new ArrayList<>();
                    Collections.addAll(draws,HomeDraw,AwayDraw);
                    teamDrawn.addMatch(draws);
                }
                teamExludeList.add(teamDrawn);
                potTeam.remove(teamDrawn);
            }
            potExludeList.add(pot);
        }

    }
    public void getAllTeams(String fileName) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(fileName))) {
            if (sc.hasNextLine()) {
                sc.nextLine();  // Skip the header line
            }
            while (sc.hasNextLine()) {
                String[] teamDetails = sc.nextLine().split(",");
                int potNo = Integer.parseInt(teamDetails[2]);
                Team team = new Team(teamDetails[1], teamDetails[0], potNo);
                potList.get(potNo - 1).addTeam(team);
            }
        }
    }
        public static List<Team> getPossibleOpponents(Team teamDrawn, int potNo) {
        String nationalAssoc = teamDrawn.getNationalAssoc();
        return (potList.get(potNo-1).getTeams().stream()
                .filter(team -> !nationalAssoc.equals(team.getNationalAssoc()))
                .filter(team -> !teamExludeList.contains(team))
                .collect(Collectors.toList()));
    }
}
