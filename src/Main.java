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
    private static List<Pot> potList= new ArrayList<>();


    public static void main(String[] args) {
        Collections.addAll(potList,pot1,pot2,pot3,pot4);

        Main mainInstance = new Main();
        try {
            mainInstance.getAllTeams("UEFA_Champions_League_2024_2025_Teams_with_Pots.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < potList.size(); i++) {
            Pot pot = potList.get(i);  // Get the current Pot
            List<Team> potTeam = pot.getTeams();
            Random rand = new Random();

            for (Iterator<Team> it = potTeam.iterator(); it.hasNext();) {
                // Generate random integers in range 0 to potTeam.size() - 1
                int teamSelector = rand.nextInt(potTeam.size());
                Team teamDrawn = potTeam.get(teamSelector);

                // Use the index 'i' to represent the index of the current Pot in potList
                getOpponents(teamDrawn, i);  // Pass the pot index to getOpponents or use it as needed
            }
        }
    }

    public void getAllTeams(String fileName) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(fileName))) {
            if (sc.hasNextLine()) {
                sc.nextLine();  // Skip the header line
            }
            while (sc.hasNextLine()) {
                String[] teamDetails = sc.nextLine().split(",");
                int potNo =  Integer.parseInt(teamDetails[2]);
                Team team = new Team(teamDetails[1], teamDetails[0], potNo);
                potList.get(potNo).addTeam(team);
            }
        }
    }
    public static void getOpponents(Team teamDrawn, int potNo){

        List<Team> possibleOpponents  = getPossibleOpponents(teamDrawn,potNo);
        Random rand = new Random();
        int teamSelector = rand.nextInt(possibleOpponents.size());
        Match HomeDraw =  new Match(teamDrawn,possibleOpponents.get(teamSelector));
        possibleOpponents.remove(teamSelector);
        teamSelector = rand.nextInt(possibleOpponents.size());
        Match AwayDraw = new Match(possibleOpponents.get(teamSelector),teamDrawn);
        List<Match> draws = new ArrayList<>();
        Collections.addAll(draws,HomeDraw,AwayDraw);
        potList.get(potNo).addTeamFixtures(teamDrawn,draws);


    }
    public static List<Team> getPossibleOpponents(Team teamDrawn, int potNo) {
        String nationalAssoc = teamDrawn.getNationalAssoc();
        List<Team> possibleOpponents = potList.get(potNo).getTeams().stream()
                .filter(team -> !nationalAssoc.equals(team.getNationalAssoc()))
                .collect(Collectors.toList());
        return possibleOpponents;
    }
}
