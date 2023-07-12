package com.gsc.tvcmanager.utils;

import com.rg.dealer.Dealer;
import com.sc.commons.utils.DateTimerTasks;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class ReportUtils {

    private final static String SHEET_YEAR_NAME 					= "Acumulado Anual";



    public static HSSFCell createCell(HSSFRow row, int column, String cellValue, HSSFCellStyle cellStyle) {
        HSSFCell cell = null;
        cell = row.createCell(column);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    public static HSSFCell createCellNumeric(HSSFRow row, int column, double cellValue, HSSFCellStyle cellStyle) {
        HSSFCell cell = null;
        cell = row.createCell(column);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    public static HSSFCell createCellFormula(HSSFRow row, int column, String formulaType, HSSFCellStyle cellStyle) {
        HSSFCell cell = null;
        int rowNum = row.getRowNum()+1;
        String strFormula = "D"+rowNum+"/C"+rowNum;
        if("SN".equals(formulaType))
            strFormula = "E"+rowNum+"/D"+rowNum;
        cell = row.createCell(column);
        cell.setCellFormula(strFormula);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    public static HSSFCell createCellTtFormula(HSSFRow row, int column, String formulaType, HSSFCellStyle cellStyle) {
        HSSFCell cell = null;
        int rowNum = row.getRowNum();
        String strFormula = "";
        if("OBJETIVO".equals(formulaType))
            strFormula = "SUM(C4:C"+rowNum+")";
        if("TVC".equals(formulaType))
            strFormula = "SUM(D4:D"+rowNum+")";
        if("SN".equals(formulaType))
            strFormula = "SUM(E4:E"+rowNum+")";
        cell = row.createCell(column);
        cell.setCellFormula(strFormula);
        cell.setCellStyle(cellStyle);

        return cell;
    }

    public static int createTitleLines(HSSFWorkbook workBook, HSSFSheet sheet, int month, int year, int currentRow, String oidNet) {

        HSSFRow row = null;
        HSSFCellStyle styleTitle = getTitleStyle(workBook, "TEXT", oidNet);

        int column = 1;
        sheet.setDefaultRowHeight((short) (250));
        if(sheet.getSheetName().equals(SHEET_YEAR_NAME)){

        }else{

        }
        //Linha titulo 1
        row = sheet.createRow(currentRow);
        int columnTo = column+1;
        sheet.addMergedRegion(new CellRangeAddress( currentRow, currentRow, column, columnTo));
        createCell(row, column, "REDE TUC", styleTitle);
        column=columnTo+1;
        columnTo = column+1;
        sheet.addMergedRegion(new CellRangeAddress( currentRow, currentRow, column, columnTo));
        if(sheet.getSheetName().equals(SHEET_YEAR_NAME)){
            createCell(row, column, "Previsão " + year, styleTitle);
        }else{
            createCell(row, column, "Previsão " + DateTimerTasks.ptMonths[month-1] + " " + year, styleTitle);
        }
        column=columnTo+1;
        columnTo = column+1;
        sheet.addMergedRegion(new CellRangeAddress( currentRow, currentRow, column, columnTo));
        createCell(row, column, "Cumprimento %", styleTitle);

        //Linha titulo 2
        currentRow++;
        column = 1;
        row = sheet.createRow(currentRow);
        createCell(row, column, "CONCESSÕES", styleTitle);
        sheet.setColumnWidth(column, (short) (30 * 256));
        createCell(row, ++column, "OBJECTIVO TUC", styleTitle);
        sheet.setColumnWidth(column, (short) (15 * 256));
        createCell(row, ++column, "TUC", styleTitle);
        sheet.setColumnWidth(column, (short) (15 * 256));
        createCell(row, ++column, "COMPRAS TCAP", styleTitle);
        sheet.setColumnWidth(column, (short) (15 * 256));
        createCell(row, ++column, "TUC/OBJETIVO", styleTitle);
        sheet.setColumnWidth(column, (short) (15 * 256));
        createCell(row, ++column, "COMPRAS TCAP/TUC", styleTitle);
        sheet.setColumnWidth(column, (short) (15 * 256));

        return currentRow;
    }


    private static HSSFFont getFont(HSSFWorkbook wb, String fontType, int fontSize, int color) {
        HSSFFont fontTitle = wb.createFont();
        fontTitle.setFontName(fontType);
        fontTitle.setColor((short)color);
        fontTitle.setFontHeightInPoints((short) fontSize);
        return fontTitle;
    }

    public static HSSFCellStyle getTitleStyle(HSSFWorkbook workBook, String valueType, String oidNet){

        HSSFCellStyle styleTitle = workBook.createCellStyle();
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitle.setWrapText(true);
        styleTitle.setBorderBottom(BorderStyle.THIN);
        styleTitle.setBorderLeft(BorderStyle.THIN);
        styleTitle.setBorderTop(BorderStyle.THIN);
        styleTitle.setBorderRight(BorderStyle.THIN);
        styleTitle.setFont(getFont(workBook, "Arial",10,1));
        styleTitle.setFillForegroundColor(Dealer.OID_NET_LEXUS.equals(oidNet)? IndexedColors.GREY_50_PERCENT.getIndex() : IndexedColors.DARK_RED.getIndex());
        styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if("PERCENT".equals(valueType))
            styleTitle.setDataFormat(workBook.createDataFormat().getFormat("0.00%"));

        return styleTitle;
    }

    public static HSSFCellStyle getDetailStyle(HSSFWorkbook workBook, HorizontalAlignment align, String valueType){
        HSSFCellStyle styleDetailsText = workBook.createCellStyle();
        styleDetailsText.setAlignment(align);
        HSSFFont detailsfont = getFont(workBook, "Arial", 10, 0);
        styleDetailsText.setFont(detailsfont);
        styleDetailsText.setBorderBottom(BorderStyle.THIN);
        styleDetailsText.setBorderLeft(BorderStyle.THIN);
        styleDetailsText.setBorderTop(BorderStyle.THIN);
        styleDetailsText.setBorderRight(BorderStyle.THIN);
        if("NUMBER".equals(valueType))
            styleDetailsText.setDataFormat(workBook.createDataFormat().getFormat("0"));
        if("PERCENT".equals(valueType))
            styleDetailsText.setDataFormat(workBook.createDataFormat().getFormat("0.00%"));

        return styleDetailsText;
    }
}
