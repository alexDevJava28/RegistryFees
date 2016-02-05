package print;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.util.Date;

/**
 * Created by khodackovskiy on 28.01.2016.
 */
public class JtableInPDF {

    private JTable table;
    private Date date;
    private double total;
    private PDDocument doc;

    private final int colWidth1 = 30;
    private final int colWidth2 = 250;
    private final int colWidth3 = 100;
    private final int colWidth4 = 350;
    private final int colWidth5 = 72;
    private final int rowHeight = 20;
    private final int endPage = 70;




    public JtableInPDF(JTable table, Date date, double total) {

        this.table = table;
        this.date = date;
        this.total = total;
    }

    public PDDocument createPDF() {

            try(PDDocument insideDoc = new PDDocument()){

                if (table.getModel().getColumnCount() == 5) {

                    createNewPage(insideDoc);

                }else {

                    createNewPageLoad(insideDoc);

                }

                doc = insideDoc;
                doc.save("print.pdf");

            }catch (IOException ioe){

                ioe.printStackTrace();

            }catch (COSVisitorException cve){

                cve.printStackTrace();
            }

        return doc;
    }

    public void createNewPage(PDDocument doc){

        TableModel model = table.getModel();
        int rows = model.getRowCount()+1;
        int cols = model.getColumnCount();

        int x = 20;
        int y = 550;

            //Create new page
            PDFont font = PDType1Font.HELVETICA;
            int fontSize = 12;

            PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
            page.setRotation(90);
            doc.addPage(page);

            PDRectangle pageSize = page.findMediaBox();
            float pageWidth = pageSize.getWidth();

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                // add the rotation using the current transformation matrix
                // including a translation of pageWidth to use the lower left corner as 0,0 reference
                cs.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);

                cs.setFont(font, fontSize);
                cs.beginText();
                cs.moveTextPositionByAmount(x, y);
                cs.drawString("Registy of payments");
                cs.endText();
                cs.beginText();
                cs.moveTextPositionByAmount(x, y - 20);
                cs.drawString(new Date().toString());
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA_BOLD, 24);
                cs.beginText();
                cs.moveTextPositionByAmount(x + 330, y - 70);
                cs.drawString(date.toString());
                cs.endText();

            } catch (IOException ioe) {

                ioe.printStackTrace();
            }

            try (PDPageContentStream cs = new PDPageContentStream(doc, page, true, true)) {


                int nexty = y - 100;

                drawTable(x, nexty, rows, cs, page, doc);

                font = PDType1Font.HELVETICA_BOLD;
                fontSize = 12;
                cs.setFont(font, fontSize);

                float textx = x;
                float texty = nexty - rowHeight + (rowHeight - fontSize) / 2;

                for (int i = 0; i < cols; i++) {

                    float width = font.getStringWidth(model.getColumnName(i)) / 1000 * fontSize;

                    switch (i) {

                        case 0:
                            textx += (colWidth1 - width) / 2;
                            break;
                        case 1:
                            textx += (colWidth2 - width) / 2;
                            break;
                        case 2:
                            textx += (colWidth3 - width) / 2;
                            break;
                        case 3:
                            textx += (colWidth4 - width) / 2;
                            break;
                        case 4:
                            textx += (colWidth5 - width) / 2;
                            break;
                    }

                    cs.beginText();
                    cs.moveTextPositionByAmount(textx, texty);
                    cs.drawString(model.getColumnName(i));
                    cs.endText();

                    switch (i) {

                        case 0:
                            textx += width + (colWidth1 - width) / 2;
                            break;
                        case 1:
                            textx += width + (colWidth2 - width) / 2;
                            break;
                        case 2:
                            textx += width + (colWidth3 - width) / 2;
                            break;
                        case 3:
                            textx += width + (colWidth4 - width) / 2;
                            break;
                        case 4:
                            textx += width + (colWidth5 - width) / 2;
                            break;
                    }

                }

                fillTable(x, nexty, rows, cols, model, 0, cs, doc);

            } catch (IOException ioe) {

                ioe.printStackTrace();
            }
    }

    public void createNewPageLoad(PDDocument doc){

        TableModel model = table.getModel();
        date = null;
        int rows = model.getRowCount()+1;
        int cols = model.getColumnCount();

        int x = 20;
        int y = 550;

        for (int k = 0; k < rows-1; k++) {

            if (!model.getValueAt(k, 1).equals(date)) {

                date = (Date) model.getValueAt(k, 1);

                //Create new page
                PDFont font = PDType1Font.HELVETICA;
                int fontSize = 12;

                PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
                page.setRotation(90);
                doc.addPage(page);

                PDRectangle pageSize = page.findMediaBox();
                float pageWidth = pageSize.getWidth();

                try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                    // add the rotation using the current transformation matrix
                    // including a translation of pageWidth to use the lower left corner as 0,0 reference
                    cs.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);

                    cs.setFont(font, fontSize);
                    cs.beginText();
                    cs.moveTextPositionByAmount(x, y);
                    cs.drawString("Registy of payments");
                    cs.endText();
                    cs.beginText();
                    cs.moveTextPositionByAmount(x, y - 20);
                    cs.drawString(new Date().toString());
                    cs.endText();

                    cs.setFont(PDType1Font.HELVETICA_BOLD, 24);
                    cs.beginText();
                    cs.moveTextPositionByAmount(x + 330, y - 70);
                    cs.drawString(date.toString());
                    cs.endText();

                } catch (IOException ioe) {

                    ioe.printStackTrace();
                }

                try (PDPageContentStream cs = new PDPageContentStream(doc, page, true, true)) {


                    int nexty = y - 100;

                    drawTable(x, nexty, rows, cs, page, doc);

                    font = PDType1Font.HELVETICA_BOLD;
                    fontSize = 12;
                    cs.setFont(font, fontSize);

                    float textx = x;
                    float texty = nexty - rowHeight + (rowHeight - fontSize) / 2;

                    for (int i = 0; i < cols; i++) {

                        float width = font.getStringWidth(model.getColumnName(i)) / 1000 * fontSize;

                        switch (i) {

                            case 0:
                                textx += (colWidth1 - width) / 2;
                                break;
                            case 1:
                                textx += (colWidth2 - width) / 2;
                                break;
                            case 2:
                                textx += (colWidth3 - width) / 2;
                                break;
                            case 3:
                                textx += (colWidth4 - width) / 2;
                                break;
                            case 4:
                                textx += (colWidth5 - width) / 2;
                                break;
                        }

                        cs.beginText();
                        cs.moveTextPositionByAmount(textx, texty);
                        cs.drawString(model.getColumnName(i));
                        cs.endText();

                        switch (i) {

                            case 0:
                                textx += width + (colWidth1 - width) / 2;
                                break;
                            case 1:
                                textx += width + (colWidth2 - width) / 2;
                                break;
                            case 2:
                                textx += width + (colWidth3 - width) / 2;
                                break;
                            case 3:
                                textx += width + (colWidth4 - width) / 2;
                                break;
                            case 4:
                                textx += width + (colWidth5 - width) / 2;
                                break;
                        }

                    }

                    fillTable(x, nexty, rows, cols, model, 0, cs, doc);

                } catch (IOException ioe) {

                    ioe.printStackTrace();
                }
            }
        }
    }

    private void drawTable (int x, int nexty, int rows, PDPageContentStream cs, PDPage page, PDDocument doc) throws IOException{

            int nextyStart = nexty;
            int rowCount = rows;

            cs.setLineWidth(0.8f);

            //draw the rows
            float tableWidth = page.findMediaBox().getHeight() - x - x;

            for (int i = 0; i <= rows; i++) {

                cs.drawLine(x, nexty, x + tableWidth, nexty);
                nexty -= rowHeight;
                --rowCount;

                if (rowCount == 0){

                    cs.drawLine(x, nexty-20, x + tableWidth, nexty-20);

                }

                if (nexty <= endPage) {

                    int start = 550;

                    PDPage nowa = new PDPage(PDPage.PAGE_SIZE_A4);
                    nowa.setRotation(90);
                    doc.addPage(nowa);

                    PDRectangle pageSize = nowa.findMediaBox();
                    float pageWidth = pageSize.getWidth();

                    try (PDPageContentStream newCS = new PDPageContentStream(doc, nowa, true, true)) {

                        newCS.concatenate2CTM(0, 1, -1, 0, pageWidth, 0);
                        drawTable(x, start, rowCount, newCS, nowa, doc);

                    }

                    nexty += rowHeight;
                    break;

                }
            }

            //draw the columns
            int nextx = x;

            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth1;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth2;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth3;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth4;
            cs.drawLine(nextx, nextyStart, nextx, nexty);
            nextx += colWidth5;
            cs.drawLine(nextx, nextyStart, nextx, nexty);

    }

    private void fillTable (int x, int nexty, int rows, int cols, TableModel model, int rowCount, PDPageContentStream cs, PDDocument doc) throws IOException{

        PDFont font = PDType1Font.HELVETICA;
        int fontSize = 12;
        cs.setFont(font, fontSize);
        float textx = x;
        float texty = nexty - (rowHeight*2) + (rowHeight-fontSize)/2;
        String text;
        int countPage = 1;

        for (int i = rowCount; i < rows-1; i++) {

            for (int j = 0; j < cols; j++) {

                text = model.getValueAt(i, j).toString();
                float width = font.getStringWidth(text)/1000*fontSize;

                switch (j){

                    case 0:
                        textx += (colWidth1 - width)/2;
                        break;
                    case 1:
                        textx += 2;
                        break;
                    case 2:
                        textx += (colWidth3 - width)/2;
                        break;
                    case 3:
                        textx += 2;
                        break;
                    case 4:

                        if (text.equals("false")){

                            text = "not paid";

                        }else{

                            text = "paid";
                        }

                        width = font.getStringWidth(text)/1000*fontSize;
                        textx += (colWidth5 - width)/2;
                        break;
                }

                cs.beginText();
                cs.moveTextPositionByAmount(textx, texty);
                cs.drawString(text);
                cs.endText();

                switch (j){

                    case 0:
                        textx += width + (colWidth1 - width)/2;
                        break;
                    case 1:
                        textx += colWidth2;
                        break;
                    case 2:
                        textx += width + (colWidth3 - width)/2;
                        break;
                    case 3:
                        textx += colWidth4;
                        break;
                    case 4:
                        textx += width + (colWidth5 - width)/2;
                        break;
                }

            }

            textx = x;
            texty -= rowHeight;
            ++rowCount;

            if (texty <= endPage+10){

                int start = 570;

                try(PDPageContentStream newCS = new PDPageContentStream(doc, (PDPage) doc.getDocumentCatalog().getAllPages().get(countPage), true, true)){

                    fillTable(x, start, rows, cols, model, rowCount, newCS, doc);
                }

                break;

            }

        }

        if (rowCount == rows-1) {

            font = PDType1Font.HELVETICA_BOLD;
            fontSize = 14;
            cs.setFont(font, fontSize);
            float widthTotal = font.getStringWidth(String.valueOf(total)) / 1000 * fontSize;
            float textxTotal = textx + colWidth1 + colWidth2 + (colWidth3 - widthTotal) / 2;
            cs.beginText();
            cs.moveTextPositionByAmount(textx + colWidth1, texty);
            cs.drawString("Total:");
            cs.endText();
            cs.beginText();
            cs.moveTextPositionByAmount(textxTotal, texty);
            cs.drawString(String.valueOf(total));
            cs.endText();

        }

    }
}
