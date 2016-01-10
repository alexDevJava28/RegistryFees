import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Created by Пользователь on 10.01.2016.
 */
public class ExcelExporter {

    public void exportTable (JTable table, File file){

       try {

           TableModel model = table.getModel();
           FileWriter excel = new FileWriter(file);

           for (int i = 0; i < model.getColumnCount(); i++) {

               excel.write(model.getColumnName(i) + "\t");
           }

           excel.write("\n");

           for (int i = 1; i < model.getRowCount(); i++) {

               for (int j = 0; j < model.getColumnCount(); j++) {

                   excel.write(model.getValueAt(i, j).toString() + "\t");

               }

               excel.write("\n");

           }

           excel.close();

       }catch (IOException e){

           e.printStackTrace();
       }

    }
}
