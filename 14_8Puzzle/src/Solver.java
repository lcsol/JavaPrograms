import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Stack;

public class Solver {
    private SearchNode goal;

    private class SearchNode implements Comparable<SearchNode>  {
        private final Board board;
        private SearchNode predecessor;
        private int moves;
        private boolean isTwin;

        public SearchNode(Board board, SearchNode predecessor, boolean isTwin) {
            this.board = board;
            this.predecessor = predecessor;
            if (predecessor != null) moves = predecessor.moves + 1;
            else moves = 0;
            this.isTwin = isTwin;
        }
        @Override
        public int compareTo(SearchNode n) {
            int priority = this.board.manhattan() + this.moves - n.board.manhattan() - n.moves;
            if (priority == 0) return this.board.manhattan() - n.board.manhattan();
            return priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        goal = null;

        MinPQ<SearchNode> queue = new MinPQ<>();
        queue.insert(new SearchNode(initial, null, false));
        queue.insert(new SearchNode(initial.twin(), null, true));

//        MinPQ<SearchNode> twinQueue = new MinPQ<>();
//        twinQueue.insert(new SearchNode(initial.twin(), null));
//        while (true) {
//            goal = solve(queue);
//            if (goal != null || solve(twinQueue) != null) break;
//        }
        while (!queue.isEmpty()) {
            SearchNode current = queue.delMin();
            if (current.board.isGoal()) {
                if (!current.isTwin) goal = current;
                break;
            }
            for (Board neighbor : current.board.neighbors()) {
                if (current.predecessor == null || !neighbor.equals(current.predecessor.board))
                    queue.insert(new SearchNode(neighbor, current, current.isTwin));
            }
        }
    }
//    private SearchNode solve(MinPQ<SearchNode> queue) {
//        if (queue.isEmpty()) return null;
//        SearchNode current = queue.delMin();
//        if (current.board.isGoal()) return current;
//        for (Board neighbor : current.board.neighbors()) {
//            if (current.predecessor == null || !neighbor.equals(current.predecessor.board))
//                queue.insert(new SearchNode(neighbor, current));
//        }
//        return null;
//    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return goal != null;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? goal.moves : -1;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> res = new Stack<>();
        SearchNode temp = goal;
        while (temp != null) {
            res.push(temp.board);
            temp = temp.predecessor;
        }
        Stack<Board> ans = new Stack<>();
        while (!res.empty()) {
            ans.push(res.pop());
        }
        return ans;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}