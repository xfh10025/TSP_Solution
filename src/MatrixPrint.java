/* @author:xfh
* @data:2023.4.19
* @description:打印dp数组
* */
public class MatrixPrint {
    MatrixPrint(){}
    public void print(int[][]matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%-5d",matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println("---------------------finish----------------------");
    }
}
