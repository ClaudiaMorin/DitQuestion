
package ExpeditorsDITQuestion;

import java.util.Scanner;

public class Driver {

    public static void main(String[] args){

//        int minAge = 4;
//        int maxAge = 99;
//        String fileIn = "C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\DataFile.txt";
//        String fileOut = "C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\D6File.txt";

        ExpeditorsQuestion EQ = new ExpeditorsQuestion();

        Scanner scannerIn = new Scanner(System.in);
        System.out.println("Please enter the file path of the source file, Example - C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\DataFile.txt");
        String fileIn = scannerIn.nextLine();
        System.out.println("Please enter the file path of the destination file");
        String fileOut = scannerIn.nextLine();
        System.out.println("Please enter the min age to be included in the household list, Example - 19");
        String minAgeString = scannerIn.nextLine();
        int minAge = Integer.parseInt(minAgeString);
        System.out.println("Please enter the max age to be included in the household list");
        String maxAgeString = scannerIn.nextLine();
        int maxAge = Integer.parseInt(maxAgeString);

        EQ.processProfile(fileIn, fileOut);
        EQ.writeAgeRangeToFile(minAge, maxAge);
        System.out.println("All done, go check your file!");
    }
}
