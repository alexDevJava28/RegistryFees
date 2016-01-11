
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;

/**
 * Created by Пользователь on 10.01.2016.
 */
public class ExcelExporter {

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFRow row;
    XSSFCell cell;

    TableModel model;


    public void exportTable (JTable table, Date date) {

            try{

                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Registry");

                row = sheet.createRow(0);
                cell = row.createCell(1);
                cell.setCellValue(date);

                model = table.getModel();

                row = sheet.createRow(2);

                for (int i = 0; i <model.getColumnCount(); i++) {

                    cell = row.createCell(0);
                    cell.setCellValue(model.getColumnName(i));

                }

                for (int i = 1; i < model.getRowCount(); i++) {

                    row = sheet.createRow(i+2);

                        for (int j = 0; j < model.getColumnCount(); j++) {

                            cell = row.createCell(j);
                            cell.setCellValue(model.getValueAt(i, j).toString());

                        }

                }

            FileOutputStream out = new FileOutputStream(new File("123.xlsx"));
            workbook.write(out);
            out.close();

            }catch (IOException e){

                e.printStackTrace();

        }
    }
}
