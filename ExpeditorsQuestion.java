
package ExpeditorsDITQuestion;

import java.util.*;

public class ExpeditorsQuestion {
    public final Dictionary<String, ArrayList<String[]>> familyMap;
    public final Dictionary<Integer, String> familyMapToGroupID;
    private DataInputOutFile fileProcessor;
    ExpeditorsQuestion()
    {
        familyMap = new Hashtable<>();
        familyMapToGroupID = new Hashtable<>();
    }

    //using dictionary.
    public void processProfile(String textFileIn, String textFileOut)
    {
        fileProcessor = new DataInputOutFile(textFileIn, textFileOut);
        ArrayList<String> entry = fileProcessor.readFile();
        for (String stringLine: entry)
        {
            String[] entryCleaned = cleanEntryString(stringLine);
            if(entryCleaned != null)
            {
                String address = entryCleaned[0];
                String[] newMember = new String[]{entryCleaned[1], entryCleaned[2], entryCleaned[3]};
                if(familyMap.get(address) == null)
                {
                    ArrayList<String[]> newPerson = new ArrayList<>();
                    newPerson.add(newMember);
                    familyMap.put(address, newPerson);
                    familyMapToGroupID.put(familyMapToGroupID.size(), address);
                }
                else //family address already exists!
                {
                    familyMap.get(address).add(newMember);
                }
            }
        }
        removeDups();
        sortArraysLastFirst();
    }

    //All the string manipulation and removal of white spaces goes here.
    private  String[] cleanEntryString(String entry)
    {
        String[] profile = entry.replaceAll("\"", "").split(",");
        if(!checkStringFormatting(profile))
            return null;

        //Makes sure that the address only has one white space between the street number and the street, etc.
        String[] addressTemp = profile[2].toUpperCase().replaceAll("\\p{Punct}", "").split(" ");
        String addressClean = String.join( " ",addressTemp );
        String address = (addressClean + " " + profile[3].toUpperCase().replaceAll("\\p{Punct}", "").strip() + " " + profile[4].toUpperCase().replaceAll("\\p{Punct}", "").strip());

        String firstName = profile[0].substring(0, 1).toUpperCase() + profile[0].toLowerCase().substring(1).stripLeading().trim();
        String lastName = profile[1].substring(0, 1).toUpperCase() + profile[1].toLowerCase().substring(1).stripLeading().trim();
        String[] entryCleaned = new String[]{ address, firstName, lastName, profile[5].trim()};
        return entryCleaned;
    }

    /* Make sure we aren't adding mutiples of the same person */
    //Need to convert the String[] to a simple String so that I can add them to the hashset and the
    //dups will be dumped, a String[] is seen as a unique object so the set conversion won't work as well
    //This might be a costly way to remove dups but it is better than doing a loop through everytime someone new is added!
    private void removeDups()
    {
        for(int count = 0; count < familyMapToGroupID.size(); count++) {
            String addressAsKey = familyMapToGroupID.get(count);
            Set<String> noDups = new HashSet<>();
            if(familyMap.get(addressAsKey).size() > 1)
            {
                for(int i = 0; i < familyMap.get(addressAsKey).size(); i++)
                {
                    String temp = String.join( ",", familyMap.get(addressAsKey).get(i));
                    noDups.add(temp);
                }

                familyMap.get(addressAsKey).clear();
                List<String> cleanList = new ArrayList<>(noDups);
                for(int i = 0; i < cleanList.size(); i++)
                {
                    String[] temp = cleanList.get(i).split(",");
                    familyMap.get(addressAsKey).add(temp);
                }
            }
        }
    }

    /*  Sorts the household members by lastname, then first. */
    private void sortArraysLastFirst()
    {
        Comparator<String[]> compLastName = (s1, s2) -> s1[1].compareTo(s2[1]);
        Comparator<String[]> compFirstName = (s1, s2) -> s1[0].compareTo(s2[0]);
        Comparator<String[]> compAge = (s1, s2) -> Integer.compare(Integer.parseInt(s2[2]), Integer.parseInt(s1[2]));
        Comparator<String[]> compByName = compLastName.thenComparing(compFirstName).thenComparing(compAge);

        for(int count = 0; count < familyMapToGroupID.size(); count++) {
            ArrayList<String[]> family = familyMap.get(familyMapToGroupID.get(count));

            if(family.size() > 1)
            {
                family.sort(compByName);
            }
        }
    }

    /* Verifying the input string's format for required items */
    private boolean checkStringFormatting(String[] member)
    {
        if(member == null || member.length != 6)
            return false;
        try {
            int i = Integer.parseInt(member[5]);
        }
        catch(Exception e) {
            return false;
        }

        return true;
    }

    /* Prints the households and individual members who are over the age of 18 */
    private ArrayList<String> familyMembersInAgeRange(int minAge, int maxAge)
    {
        ArrayList<String> retArrayList = new ArrayList<>();

        for(int count = 0; count < familyMapToGroupID.size(); count++)
        {
            ArrayList<String[]> family = familyMap.get(familyMapToGroupID.get(count));
            String address = "GroupID: " + count + "  Occupant(s): " + String.valueOf(family.size());
            boolean found = false;
            for(String[] member : family)
            {
                int age = Integer.parseInt(member[2]);
                if( age >= minAge && age <= maxAge)
                {
                    if(!found)
                    {
                       retArrayList.add(address);
                       found = true;
                    }
                    retArrayList.add("     " + member[0] + ", " + member[1] + ", " + familyMapToGroupID.get(count) + ", " + member[2]  );
                }
            }
        }
        return retArrayList;
    }

    public void writeAgeRangeToFile(int minAge, int maxAge)
    {
        ArrayList<String> res = familyMembersInAgeRange(minAge, maxAge);
        fileProcessor.writeFile(res);
    }
}
