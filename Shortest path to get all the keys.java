class Solution {
    private static final int[][] dir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public int shortestPathAllKeys(String[] grid) {
        int height = grid.length, width = grid[0].length();
        char[][] g = new char[height][width];
        int startX = -1, startY = -1;
        int expect = 0;
        for(int i = 0; i < height; i++){
            char[] arr = grid[i].toCharArray();
            for(int j = 0; j < width; j++){
                g[i][j] = arr[j];
                if(arr[j] == '@'){
                    startX = i;
                    startY = j;
                }else if(arr[j] >= 'a' && arr[j] <= 'f'){
                    expect |= 1 << (arr[j] - 'a');
                }
            }
        }
        Queue<Integer> q = new LinkedList<>(); //int[] records [x, y]
        int startState = (startX << 16) | (startY << 8) | 0;
        q.offer(startState);
        int step = -1;
        Set<Integer> visited = new HashSet<>();
        while(!q.isEmpty()){
            int size = q.size();
            ++step;
            for(int i = 0; i < size; i++){
                int cur = q.poll();
                if((cur & (0b111111)) == expect) return step;
                if(visited.contains(cur)) continue;
                visited.add(cur);
                int tx = 0, ty = 0;
                int curX = (cur >> 16) & 0xFF, curY = (cur >> 8) & 0xFF;
                int curKey = cur & 0xFF;
                for(int d = 0; d < 4; d++){
                    tx = curX + dir[d][0];
                    ty = curY + dir[d][1];
                    if(tx >= 0 && tx < height && ty >= 0 && ty < width && g[tx][ty] != '#'){
                        if(isUpper(g[tx][ty]) && (cur & (1 << (g[tx][ty] - 'A'))) == 0) continue;
                        int nextState = (tx << 16) | (ty << 8) | (!isLower(g[tx][ty]) ? curKey : (curKey | (1 << (g[tx][ty] - 'A'))));
                        q.offer(nextState);
                    }
                }
            }
        }
        return -1;
    }
    private boolean isUpper(char c){
        return c >= 'A' && c <= 'F';
    }
    private boolean isLower(char c){
        return c >= 'a' && c <= 'f';
    }
}
