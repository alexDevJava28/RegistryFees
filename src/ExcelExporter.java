
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;
import java.awt.*;
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
                sheet.setColumnWidth(1, 10000);
                sheet.setColumnWidth(2, 5000);
                sheet.setColumnWidth(3, 12000);
                sheet.setColumnWidth(4, 3500);

                sheet.getPrintSetup().setLandscape(true);

                row = sheet.createRow((short)0);
                cell = row.createCell(1);

                CellStyle cs = workbook.createCellStyle();

                DataFormat df = workbook.createDataFormat();
                cs.setDataFormat(df.getFormat("dd.mm.yyyy"));

                cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

                XSSFFont font = workbook.createFont();
                font.setFontHeightInPoints((short) 14);
                font.setFontName("Times New Roman");
                font.setBold(true);
                cs.setFont(font);

                cell.setCellValue(date);
                cell.setCellStyle(cs);

                model = table.getModel();

                row = sheet.createRow((short)2);

                for (int i = 0; i <model.getColumnCount(); i++) {

                    cell = row.createCell(i);
                    cell.setCellValue(model.getColumnName(i));

                    cs = workbook.createCellStyle();
                    cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                    cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
                    cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
                    cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                    cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);
                    cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

                    XSSFFont fontHead = workbook.createFont();
                    fontHead.setFontHeightInPoints((short) 12);
                    fontHead.setFontName("Times New Roman");
                    fontHead.setBold(true);
                    cs.setFont(fontHead);

                    cell.setCellStyle(cs);

                }

                int rowCount = 3;

                for (int i = 0; i <model.getRowCount(); i++) {

                    row = sheet.createRow(rowCount);

                        for (int j = 0; j < model.getColumnCount(); j++) {

                            cell = row.createCell(j);
                            cell.setCellValue(model.getValueAt(i, j).toString());

                            if (j == 1 || j == 3){

                                cell.setCellStyle(styleNamePurpose());

                            }else {

                                cell.setCellStyle(styleIdSumStatus());
                            }

                        }

                    rowCount++;

                }

                FileSystemView fs = FileSystemView.getFileSystemView();
                FileOutputStream out = new FileOutputStream(fs.getHomeDirectory() + "\\Registry.xlsx");
                workbook.write(out);
                out.close();

                Desktop.getDesktop().open(new File("Registry.xlsx"));

            }catch (IOException e){

                e.printStackTrace();

        }
    }

    public CellStyle styleIdSumStatus (){

        CellStyle cs = workbook.createCellStyle();

        cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        XSSFFont fontIdSumSt = workbook.createFont();
        fontIdSumSt.setFontHeightInPoints((short) 11);
        fontIdSumSt.setFontName("Times New Roman");
        cs.setFont(fontIdSumSt);

        return cs;

    }

    public CellStyle styleNamePurpose (){

        CellStyle cs = workbook.createCellStyle();

        cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cs.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);

        XSSFFont fontNamePur = workbook.createFont();
        fontNamePur.setFontHeightInPoints((short) 11);
        fontNamePur.setFontName("Times New Roman");
        cs.setFont(fontNamePur);

        return cs;

    }
}
