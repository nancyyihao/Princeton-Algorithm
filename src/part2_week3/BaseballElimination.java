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
    private int maxIndex;
    private int maxWins;

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
        maxWins = 0;
        for (int i = 0; i < numberOfTeams; i++)
            if (win.get(i) > maxWins) {
                maxWins = win.get(i);
                maxIndex = i;
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

        if (triviallyEliminated(index)) {
            return true;
        }
        Graph graph = buildGraphFor(index);
        for (FlowEdge edge : graph.network.adj(graph.source)) {
            if (edge.flow() < edge.capacity()) {
                return true;
            }
        }
        return false;
    }

    private boolean triviallyEliminated(int id) {
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != id) {
                if (win.get(id) + remaining.get(id) < win.get(i)) {
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
        if (triviallyEliminated(index)) {
            result.add(teams.get(maxIndex));
            return result;
        }

        Graph g = buildGraphFor(index);
        for (FlowEdge edge : g.network.adj(g.source)) {
            if (edge.flow() < edge.capacity()) {
                for (String t : teams()) {
                    int id = teams.indexOf(t);
                    if (g.ff.inCut(id)) {
                        result.add(t);
                    }
                }
            }
        }

        return result.isEmpty() ? null : result;
    }

    private Graph buildGraphFor(int index) {
        int n = numberOfTeams();
        int source = n;
        int sink = n + 1;
        int gameNode = n + 2;
        int currentMaxWins = win.get(index) + remaining.get(index);
        Set<FlowEdge> edges = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (i == index || win.get(i) + remaining.get(i) < maxWins) {
                continue;
            }

            for (int j = 0; j < i; j++) {
                if (j == index || games[i][j] == 0 || win.get(j) + remaining.get(j) < maxWins) {
                    continue;
                }

                edges.add(new FlowEdge(source, gameNode, games[i][j]));
                edges.add(new FlowEdge(gameNode, i, Double.POSITIVE_INFINITY));
                edges.add(new FlowEdge(gameNode, j, Double.POSITIVE_INFINITY));
                gameNode++;
            }
            edges.add(new FlowEdge(i, sink, currentMaxWins - win.get(i)));
        }

        FlowNetwork network = new FlowNetwork(gameNode);
        for (FlowEdge edge : edges) {
            network.addEdge(edge);
        }
        FordFulkerson ff = new FordFulkerson(network, source, sink);
        return new Graph(ff, network, source, sink);
    }

    private class Graph {
        FordFulkerson ff;
        FlowNetwork network;
        int source;
        int sink;

        public Graph(FordFulkerson ff, FlowNetwork network, int source, int sink) {
            super();
            this.ff = ff;
            this.network = network;
            this.source = source;
            this.sink = sink;
        }
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
