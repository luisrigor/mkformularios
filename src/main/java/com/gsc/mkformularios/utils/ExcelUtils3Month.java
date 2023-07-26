package com.gsc.mkformularios.utils;

import lombok.extern.log4j.Log4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.gsc.mkformularios.service.impl.pvm.PVM3MonthReport.ptMonths;
import static com.gsc.mkformularios.service.impl.pvm.PVMReportStyles.*;
import static com.gsc.mkformularios.service.impl.pvm.PVMReportStyles.getStyleVariation;


@Log4j
public class ExcelUtils3Month {

    private static HSSFCellStyle styleTitleLevel1 = null;
    private static HSSFCellStyle styleTitleLevel2 = null;
    private static HSSFCellStyle styleDetailsLeft = null;
    private static HSSFCellStyle styleDetailsCenterText = null;
    private static HSSFCellStyle styleDetailsCenterNumber = null;
    private static HSSFCellStyle borderStyleLeft = null;
    private static HSSFCellStyle borderStyleCenterNumber = null;
    private static HSSFCellStyle styleVariation = null;
    private static HSSFCellStyle borderStyleCenterYellowFilled = null;
    private static HSSFCellStyle borderStyleCenterBlueFilled = null;
    private static HSSFCellStyle borderStyleCenterGreenFilled = null;
    private static HSSFCellStyle borderStyleCenterYellowDotted = null;
    private static HSSFCellStyle borderStyleCenterBlueDotted = null;
    private static HSSFCellStyle borderStyleCenterGreenDotted = null;

    public static final String[] EXCEL_COLUMNS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ", "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ", "EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX", "EY", "EZ", "FA", "FB", "FC", "FD", "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX", "FY", "FZ", "GA", "GB", "GC", "GD", "GE", "GF", "GG", "GH", "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP", "GQ", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ"};
    public static final String EXCEL_SUM_STRING_SEPARATOR = ",";
    public static final String COD_CONCESS_COL = "Cod Concessionário";
    public static final String CONCESS_COL = "Concessionário";

    public static void initStyles(HSSFWorkbook workBook) {
        styleTitleLevel1			 = getTitleStyleLevel1(workBook);
        styleTitleLevel2			 = getTitleStyleLevel2(workBook);
        styleDetailsLeft 			 = getDetailsStyleLeft(workBook);
        styleDetailsCenterText 		 = getDetailsStyleCenterText(workBook);
        styleDetailsCenterNumber	 = getDetailsStyleCenterNumber(workBook);
        borderStyleLeft 			 = getBorderStyleLeft(workBook);
        borderStyleCenterNumber 	 = getBorderStyleCenterNumber(workBook);

        borderStyleCenterYellowFilled = getBorderStyleCenterYellowFilled(workBook);
        borderStyleCenterGreenFilled  = getBorderStyleCenterGreenFilled(workBook);
        borderStyleCenterBlueFilled = getBorderStyleCenterBlueFilled(workBook);

        borderStyleCenterYellowDotted = getBorderStyleCenterYellowDotted(workBook);
        borderStyleCenterGreenDotted  = getBorderStyleCenterGreenDotted(workBook);
        borderStyleCenterBlueDotted = getBorderStyleCenterBlueDotted(workBook);

        styleVariation				  = getStyleVariation(workBook);
    }

    public static HSSFFont getArial(HSSFWorkbook wb, int fontSize) {
        HSSFFont fontTitle = wb.createFont();
        fontTitle.setFontName("Arial");
        fontTitle.setFontHeightInPoints((short) fontSize);
        return fontTitle;
    }

    public static HSSFCell createCell(HSSFSheet sheet, int rowNumber, short column, String value, int columnWidth) {
        HSSFRow row = sheet.getRow(rowNumber);
        if(row==null)
            row = sheet.createRow(rowNumber);
        HSSFCell cell = row.createCell(column);

        cell.setCellValue(value);
        if (columnWidth>0)
            sheet.setColumnWidth(column, (short) (columnWidth * 256));
        return cell;
    }

    public static HSSFCell createCell(HSSFSheet sheet, int rowNumber, short column, Integer value, int columnWidth) {
        HSSFRow row = sheet.getRow(rowNumber);
        if(row==null)
            row = sheet.createRow(rowNumber);
        HSSFCell cell = row.createCell(column);

        if(value!=null)
            cell.setCellValue(value);
        if (columnWidth>0)
            sheet.setColumnWidth(column, (short) (columnWidth * 256));
        return cell;
    }

    public static void createSheetTitle(HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, String title ) {
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 20));
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , 0, (short)1, title, (short)-1);
        cell.setCellStyle(styleTitleLevel1);
        cell.setCellStyle(styleTitleLevel1);
    }

    private static String GetColumnName(String colName, String month) {
        String result = colName;
        if (colName.toUpperCase().startsWith("M1"))
            result = ptMonths[Integer.parseInt(month)].substring(0,3);
        else if (colName.toUpperCase().startsWith("D1"))
            result = "D1";
        else if (colName.toUpperCase().startsWith("M2"))
            result = ptMonths[Integer.parseInt(month)+1].substring(0,3);
        else if (colName.toUpperCase().startsWith("D2"))
            result = "D2";
        else if (colName.toUpperCase().startsWith("M3"))
            result = ptMonths[Integer.parseInt(month)+2].substring(0,3);
        else if (colName.toUpperCase().startsWith("TT-"))
            result = "Total";
        else if (colName.toUpperCase().startsWith("TT-ORC-"))
            result = "Orc.";

        return result;
    }

    public static void createSheetPlatesSubTitle(int currentRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet,
                                                 List<String> metaData, String month, String modelType) throws SQLException {

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow+1, (short)1, modelType, (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow+1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        currentSheet.addMergedRegion(new CellRangeAddress(currentRow+1,  currentRow+2, 1, 2));

        int startRow = currentRow;
        int currentColumn = 2;
        int columnCount = metaData.size();

        int colToPrint = 0;
        for (int i = currentColumn; i < columnCount-7; i++) {
            Object header = metaData.get(i + 1);
            String colName = header.toString();

            colToPrint = currentColumn + 1;

            cell = ExcelUtils3Month.createCell(currentSheet , startRow+1, (short) colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, colName.substring(3), (short)3);
            cell.setCellStyle(styleTitleLevel2);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, GetColumnName(colName, month), (short)-1);
            cell.setCellStyle(styleTitleLevel2);
            if (colName.toUpperCase().startsWith("M3") ) {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow+1,  startRow+1, colToPrint-4, colToPrint));
                } catch (Exception e) {
                    log.error("Error merging cells", e);
                }
            }
            currentColumn++;
        }
        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Orç.", (short)-1);
        cell.setCellStyle(borderStyleCenterBlueFilled);
        currentSheet.addMergedRegion(new CellRangeAddress(startRow+1,  startRow+1, colToPrint-1, colToPrint));
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+1].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+1].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Orç.", (short)-1);
        currentSheet.addMergedRegion(new CellRangeAddress(startRow+1,  startRow+1, colToPrint-1, colToPrint));
        cell.setCellStyle(borderStyleCenterBlueFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+2].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+2].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Orç.", (short)-1);
        currentSheet.addMergedRegion(new CellRangeAddress(startRow+1,  startRow+1, colToPrint-1, colToPrint));
        cell.setCellStyle(borderStyleCenterBlueFilled);
        colToPrint++;
    }

    public static List createPlatesLine(HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, Map<String,Object> rs, int currentRow,
                                        int totalColumns) throws SQLException {
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)3);
        cell.setCellStyle(borderStyleLeft);
        cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)30);
        cell.setCellStyle(borderStyleLeft);
        List<HSSFCell> cellsToColapse = new ArrayList<HSSFCell>();

        List<String> platesMetadata = new ArrayList<>();
        if(!rs.isEmpty())
            platesMetadata =  new ArrayList<>(rs.keySet());

        int leftColumn = 3;
        for (int i = leftColumn; i < totalColumns; i++) {

            Integer curValue = fromObjectToInt(rs.get(platesMetadata.get(i)));

            if(curValue==null||curValue==0) {
                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)i, "", (short)-1);
            } else {
                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)i, curValue, (short)-1);
            }

            if (i==totalColumns-6 ||i==totalColumns-4 ||i==totalColumns-2) {
                String curRow = String.valueOf(currentRow+1);

                int endCol = i-5;
                int startCol = leftColumn-1;
                StringBuilder formula = new StringBuilder("SUM(");
                while (endCol>startCol) {
                    formula.append(ExcelUtils3Month.EXCEL_COLUMNS[endCol] + curRow + EXCEL_SUM_STRING_SEPARATOR);
                    endCol-=5;
                }
                formula.deleteCharAt(formula.length()-1);
                formula.append(")");

                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)i, "", (short)-1);
                cell.setCellFormula(formula.toString());

                cell.setCellStyle(borderStyleCenterYellowFilled);

            } else if (i==totalColumns-5 ||i==totalColumns-3 ||i==totalColumns-1){
                cell.setCellStyle(borderStyleCenterBlueFilled);
                if (curValue==null||curValue==0)
                    cell.setCellValue(0);
                cellsToColapse.add(cell);
            }
            else
                cell.setCellStyle(borderStyleCenterNumber);
        }

        return cellsToColapse;
    }

    public static void createPlatesTotalLine(int topRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, int currentRow,
                                              int totalColumns, boolean withFrotas, boolean isCaOnly, boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        if (withFrotas)
            cellValue = "SUB-TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 3; i < totalColumns; i++) {
            cell = row1.createCell((short)i);

            String formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + topRow + ":" + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + (currentRow-1) + ":" + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + (currentRow-3) + "," + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";


            cell.setCellFormula(formula);
            cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createSheetTotalPlatesSalesSubTitle(String headerType, int currentRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, String month) throws SQLException {

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)1, "TOTAL", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 1, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);


        currentSheet.addMergedRegion(new CellRangeAddress(currentRow,  currentRow+2, 1, 2));

        int startRow = currentRow;
        int currentColumn = 2;

        int colToPrint = currentColumn;


        for (int i = 0; i < 3; i++) {

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint, ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            if(i < 2)
            {
                colToPrint++;
                cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)3);
                cell.setCellStyle(borderStyleCenterYellowFilled);

                cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
                cell.setCellStyle(borderStyleCenterYellowFilled);
                if(i == 0) {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D1", (short)-1);
                } else {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D2", (short)-1);
                }
                cell.setCellStyle(styleTitleLevel2);
            }

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);
            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, headerType.equalsIgnoreCase("PLATES")?"Orç":"Mat", (short)-1);
            cell.setCellStyle(borderStyleCenterBlueFilled);

            //Merge M�s
            if(i < 2) {
                currentSheet.addMergedRegion(new CellRangeAddress(startRow+1,  currentRow+1, colToPrint-2, colToPrint));
            } else {
                currentSheet.addMergedRegion(new CellRangeAddress(startRow+1,  currentRow+1, colToPrint-1, colToPrint));
            }
        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)3);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "TOTAL", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)3);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, headerType.equalsIgnoreCase("PLATES")?"Orç":"Mat", (short)-1);
        cell.setCellStyle(borderStyleCenterBlueFilled);

        //Merge TRIM
        currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-1), (short)colToPrint));

        //Merge Total Comerciais
        currentSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, (short)(colToPrint-9), (short)colToPrint));


        for (int i = 0; i < 3; i++) {

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint, ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
            cell.setCellStyle(borderStyleCenterYellowFilled);



            if(i < 2)
            {
                colToPrint++;
                cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)3);
                cell.setCellStyle(borderStyleCenterYellowFilled);

                cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
                cell.setCellStyle(borderStyleCenterYellowFilled);
                if(i == 0) {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D1", (short)-1);
                } else {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D2", (short)-1);
                }
                cell.setCellStyle(styleTitleLevel2);
            }

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);
            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, headerType.equalsIgnoreCase("PLATES")?"Orç":"Mat", (short)-1);
            cell.setCellStyle(borderStyleCenterBlueFilled);

            //Merge M�s
            if(i < 2) {
                currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-2), (short)colToPrint));
            } else {
                currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-1), (short)colToPrint));
            }
        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)3);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "TOTAL", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)3);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, headerType.equalsIgnoreCase("PLATES")?"Orç":"Mat", (short)-1);
        cell.setCellStyle(borderStyleCenterBlueFilled);

        //Merge TRIM
        currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-1), (short)colToPrint));

        //Merge Total PASSAGEIROS
        currentSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, (short)(colToPrint-9), (short)colToPrint));

        for (int i = 0; i < 3; i++) {

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint,headerType.equalsIgnoreCase("PLATES")?" TOTAL MATRICULAS":" TOTAL VENDAS" , (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint, ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
            cell.setCellStyle(borderStyleCenterYellowFilled);


            if(i < 2)
            {
                colToPrint++;
                cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, headerType.equalsIgnoreCase("PLATES")?" TOTAL MATRICULAS":" TOTAL VENDAS" , (short)3);
                cell.setCellStyle(borderStyleCenterYellowFilled);

                cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
                cell.setCellStyle(borderStyleCenterYellowFilled);
                if(i == 0) {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D1", (short)-1);
                } else {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D2", (short)-1);
                }
                cell.setCellStyle(styleTitleLevel2);
            }


            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, headerType.equalsIgnoreCase("PLATES")?" TOTAL MATRICULAS":" TOTAL VENDAS" , (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)3);
            cell.setCellStyle(borderStyleCenterYellowFilled);
            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, headerType.equalsIgnoreCase("PLATES")?"Orç":"Mat", (short)-1);
            cell.setCellStyle(borderStyleCenterBlueFilled);

            //Merge M�s
            if(i < 2) {
                currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-2), (short)colToPrint));
            } else {
                currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-1), (short)colToPrint));
            }
        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, headerType.equalsIgnoreCase("PLATES")?" TOTAL MATRICULAS":" TOTAL VENDAS" , (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)3);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "TOTAL", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, headerType.equalsIgnoreCase("PLATES")?" TOTAL MATRICULAS":" TOTAL VENDAS" , (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)3);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, headerType.equalsIgnoreCase("PLATES")?"Orç":"Mat", (short)-1);
        cell.setCellStyle(borderStyleCenterBlueFilled);

        //Merge TRIM
        currentSheet.addMergedRegion(new CellRangeAddress(startRow+1, currentRow+1, (short)(colToPrint-1), (short)colToPrint));

        //Merge Total PASSAGEIROS
        currentSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, (short)(colToPrint-9), (short)colToPrint));

    }

    public static void createTotalPlatesSalesLine(HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, Map<String,Object> rs, Map<String,Object> rs2, int currentRow, int totalColumns) throws SQLException {

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)3);
        cell.setCellStyle(borderStyleLeft);
        cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)30);
        cell.setCellStyle(borderStyleLeft);

        List<String> platesMetadata = new ArrayList<>();
        if(!rs.isEmpty())
            platesMetadata =  new ArrayList<>(rs.keySet());

        int leftColumn = 3;
        for (int i = leftColumn; i < totalColumns; i++) {
            Integer curValue = fromObjectToInt(rs.get(platesMetadata.get(i)));
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)i, curValue, (short)-1);

            Integer curValue2 = fromObjectToInt(rs.get(platesMetadata.get(i)));
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(i+10), curValue2, (short)-1);


            String formula = ExcelUtils3Month.EXCEL_COLUMNS[3] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[6] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[9] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(11), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[5] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[8] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[10] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(12), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[13] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[16] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[19] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(21), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[15] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[18] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[20] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(22), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[3] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[13] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(23), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[4] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[14] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(24), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[5] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[15] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(25), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[6] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[16] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(26), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[7] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[17] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(27), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[8] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[18] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(28), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[9] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[19] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(29), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[10] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[20] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(30), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[11] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[21] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(31), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[12] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[22] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(32), "", (short)-1);
            cell.setCellFormula(formula);
        }
    }

    public static void createPlatesSalesTotalLine(int topRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet,
                                                   int currentRow, int totalColumns, boolean withFrotas, boolean isCaOnly,
                                                   boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";
        if (withFrotas)
            cellValue = "SUB-TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 3; i <33 ; i++) {
            cell = row1.createCell((short)i);

            String formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + topRow + ":" + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + (currentRow-1) + ":" + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + (currentRow-3) + "," + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";

            cell.setCellFormula(formula);
            cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createSheetSalesSubTitle(int currentRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, List<String> metaData, String month, String modelType) throws SQLException {

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow+1, (short)1, modelType, (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow+1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        currentSheet.addMergedRegion(new CellRangeAddress(currentRow + 1, currentRow + 2, 1, 2));

        int startRow = currentRow;
        int currentColumn = 2;
        int columnCount = metaData.size();

        int colToPrint = 0;
        for (int i = currentColumn; i < columnCount-1; i++) {
            Object header = metaData.get(i + 1);
            String colName = header.toString();

            colToPrint = currentColumn + 1;

            cell = ExcelUtils3Month.createCell(currentSheet , startRow+1, (short) colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, colName.substring(3), (short)3);
            cell.setCellStyle(styleTitleLevel2);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, GetColumnName(colName, month), (short)-1);
            cell.setCellStyle(styleTitleLevel2);
            if (colName.toUpperCase().startsWith("M3") ) {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short) (colToPrint - 4), (short) colToPrint));
                }catch (Exception e) {
                    log.error("Error merging cells", e);
                }
            }
            currentColumn++;
        }
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Mat", (short)-1);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Dif", (short)-1);
        cell.setCellStyle(borderStyleCenterBlueFilled);
        currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short) (colToPrint - 2), (short) colToPrint));
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+1].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+1].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Mat", (short)-1);
        currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short) (colToPrint - 1), (short) colToPrint));
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+1].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Dif", (short)-1);
        try {
            currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short)(colToPrint - 2), (short)colToPrint));
        }catch (Exception e) {
            log.error("Error merging cells", e);
        }
        cell.setCellStyle(borderStyleCenterBlueFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+2].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+2].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Mat", (short)-1);
        try {
            currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short)(colToPrint - 1), (short)colToPrint));
        }catch (Exception e) {
            log.error("Error merging cells", e);
        }
        colToPrint++;

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+2].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Dif", (short)-1);
        try {
            currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short) (colToPrint - 2), (short) colToPrint));
        }catch (Exception e) {
            log.error("Error merging cells", e);
        }
        cell.setCellStyle(borderStyleCenterBlueFilled);
        colToPrint++;
    }

    public static List createSalesLine(HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, Map<String,Object> rs, int currentRow, int totalColumns) throws SQLException {

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)3);
        cell.setCellStyle(borderStyleLeft);
        cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)30);
        cell.setCellStyle(borderStyleLeft);
        List<HSSFCell> currCells = new ArrayList<HSSFCell>();

        StringBuffer formulaTotal = new StringBuffer();
        formulaTotal.append(  "0" );

        List<String> platesMetadata = new ArrayList<>();
        if(!rs.isEmpty())
            platesMetadata =  new ArrayList<>(rs.keySet());

        int leftColumn = 3;
        for (int i = leftColumn; i <= totalColumns-1; i++) {
            Integer curValue = null;
            try {
                curValue = fromObjectToInt(rs.get(platesMetadata.get(i)));
            }catch (Exception e) {
                curValue = null;
            }
            int colToPrint = i;
            if (curValue==null||curValue==0) {
                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
            } else {
                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)colToPrint, curValue, (short)-1);
            }
        }
        currCells.add(createNextsColumn(currentSheet, totalColumns-5, currentRow, totalColumns,    0));
        currCells.add(createNextsColumn(currentSheet, totalColumns-3, currentRow, totalColumns+3, -1));
        currCells.add(createNextsColumn(currentSheet, totalColumns-1, currentRow, totalColumns+6, -2));

        return currCells;
    }

    public static void createSalesTotalLine(int topRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, int currentRow, int totalColumns, boolean withFrotas, boolean isCaOnly, boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";
        if (withFrotas)
            cellValue = "SUB-TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        totalColumns += 9; // 3 x 3
        for (int i = 4; i <= totalColumns ; i++) {
            int colToPrint = i - 1;
            cell = row1.createCell((short)colToPrint);

            String formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + topRow + ":" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + (currentRow-1) + ":" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + (currentRow-3) + "," + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            cell.setCellFormula(formula);

            if (colToPrint == totalColumns - 1 || colToPrint == totalColumns - 4 || colToPrint == totalColumns - 7)
                cell.setCellStyle(borderStyleCenterBlueFilled);
            else
                cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createSheetVDVCSubTitle(int currentRow, HSSFSheet currentSheet,	List<String> metaData, String month, String modelType) throws SQLException {
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow+1, (short)1, modelType, (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow+1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        currentSheet.addMergedRegion(new CellRangeAddress(currentRow + 1, currentRow + 2, (short) 1, (short) 2));

        int startRow = currentRow;
        int currentColumn = 2;
        int columnCount = metaData.size();

        int colToPrint = 0;
        for (int i = currentColumn; i < columnCount-1; i++) {
            Object header = metaData.get(i + 1);
            String colName = header.toString();

            colToPrint = currentColumn + 1;

            cell = ExcelUtils3Month.createCell(currentSheet , startRow+1, (short) colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, colName.substring(3), (short)3);
            cell.setCellStyle(styleTitleLevel2);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, GetColumnName(colName, month), (short)-1);
            cell.setCellStyle(styleTitleLevel2);
            if (colName.toUpperCase().startsWith("M3") ) {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, startRow + 1, (short)(colToPrint - 4), (short)colToPrint));
                } catch (Exception e) {
                    log.error("Error merging cells", e);
                }
            }

            currentColumn++;
        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+1].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short)colToPrint, ptMonths[Integer.parseInt(month)+2].substring(0,3), (short)3);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
    }

    public static void createVDVCLine(HSSFSheet currentSheet, Map<String,Object> rs, int currentRow, int totalColumns) throws SQLException {
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)3);
        cell.setCellStyle(borderStyleLeft);
        cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)30);
        cell.setCellStyle(borderStyleLeft);
        List<HSSFCell> currCells = new ArrayList<HSSFCell>();

        StringBuffer formulaTotal = new StringBuffer();
        formulaTotal.append(  "0" );

        List<String> platesMetadata = new ArrayList<>();
        if(!rs.isEmpty())
            platesMetadata =  new ArrayList<>(rs.keySet());


        int leftColumn = 3;
        for (int i = leftColumn; i <= totalColumns-1; i++) {

            Integer curValue = fromObjectToInt(rs.get(platesMetadata.get(i)));
            int colToPrint = i;
            if (curValue==null||curValue==0) {
                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
            } else {
                cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)colToPrint, curValue, (short)-1);
            }
        }
        currCells.add(createNextsColumnVDVC(currentSheet, totalColumns-5, currentRow, totalColumns));
        currCells.add(createNextsColumnVDVC(currentSheet, totalColumns-3, currentRow, totalColumns+1));
        currCells.add(createNextsColumnVDVC(currentSheet, totalColumns-1, currentRow, totalColumns+2));
    }

    public static void createVDVCTotalLine(int topRow, HSSFSheet currentSheet, int currentRow, int totalColumns) {
        String cellValue = "TOTAL";


        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        totalColumns += 3;
        for (int i = 4; i <= totalColumns ; i++) {
            int colToPrint = i - 1;
            cell = row1.createCell((short)colToPrint);

            String formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + topRow + ":" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            cell.setCellFormula(formula);

            if (colToPrint == totalColumns - 1 || colToPrint == totalColumns - 2 || colToPrint == totalColumns - 3)
                cell.setCellStyle(borderStyleCenterBlueFilled);
            else
                cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createSheetTotalVDVCSubTitle(int currentRow, HSSFSheet currentSheet, String month) {
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)1, "TOTAL", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 1, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow + 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);

        currentSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow + 2, (short) 1, (short) 2));

        int startRow = currentRow;
        int currentColumn = 2;

        int colToPrint = currentColumn;


        for (int i = 0; i < 3; i++) {

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)2);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint, ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)2);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            if(i < 2)
            {
                colToPrint++;
                cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)2);
                cell.setCellStyle(borderStyleCenterYellowFilled);

                cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)2);
                cell.setCellStyle(borderStyleCenterYellowFilled);
                if(i == 0) {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D1", (short)-1);
                } else {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D2", (short)-1);
                }
                cell.setCellStyle(styleTitleLevel2);
            }

            //Merge M�s
            if(i < 2) {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow + 1, (short)(colToPrint - 1), (short)colToPrint));
                }catch (Exception e){
                    log.error("Error Merging cells", e);
                }
            } else {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow + 1, (short) colToPrint, (short) colToPrint));
                }catch (Exception e){
                    log.error("Error Merging cells", e);
                }
            }


        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total PASSAGEIROS", (short)1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)1);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "TOTAL", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);


        //Merge Total Passageiros
        try {
            currentSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, (short)(colToPrint - 5), (short)colToPrint));
        }catch (Exception e){
            log.error("Error Merging cells", e);
        }


        for (int i = 0; i < 3; i++) {

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)2);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint, ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)2);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            if(i < 2)
            {
                colToPrint++;
                cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)2);
                cell.setCellStyle(borderStyleCenterYellowFilled);

                cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)2);
                cell.setCellStyle(borderStyleCenterYellowFilled);
                if(i == 0) {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D1", (short)-1);
                } else {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D2", (short)-1);
                }
                cell.setCellStyle(styleTitleLevel2);
            }


            //Merge M�s
            if(i < 2) {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow + 1, (short) (colToPrint - 1), (short) colToPrint));
                }catch (Exception e){
                    log.error("Error Merging cells", e);
                }
            } else {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow + 1, (short) (colToPrint), (short) colToPrint));
                }catch (Exception e){
                    log.error("Error Merging cells", e);
                }
            }
        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "Total COMERCIAIS", (short)1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)1);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "TOTAL", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);


        //Merge Total Comerciais
        try {
            currentSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, (short)(colToPrint - 5), (short)colToPrint));
        }catch (Exception e){
            log.error("Error Merging cells", e);
        }


        for (int i = 0; i < 3; i++) {

            colToPrint++;
            cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "TOTAL VDVC" , (short)2);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint, ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)2);
            cell.setCellStyle(borderStyleCenterYellowFilled);

            cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "Total", (short)-1);
            cell.setCellStyle(borderStyleCenterYellowFilled);


            if(i < 2)
            {
                colToPrint++;
                cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "TOTAL VDVC" , (short)2);
                cell.setCellStyle(borderStyleCenterYellowFilled);

                cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  ptMonths[Integer.parseInt(month)+i].substring(0,3), (short)2);
                cell.setCellStyle(borderStyleCenterYellowFilled);
                if(i == 0) {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D1", (short)-1);
                } else {
                    cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "D2", (short)-1);
                }
                cell.setCellStyle(styleTitleLevel2);
            }

            //Merge M�s

            if(i < 2) {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow + 1, (short)(colToPrint - 1), (short)colToPrint));
                }catch (Exception e){
                    log.error("Error Merging cells", e);
                }

            } else {
                try {
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow + 1, (short)colToPrint, (short)colToPrint));
                }catch (Exception e){
                    log.error("Error Merging cells", e);
                }
            }
        }

        colToPrint++;
        cell = ExcelUtils3Month.createCell(currentSheet, startRow, (short) colToPrint, "TOTAL VDVC" , (short)1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+1, (short) colToPrint,  "TRIM.", (short)1);
        cell.setCellStyle(styleTitleLevel2);

        cell = ExcelUtils3Month.createCell(currentSheet, startRow+2, (short)colToPrint, "TOTAL", (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);

        //Merge Total TotalVDVC
        try {
            currentSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, (short)(colToPrint - 5), (short)colToPrint));
        }catch (Exception e){
            log.error("Error Merging cells", e);
        }



    }

    public static void createTotalVDVCLine(HSSFSheet currentSheet, Map<String,Object> rs, Map<String,Object> rs2, int currentRow, int totalColumns) throws SQLException {
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)3);
        cell.setCellStyle(borderStyleLeft);
        cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)30);
        cell.setCellStyle(borderStyleLeft);

        List<String> platesMetadata = new ArrayList<>();
        if(!rs.isEmpty())
            platesMetadata =  new ArrayList<>(rs.keySet());

        List<String> platesMetadata2 = new ArrayList<>();
        if(!rs2.isEmpty())
            platesMetadata2 =  new ArrayList<>(rs2.keySet());

        int leftColumn = 3;
        for (int i = leftColumn; i < totalColumns; i++) {
            Integer curValue = fromObjectToInt(rs.get(platesMetadata.get(i)));
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)i, curValue, (short)-1);

            Integer curValue2 = (Integer) rs2.get(platesMetadata2.get(i));
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(i+6), curValue2, (short)-1);

            String formula = ExcelUtils3Month.EXCEL_COLUMNS[3] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[5] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[7] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(8), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[9] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[11] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[13] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(14), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[3] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[9] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(15), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[4] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[10] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(16), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[5] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[11] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(17), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[6] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[12] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(18), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[7] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[13] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(19), "", (short)-1);
            cell.setCellFormula(formula);

            formula = ExcelUtils3Month.EXCEL_COLUMNS[8] + (currentRow+1) + "+" + ExcelUtils3Month.EXCEL_COLUMNS[14] + (currentRow+1);
            cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(20), "", (short)-1);
            cell.setCellFormula(formula);

        }

    }

    public static void createTotalTotalVDVCLine(int topRow, HSSFSheet currentSheet, int currentRow) {
        String cellValue = "TOTAL";


        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 3; i < 21 ; i++) {
            cell = row1.createCell((short)i);

            String formula = "SUM(" + ExcelUtils3Month.EXCEL_COLUMNS[i] + topRow + ":" + ExcelUtils3Month.EXCEL_COLUMNS[i] + currentRow + ")";

            cell.setCellFormula(formula);
            if(i<20){
                cell.setCellStyle(borderStyleCenterYellowFilled);
            }else{
                cell.setCellStyle(borderStyleCenterBlueFilled);
            }
        }

    }

    private static HSSFCell createNextsColumn(HSSFSheet currentSheet, int endCol, int currentRow, int colToPrint, int matColumn) {

        int startCol = 2;
        StringBuffer formula = new StringBuffer("SUM(");
        String auxRow = String.valueOf(currentRow+1);
        while (endCol>startCol) {
            formula.append(ExcelUtils3Month.EXCEL_COLUMNS[endCol] + auxRow + EXCEL_SUM_STRING_SEPARATOR);
            endCol-=5;
        }
        formula.deleteCharAt(formula.length()-1);
        formula.append(")");

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(colToPrint), "", (short)-1);
        cell.setCellFormula(formula.toString());
        cell.setCellStyle(borderStyleCenterYellowFilled);

        formula = new StringBuffer("'Tot. Matríc.'!" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint + matColumn] + (currentRow+1));
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(colToPrint+1), "", (short)-1);
        cell.setCellFormula(formula.toString());

        formula = new StringBuffer(ExcelUtils3Month.EXCEL_COLUMNS[colToPrint +1] + (currentRow+1) + "-" + ExcelUtils3Month.EXCEL_COLUMNS[colToPrint ] + (currentRow+1));
        cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(colToPrint+2), "", (short)-1);
        cell.setCellFormula(formula.toString());
        cell.setCellStyle(borderStyleCenterBlueFilled);
        return cell;

    }

    private static HSSFCell createNextsColumnVDVC(HSSFSheet currentSheet, int endCol, int currentRow, int colToPrint) {
        int startCol = 2;
        StringBuffer formula = new StringBuffer("SUM(");
        String auxRow = String.valueOf(currentRow+1);
        while (endCol>startCol) {
            formula.append(ExcelUtils3Month.EXCEL_COLUMNS[endCol] + auxRow + EXCEL_SUM_STRING_SEPARATOR);
            endCol-=5;
        }
        formula.deleteCharAt(formula.length()-1);
        formula.append(")");

        HSSFCell cell = ExcelUtils3Month.createCell(currentSheet , currentRow, (short)(colToPrint), "", (short)-1);
        cell.setCellFormula(formula.toString());
        cell.setCellStyle(borderStyleCenterYellowFilled);

        return cell;
    }


    private static Integer fromObjectToInt(Object value){
        if (value != null) {
            if (value instanceof Integer) {
                return  (Integer) value;
            } else {
                try {
                    return Integer.parseInt(value.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }


}
