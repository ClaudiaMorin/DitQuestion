
package ExpeditorsDITQuestion;

import java.util.*;

/*
In answering this question I attempted to keep the code flexible for more than the original intent of simply processing
a given data file once, what if there are multiple files or I am asked to extend this to users who are adding more households or people??
To this end I kept three dictionaries because I needed to maintain a set of both the members of a unique family
and the uniqueness of each address.  Testing for the uniqueness of an address was fairly simple as I just checked the dictionary keys as they
have to be unique by definition.  Uniqueness of people within an address was more of a trade-off between creating an "Easy-to-sort-print" arraylist of String[]
or using a Set<String> that could be used to insure uniqueness within an address.  I opted to keep both.  More data storage but less repeated processing...
 */
public class ExpeditorsQuestion {
    //Keeps member data as strings so that the test for uniqueness can happen easily
    private final Dictionary<String, Set<String>> familySets;
    //Designed to allow simple data storage for sorting and printing
    public final Dictionary<String, ArrayList<String[]>> familyMaptoData;
    public final Dictionary<Integer, String> GroupIDMappingFamilyAddress;
    private DataInputOutFile fileProcessor;
    ExpeditorsQuestion()
    {
        familySets = new Hashtable<>();
        GroupIDMappingFamilyAddress = new Hashtable<>();
        familyMaptoData = new Hashtable<>();
    }

    //using dictionary.
    public void processProfileFile(String textFileIn, String textFileOut)
    {
        fileProcessor = new DataInputOutFile(textFileIn, textFileOut);
        ArrayList<String> entry = fileProcessor.readFile();
        for (String stringLine: entry)
        {
            String[] entryCleaned = cleanEntryString(stringLine);
            if(entryCleaned != null)
            {
                String address = entryCleaned[0];
                String newMember = entryCleaned[1];
                //if the family map doesn't exist we need to create it.
                if(familySets.get(address) == null ) {
                    GroupIDMappingFamilyAddress.put(GroupIDMappingFamilyAddress.size(), address);
                    Set<String> newSet = new HashSet<>();
                    familySets.put(address, newSet);
                }
                //now add new member!  If this isn't a unique member the Set will fail to add!
                familySets.get(address).add(newMember);
            }
        }
        createFamilyMapData();
        sortArraysLastFirst();
    }

    //All the string manipulation and removal of white spaces.  The array that is returned contains the address in
    //String[0] and the Member in String[1] for ease of extraction
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
        String[] entryCleaned = {address, firstName + " " + lastName + " " + profile[5].trim()};
        return entryCleaned;
    }

    //Designed to convert the maps of unique members into datasets that are more easily sorted, printed, etc
    private void createFamilyMapData()
    {

        for(int familyID = 0; familyID <  GroupIDMappingFamilyAddress.size(); familyID++)
        {
            String address = GroupIDMappingFamilyAddress.get(familyID);
            ArrayList<String[]> buildFamily = new ArrayList<>();
            Set<String> curFamily = familySets.get(address);
            for(String member : curFamily)
            {
                String[] curr = member.split(" ");
                buildFamily.add(curr);
            }
            familyMaptoData.put(address, buildFamily);
        }
    }

    /*  Sorts the household members by lastname, then first. */
    private void sortArraysLastFirst()
    {
        Comparator<String[]> compLastName = (s1, s2) -> s1[1].compareTo(s2[1]);
        Comparator<String[]> compFirstName = (s1, s2) -> s1[0].compareTo(s2[0]);
        Comparator<String[]> compAge = (s1, s2) -> Integer.compare(Integer.parseInt(s2[2]), Integer.parseInt(s1[2]));
        Comparator<String[]> compByName = compLastName.thenComparing(compFirstName).thenComparing(compAge);

        for(int count = 0; count < GroupIDMappingFamilyAddress.size(); count++) {
            ArrayList<String[]> family = familyMaptoData.get(GroupIDMappingFamilyAddress.get(count));

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

        for(int count = 0; count < GroupIDMappingFamilyAddress.size(); count++)
        {
            ArrayList<String[]> family = familyMaptoData.get(GroupIDMappingFamilyAddress.get(count));
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
                    retArrayList.add("     " + member[0] + ", " + member[1] + ", " + GroupIDMappingFamilyAddress.get(count) + ", " + member[2]  );
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
