import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final Pot pot1 = new Pot(1);
    private static final Pot pot2 = new Pot(2);
    private static final Pot pot3 = new Pot(3);
    private static final Pot pot4 = new Pot(4);
    private static List<Team> teamExludeList = new ArrayList<>();
    private static final List<Pot> potList = new ArrayList<>();
    private static List <Pot> potExludeList = new ArrayList<>();
    private static List<Team> teamsFromPot ;



    public static void main(String[] args) {
        Collections.addAll(potList, pot1, pot2, pot3, pot4);
        Main mainInstance = new Main();
        try {
            mainInstance.getAllTeams("UEFA_Champions_League_2024_2025_Teams_with_Pots.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner myObj = new Scanner(System.in);
        String instructions =     "********************************************\n" +
                "*  Welcome to the UEFA Champions League Draw  *\n" +
                "********************************************\n" +
                "\n" +
                "Inputs:\n" +
                "  - D : Draw a team\n" +
                "  - 1-4 : Show pot fixture list for Pot 1 to Pot 4\n" +
                "  - R : Reset the draw\n" +
                "  - Exit : Quit the application\n" +
                "\n" +
                "********************************************\n";

        System.out.print(instructions);
        List<String> validInput = new ArrayList<>();
        Collections.addAll(validInput, "D", "1","2","3","4","R","EXIT");
        String input = myObj.nextLine().toUpperCase();
        int currentPot = 0; //Index 0 == Pot 1
        Pot pot = potList.get(currentPot);  // Get the current Pot
        teamsFromPot = new ArrayList<>(pot.getTeams());
        while (!input.equals("EXIT")){
            if (!validInput.contains(input)){
                System.out.print("Invalid input please enter a correct value \n");
                System.out.print(instructions);
                input = myObj.nextLine().toUpperCase();
                continue;
            }
            switch (input){
                case "1":
                case "2":
                case "3":
                case "4":
                    displayPotFixture(Integer.parseInt(input));
                    break;
                case "D":
                    Team teamDrawn;
                    Random rand = new Random();
                    if (teamsFromPot.size() !=1){
                        int teamSelector = rand.nextInt(teamsFromPot.size());
                        teamDrawn = teamsFromPot.get(teamSelector);
                        drawTeamFixtures(teamDrawn);
                        System.out.println(teamDrawn.getTeamName());
                    }
                    else{
                        teamDrawn = teamsFromPot.getFirst();
                        drawTeamFixtures(teamDrawn);
                        System.out.println(teamDrawn.getTeamName());
                    }
                    if (teamDrawn.getMatchList().size() == 8){
                        teamsFromPot.remove(teamDrawn);
                        break;
                    }
                    if (teamsFromPot.isEmpty()){
                        potExludeList.add(pot);
                        currentPot++;
                        pot = potList.get(currentPot);
                        teamsFromPot =new ArrayList<>(pot.getTeams());
                    }

                    break;

                case "R":
                    potExludeList = new ArrayList<>();
                    teamExludeList = new ArrayList<>();
                    for (Pot potNo :potList){
                        teamsFromPot = potNo.getTeams();
                        for (Team team : teamsFromPot){
                            team.clearFixtureList(potList,potNo.getPotNo());
                        }
                    }
                    break;
            }
            System.out.print(instructions);
            input = myObj.nextLine().toUpperCase();
        }

    }
    public static void drawTeamFixtures(Team teamDrawn){
        Random rand = new Random();
        for (Pot teamPotDraw : potList) {
            if (potExludeList.contains(teamPotDraw)){
                        continue;
            }
//            if (teamPotDraw.size()==1){
//                System.out.println("Guess What your guess was right");
//            }
            List<Team> validTeams = getPossibleOpponents(teamDrawn, teamPotDraw.getPotNo());
            int opposition;
            Match HomeDraw = null;
            Match AwayDraw = null;
            Map<String, Match> potFixtures = teamDrawn.getFixtureMap().get(teamPotDraw).getFirst();
            if (validTeams.size()<= 1){
                continue;
            }
            if (potFixtures.get("Home") ==null){
                opposition = rand.nextInt(validTeams.size());
                Team homeOpposition  = validTeams.get(opposition);
                while (homeOpposition.getFixture(teamDrawn.getPot(), "Away")!=null){
                    opposition = rand.nextInt(validTeams.size());
                    homeOpposition  = validTeams.get(opposition);
                }
                HomeDraw =  new Match(teamDrawn,homeOpposition);
                validTeams.remove(opposition);
            }
            if (potFixtures.get("Away") == null){
                opposition = rand.nextInt(validTeams.size());
                Team awayOpposition  = validTeams.get(opposition);
                while (awayOpposition.getFixture(teamDrawn.getPot(), "Home")!=null){
                    opposition = rand.nextInt(validTeams.size());
                    awayOpposition  = validTeams.get(opposition);
                }
                AwayDraw =  new Match(awayOpposition,teamDrawn);
                validTeams.remove(opposition);
            }
            if (HomeDraw != null || AwayDraw !=null){
                List<Match> draws = new ArrayList<>();
                Collections.addAll(draws,HomeDraw,AwayDraw);
                teamDrawn.addMatch(draws,teamPotDraw);
            }

        }
        teamExludeList.add(teamDrawn);
        teamsFromPot.remove(teamDrawn);
    }
    public static void displayPotFixture(int potNo){
        Pot pot = potList.get(potNo-1);  // Get the current Pot
        List<Team> potTeam = pot.getTeams();
        System.out.printf("%-20s%-40s%-40s%-40s%-40s%-40s%-40s%-40s%-40s\n", "POT 1", "HOME", "AWAY", "HOME",
                "AWAY", "HOME", "AWAY", "HOME", "AWAY");
        for (Team team :potTeam){
            HashMap<Pot, List < Map<String, Match>>> teamFixtures= team.getFixtureMap();
            // Assuming you have a method to get opponent team names for home and away fixtures
            System.out.printf("%-20s%-40s%-40s%-40s%-40s%-40s%-40s%-40s%-40s\n",
                    team.getTeamName(),
                    getOpponentName(team, potList.get(0), "Home"),
                    getOpponentName(team, potList.get(0), "Away"),
                    getOpponentName(team, potList.get(1), "Home"),
                    getOpponentName(team, potList.get(1), "Away"),
                    getOpponentName(team, potList.get(2), "Home"),
                    getOpponentName(team, potList.get(2), "Away"),
                    getOpponentName(team, potList.get(3), "Home"),
                    getOpponentName(team, potList.get(3), "Away"));

        }
}
    public static String getOpponentName(Team team, Pot pot, String type) {
        Match match = team.getFixture(pot, type);  // Get the fixture based on pot and type
        if (match != null && match.getOpponent(team) != null) {
            return match.getOpponent(team).getTeamName();  // Return the opponent's name
        } else {
            return "N/A";  // Return "N/A" if no match or opponent is null
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
            Team team = new Team(teamDetails[1], teamDetails[0], potList,potNo);
            potList.get(potNo - 1).addTeam(team);
        }

    }
}
    public static List<Team> getPossibleOpponents(Team teamDrawn, int potNo) {
        String nationalAssoc = teamDrawn.getNationalAssoc();
        Map<String, Long> nationalAssocOfOpponents = teamDrawn.getMatchList().stream()
                .map(match -> match.getOpponent(teamDrawn))
                .map(Team::getNationalAssoc)
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        List<String> filteredNationalAssoc = nationalAssocOfOpponents.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        return (potList.get(potNo-1).getTeams().stream()
                .filter(team -> !nationalAssoc.equals(team.getNationalAssoc()))
                .filter(team -> !teamExludeList.contains(team))
                .filter(team -> team.getMatchList().size()<8)
                .filter(team -> !filteredNationalAssoc.contains(team.getNationalAssoc()))
                .collect(Collectors.toList()));
    }
}
