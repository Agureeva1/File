import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    public static List<String[]> list = new ArrayList<>();

    // сохранение данных, внесенных покупателем
    public static void log(int productNum, int amount) {
        list.add(new String[]{Integer.toString(productNum), Integer.toString(amount)});
        System.out.println();
    }


    //  сохранение всего журнала действия в файл в формате csv
    public static void exportCSV(File file) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("log.csv", true))) {
            csvWriter.writeAll(list);
        }
    }
}