package part2_week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseballElimination {

    private int numberOfTeams;
    private List<String> teams = new ArrayList<>();
    private List<Integer> remaining = new ArrayList<>();
    private List<Integer> loss = new ArrayList<>();
    private List<Integer> win = new ArrayList<>();
    private int[][] games;

    public BaseballElimination(String filename) {
        In in = new In(new File(filename));
        numberOfTeams = in.readInt();
        games = new int[numberOfTeams][numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(in.readString());
            win.add(in.readInt());
            loss.add(in.readInt());
            remaining.add(in.readInt());
            for (int j = 0; j< numberOfTeams; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return numberOfTeams;
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        int index = teams.indexOf(team);
        validateCondition(index == -1);

        return win.get(index);
    }

    public int losses(String team) {
        int index = teams.indexOf(team);
        validateCondition(index == -1);

        return loss.get(index);
    }

    public int remaining(String team) {
        int index = teams.indexOf(team);
        validateCondition(index == -1);

        return remaining.get(index);
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int index1 = teams.indexOf(team1);
        int index2 = teams.indexOf(team2);
        validateCondition(index1 == -1 || index2 == -1);

        return games[index1][index2];
    }

    public boolean isEliminated(String team) {
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    private void validateCondition(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("args not valid!!!");
    }

    public static void main(String[] args) {

        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            StdOut.print(team + " ");
        }
        StdOut.println();
        // for case teams12.txt
        StdOut.println("numberOfTeams--->" + division.numberOfTeams());
        StdOut.println("wins--->" + division.wins("Poland"));
        StdOut.println("losses--->" + division.losses("Poland"));
        StdOut.println("remaining--->" + division.remaining("Poland"));
        StdOut.println("Poland against Egypt--->" + division.against("Poland","Egypt"));


//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
    }
}
