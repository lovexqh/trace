package com.trace.puzzle;
 import java.util.ArrayList;  
 import java.util.List;  
   
 public class PongBall {  
     private int[] size;  
     List<int[]> steps = new ArrayList<int[]>();  
     private long[][] pad;  
   
     public PongBall(int sizeX, int sizeY) {  
         this.size = new int[] { sizeX, sizeY };  
         this.pad = new long[size[0]][size[1]];  
     }  
   
     public static void main(String[] args) {  
         //*随机结果  
         String[] direction = { "右", "下", "左", "上" };  
         PongBall p = new PongBall(3,3);  
         p.randomBall(3);  
         String startStatus = "起始状态:\r\n" + p;  
         System.out.println(startStatus);  
         if (p.moveBall()) {  
             for (int[] ary : p.steps) {  
                 System.out.println("在数组坐标：(" + ary[0] + "," + ary[1] + ")处碰撞,方向：" + direction[ary[2]]);  
             }  
             System.out.println("最终结果：\r\n" + p);  
         } else {  
             System.out.println("无解！");  
         }  
         //*/  
         /*必然出现一个结果 
         String[] direction = { "右", "下", "左", "上" }; 
         PongBall p; 
         boolean result; 
         String startStatus ; 
         do { 
             p = new PongBall(7,8); 
             p.randomBall(5); 
             startStatus = "起始状态:\r\n" + p; 
         } while(!(result=p.moveBall())); 
         System.out.println(startStatus); 
         if (result) { 
             for (int[] ary : p.steps) { 
                 System.out.println("在数组坐标：(" + ary[0] + "," + ary[1] + ")处碰撞,方向：" + direction[ary[2]]); 
             } 
             System.out.println("最终结果：\r\n" + p); 
         } else { 
             System.out.println("无解！"); 
         } 
         //*/  
     }  
     public boolean moveBall() {  
         upBit();  
         boolean result = _moveBall();  
         if(!result) {  
             downBit();  
         }  
         return result;  
     }  
     private boolean _moveBall() {  
         int cnt = 0;  
         for (int i = 0; i < size[0]; i++) {  
             for (int j = 0; j < size[1]; j++) {  
                 if ((pad[i][j] & 1) == 1) {  
                     cnt++;  
                     for (int d = 0; d < 4; d++) {  
                         int x = i, y = j, hitCount = 0;  
                         for (int step = 0; step >= 0; step++) {  
                             int dx = (2 - d) * (d & 1), dy = (1 - d) * ((d + 1) & 1);  
                             x += dx;  
                             y += dy;  
                             if (x >= size[0] || x < 0 || y >= size[1] || y < 0) {// 当有球到边界时  
                                 if (hitCount > 0) {// 有过碰撞  
                                     pad[x - dx][y - dy] &= -2;// 最后一位清零  
                                     return moveBall();  
                                 } else {// 没有碰撞  
                                     recover();  
                                     break;  
                                 }  
                             }  
                             if ((pad[x][y] & 1) == 0) {// 下一位为空  
                                 pad[x - dx][y - dy] &= -2;  
                                 pad[x][y] |= 1;  
                             } else {// ==1  
                                 if (step == 0) {// 两球紧挨  
                                     recover();  
                                     break;  
                                 } else {// 碰撞  
                                     steps.add(new int[] { x - dx, y - dy, d });  
                                     hitCount++;  
                                 }  
                             }  
                         }  
                     }  
                 }  
             }  
         }  
         return cnt == 1;  
     }  
   
     private void upBit() {  
         for (int i = 0; i < size[0]; i++) {  
             for (int j = 0; j < size[1]; j++) {  
                 pad[i][j] <<= 1;  
                 pad[i][j] |= ((pad[i][j] >>> 1) & 1);  
             }  
         }  
     }  
   
     private void downBit() {  
         for (int i = 0; i < size[0]; i++) {  
             for (int j = 0; j < size[1]; j++) {  
                 pad[i][j] >>>= 1;  
             }  
         }  
     }  
   
     private void recover() {  
         for (int i = 0; i < size[0]; i++) {  
             for (int j = 0; j < size[1]; j++) {  
                 pad[i][j] &= Integer.MAX_VALUE - 1;  
                 pad[i][j] |= (pad[i][j] >>> 1) & 1;  
             }  
         }  
     }  
   
     public void randomBall(int ballCount) {  
         if (size[0] * size[1] < ballCount) {  
             throw new RuntimeException("Too many balls!");  
         }  
         int currentCount = 0;  
         while (currentCount < ballCount) {  
             int padX = (int) (Math.random() * size[0]);  
             int padY = (int) (Math.random() * size[1]);  
             if (pad[padX][padY] == 0) {  
                 currentCount++;  
                 pad[padX][padY] = 3;  
             }  
         }  
     }  
   
     public String toString() {  
         StringBuffer sb = new StringBuffer();  
         for (int i = 0; i < pad.length; i++) {  
             for (int j = 0; j < pad[i].length; j++) {  
                 sb.append((pad[i][j] & 1)==1?"O":"X").append(" ");  
             }  
             sb.append("\r\n");  
         }  
         return sb.toString();  
     }  
 }  