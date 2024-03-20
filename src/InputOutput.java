import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputOutput {
    private static Number number = new Number();
    public Map<String, String> fetchOpcodes(String file) {
        Map<String, String> opcodesMap = new HashMap<>();

        String filename = "./src/input/" + file;
        
        BufferedReader reader = null;

        try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
            String trimmedLine;

			while (line != null) {
				// System.out.println(line);
                trimmedLine = line.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
                String[] codes = trimmedLine.split(",");
                opcodesMap.put(codes[0], codes[1]);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}        

        return opcodesMap;
	}
	  
    public ArrayList<String> fetchInstructions(String file) {

        String filename = "./src/input/" + file;
        ArrayList<String> myInstructions = new ArrayList<String>();
        BufferedReader reader;
        String trimmedInput;

        try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();

			while (line != null) {
				// System.out.println(line);
				// read next line
                trimmedInput = line.trim().replaceAll("\\s+", " ").replaceAll(" *;.*", "");
                myInstructions.add(trimmedInput);
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return myInstructions;
	}	

    public void outputFiles(ArrayList<ArrayList<String>> assemblerData) {
        String path = "src/output/";
        String filePath1 = path + "load_file.txt";
        String filePath2 = path + "listing_file.txt";

        BufferedWriter writer1 = null;
        BufferedWriter writer2 = null;

        try {

            File file1 = new File(filePath1);
            File file2 = new File(filePath2);
       
            /* This logic will make sure that the file 
             * gets created if it is not present at the
             * specified location*/
            
            FileWriter fw1 = new FileWriter(file1);
            FileWriter fw2 = new FileWriter(file2);
            writer1 = new BufferedWriter(fw1);
            writer2 = new BufferedWriter(fw2);
            
            ArrayList<String> memoryLocation = assemblerData.get(0);
            ArrayList<String> octalInstructions = assemblerData.get(1);
            ArrayList<String> instructions = assemblerData.get(2);

            for (int i = 0; i < instructions.size(); i++) {
                if (memoryLocation.get(i) != "" && octalInstructions.get(i) != "" ) {
                    writer1.write(memoryLocation.get(i) + "\t\t\t" + octalInstructions.get(i) + "\n");
                } 
                writer2.write(memoryLocation.get(i) + "\t\t" + octalInstructions.get(i) + "\t\t" + instructions.get(i) + "\n");
            }

            System.out.println("File written Successfully");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(writer1 != null) {
                    writer1.close();
                }

                if(writer2 != null) {
                    writer2.close();
                }
 
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }


    public void outputAssemblerData(List<Integer> memoryAddress, List<Integer> generalRegister, List<Integer> indexRegister) {
        String path = "src/output/";
        String filePath = path + "assembler_data.txt";
        BufferedWriter writer = null;

        try {
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file);
            writer = new BufferedWriter(fw);

            writer.write("Memory Address: \n");

            for (int i = 0; i < memoryAddress.size(); i++) {
                writer.write(Integer.toString(i) + ": " + Integer.toString(memoryAddress.get(i)) + "\t");
            }
            writer.write("\n\n");
    
            writer.write("General Register: \n");
            for (int i = 0; i < generalRegister.size(); i++) {
                writer.write("R" + Integer.toString(i) + ": " + Integer.toString(generalRegister.get(i)) + "\t\t");
            }
            writer.write("\n\n");
    
            writer.write("Index Register: \n");
            for (int i = 0; i < indexRegister.size(); i++) {
                writer.write("X" + Integer.toString(i + 1) + ": " + Integer.toString(indexRegister.get(i)) + "\t\t");
            }
            writer.write("\n");


        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }

            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }

        return;
    }

    public void printAssemblerData(List<Integer> memoryAddress, List<Integer> generalRegister, List<Integer> indexRegister) {

        System.out.println("Memory Address: ");

        for (int i = 0; i < memoryAddress.size(); i++) {
            System.out.print(Integer.toString(i) + ": " + Integer.toString(memoryAddress.get(i)) + "\t");
        }
        System.out.println();

        System.out.println("General Register: ");
        for (int i = 0; i < generalRegister.size(); i++) {
            System.out.print("R" + Integer.toString(i) + ": " + Integer.toString(generalRegister.get(i)) + "\t");
        }
        System.out.println();

        System.out.println("Index Register:");
        for (int i = 0; i < indexRegister.size(); i++) {
            System.out.print("X" + Integer.toString(i + 1) + ": " + Integer.toString(indexRegister.get(i)) + "\t");
        }
        System.out.println();

        return;
    }

        // Method to input character to register from device
    public char inputCharacterToRegister(List<Integer> generalRegister, int r, int devid) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter a character from device " + devid + ": ");
        try {
            char inputChar = (char)reader.read();
            return inputChar;
        } catch (IOException e) {
            System.out.println("Error reading character from device " + devid + ": " + e.getMessage());
            return (char) -1;
        }
    }
}
