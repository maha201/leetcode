class Solution {
    List<Set<Integer>> regions;
    List<Set<Integer>> frontiers;
    List<Integer> perimeters;
    boolean[] visited;
    int R;
    int C;
    int[][] grid;
    int[] dr = new int[]{0, 0, -1, 1};
    int[] dc = new int[]{-1, 1, 0, 0};
    
    public int containVirus(int[][] grid) {
        if (grid == null || grid[0] == null || grid.length == 0 || grid[0].length == 0) return 0;
        this.grid = grid;
        R = grid.length;
        C = grid[0].length;
        int ret = 0;
        
        while (true) {
            visited = new boolean[R * C];
            regions = new ArrayList<>();
            frontiers = new ArrayList<>();
            perimeters = new ArrayList<>();
            
            // find all connected components, their frontier points and perimeters of each infected area
            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    if (grid[r][c] == 1 && !visited[r * C + c]) {
                        regions.add(new HashSet<Integer>());
                        frontiers.add(new HashSet<Integer>());
                        perimeters.add(0);
                        dfs(r, c);
                    }
                }
            }
            
            if (regions.isEmpty()) {
                // no new region get infected, either all cells are infected or all remaining cells are isolated by walls
                break;
            }
            
            // find the most hazardous region index
            int triageIndex = 0;
            for (int i = 0; i < frontiers.size(); i++) {
                if (frontiers.get(i).size() > frontiers.get(triageIndex).size()) {
                    triageIndex = i;
                }
            }
            ret += perimeters.get(triageIndex);
            
            // quarantine the most hazardous region
            // other regions infect their adjacent cells
            for (int i = 0; i < regions.size(); i++) {
                if (i == triageIndex) { // quarantine the most hazardous region
                    for (int index : regions.get(i)) {
                        grid[index / C][index % C] = -1;
                    }
                } else { // other regions spread the infection
                    for (int index : regions.get(i)) {
                        int r = index / C;
                        int c = index % C;
                        for (int k = 0; k < 4; k++) {
                            int newR = r + dr[k];
                            int newC = c + dc[k];
                            if (isValid(newR, newC) && grid[newR][newC] == 0) {
                                grid[newR][newC] = 1;
                            }
                        }
                    }
                }
            }
        }
        
        return ret;
    }
    
    private void dfs(int r, int c) {
        if (!visited[r * C + c]) {
            visited[r * C + c] = true;
            int N = regions.size();
            regions.get(N - 1).add(r * C + c);
            for (int i = 0; i < 4; i++) {
                int newR = r + dr[i];
                int newC = c + dc[i];
                if (isValid(newR, newC)) {
                    if (grid[newR][newC] == 1) {
                        dfs(newR, newC);
                    } else if (grid[newR][newC] == 0) {
                        // if this not an affected area, increase the perimeter and record this frontier point
                        frontiers.get(N - 1).add(newR * R + newC);
                        perimeters.set(N - 1, perimeters.get(N - 1) + 1);
                    }
                }
            }
        }
    }
    
    private boolean isValid(int r, int c) {
        return r >= 0 && r < R && c >= 0 && c < C;
    }
}
