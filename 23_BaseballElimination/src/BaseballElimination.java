import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private final int number;
    private final ArrayList<String> teamList;
    private final int[] winList, lossList, remainList;
    private final int[][] againstList;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        number = in.readInt();
        teamList = new ArrayList<>(number);
        winList = new int[number];
        lossList = new int[number];
        remainList = new int[number];
        againstList = new int[number][number];
        for (int i = 0; i < number; i++) {
            teamList.add(in.readString());
            winList[i] = in.readInt();
            lossList[i] = in.readInt();
            remainList[i] = in.readInt();
            for (int j = 0; j < number; j++) {
                againstList[i][j] = in.readInt();
            }
        }
    }
    // number of teams
    public int numberOfTeams() {
        return number;
    }
    // all teams
    public Iterable<String> teams() {
        return teamList;
    }
    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);
        return winList[teamList.indexOf(team)];
    }
    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);
        return lossList[teamList.indexOf(team)];
    }
    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);
        return remainList[teamList.indexOf(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        int idx1 = teamList.indexOf(team1), idx2 = teamList.indexOf(team2);
        return againstList[idx1][idx2];
    }
    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);
        int teamIdx = teamList.indexOf(team);
        // Trivial elimination
        for (int i = 0; i < number; i++) {
            if (winList[teamIdx] + remainList[teamIdx] < winList[i]) return true;
        }
        // Nontrivial elimination
        int againstVertices = ((number - 1) * (number - 2)) / 2;
        int v = 2 + againstVertices + number - 1;
        FlowNetwork fn = establish(teamIdx, againstVertices, v);
        FordFulkerson ff = new FordFulkerson(fn, 0, v - 1);
        int w;
        for (FlowEdge e : fn.adj(0)) {
            w = e.other(0);
            if (ff.inCut(w) && e.residualCapacityTo(w) != 0) return true;
        }
        return false;
    }
    // establish flow network
    private FlowNetwork establish(int teamIdx, int againstVertices, int v) {
        FlowNetwork fn = new FlowNetwork(v);
        int gameVerticesIdx = 1, teamVerticesIdx1 = 0, teamVerticesIdx2, teamVerticesCapacity;
        for (int i = 0; i < number; i++) {
            if (i != teamIdx) {
                teamVerticesIdx1 = (teamIdx > i) ? againstVertices + i + 1 : againstVertices + i;
                teamVerticesCapacity = Math.max(0, winList[teamIdx] + remainList[teamIdx] - winList[i]);
                fn.addEdge(new FlowEdge(teamVerticesIdx1, v - 1, teamVerticesCapacity)); // add edge from team vertices to t
            }
            for (int j = 0; j < number; j++) {
                if (i < j && i != teamIdx && j != teamIdx) {
                    teamVerticesIdx2 = (teamIdx > j) ? againstVertices + j + 1 : againstVertices + j;
                    fn.addEdge(new FlowEdge(0, gameVerticesIdx, againstList[i][j])); // add edge from s to game vertices
                    fn.addEdge(new FlowEdge(gameVerticesIdx, teamVerticesIdx1, Double.POSITIVE_INFINITY)); // add edge from game vertices to team vertex 1
                    fn.addEdge(new FlowEdge(gameVerticesIdx, teamVerticesIdx2, Double.POSITIVE_INFINITY)); // add edge from game vertices to team vertex 2
                    gameVerticesIdx++;
                }
            }
        }
        return fn;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        if (!isEliminated(team)) return null;
        ArrayList<String> res = new ArrayList<>();
        int teamIdx = teamList.indexOf(team);
        for (int i = 0; i < number; i++) {
            if (winList[teamIdx] + remainList[teamIdx] < winList[i]) {
                res.add(teamList.get(i));
                return res;
            }
        }
        int againstVertices = ((number - 1) * (number - 2)) / 2;
        int v = 2 + againstVertices + number - 1;
        FlowNetwork fn = establish(teamIdx, againstVertices, v);
        FordFulkerson ff = new FordFulkerson(fn, 0, v - 1);
        int teamVerticesIdx;
        for (int i = 0; i < number; i++) {
            if (i != teamIdx) {
                teamVerticesIdx = (teamIdx > i) ? againstVertices + i + 1 : againstVertices + i;
                if (ff.inCut(teamVerticesIdx)) res.add(teamList.get(i));
            }
        }
        return res;
    }

    private void checkTeam(String team) {
        if (!teamList.contains(team)) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
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
