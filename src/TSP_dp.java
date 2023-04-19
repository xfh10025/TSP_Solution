/*
* @author:xfh
* @data:2023.4.19
* @description:动态规划解决tsp问题
* */
import java.util.HashSet;
import java.util.Set;

public class TSP_dp {
    public static void solution(int[][] map){
        int n = map.length;
        int row = 1<<(n-1);
        int dp[][] = new int[n][row];
        for (int i = 0; i < n; i++) {//初始化第一列
            dp[i][0] = map[i][0];
        }

        for (int j = 1; j < row; j++) {
            for (int i = 1; i < n; i++) {   //  竖着填表
                if(((j >> (i-1)) & 1) == 0){//0表示该位没选中
                    int min = Integer.MAX_VALUE;
                    for (int k = 1; k < n; k++) {// 找子问题
                        if(((j >> (k-1)) & 1) == 1){//子问题中被选中的位置
                            int num = map[i][k] + dp[k][j^(1<<(k-1))];
                            min = min < num? min :num;
                        }
                    }
                    dp[i][j] = min;
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int k = 1; k < n; k++) {// 找子问题
            if((((row - 1) >> (k-1)) & 1) == 1){//子问题中被选中的位置
                int num = map[0][k] + dp[k][(row - 1) ^ (1<<(k-1))];
                min = min < num? min :num;
            }
        }
        dp[0][row - 1] = min;
        new MatrixPrint().print(dp);
        System.out.print("路线是:");
        PrintPath(dp,map);
        System.out.println("最小值："+min);
    }

    private static void PrintPath(int[][] dp,int [][]map) {
        int[] path = new int[dp.length + 1];
        int s = dp[0].length - 1;
        Set<Integer> visited = new HashSet<>();
        visited.add(0);

        int cur = 0;
        for (int i = 1; i < dp.length; i++) {//循环找path
            for (int j = 1; j < dp.length; j++) {
                if (!visited.contains(j) && dp[cur][s] == map[cur][j] + dp[j][s^(1<<(j - 1))]){
                    s ^= 1 << (j - 1);
                    cur = j;
                    path[i] = cur;
                    visited.add(cur);
                    break;
                }
            }
        }

        for (int i = 0; i < path.length; i++) {
            if (i == path.length - 1)
                System.out.println(path[i]);
            else
                System.out.print(path[i] + "➡");
        }
    }

    public static void main(String[] args) {
        int [][] map1 = {
                {0,3,1,5,8},
                {3,0,6,7,9},
                {1,6,0,4,2},
                {5,7,4,0,3},
                {8,9,2,3,0}
        };
        int [][] map2 = {
                {0,3,6,7},
                {5,0,2,3},
                {6,4,0,2},
                {3,7,5,0}
        };
        solution(map1);
        solution(map2);
    }
}
