class Solution {
public:
    int strangePrinter(string s) {
        if(s.size() == 0) return 0;
        
        string tmp(1, s[0]);
        for(int i = 1; i < s.size(); i++){
            if(s[i] != s[i-1]){
                tmp += s[i];
            }
        }
        s = tmp;
        int n = s.size();
        
        vector<vector<int>> dp(n, vector(n, INT_MAX));
        
        for(int i = n-1; i >= 0; i--){
            for(int dist = 0; i+dist < n; dist++){
                int j = i+dist;
                if(dist == 0){
                    //base case: one char
                    dp[i][j] = 1;
                }else if(dist == 1){
                    //base case: two chars
                    /*
                    if the two chars are the same, 
                    we need just one print,
                    o.w. we need two prints
                    */
                    dp[i][j] = 1 + (s[i] != s[j]);
                }else{
                    /*
                    split s[i...j] into s[i...k] and s[k+1...j]
                    note that k can be i!
                    
                    k>=i, k+1 <=j
                    */
                    for(int k = i; k <= j-1; k++){
                        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j] - (s[k] == s[j]));
                    }
                }
            }
        }
        
        return dp[0][n-1];
    }
};
