package com.trace.puzzle;
 import java.util.Random;  
   
 public class ArrayBall {  
     private StringBuilder solution = new StringBuilder();  
   
     public static void main(String[] args) {  
         ArrayBall main = new ArrayBall();  
         // main.doHit(main.generateTable(5, 5, 4));  
         boolean[][] table = new boolean[5][5];  
         table[0][0] = true;  
         table[2][0] = true;  
         table[3][0] = true;  
         table[2][4] = true;  
         table[3][0] = true;  
         table[0][3] = true;  
         System.out.println("table is: \n" + main.getTableString(table));  
         main.doHit(table);  
         if (main.solution.length() == 0) {  
             System.out.println("There is not any solution");  
         } else {  
             main.solution.insert(0, "There is a solution.\n");  
             System.out.println(main.solution.toString());  
         }  
     }  
   
     public boolean[][] generateTable(int rowSize, int colSize, int nums) {  
         boolean[][] table = new boolean[rowSize][colSize];  
         Random random = new Random();  
         while (nums != 0) {  
             int x = random.nextInt(rowSize);  
             int y = random.nextInt(colSize);  
             if (table[x][y] == true) {  
                 continue;  
             } else {  
                 table[x][y] = true;  
                 nums--;  
             }  
         }  
         System.out.println("New generate table is: \n");  
         System.out.println(getTableString(table));  
         return table;  
     }  
   
     public boolean doHit(boolean[][] table) {  
         for (int i = 0; i < table.length; i++) {  
             for (int j = 0; j < table[i].length; j++) {  
                 if (table[i][j]) {  
                     if (goUp(table, i, j)) {  
                         return true;  
                     }  
                     if (goLeft(table, i, j)) {  
                    	 return true;  
                     }  
                     if (goRight(table, i, j)) {  
                    	 return true;  
                     }  
                     if (goDown(table, i, j)) {  
                         return true;  
                     }  
                 }  
             }  
         }  
         return false;  
     }  
   
     private boolean goUp(boolean[][] table, int i, int j) {  
         table = getNewCopy(table);  
         boolean didHit = false;  
         StringBuilder sb = new StringBuilder();  
         sb.append("the ball at (").append(i).append(",").append(j).append(  
                 ") hit the ball by up going.");  
         if (i - 2 >= 0 && !table[i - 1][j]) {  
             for (int k = i - 2; k >= 0; k--) {  
                 if (table[k][j]) {  
                     didHit = true;  
                     table[i][j] = false;  
                     table[k + 1][j] = true;  
                     table[k][j] = false;  
                     i = k;  
                 }  
             }  
         }  
         sb.append(" After hit, the new table is:\n").append(  
                 getTableString(table));  
         boolean result = checkAndDo(table, didHit);  
         if (result) {  
             solution.insert(0, sb.toString());  
         }  
         return result;  
     }  
   
     private boolean goDown(boolean[][] table, int i, int j) {  
         table = getNewCopy(table);  
         boolean didHit = false;  
         StringBuilder sb = new StringBuilder();  
         sb.append("the ball at (").append(i).append(",").append(j).append(  
                 ") hit the ball by down going.");  
         if (i + 2 < table.length && !table[i + 1][j]) {  
             for (int k = i + 2; k < table.length; k++) {  
                 if (table[k][j]) {  
                     didHit = true;  
                     table[i][j] = false;  
                     table[k - 1][j] = true;  
                     table[k][j] = false;  
                     i = k;  
                 }  
             }  
         }  
         sb.append(" After hit, the new table is:\n").append(  
                 getTableString(table));  
         boolean result = checkAndDo(table, didHit);  
         if (result) {  
             solution.insert(0, sb.toString());  
         }  
         return result;  
     }  
   
     private boolean goLeft(boolean[][] table, int i, int j) {  
         table = getNewCopy(table);  
         boolean didHit = false;  
         StringBuilder sb = new StringBuilder();  
         sb.append("the ball at (").append(i).append(",").append(j).append(  
                 ") hit the ball by left going.");  
         if (j - 2 >= 0 && !table[i][j - 1]) {  
             for (int k = j - 2; k >= 0; k--) {  
                 if (table[i][k]) {  
                     didHit = true;  
                     table[i][j] = false;  
                     table[i][k + 1] = true;  
                     table[i][k] = false;  
                     j = k;  
                 }  
             }  
         }  
         sb.append(" After hit, the new table is:\n").append(  
                 getTableString(table));  
         boolean result = checkAndDo(table, didHit);  
         if (result) {  
             solution.insert(0, sb.toString());  
         }  
         return result;  
     }  
   
     private boolean goRight(boolean[][] table, int i, int j) {  
         table = getNewCopy(table);  
         boolean didHit = false;  
         StringBuilder sb = new StringBuilder();  
         sb.append("the ball at (").append(i).append(",").append(j).append(  
                 ") hit the ball by right going.");  
         if (j + 2 < table[0].length && !table[i][j + 1]) {  
             for (int k = j + 2; k < table[i].length; k++) {  
                 if (table[i][k]) {  
                     didHit = true;  
                     table[i][j] = false;  
                     table[i][k - 1] = true;  
                     table[i][k] = false;  
                     j = k;  
                 }  
             }  
         }  
         sb.append(" After hit, the new table is:\n").append(  
                 getTableString(table));  
         boolean result = checkAndDo(table, didHit);  
         if (result) {  
             solution.insert(0, sb.toString());  
         }  
         return result;  
     }  
   
     private boolean checkAndDo(boolean[][] table, boolean didHit) {  
    	 String a = getTableString(table);
         System.out.println(a);
    	 if (isEnd(table)) {  
             return true;  
         } else {  
             if (didHit) {  
                 return doHit(table);  
             } else {  
                 return false;  
             }  
         }  
     }  
   
     private boolean[][] getNewCopy(boolean[][] table) {  
         boolean[][] copy = table.clone();  
         for (int i = 0; i < copy.length; i++) {  
             copy[i] = table[i].clone();  
         }  
         return copy;  
     }  
   
     private boolean isEnd(boolean[][] table) {  
         boolean haveOne = false;  
         for (int i = 0; i < table.length; i++) {  
             for (int j = 0; j < table[i].length; j++) {  
                 if (table[i][j] && !haveOne) {  
                     haveOne = true;  
                 } else if (table[i][j] && haveOne) {  
                     return false;  
                 }  
             }  
         }  
         return true;  
     }  
   
     private String getTableString(boolean[][] table) {  
         StringBuilder sb = new StringBuilder();  
         for (int i = 0; i < table.length; i++) {  
             for (int j = 0; j < table[i].length; j++) {  
                 if (j != 0) {  
                     sb.append(" , ");  
                 }  
                 if (table[i][j]) {  
                     sb.append("O");  
                 } else {  
                     sb.append("X");  
                 }  
             }  
             sb.append("\n");  
         }  
         return sb.toString();  
     }  
 } 