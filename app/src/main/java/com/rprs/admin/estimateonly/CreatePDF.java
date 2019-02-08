package com.rprs.admin.estimateonly;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreatePDF {

    Context context;
    ArrayList<TransModel> dataModels;
    String date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("ssmmHHddMMyy");
    Calendar cal;

    public CreatePDF(Context context ,ArrayList<TransModel> dataModels) {
        this.context = context;
        this.dataModels=dataModels;
        cal = Calendar.getInstance();
    }

    public void createfile(){
        String filename=sdf1.format(cal.getTime());
        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/Estimate/" + filename+".pdf";

        // Add Permission into Manifest.xml
        // <uses-permission
        // android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>


        // Create New Blank Document
        Document document = new Document(PageSize.A4);

        // Create Directory in External Storage
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Estimate");
        myDir.mkdirs();

        // Create Pdf Writer for Writting into New Created Document
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));

            // Open Document for Writting into document
            document.open();

            // User Define Method
            //addMetaData(document);
            addTitlePage(document);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Exception1 ",e.getMessage());
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Exception2 ",e.getMessage());
        }

        // Close Document after writting all content
        document.close();
        Toast.makeText(context, "PDF File is Created. Location : " + FILE,
                Toast.LENGTH_LONG).show();
    }


    public void addTitlePage(Document document) throws DocumentException {
        // Font Style for Document
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        // Start New Paragraph
        Paragraph prHead = new Paragraph();
        // Set Font in this Paragraph
        prHead.setFont(titleFont);
        // Add item into Paragraph
        prHead.add("Expense & Income Details\n");
        prHead.setAlignment(Element.ALIGN_CENTER);
        document.add(prHead);

        Paragraph prProfile = new Paragraph();
        prProfile.setFont(smallBold);
        prProfile.add(" Date :  "+sdf.format(cal.getTime()));
        prProfile.setSpacingBefore(10);
        prProfile.setIndentationRight(40);
        prProfile.setFont(normal);
        prProfile.setAlignment(Element.ALIGN_RIGHT);
        document.add(prProfile);

        PdfPTable table = new PdfPTable(3);
        table.setSpacingAfter(10);
        table.setSpacingBefore(10);
        //table.setWidthPercentage(85.0f);

        Paragraph prProfiles = new Paragraph(new Phrase(new Chunk("Category Name ", catFont)));
        prProfiles.setAlignment(Element.ALIGN_CENTER);
        prProfiles.setSpacingAfter(3.0f);
        Paragraph prProfiles2 = new Paragraph(new Phrase(new Chunk("Date", catFont)));
        prProfiles2.setAlignment(Element.ALIGN_CENTER);
        Paragraph prProfiles3 = new Paragraph(new Phrase(new Chunk("Amount", catFont)));
        prProfiles3.setAlignment(Element.ALIGN_CENTER);
        //creating cell
        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        //c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setPaddingBottom(10);
        c1.addElement(prProfiles);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        //c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.addElement(prProfiles2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        //c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.addElement(prProfiles3);
        table.addCell(c1);
        Double total=0.0;
        table.setHeaderRows(1);
        for (int i=0;i<dataModels.size();i++){
            TransModel transModel=dataModels.get(i);
            table.addCell(transModel.getCateg_name());
            table.addCell(transModel.getDate());
            PdfPCell cells=new PdfPCell(new Phrase(String.valueOf(transModel.getAmount())+".0 Rs/-"));
            cells.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cells.setPadding(5);
            table.addCell(cells);
            total=total+transModel.getAmount();
        }
        PdfPCell pdfPCell=new PdfPCell(new Phrase(new Chunk("Total",catFont)));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setPadding(5);
        pdfPCell.setColspan(2);
        table.addCell(pdfPCell);
        PdfPCell pdfPCell2=new PdfPCell(new Phrase(new Chunk(total+" Rs/- ",catFont)));
        pdfPCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell2.setPadding(5);
        table.addCell(pdfPCell2);
        // Add all above details into Document
        document.add(table);

        /*Paragraph prProfile = new Paragraph();
        prProfile.setFont(smallBold);
        prProfile.add("\n \n Profile : \n ");
        prProfile.setFont(normal);
        prProfile.setAlignment(Element.ALIGN_RIGHT);
        prProfile.add("\nI am Mr. XYZ. I am Android Application Developer at TAG.");
        prProfile.setFont(smallBold);
        document.add(prProfile);*/

        // Create new Page in PDF
        document.newPage();
    }


}
