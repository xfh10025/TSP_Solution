/* @author:xfh
 * @data:2023.4.19
 * @description:用蛮力法，回溯法，分支限界法解决tsp问题
 * */
import java.util.*;

public class TSP {
    //蛮力法
    public static void Violent(int[][]map){
        List<List<Integer>> paths = new ArrayList<>();
        int []destination = new int[map.length + 1];
        for (int i = 0; i < destination.length - 1; i++) {
            destination[i] = i;
        }
        //全排列获得所有的组合情况
        pailie(1,destination.length - 2,destination,paths);
//        System.out.println(paths.size());
//        for (int i = 0; i < paths.size(); i++) {
//            System.out.println(paths.get(i));
//        }
        List<Integer> opt = null;
        int len = 100;
        int sum;
        for (int i = 0; i < paths.size(); i++) {
            List<Integer>tmp = paths.get(i);
            sum = 0;
            for (int j = 0; j < map.length; j++) {
                sum += map[tmp.get(j)][tmp.get(j+1)];
            }
            if (sum < len){
                len = sum;
                opt = tmp;
            }
        }
        System.out.print("路线是:");
        for (int i = 0; i < opt.size(); i++) {
            if (i == opt.size() - 1)
                System.out.println(opt.get(i));
            else
                System.out.print(opt.get(i) + "➡");
        }
        System.out.println("长度是:"+len);
    }
    public static void pailie(int start, int end, int[] destination, List<List<Integer>> paths){
        if (start == end){
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < destination.length; i++) {
                list.add(destination[i]);
            }
            paths.add(list);
            return;
        }
        for (int i = start; i <= end; i++) {
            swap(start,i,destination);
            pailie(start+1,end,destination,paths);
            swap(start,i,destination);
        }
    }
    public static void swap(int start,int end,int[] destination){
        int temp = destination[start];
        destination[start] = destination[end];
        destination[end] = temp;
    }

    /*------------------------------------------------------------------------------------------*/
    //回溯法
    private static int ans = 100;
    private static int[] path;

    public static void BackTrack(int[][]map){
        path = new int[map.length + 1];
        int []tmp = new int[map.length + 1];
        int []visited = new int[map.length];
        visited[0] = 1;
        int index = 1;
        for (int i = 1; i < map.length; i++) {
            tmp[index++] = i;
            visited[i] = 1;
            dfs(map,tmp,i,index,visited,map[0][i]);
            tmp[--index] = 0;
            visited[i] = 0;
        }
        System.out.print("路线是:");
        for (int i = 0; i < path.length; i++) {
            if (i == path.length - 1)
                System.out.println(path[i]);
            else
                System.out.print(path[i] + "➡");
        }
        System.out.println("长度是:"+ans);
    }
    public static void dfs(int[][]map,int[]tmp,int now ,int index,int []visited,int sum){
        if (index == map.length){
            sum += map[now][0];
            if (sum < ans){
                ans = sum;
                for (int i = 0; i < tmp.length; i++) {
                    path[i] = tmp[i];
                }
            }
            return;
        }
        for (int i = 0; i < map.length; i++) {
            if (visited[i] == 1)
                continue;
            sum += map[now][i];
            if (sum > ans)//剪枝，不可能成为最优解了
                return;
            tmp[index++] = i;
            visited[i] = 1;
            dfs(map,tmp,i,index,visited,sum);
            visited[i] = 0;
            tmp[--index] = 0;
            sum -= map[now][i];
        }
    }
    /*------------------------------------------------------------------------------------------*/
    //分支限界法
    private static Queue<Node> queue = null;
    private static int UB = 0;

    public static void initUb(int[][] map){//贪心法初始化上界
        int visited[] = new int[map.length];
        visited[0] = 1;
        greedy_dfs(map,0,0,visited);
    }
    public static void greedy_dfs(int [][]map,int pos,int layer,int[] visited){
        if (layer == map.length - 1){
            UB += map[pos][0];
            return;
        }
        int min = 100;
        int index = 0;
        for (int i = 1; i < map.length; i++) {
            if (map[pos][i] == 0)
                continue;
            if (map[pos][i] < min && visited[i] == 0){
                min = map[pos][i];
                index = i;
            }
        }
        visited[index] = 1;
        UB += min;
        greedy_dfs(map,index,layer+1,visited);
    }
    public static void BranchBound(int[][]map){//分支限界法
        //贪心获取初始ub
        initUb(map);
        int sum = 0;
        for (int i = 0; i < map.length; i++) {
            int []tmp = getMin2(map[i]);
            sum += tmp[0] + tmp[1];
        }
        int lb = sum / 2;
        Node head = new Node(lb,new ArrayList<>(Arrays.asList(0)),0);
        queue = new LinkedList<>();
        queue.offer(head);
        bfs(map,lb);
    }
    private static void bfs(int[][] map,int LB) {
        List<Integer> opt = null;
        int len = 100;
        while(!queue.isEmpty()){
            Node node = queue.poll();
            int[]visited = new int[map.length];
            for (int i = 0; i < node.path.size(); i++) {
                visited[node.path.get(i)] = 1;
            }
            int now = node.now;
            for (int i = 1; i < map.length; i++) {
                if(i == now || visited[i] == 1)
                    continue;
                visited[i] = 1;
                int lb = getLb(map,node.path,i);
                if (lb >= LB && lb <= UB){
                    Node nn = new Node(lb,CreateNewPath(node.path,new ArrayList<>(),i),i);
                    if (nn.path.size() == map.length){
                        int tmp = getLen(map,nn.path);
                        if (tmp < len){
                            len = tmp;
                            opt = CreateNewPath(nn.path,new ArrayList<>(),0);
                        }
                    }
                    else
                        queue.offer(nn);
                }
            }
            queueSort();
        }
        System.out.print("路线是:");
        for (int i = 0; i < opt.size(); i++) {
            if (i == opt.size() - 1)
                System.out.println(opt.get(i));
            else
                System.out.print(opt.get(i) + "➡");
        }
        System.out.println("长度是:"+len);
    }

    private static int getLen(int[][] map, List<Integer> path) {//获取当前路径的长度
        int sum = 0;
        for (int i = 0; i < path.size(); i++) {
            if (i == path.size() - 1){
                sum += map[path.get(i)][0];
            }
            else
                sum += map[path.get(i)][path.get(i+1)];
        }
        return sum;
    }


    private static void queueSort(){//将广搜队列排序，分支限界法每次选择lb最小的节点进行搜索
        Node[] nodes = new Node[queue.size()];
        int i = 0;
        while(!queue.isEmpty()){
            nodes[i++] = queue.poll();
        }
        Arrays.sort(nodes);
        for (i = 0; i < nodes.length; i++) {
            queue.offer(nodes[i]);
        }
    }
    private static int getMin1(int[] arr,int pos){//确定了某个顶点，选择除他外的最小的顶点
        int min = 100;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0 || i == pos)
                continue;
            if (arr[i] < min){
                min = arr[i];
            }
        }
        return min;
    }
    private static int getLb(int [][]map,List<Integer> origin,int now) {//计算lb
        int sum = 0;
//        int sum = 0;
//        if (origin.size() > 1)
//            sum += map[0][now]*2 + map[0][origin.get(1)];
//        else{
//            sum += map[0][now]*2 + getMin1(map[0],now) + getMin1(map[now],0);
//        }
        int hash[] = new int[map.length];
        hash[now] = 1;
        for (int i = 0; i < origin.size(); i++) {
            hash[origin.get(i)] = 1;
        }
        for (int i = 0; i < hash.length; i++) {
            if(hash[i] == 0){
                int []tmp = getMin2(map[i]);
                sum += tmp[0]+tmp[1];
            }
        }
        int i;
        for (i = 0; i < origin.size(); i++) {
            int pos = origin.get(i);
            if (i == 0){
                if (origin.size() == 1){
                    sum += map[0][now]*2 + getMin1(map[0],now) + getMin1(map[now],0);
                }
                else {
                    int next = origin.get(i + 1);
                    sum += map[pos][next] + getMin1(map[0],next);
                }
            }
            else if(i == origin.size() - 1){
                int pre = origin.get(i - 1);
                sum += map[pos][pre] + map[pos][now]*2 + getMin1(map[now],pos);
            }

            else{
                int pre = origin.get(i - 1);
                int next = origin.get(i + 1);
                sum += map[pos][pre] + map[pos][next];
            }
        }
        return (sum + 1)/2;
    }

    public static List<Integer> CreateNewPath(List<Integer> origin,List<Integer> copy,int now){//将当前顶点加入到路径中
        for (int i = 0; i < origin.size(); i++) {
            copy.add(origin.get(i));
        }
        copy.add(now);
        return copy;
    }
    public static int[] getMin2(int[] arr){//邻接矩阵每行选择两个最小的点，作为初始的lb
        int min1 = 100,min2 = 100;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0)
                continue;
            if (arr[i] < min1){
                min2 = min1;
                min1 = arr[i];
            }
            else if (arr[i] > min1 && arr[i] < min2){
                min2 = arr[i];
            }
        }
        return new int[]{min1, min2};
    }
    public static void main(String[] args) {
        int [][] map = {
                {0,3,1,5,8},
                {3,0,6,7,9},
                {1,6,0,4,2},
                {5,7,4,0,3},
                {8,9,2,3,0}
        };
        BackTrack(map);
        Violent(map);
        BranchBound(map);
    }
}
