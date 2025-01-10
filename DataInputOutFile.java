package ExpeditorsDITQuestion;


import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class DataInputOutFile {

    File fileToRead;
    File fileToWrite;
    public ArrayList<String> data;
    public DataInputOutFile(String filetoReadString, String fileToWriteString)
    {
        fileToRead = new File(filetoReadString);
        fileToWrite = new File(fileToWriteString);
    }

    public ArrayList<String> readFile(){
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
            System.out.println(e.getMessage());
        }

        return data;
    }

    public void writeFile(ArrayList<String> dataToWrite){
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
