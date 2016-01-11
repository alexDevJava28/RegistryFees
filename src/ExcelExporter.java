
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.*;

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
                sheet.setColumnWidth(0, 1500);
                sheet.setColumnWidth(1, 9000);
                sheet.setColumnWidth(2, 5000);
                sheet.setColumnWidth(3, 10000);
                sheet.setColumnWidth(4, 2000);

                row = sheet.createRow((short)0);
                cell = row.createCell(1);
                CellStyle cs = workbook.createCellStyle();
                DataFormat df = workbook.createDataFormat();
                cs.setDataFormat(df.getFormat("dd.mm.yyyy"));
                cell.setCellValue(date);
                cell.setCellStyle(cs);

                model = table.getModel();

                row = sheet.createRow((short)2);

                for (int i = 0; i <model.getColumnCount(); i++) {

                    cell = row.createCell(i);
                    cell.setCellValue(model.getColumnName(i));

                }

                int rowCount = 3;

                for (int i = 0; i <model.getRowCount(); i++) {

                    row = sheet.createRow(rowCount);

                        for (int j = 0; j < model.getColumnCount(); j++) {

                            cell = row.createCell(j);
                            cell.setCellValue(model.getValueAt(i, j).toString());

                        }

                    rowCount++;

                }

            FileOutputStream out = new FileOutputStream(new File("D:\\123.xlsx"));
            workbook.write(out);
            out.close();

            }catch (IOException e){

                e.printStackTrace();

        }
    }
}
