package Utils;

import Store.Receipt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOreceipt {
    public static void writeReceipt(String outputFile, Receipt bill){

        try(FileWriter fout = new FileWriter( new File( outputFile ), true)  ){

            fout.append( bill.toString() + System.lineSeparator() );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readReceipt(String inputFile ){
        StringBuilder receiptBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader( new FileReader( inputFile) )) {
            String line;
            while ((line = reader.readLine()) != null) {
                receiptBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiptBuilder.toString();
    }
}
