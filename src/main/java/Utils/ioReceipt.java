package Utils;

import Store.Receipt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ioReceipt {
    public static void writeReceipt(String outputFile, Receipt bill){

        try(FileWriter fout = new FileWriter( new File( outputFile ), true)  ){

            fout.append( bill.toString() + System.lineSeparator() );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readReceipt(String inputFile ){
        List<String> billData_list = new ArrayList<>();

        try (  FileReader fin = new FileReader( inputFile )   ){

            BufferedReader bufferedReader = new BufferedReader(fin);
            String line;
            while( (line = bufferedReader.readLine()) != null ){
                billData_list.add(line);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return billData_list;
    }
}
