/* @author:xfh
 * @data:2023.4.19
 * @description:分支限界法节点
 * */
import java.util.List;

public class Node implements Comparable<Node>{
    public int lb;
    public int now;//当前顶点
    public List<Integer> path;
    Node(){}
    Node(int lb, List<Integer> path, int now){
        this.lb = lb;
        this.path = path;
        this.now = now;
    }
    public String toString(){
        StringBuilder sb =new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i != path.size() - 1)
                sb.append("➡");
        }
        return "lb:"+this.lb+'\n'+"path"+"("+path.size()+"):"+sb.toString();
    }
    @Override
    public int compareTo(Node node){
        return this.lb - node.lb;
    }
}
