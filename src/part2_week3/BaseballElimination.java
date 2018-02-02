package part2_week3;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseballElimination {

    private int numberOfTeams;
    private List<String> teams = new ArrayList<>();
    private List<Integer> remaining = new ArrayList<>();
    private List<Integer> loss = new ArrayList<>();
    private List<Integer> win = new ArrayList<>();
    private int[][] games;
    private int maxWinIndex;
    private int maxWins;
    private int V;
    private int s;
    private int t;

    public BaseballElimination(String filename) {
        In in = new In(filename);
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

        maxWinIndex = 0;
        maxWins = 0;
        for(int i = 0 ; i<win.size(); i++)
            if (win.get(i) > maxWins) {
                maxWins = win.get(i);
                maxWinIndex = i;
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
        int index = teams.indexOf(team);
        validateCondition(index == -1);

        if (triviallyEliminated(index)) return true;
        FlowNetwork flowNetwork = buildNetwork(index);
        if (flowNetwork != null) {
            FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);
            for (FlowEdge edge : flowNetwork.adj(s)) {
                if (edge.flow() < edge.capacity()) {
                    return true;
                }
            }
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int index = teams.indexOf(team);
        validateCondition(index == -1);
        Set<String> result = new HashSet<>();

        FlowNetwork flowNetwork = buildNetwork(index);
        if (flowNetwork == null) {
            result.add(teams.get(maxWinIndex));
            return result;
        } else {
            FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);
            for (FlowEdge edge : flowNetwork.adj(s)) {
                if (edge.flow() < edge.capacity()) {
                    for (String t : teams()) {
                        int id = teams.indexOf(t);
                        if (fordFulkerson.inCut(id)) {
                            result.add(t);
                        }
                    }
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    private FlowNetwork buildNetwork(int index) {
        if (triviallyEliminated(index)) return null;
        int curMaxWins = win.get(index) + remaining.get(index);
        s = numberOfTeams;
        t = numberOfTeams + 1;
        V = t;
        Set<FlowEdge> edges = new HashSet<>();
        for (int i=0; i< numberOfTeams; i++) {
            if (i == index) continue;
            for (int j=i+1; j< numberOfTeams; j++) {
                if (j == index || games[i][j] == 0) continue;
                V++;
                edges.add(new FlowEdge(s, V, games[i][j]));
                edges.add(new FlowEdge(V, i, Double.POSITIVE_INFINITY));
                edges.add(new FlowEdge(V, j, Double.POSITIVE_INFINITY));
            }
            if (curMaxWins - win.get(i) > 0)
                edges.add(new FlowEdge(i, t, curMaxWins - win.get(i)));
        }

        FlowNetwork flowNetwork = new FlowNetwork(V+1);
        for (FlowEdge edge : edges) {
            flowNetwork.addEdge(edge);
        }
        return flowNetwork;
    }

    private boolean triviallyEliminated(int index) {
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == index) continue;
            if (win.get(index) + remaining.get(index) < win.get(i)) {
                return true;
            }
        }
        return false;
    }

    private void validateCondition(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("args not valid!!!");
    }

    public static void main(String[] args) {

//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            StdOut.print(team + " ");
//        }
//        StdOut.println();
//        // for case teams12.txt
//        StdOut.println("numberOfTeams--->" + division.numberOfTeams());
//        StdOut.println("wins--->" + division.wins("Poland"));
//        StdOut.println("losses--->" + division.losses("Poland"));
//        StdOut.println("remaining--->" + division.remaining("Poland"));
//        StdOut.println("Poland against Egypt--->" + division.against("Poland","Egypt"));

        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
