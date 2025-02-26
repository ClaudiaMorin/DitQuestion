package ExpeditorsDITQuestion;


import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
Handles the data file reading and writing
*/
public class DataInputOutFile {

    public ArrayList<String> data;
    public DataInputOutFile()
    {}

    //Scans each line of a file and returns the data as an array of strings, will throw an error message to the user

    public ArrayList<String> readFile(String filetoReadString){
        File fileToRead = new File(filetoReadString);
        data = new ArrayList<String>();
        try {
            Scanner scannerToRead = new Scanner(fileToRead);
            while (scannerToRead.hasNextLine()) {
                String dataTemp = scannerToRead.nextLine();
                data.add(dataTemp);
            }
            scannerToRead.close();
        }
        catch(Exception e){
            System.out.println("Hey, you messed up someplace but you can try again!");
            System.out.println(e.getMessage());
        }

        return data;
    }

    //handles writing the data (ArrayList<String> to a specified file
    public void writeFile(String fileToWriteString, ArrayList<String> dataToWrite){
        File fileToWrite = new File(fileToWriteString);
        try {
            fileToWrite.createNewFile();
            PrintStream fs = new PrintStream(fileToWrite);
            for (String line : dataToWrite) {
                fs.println(line);
            }
            fs.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
