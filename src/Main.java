import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    private List<Team> teams;

    public static void main(String[] args) {
        Main mainInstance = new Main();
        try {
            mainInstance.teams = mainInstance.getAllTeams("UEFA_Champions_League_2024_2025_Teams_with_Pots.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Team> getAllTeams(String fileName) throws FileNotFoundException {
        List<Team> teamList = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(fileName))) {
            if (sc.hasNextLine()) {
                sc.nextLine();  // Skip the header line
            }
            while (sc.hasNextLine()) {
                String[] teamDetails = sc.nextLine().split(",");
                Team team = new Team(teamDetails[1], teamDetails[0], Integer.parseInt(teamDetails[2]));
                teamList.add(team);
            }
        }
        return Collections.unmodifiableList(teamList);
    }
}
