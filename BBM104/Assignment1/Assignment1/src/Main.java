import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    private static List<String> moveList;
    private static int i =1;
    private static int points =0;
    private static int column;
    private static int row;
    public static void main(String[] args) {

        writeToFile("output.txt","",false,false); // initialize empty output.txt
        writeOutput("Game board:");
        String board = Arrays.toString(readFile(args[0], true, false));
        board = board.substring(1,board.length()-1);  // removing "[" and "]" characters
        ArrayList<String> tempList = new ArrayList<String>(Arrays.asList(board.split(", ")));
        ArrayList<ArrayList<String>> boardList = new ArrayList<ArrayList<String>>();  // making two-dimensional array list for board
        for (String element:tempList){
            ArrayList<String> elements = new ArrayList<String>(Arrays.asList(element.split(" ")));
            boardList.add(elements);
        }
        //the for loop down below, prints the game board
        for (int i = 0; i < boardList.size(); i++){
            for (int j = 0; j < boardList.get(0).size(); j++){
                writeToFile("output.txt",boardList.get(i).get(j)+" ",true,false);
            }
            writeOutput("");
        }
        writeOutput("");
        writeOutput("Your movement is:");
        String[] moves = readFile(args[1], true, false);

        moveList = new ArrayList<String>();
        for (String str:moves){
            str = str.replace(" ", "");  // removing spaces for convenience
            moveList.add(Arrays.toString(str.split(" ")));
        }
        for (String element:moveList){
            element = element.replace(" ", "");
        }
        //finding our ball "*"
        for(int i=0;i<boardList.size();i++){
            for(int j=0;j<boardList.get(0).size();j++){
                if (boardList.get(i).get(j).equals("*")){
                    row =i;
                    column =j;
                    break;
                }
            }
        }
        while (i < moveList.get(0).length()-1){  // reading every move
            if (moveList.get(0).charAt(i) == 'L'){
                if (column == 0 && boardList.get(row).get(boardList.get(row).size()-1).equals("H") || column != 0 && boardList.get(row).get(column-1).equals("H")){
                    fallIntoHole(boardList);
                }
                if (column == 0 && boardList.get(row).get(boardList.get(row).size()-1).equals("W") || column != 0 && boardList.get(row).get(column-1).equals("W")){
                    // if the ball move towards the wall, it bounces back two units
                    // so, instead of left, it should go right one unit
                    rightMethod(boardList);
                }else{ // if the ball does not move towards the wall and moves at the direction of it should be moved
                    leftMethod(boardList);
                }
            } else if (moveList.get(0).charAt(i) == 'R') {
                if (column == boardList.get(row).size()-1 && boardList.get(row).get(0).equals("H") || column != boardList.get(row).size()-1 && boardList.get(row).get(column+1).equals("H")){
                    fallIntoHole(boardList);
                }
                if (column == boardList.get(row).size()-1 && boardList.get(row).get(0).equals("W") || column != boardList.get(row).size()-1 && boardList.get(row).get(column+1).equals("W")){
                    // same as the left one, if the ball move towards the wall, it bounces back two units and goes left instead of right
                    // then, repeating same steps as the "left" one
                    leftMethod(boardList);
                }else{
                    rightMethod(boardList);
                }
            } else if (moveList.get(0).charAt(i) == 'U') {
                if (row == 0 && boardList.get(boardList.size()-1).get(column).equals("H") || row != 0 && boardList.get(row-1).get(column).equals("H")){
                    fallIntoHole(boardList);
                }
                if (row == 0 && boardList.get(boardList.size()-1).get(column).equals("W") || row != 0 && boardList.get(row-1).get(column).equals("W")){
                    downMethod(boardList);
                }else{
                    upMethod(boardList);
                }
            } else if (moveList.get(0).charAt(i) == 'D') {
                if (row == boardList.size()-1 && boardList.get(0).get(column).equals("H") || row != boardList.size()-1 && boardList.get(row+1).get(column).equals("H")){
                    fallIntoHole(boardList);
                }
                if (row == boardList.size()-1 && boardList.get(0).get(column).equals("W") || row != boardList.size()-1 && boardList.get(row+1).get(column).equals("W")){
                    upMethod(boardList);
                }else{
                    downMethod(boardList);
                }
            }
            i++;
        }
        writeMoves();
        writeOutput("Your output is:");
        for (int x = 0; x < boardList.size(); x++){
            for (int y = 0; y < boardList.get(0).size(); y++){
                writeToFile("output.txt",boardList.get(x).get(y)+" ",true,false);
            }
            writeOutput("");
        }
        writeOutput("");
        writeOutput("Score: "+points);
    }

    public static void leftMethod(ArrayList<ArrayList<String>> boardList){
        if (column == 0){  // if the ball is at the left end
            String temp = boardList.get(row).get(column);
            boardList.get(row).set(column,boardList.get(row).get(boardList.get(row).size()-1));
            boardList.get(row).set(boardList.get(row).size()-1,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            column = boardList.get(row).size()-1;
        }else{
            String temp = boardList.get(row).get(column);
            boardList.get(row).set(column,boardList.get(row).get(column-1));
            boardList.get(row).set(column-1,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            column--;
        }
    }
    public static void rightMethod(ArrayList<ArrayList<String>> boardList){
        if (column == boardList.get(row).size()-1){  // if the ball is at the right end
            String temp = boardList.get(row).get(column);  // swapping locations with three columns of code from here
            boardList.get(row).set(column,boardList.get(row).get(0));
            boardList.get(row).set(0,temp);
            // calculating points according to colors and changing some balls to "X" down below
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            column = 0;
        }else{
            String temp = boardList.get(row).get(column);
            boardList.get(row).set(column,boardList.get(row).get(column+1));
            boardList.get(row).set(column+1,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            column++;
        }
    }
    public static void downMethod(ArrayList<ArrayList<String>> boardList){
        if (row == boardList.size()-1){
            String temp = boardList.get(0).get(column);
            boardList.get(0).set(column,boardList.get(boardList.size()-1).get(column));
            boardList.get(boardList.size()-1).set(column,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            row = 0;
        }else{
            String temp = boardList.get(row).get(column);
            boardList.get(row).set(column,boardList.get(row+1).get(column));
            boardList.get(row+1).set(column,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            row++;
        }
    }
    public static void upMethod(ArrayList<ArrayList<String>> boardList){
        if (row == 0){
            String temp = boardList.get(boardList.size()-1).get(column);
            boardList.get(boardList.size()-1).set(column,boardList.get(0).get(column));
            boardList.get(0).set(column,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            row = boardList.size()-1;
        }else{
            String temp = boardList.get(row).get(column);
            boardList.get(row).set(column,boardList.get(row-1).get(column));
            boardList.get(row-1).set(column,temp);
            if (boardList.get(row).get(column).equals("R") || boardList.get(row).get(column).equals("Y") || boardList.get(row).get(column).equals("B")){
                calculatePoints(boardList.get(row).get(column));
                boardList.get(row).set(column,"X");
            }
            row--;
        }
    }
    public static void writeMoves(){
        for(int k=1;k<i+1;k++){
            writeToFile("output.txt", moveList.get(0).charAt(k)+" ", true, false);
        }
        writeOutput("\n");
    }
    public static void fallIntoHole(ArrayList<ArrayList<String>> boardList){
        writeMoves();
        boardList.get(row).set(column," ");
        writeOutput("Your output is:");
        for (int x = 0; x < boardList.size(); x++){
            for (int y = 0; y < boardList.get(0).size(); y++){
                writeToFile("output.txt",boardList.get(x).get(y)+" ",true,false);
            }
            writeOutput("");
        }
        writeOutput("");
        writeOutput("Game Over!");
        writeOutput("Score: "+points);
        System.exit(0);  // if the ball falls into hole, the game must finish
    }

    // the method down below calculate total points according to color of ball
    public static void calculatePoints(String ball){
        if (ball.equals("R")){
            Main.points+=10;
        } else if (ball.equals("Y")) {
            Main.points+=5;
        } else if (ball.equals("B")) {
            Main.points-=5;
        }
    }
    public static void writeOutput(String content){  // to shorten the writing method
        writeToFile("output.txt", content, true, true);
    }
    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
            if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { //Trims each line.
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) { //Returns null if there is no such a file.
            e.printStackTrace();
            return null;
        }
    }
    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
    }
}