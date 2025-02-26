
package ExpeditorsDITQuestion;

import java.util.Scanner;

//Entry point of program
//Handles console input from user
public class Driver {

    public static void main(String[] args){


        //****************For testing ! without the console stuff
//        String fileIn1 = "C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\DataFile.txt";
//        String fileIn2 = "C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\DataFile1.txt";
//        String fileOut1 = "C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\OutFileTest1.txt";
//        String fileOut2 = "C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\OutFileTest2.txt";

//        ExpeditorsQuestion EQ = new ExpeditorsQuestion();
//        String choice = "L";

//        EQ.processProfileInputFile(fileIn1);
//        EQ.processProfileOutputFile(fileOut1, 4, 20);
//        EQ.processProfileInputFile(fileIn2);
//        EQ.processProfileOutputFile(fileOut2, 40, 50);

        //****************For testing ! without the console stuff

        ExpeditorsQuestion EQ = new ExpeditorsQuestion();
        String choice = "L";

        Scanner scannerIn = new Scanner(System.in);
        System.out.println("*************  Welcome to the Household Data Aggregator! ************* \n" +
                "You can add data to the household repository multiple times from multiple files! \n" +
                "You can output the data to multiple files multiple times narrowing your results by age range! \n" +
                "You can even add data after you have printed to an output file!  \n" +
                "So much fun with Data! \n" +
                "**************************************** \n");

        while(!choice.equalsIgnoreCase("Q"))
        {
            System.out.println("Typing 'A' = Adding Data to the Data Aggregator. \n" +
                    "Typing 'O' = Outputing the existing data repository to a file. \n" +
                    "Typing 'Q'! = Quit the program. \n");

            choice = scannerIn.nextLine();
            if(choice.equalsIgnoreCase("Q") )
                return;

            if(choice.equalsIgnoreCase("A") )
            {
                System.out.println("Please enter the file path of the source file, Example - C:\\Users\\BlueC\\Java17_Projects\\Expeditors3\\src\\ExpeditorsDITQuestion\\DataFile.txt");
                String fileIn = scannerIn.nextLine();
                EQ.processProfileInputFile(fileIn);
            }

            if(choice.equalsIgnoreCase("O") )
            {
                System.out.println("Please enter the file path of the destination file");
                String fileOut = scannerIn.nextLine();
                System.out.println("Please enter the min age to be included in the household list, Example - 19");
                String minAgeString = scannerIn.nextLine();
                int minAge = Integer.parseInt(minAgeString);
                System.out.println("Please enter the max age to be included in the household list");
                String maxAgeString = scannerIn.nextLine();
                int maxAge = Integer.parseInt(maxAgeString);
                EQ.processProfileOutputFile(fileOut, minAge, maxAge);
                System.out.println("All done, go check your file!");
            }
        }
        System.out.println("Quitting now!");
    }
}
