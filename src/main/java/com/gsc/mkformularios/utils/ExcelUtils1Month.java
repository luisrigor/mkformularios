package com.gsc.mkformularios.utils;

import com.gsc.mkformularios.dto.PVMCarmodelForecast;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import java.sql.SQLException;
import java.util.*;

import static com.gsc.mkformularios.service.impl.pvm.PVMReportStyles.*;
import static com.gsc.mkformularios.service.impl.pvm.PVMReportStyles.getStyleVariation;

public class ExcelUtils1Month {

    public static final String[] EXCEL_COLUMNS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ", "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ", "EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX", "EY", "EZ", "FA", "FB", "FC", "FD", "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX", "FY", "FZ", "GA", "GB", "GC", "GD", "GE", "GF", "GG", "GH", "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP", "GQ", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ"};

    public static HSSFCellStyle styleTitleLevel1 = null;
    public static HSSFCellStyle styleTitleLevel2 = null;
    public static HSSFCellStyle styleDetailsLeft = null;
    public static HSSFCellStyle styleDetailsCenterText = null;
    public static HSSFCellStyle styleDetailsCenterNumber = null;
    public static HSSFCellStyle borderStyleLeft = null;
    public static HSSFCellStyle borderStyleCenterNumber = null;
    public static HSSFCellStyle styleVariation = null;
    public static HSSFCellStyle borderStyleCenterYellowFilled = null;
    public static HSSFCellStyle borderStyleCenterBlueFilled = null;
    public static HSSFCellStyle borderStyleCenterGreenFilled = null;
    public static HSSFCellStyle borderStyleCenterYellowDotted = null;
    public static HSSFCellStyle borderStyleCenterBlueDotted = null;
    public static HSSFCellStyle borderStyleCenterGreenDotted = null;
    public static HSSFCellStyle styleForecast = null;
    public static HSSFCellStyle styleOrderTotals = null;



    public static final String COD_CONCESS_COL = "Cód Concessionário";
    public static final String CONCESS_COL = "Concessionário";
    //TODO validar actualizacion sub_total_columns y budget columns
    public static Hashtable<Integer, Integer> SUB_TOTAL_COLUMNS = null;
    public static Hashtable<Integer, Integer> BUDGET_COLUMNS = null;


    public static void initStyles(HSSFWorkbook workBook) {
        SUB_TOTAL_COLUMNS  = new Hashtable<Integer, Integer>();

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

        HSSFCellStyle styleGen 		 = getForecastStyle(workBook);
        styleGen.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        styleGen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleForecast				 = styleGen;
        styleOrderTotals			 = getForecastStyle(workBook);
        BUDGET_COLUMNS = new Hashtable<Integer, Integer>();


    }

    private static HSSFCellStyle getForecastStyle(HSSFWorkbook wb) {
        HSSFCellStyle styleForecast = wb.createCellStyle();
        styleForecast.setAlignment(HorizontalAlignment.CENTER);
        HSSFFont fontTitle = getDetailsFont(wb);
        styleForecast.setFont(fontTitle);
        styleForecast.setVerticalAlignment(VerticalAlignment.CENTER);
        styleForecast.setBorderBottom(BorderStyle.THIN);
        styleForecast.setBorderLeft(BorderStyle.THIN);
        styleForecast.setBorderTop(BorderStyle.THIN);
        styleForecast.setBorderRight(BorderStyle.THIN);
        return styleForecast;
    }

    public static HSSFCell createCell(HSSFSheet sheet, int rowNumber, int column, String value, int columnWidth) {
        HSSFRow row = sheet.getRow(rowNumber);
        if(row==null)
            row = sheet.createRow(rowNumber);

        HSSFCell cell = row.createCell(column);
        if(value!=null&&!value.equals("null"))
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
        String valueD = value==null?"": String.valueOf(value);
        cell.setCellValue(valueD);
        if (columnWidth>0)
            sheet.setColumnWidth(column, (short) (columnWidth * 256));
        return cell;
    }

    public static void createSalesTotalLine(HSSFSheet currentSheet, int currentRow, int totalColumns, boolean withFrotas, boolean isCaOnly, boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";
        if (withFrotas)
            cellValue = "SUB-TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 4; i <= totalColumns; i++) {
            int colToPrint = i - 1;
            cell = row1.createCell((short)colToPrint);

            String formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + "4:" + EXCEL_COLUMNS[colToPrint] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + (currentRow-3) + ":" + EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + (currentRow-5) + "," +EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            cell.setCellFormula(formula);

            if (colToPrint == totalColumns-3)
                cell.setCellStyle(borderStyleCenterGreenFilled);
            else if (colToPrint == totalColumns-2){
                cell.setCellStyle(borderStyleCenterBlueFilled);
            }
            else if (colToPrint == totalColumns-1)
                cell.setCellStyle(styleDetailsCenterNumber);
            else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(i)))
                cell.setCellStyle(borderStyleCenterYellowFilled);
            else
                cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createPlatesTotalLine(HSSFSheet currentSheet, int currentRow, int totalColumns, boolean withFrotas, boolean isCaOnly, boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";
        if (withFrotas)
            cellValue = "SUB-TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 3; i < totalColumns-1; i++) {
            cell = row1.createCell((short)i);

            String formula = "SUM(" + EXCEL_COLUMNS[i] + "4:" + EXCEL_COLUMNS[i] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + EXCEL_COLUMNS[i] + (currentRow-3) + ":" +EXCEL_COLUMNS[i] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + EXCEL_COLUMNS[i] + (currentRow-5) + "," +EXCEL_COLUMNS[i] + currentRow + ")";

            cell.setCellFormula(formula);
            if (BUDGET_COLUMNS.containsKey(new Integer(i)))
                cell.setCellStyle(borderStyleCenterBlueFilled);
            else
                cell.setCellStyle(borderStyleCenterYellowFilled);
        }


        cell = createCell(currentSheet , currentRow, (short)(totalColumns-1), String.valueOf(totalColumns), (short)-1);

        //Varia��o
        String formula = "((" + EXCEL_COLUMNS[totalColumns-3] + (currentRow+1) + "-" + EXCEL_COLUMNS[totalColumns-2] + (currentRow+1) +")/" + EXCEL_COLUMNS[totalColumns-2] + (currentRow+1) + ")*100";

        cell.setCellFormula(formula);
        cell.setCellStyle(styleVariation);

    }
    public static void createPlatesSalesTotalLine(int topRow, HSSFWorkbook currentWorkBook, HSSFSheet currentSheet, int currentRow, int totalColumns, boolean withFrotas, boolean isCaOnly, boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";
        if (withFrotas)
            cellValue = "SUB-TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 3; i <33 ; i++) {
            cell = row1.createCell((short)i);

            String formula = "SUM(" + EXCEL_COLUMNS[i] + topRow + ":" + EXCEL_COLUMNS[i] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + EXCEL_COLUMNS[i] + (currentRow-1) + ":" +EXCEL_COLUMNS[i] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + EXCEL_COLUMNS[i] + (currentRow-3) + "," +EXCEL_COLUMNS[i] + currentRow + ")";

            cell.setCellFormula(formula);
            cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createSalesLine(HSSFSheet currentSheet, Map<String, Object> rs, int currentRow, int totalColumns) throws SQLException {

        HSSFCell cell = createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);
        cell = createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);

        StringBuffer formulaTotal = new StringBuffer();
        formulaTotal.append(  "0" );

        Integer compInt = 0;

        int leftColumn = 3;

        List<String> metadaKeys = new ArrayList<>();
        if(!rs.isEmpty())
            metadaKeys =  new ArrayList<>(rs.keySet());

        for (int i = 4; i < totalColumns; i++) {

            Integer curValue = fromObjectToInt(rs.get(metadaKeys.get(i)));
            int colToPrint = i - 1;

            if(colToPrint+1 == totalColumns) {
                // DIF
                String formula = EXCEL_COLUMNS[leftColumn] + (currentRow+1) + "-" + EXCEL_COLUMNS[leftColumn+1] + (currentRow+1);
                cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                cell.setCellFormula(formula);
            } else if(colToPrint+1 == totalColumns -1) {
                //Total Matri
                String formula = "'Tot. Matríc.'!" + EXCEL_COLUMNS[colToPrint+1] + (currentRow+1) ;
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formula, (short)-1);
                cell.setCellFormula(formula);
            } else if(colToPrint+1 == totalColumns -2) {
                //Total Vendas
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formulaTotal.toString(), (short)-1);
                cell.setCellFormula(formulaTotal.toString());
            } else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(colToPrint))) {
                String formula = "SUM(" + EXCEL_COLUMNS[leftColumn] + (currentRow+1) + ":" + EXCEL_COLUMNS[i-2] + (currentRow+1) + ")";
                cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                formulaTotal.append(  "+" + formula );
                cell.setCellFormula(formula);
                leftColumn = i;
            } else {
                if (compInt.equals(curValue)) {
                    cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                } else {
                    cell = createCell(currentSheet , currentRow, (short)colToPrint, String.valueOf(curValue), (short)-1);
                }
            }


            if (colToPrint == totalColumns-3)
                cell.setCellStyle(borderStyleCenterGreenDotted);
            else if (colToPrint == totalColumns-2)
                cell.setCellStyle(borderStyleCenterBlueDotted);
            else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(colToPrint))) {
                cell.setCellStyle(borderStyleCenterYellowDotted);
            }
            else
                cell.setCellStyle(borderStyleCenterNumber);
        }

    }


    public static List createPlatesLine(HSSFSheet currentSheet, Map<String, Object> rs, int currentRow, int totalColumns) throws SQLException {
        return createPlatesLine(currentSheet,rs,currentRow,totalColumns, true);
    }

    public static List createPlatesLine(HSSFSheet currentSheet, Map<String, Object> rs, int currentRow, int totalColumns, boolean isToAddDifColumn) throws SQLException {

        HSSFCell cell = createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);

        cell = createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);

        List<HSSFCell> cellsToColapse = new ArrayList<HSSFCell>();

        StringBuffer formulaTotal = new StringBuffer();
        formulaTotal.append( "0" );
        Integer compInt = 0;
        int leftColumn = 3;

        List<String> metadataKeys = new ArrayList<>();
        if(!rs.isEmpty())
            metadataKeys =  new ArrayList<>(rs.keySet());
        for (int i = 3; i < totalColumns-1; i++) {
            Integer curValue = fromObjectToInt(rs.get(metadataKeys.get(i)));
            int colToPrint = i - 1;

            if(colToPrint == totalColumns-4) {
                // Total Geral
                cell = createCell(currentSheet , currentRow, (short)i, formulaTotal.toString(), (short)-1);
                cell.setCellFormula(formulaTotal.toString());
            } else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(i))) {
                String formula = "SUM(" + EXCEL_COLUMNS[leftColumn] + (currentRow+1) + ":" + EXCEL_COLUMNS[i-1] + (currentRow+1) + ")";
                cell = createCell(currentSheet , currentRow, (short)i, "", (short)-1);
                formulaTotal.append(  "+" + formula );
                cell.setCellFormula(formula);
                leftColumn = i+2;
            } else if(compInt.equals(curValue)){
                cell = createCell(currentSheet , currentRow, (short)i, "", (short)-1);
            }
            else {
                cell = createCell(currentSheet , currentRow, (short)i, curValue, (short)-1);
            }

            if (SUB_TOTAL_COLUMNS.containsKey(new Integer(i))) {
                cell.setCellStyle(borderStyleCenterYellowDotted);
            } else if (BUDGET_COLUMNS.containsKey(new Integer(i))){
                cell.setCellStyle(borderStyleCenterBlueDotted);

                if ("0".equals(curValue))
                    cell.setCellValue(0);

                cellsToColapse.add(cell);
            }else
                cell.setCellStyle(borderStyleCenterNumber);
        }


        cell = createCell(currentSheet , currentRow, (short)(totalColumns-1), "", (short)-1);

        if(isToAddDifColumn){
            //Varia��o
            String formula = "((" + EXCEL_COLUMNS[totalColumns-3] + (currentRow+1) + "-" + EXCEL_COLUMNS[totalColumns-2] + (currentRow+1) +")/" + EXCEL_COLUMNS[totalColumns-2] + (currentRow+1) + ")*100";
            cell.setCellFormula(formula);
            cell.setCellStyle(styleVariation);
        }
        //TODO validar si se usa
//        cell = createCell(currentSheet , currentRow, (short)totalColumns, rs.getString(totalColumns), (short)-1);

        return cellsToColapse;
    }

    public static void createSheetSalesSubTitle(HSSFSheet currentSheet, List<String> metaData) throws SQLException {

        HSSFCell cell = createCell(currentSheet , 1, (short)1, "CONCESSÕES", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        currentSheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 2));

        int startRow = 1;
        int currentColumn = 3;
        int auxColumn = currentColumn-1;
        int columnCount = metaData.size();
        String strTotal = "total";
        for (int i = currentColumn-1; i < columnCount-1; i++) {
            Object header = metaData.get(i + 1);
            String colName = header.toString();

            int colToPrint = currentColumn;
            cell = createCell(currentSheet , startRow, (short) colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);
            if (colName.toLowerCase().indexOf(strTotal)!=-1) {
                //TOTAL VENDAS
                String secondTitle = "";
                if ( colName.indexOf(" ")!=colName.lastIndexOf(" ") ) {
                    secondTitle = colName.substring(colName.lastIndexOf(" "));
                    colName = "Sub-Total";
                }

                //Unir C�lulas
                if(colToPrint!=(auxColumn+1))
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow, startRow, auxColumn + 1, colToPrint));
                cell = createCell(currentSheet, startRow, (short)(auxColumn+1), secondTitle, (short)-1);
                cell.setCellStyle(styleTitleLevel2);

                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);

                if (colToPrint == columnCount-3)
                    cell.setCellStyle(borderStyleCenterGreenFilled);
                else if (colToPrint == columnCount-2)
                    cell.setCellStyle(borderStyleCenterBlueFilled);
                else
                    cell.setCellStyle(borderStyleCenterYellowFilled);

                SUB_TOTAL_COLUMNS.put(new Integer(colToPrint), new Integer(colToPrint));

                auxColumn = colToPrint;
            } else {
                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);
                cell.setCellStyle(styleTitleLevel2);
            }
            currentColumn++;
        }
    }


    public static void createSheetPlatesSubTitle(HSSFSheet currentSheet, List<String> metaData) throws SQLException {
        currentSheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 2));
        HSSFCell cell = createCell(currentSheet , 1, 1, "CONCESSÕES", (short)-1);cell.setCellStyle(styleTitleLevel2);
//        cell = createCell(currentSheet , 1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
//        cell = createCell(currentSheet , 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
//        cell = createCell(currentSheet , 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);

        int startRow = 1;
        int currentColumn = 2;
        int auxColumn = currentColumn;
        int columnCount = metaData.size();
        String strTotal = "total";
        for (int i = currentColumn-1; i < columnCount-1; i++) {

            String colName = metaData.get(i + 1);

            int colToPrint = currentColumn + 1;

            cell = createCell(currentSheet , startRow, colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);

            if (colName.toLowerCase().indexOf(strTotal)!=-1) {
                String strOrc = "orç.";
                if (colName.toLowerCase().indexOf(strOrc)!=-1) {
                    //TOTAL OR�.
                    cell = createCell(currentSheet, startRow+1, (short)colToPrint, "Orç.", (short)-1);
                    cell.setCellStyle(borderStyleCenterBlueFilled);
                    BUDGET_COLUMNS.put(new Integer(colToPrint), new Integer(colToPrint));

                    int posToSplit = colName.toLowerCase().indexOf(strOrc) + strOrc.length() + 2;
                    String secondTitle = "";
                    if (colName.length()>posToSplit)
                        secondTitle = colName.substring(colName.toLowerCase().indexOf(strOrc) + strOrc.length());

                    if(colToPrint!=(auxColumn+1))
                        currentSheet.addMergedRegion(new CellRangeAddress(startRow, startRow, auxColumn + 1, colToPrint));

                    cell = createCell(currentSheet, startRow, (short)(auxColumn+1), secondTitle, (short)-1);
                    cell.setCellStyle(styleTitleLevel2);

                    auxColumn = colToPrint;
                } else {
                    cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);
                    cell.setCellStyle(borderStyleCenterYellowFilled);
                    SUB_TOTAL_COLUMNS.put(new Integer(colToPrint), new Integer(colToPrint));
                }
            } else {

                if (colName.equalsIgnoreCase("Var.%")) {
                    cell = createCell(currentSheet, startRow+1, (short)(colToPrint-1), "Total Orç.", (short)-1);
                    cell.setCellStyle(borderStyleCenterBlueFilled);
                }

                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);
                cell.setCellStyle(styleTitleLevel2);
            }
            currentColumn++;
        }
    }


    public static void createSheetTitle(HSSFSheet currentSheet, String title) {
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 10));
        HSSFCell cell = createCell(currentSheet , 0, (short)1, title, (short)-1);
        cell.setCellStyle(styleTitleLevel1);
        cell.setCellStyle(styleTitleLevel1);
    }

    public static void createContractsTotalLine(HSSFSheet currentSheet, int currentRow, int totalColumns, boolean withFrotas, boolean isCaOnly, boolean isSheetTotal) {

        String cellValue = "SUB-TOTAL";
        if (withFrotas)
            cellValue = "TOTAL";

        if (isCaOnly)
            cellValue = "TOTAL";

        if (isSheetTotal)
            cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 4; i <= totalColumns; i++) {
            int colToPrint = i - 1;
            cell = row1.createCell((short)colToPrint);

            String formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + "4:" + EXCEL_COLUMNS[colToPrint] + currentRow + ")";
            if (withFrotas)
                formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + (currentRow-3) + ":" + EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            if (isSheetTotal)
                formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + (currentRow-5) + "," + EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            cell.setCellFormula(formula);

            if (colToPrint == totalColumns-7)
                cell.setCellStyle(borderStyleCenterYellowDotted);
            else if (colToPrint == totalColumns-6)
                cell.setCellStyle(borderStyleCenterBlueDotted);
            else if (colToPrint == totalColumns-5)
                cell.setCellStyle(borderStyleCenterGreenDotted);
            else if (colToPrint == totalColumns-4)
                cell.setCellStyle(borderStyleCenterNumber);
            else if (colToPrint == totalColumns-3)
                cell.setCellStyle(borderStyleCenterBlueDotted);
            else if (colToPrint == totalColumns-2)
                cell.setCellStyle(borderStyleCenterGreenDotted);
            else if (colToPrint == totalColumns-1)
                cell.setCellStyle(borderStyleCenterNumber);
            else
                cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createVDVCLine(HSSFSheet currentSheet, Map<String,Object> rs, int currentRow, int totalColumns) throws SQLException {

        HSSFCell cell = createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);
        cell = createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);

        StringBuffer formulaTotal = new StringBuffer();
        formulaTotal.append(  "0" );

        Integer compInt = 0;

        int leftColumn = 3;

        List<String> metadaKeys = new ArrayList<>();
        if(!rs.isEmpty())
            metadaKeys =  new ArrayList<>(rs.keySet());
        for (int i = 4; i < totalColumns; i++) {

            Integer curValue = fromObjectToInt(rs.get(metadaKeys.get(i)));
            int colToPrint = i - 1;

            if(colToPrint+1 == totalColumns) {
                //Total VDVC
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formulaTotal.toString(), (short)-1);
                cell.setCellFormula(formulaTotal.toString());
            } else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(colToPrint))) {
                String formula = "SUM(" + EXCEL_COLUMNS[leftColumn] + (currentRow+1) + ":" + EXCEL_COLUMNS[i-2] + (currentRow+1) + ")";
                cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                formulaTotal.append(  "+" + formula );
                cell.setCellFormula(formula);
                leftColumn = i;
            } else {
                if (compInt.equals(curValue)) {
                    cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                } else {
                    cell = createCell(currentSheet , currentRow, (short)colToPrint, String.valueOf(curValue), (short)-1);
                }
            }


            if (colToPrint == totalColumns-1)
                cell.setCellStyle(borderStyleCenterGreenDotted);
            else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(colToPrint))) {
                cell.setCellStyle(borderStyleCenterYellowDotted);
            }
            else
                cell.setCellStyle(borderStyleCenterNumber);
        }

    }

    public static void createVDVCTotalLine(HSSFSheet currentSheet, int currentRow, int totalColumns) {

        String cellValue = "TOTAL";

        HSSFRow row1 = currentSheet.createRow((short)currentRow);
        HSSFCell cell = createCell(currentSheet, currentRow, (short)2, cellValue, (short)-1);
        cell.setCellStyle(borderStyleCenterYellowFilled);
        for (int i = 4; i <= totalColumns; i++) {
            int colToPrint = i - 1;
            cell = row1.createCell((short)colToPrint);

            String formula = "SUM(" + EXCEL_COLUMNS[colToPrint] + "4:" + EXCEL_COLUMNS[colToPrint] + currentRow + ")";

            cell.setCellFormula(formula);

            if (colToPrint == totalColumns-1)
                cell.setCellStyle(borderStyleCenterBlueFilled);
            else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(colToPrint)))
                cell.setCellStyle(borderStyleCenterGreenFilled);
            else
                cell.setCellStyle(borderStyleCenterYellowFilled);
        }
    }

    public static void createVDVCSheetSubTitle(HSSFSheet currentSheet, List<String> metaData) throws SQLException {

        HSSFCell cell = createCell(currentSheet , 1, (short)1, "CONCESSÕES", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        currentSheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 2));

        int startRow = 1;
        int currentColumn = 3;
        int auxColumn = currentColumn-1;
        int columnCount = metaData.size();
        String strTotal = "total";
        for (int i = currentColumn-1; i < columnCount-1; i++) {
            Object header = metaData.get(i + 1);
            String colName = header.toString();

            int colToPrint = currentColumn;
            cell = createCell(currentSheet , startRow, (short) colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);
            if (colName.toLowerCase().indexOf(strTotal)!=-1) {

                String secondTitle = "";
                if ( colName.indexOf(" ")!=colName.lastIndexOf(" ") ) {
                    secondTitle = colName.substring(colName.lastIndexOf(" "));
                    colName = "Sub-Total";
                }

                //Unir C�lulas
                if(colToPrint!=(auxColumn + 1))
                    currentSheet.addMergedRegion(new CellRangeAddress(startRow, startRow, auxColumn + 1, colToPrint));
                cell = createCell(currentSheet, startRow, (short)(auxColumn+1), secondTitle, (short)-1);
                cell.setCellStyle(styleTitleLevel2);

                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);

                if (colToPrint == columnCount-1)
                    cell.setCellStyle(borderStyleCenterGreenFilled);
                else
                    cell.setCellStyle(borderStyleCenterYellowFilled);

                SUB_TOTAL_COLUMNS.put(new Integer(colToPrint), new Integer(colToPrint));

                auxColumn = colToPrint;
            } else {
                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);
                cell.setCellStyle(styleTitleLevel2);
            }
            currentColumn++;
        }
    }

    public static void createContractsLine(HSSFSheet currentSheet, Map<String, Object> rs, int currentRow, int totalColumns) throws SQLException {

        HSSFCell cell = createCell(currentSheet, currentRow, (short)1, (String) rs.get(COD_CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);
        cell = createCell(currentSheet, currentRow, (short)2, (String) rs.get(CONCESS_COL), (short)-1);
        cell.setCellStyle(borderStyleLeft);

        StringBuffer formulaTotal = new StringBuffer();
        formulaTotal.append(  "0" );

        int leftColumn = 3;

        List<String> metadataKeys = new ArrayList<>();
        if(!rs.isEmpty())
            metadataKeys =  new ArrayList<>(rs.keySet());
        for (int i = 4; i <= totalColumns; i++) {

            Integer curValue = fromObjectToInt(rs.get(metadataKeys.get(i)));
            int colToPrint = i - 1;

            if(colToPrint+1 == totalColumns) {
                // DIF
                String formula = EXCEL_COLUMNS[leftColumn+3] + (currentRow+1) + "-" + EXCEL_COLUMNS[leftColumn+4] + (currentRow+1);
                cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                cell.setCellFormula(formula);
            } else if(colToPrint+1 == totalColumns -1) {
                //Total Matri
                String formula = "'Tot. Matríc.'!" + EXCEL_COLUMNS[colToPrint+1-4] + (currentRow+1) ;
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formula, (short)-1);
                cell.setCellFormula(formula);
            } else if(colToPrint+1 == totalColumns -2) {
                //Total Contratos
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formulaTotal.toString(), (short)-1);
                cell.setCellFormula(formulaTotal.toString());
            } else if(colToPrint+1 == totalColumns -3) {
                //Diff
                String formula = EXCEL_COLUMNS[leftColumn] + (currentRow+1) + "-" + EXCEL_COLUMNS[leftColumn+1] + (currentRow+1);
                cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                cell.setCellFormula(formula);
            } else if(colToPrint+1 == totalColumns -4) {
                //Total Vendas
                String formula = "'Tot. Vendas'!" + EXCEL_COLUMNS[colToPrint-1] + (currentRow+1) ;
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formula, (short)-1);
                cell.setCellFormula(formula);
            } else if(colToPrint+1 == totalColumns -5) {
                //Total Contratos
                cell = createCell(currentSheet , currentRow, (short)colToPrint, formulaTotal.toString(), (short)-1);
                cell.setCellFormula(formulaTotal.toString());
//			} else if(colToPrint+1 == totalColumns -5) {
//				//DIF
//				String formula = EXCEL_COLUMNS[leftColumn] + (currentRow+1) + "-" + EXCEL_COLUMNS[leftColumn+1] + (currentRow+1);
//				cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
//				cell.setCellFormula(formula);
            } else if (SUB_TOTAL_COLUMNS.containsKey(new Integer(colToPrint))) {
                String formula = "SUM(" + EXCEL_COLUMNS[leftColumn] + (currentRow+1) + ":" + EXCEL_COLUMNS[i-2] + (currentRow+1) + ")";
                cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                formulaTotal.append(  "+" + formula );
                cell.setCellFormula(formula);
                leftColumn = i;
            } else {
                if (curValue==0) {
                    cell = createCell(currentSheet , currentRow, (short)colToPrint, "", (short)-1);
                } else {
                    cell = createCell(currentSheet , currentRow, (short)colToPrint, String.valueOf(curValue), (short)-1);
                }
            }


            if (colToPrint == totalColumns-7)
                cell.setCellStyle(borderStyleCenterYellowDotted);
            else if (colToPrint == totalColumns-6)
                cell.setCellStyle(borderStyleCenterBlueDotted);
            else if (colToPrint == totalColumns-5)
                cell.setCellStyle(borderStyleCenterGreenDotted);
            else if (colToPrint == totalColumns-3)
                cell.setCellStyle(borderStyleCenterBlueDotted);
            else if (colToPrint == totalColumns-2)
                cell.setCellStyle(borderStyleCenterGreenDotted);
            else
                cell.setCellStyle(borderStyleCenterNumber);
        }
    }

    public static void createExportOrderTotalLines(HSSFSheet salesSheet, List<PVMCarmodelForecast> vecForecasts, int currentRow) {
        int oldExportOrder = -1;
        int colspan = 0, currentColumn = 3, count = 0, startColumn = currentColumn;

        for (PVMCarmodelForecast  oModel:vecForecasts) {
            String formula = "";
            if(oModel.getExportOrder()!=oldExportOrder){
                if(colspan>0){
                    // Aqui monto a formula do somat�rio das colunas da linha anterior
                    formula = "SUM(" + EXCEL_COLUMNS[startColumn] + currentRow + ":" +EXCEL_COLUMNS[currentColumn-1] + currentRow + ")";
                    // Aqui vou fazer reset �s formulas das colunas que ficam dentro do colspan
                    int startColumnToConcat = startColumn+1;
                    while(startColumnToConcat<=(currentColumn-1)){
                        createTotalExportOrderCell(salesSheet, "", startColumnToConcat, currentRow,false);
                        startColumnToConcat++;
                    }
                    // Aqui fica na primeira coluna do colspan a formula de calculo completa
                    createTotalExportOrderCell(salesSheet, formula, startColumn, currentRow,false);
                    createForecastsRegion(salesSheet,startColumn, currentColumn-1 ,currentRow);
                }
                colspan = 0;
                startColumn = currentColumn;
            }else{
                if(oldExportOrder==-1)
                    oldExportOrder = oModel.getExportOrder();
                else
                    colspan++;
            }
            while(SUB_TOTAL_COLUMNS.containsKey(new Integer(currentColumn))){
                currentColumn = createForecastsCell(salesSheet, -1, currentColumn, currentRow, null);
                startColumn = currentColumn;
            }

            formula = "SUM(" + EXCEL_COLUMNS[currentColumn] + currentRow + ":" +EXCEL_COLUMNS[currentColumn] + currentRow + ")";
            currentColumn = createTotalExportOrderCell(salesSheet, formula, currentColumn, currentRow,false);
            oldExportOrder=oModel.getExportOrder();
        }


        if(colspan > 0){
            // Aqui monto a formula do somat�rio das colunas da linha anterior
            String formula = "SUM(" + EXCEL_COLUMNS[startColumn] + currentRow + ":" +EXCEL_COLUMNS[currentColumn-1] + currentRow + ")";
            // Aqui vou fazer reset �s formulas das colunas que ficam dentro do colspan
            int startColumnToConcat = startColumn+1;
            while(startColumnToConcat<=(currentColumn-1)){
                createTotalExportOrderCell(salesSheet, "", startColumnToConcat, currentRow,false);
                startColumnToConcat++;
            }
            // Aqui fica na primeira coluna do colspan a formula de calculo completa
            createTotalExportOrderCell(salesSheet, formula, startColumn, currentRow,false);
            createForecastsRegion(salesSheet,startColumn, currentColumn-1 ,currentRow);
        }
    }

    public static void createForecastsLines(HSSFSheet salesSheet, List<PVMCarmodelForecast> vecForecasts, int currentRow) {
        int oldExportOrder = -1;

        HSSFCell cell = createCell(salesSheet, currentRow, (short)2, "Plano (Form 01)", (short)-1);
        cell.setCellStyle(styleForecast);
        int colspan = 0, currentColumn = 3, count = 0, startColumn = currentColumn;

        ArrayList<Integer> cellsToSum = new ArrayList<Integer>();
        cellsToSum.add(startColumn);
        String subTotalCells = "";

        for (PVMCarmodelForecast oModel: vecForecasts) {
            if(oldExportOrder==-1)
                oldExportOrder = oModel.getExportOrder();
            Integer forecast = oModel.getForecast();

            if(oModel.getExportOrder()!=oldExportOrder){
                if(colspan>0){
                    createForecastsRegion(salesSheet,startColumn, currentColumn-1 ,currentRow);
                }
                colspan = 0;
                startColumn = currentColumn;
                cellsToSum.add(currentColumn);
            }else{
                colspan++;
            }
            while(SUB_TOTAL_COLUMNS.containsKey(new Integer(currentColumn))){
                subTotalCells += (currentColumn) + ";";

                currentColumn= createForecastsCell(salesSheet, -1, currentColumn, currentRow, cellsToSum);
                startColumn = currentColumn;


                cellsToSum = new ArrayList<Integer>();
                cellsToSum.add(currentColumn);
            }
            currentColumn = createForecastsCell(salesSheet, forecast, currentColumn, currentRow, null);
            oldExportOrder=oModel.getExportOrder();
        }

        if(colspan>0){
            createForecastsRegion(salesSheet,startColumn, currentColumn-1 ,currentRow);
        }

        subTotalCells += (currentColumn) + ";";

        //� necess�rio acrescentar um valor dummy ao arrayList pois no m�todo createForecastsCell, o ciclo FOR termina na penultultima posi��o
        //isto porque quando s�o escritos os subtotais anteriores j� est� a ser processada a coluna seguinte e j� esta no arrayList para ser somada no pr�ximo subtotal,
        //como este � o ultimo subtotal, n�o existe coluna seguinte.
        cellsToSum.add(0);//DUMMY!!!

        currentColumn= createForecastsCell(salesSheet, -1, currentColumn, currentRow, cellsToSum);

        subTotalCells = subTotalCells.substring(0,subTotalCells.length()-1);
        String [] arrSubTotalCells = subTotalCells.split(";");
        String formula = "SUM(";
        for(int i=0; i<arrSubTotalCells.length; i++){
            formula += EXCEL_COLUMNS[Integer.parseInt(arrSubTotalCells[i])] + (currentRow+1) + ",";
        }

        formula = formula.substring(0,formula.length()-1);
        formula += ")";

        cell = createCell(salesSheet, currentRow, (short)(currentColumn), "", (short)-1);
        cell.setCellFormula(formula);
        cell.setCellStyle(styleForecast);
    }

    public static void createForecastsTotalLines(HSSFSheet salesSheet, List<PVMCarmodelForecast> vecForecasts, int currentRow) {
        int oldExportOrder = -1;
        HSSFCell cell = createCell(salesSheet, currentRow, (short)2, "Previsão vs Plano", (short)-1);
        cell.setCellStyle(styleForecast);

        String subTotalCells = "";
        int colspan = 0, currentColumn = 3, count = 0, startColumn = currentColumn;
        ArrayList<Integer> cellsToSum = new ArrayList<Integer>();

        for (PVMCarmodelForecast oModel: vecForecasts) {
            String formula = "";
            if(oModel.getExportOrder()!=oldExportOrder){
                if(colspan>0){
                    formula = EXCEL_COLUMNS[startColumn] + (currentRow-3) + "-" +EXCEL_COLUMNS[startColumn] + (currentRow-1) ;
                    // Aqui fica sempre a formula de calculo de subtra��o da coluna total - o valor previsto, como no caso de ser um colspan so na primeira coluna
                    // � que fica o valor a subtrair.
                    createTotalExportOrderCell(salesSheet, formula, startColumn, currentRow, true);
                    createForecastsRegion(salesSheet,startColumn, currentColumn-1 ,currentRow);
                }
                colspan = 0;
                startColumn = currentColumn;
                cellsToSum.add(currentColumn);
            }else{
                if(oldExportOrder==-1)
                    oldExportOrder = oModel.getExportOrder();
                else{
                    colspan++;
                }
            }
            while(SUB_TOTAL_COLUMNS.containsKey(new Integer(currentColumn))){
                subTotalCells += (currentColumn) + ";";
                currentColumn = createForecastsCell(salesSheet, -1, currentColumn, currentRow, cellsToSum);
                startColumn = currentColumn;

                cellsToSum = new ArrayList<Integer>();
                cellsToSum.add(currentColumn);
            }

            formula = EXCEL_COLUMNS[currentColumn] + (currentRow-3) + "-" +EXCEL_COLUMNS[currentColumn] + (currentRow-1) ;
            currentColumn = createTotalExportOrderCell(salesSheet, formula, currentColumn, currentRow, true);
            oldExportOrder=oModel.getExportOrder();
        }

        if(colspan>0){
            createForecastsRegion(salesSheet,startColumn, currentColumn-1 ,currentRow);
        }

        subTotalCells += (currentColumn) + ";";

        //� necess�rio acrescentar um valor dummy ao arrayList pois no m�todo createForecastsCell, o ciclo FOR termina na penultultima posi��o
        //isto porque quando s�o escritos os subtotais anteriores j� est� a ser processada a coluna seguinte e j� esta no arrayList para ser somada no pr�ximo subtotal,
        //como este � o ultimo subtotal, n�o existe coluna seguinte.
        cellsToSum.add(0);//DUMMY!!!


        currentColumn= createForecastsCell(salesSheet, -1, currentColumn, currentRow, cellsToSum);


        subTotalCells = subTotalCells.substring(0,subTotalCells.length()-1);
        String [] arrSubTotalCells = subTotalCells.split(";");
        String formula = "SUM(";
        for(int i=0; i<arrSubTotalCells.length; i++){
            formula += EXCEL_COLUMNS[Integer.parseInt(arrSubTotalCells[i])] + (currentRow+1) + ",";
        }

        formula = formula.substring(0,formula.length()-1);
        formula += ")";

        cell = createCell(salesSheet, currentRow, (short)(currentColumn), "", (short)-1);
        cell.setCellStyle(styleForecast);
        cell.setCellFormula(formula);
    }

    public static void createSheetContractsSubTitle(HSSFSheet currentSheet, List<String> metaData) throws SQLException {

        HSSFCell cell = createCell(currentSheet , 1, (short)1, "CONCESSÕES", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 1, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 2, (short)1, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        cell = createCell(currentSheet , 2, (short)2, "", (short)-1);cell.setCellStyle(styleTitleLevel2);
        currentSheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 2));

        int startRow = 1;
        int currentColumn = 3;
        int auxColumn = currentColumn-1;
        int columnCount = metaData.size();
        String strTotal = "total";
        for (int i = currentColumn-1; i < columnCount-1; i++) {
            Object header = metaData.get(i + 1);
            String colName = header.toString();

            int colToPrint = currentColumn;
            cell = createCell(currentSheet , startRow, (short) colToPrint, "", (short)-1);
            cell.setCellStyle(styleTitleLevel2);
            if (colName.toLowerCase().indexOf(strTotal)!=-1) {
                //TOTAL VENDAS
                String secondTitle = "";
                if ( colName.indexOf(" ")!=colName.lastIndexOf(" ") ) {
                    secondTitle = colName.substring(colName.lastIndexOf(" "));
                    colName = "Sub-Total";
                }

                //Unir C�lulas
                currentSheet.addMergedRegion(new CellRangeAddress(startRow, startRow, auxColumn + 1, colToPrint));
                cell = createCell(currentSheet, startRow, (short)(auxColumn+1), secondTitle, (short)-1);
                cell.setCellStyle(styleTitleLevel2);

                //Retirar 1 e 2 do final.
                if(colName.endsWith("1") || colName.endsWith("2") ) {
                    colName = colName.substring(0,colName.length()-1);
                }

                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);

                if (colToPrint == columnCount-6)
                    cell.setCellStyle(borderStyleCenterBlueFilled);
                else if (colToPrint == columnCount-5)
                    cell.setCellStyle(borderStyleCenterGreenFilled);
                else if (colToPrint == columnCount-3)
                    cell.setCellStyle(borderStyleCenterBlueFilled);
                else if (colToPrint == columnCount-2)
                    cell.setCellStyle(borderStyleCenterGreenFilled);
                else
                    cell.setCellStyle(borderStyleCenterYellowFilled);

                SUB_TOTAL_COLUMNS.put(new Integer(colToPrint), new Integer(colToPrint));

                auxColumn = colToPrint;
            } else {
                //Retirar 1 e 2 do final.
                if(colName.endsWith("1") || colName.endsWith("2") ) {
                    colName = colName.substring(0,colName.length()-1);
                }

                cell = createCell(currentSheet, startRow+1, (short)colToPrint, colName, (short)-1);
                cell.setCellStyle(styleTitleLevel2);
            }
            currentColumn++;
        }
    }

    private static int createForecastsCell(HSSFSheet currentSheet, Integer forecast, int currentColumn, int currentRow, ArrayList<Integer> subTotal){
        HSSFCell cell = null;
        if(forecast!=null&&forecast==-1){
            cell = createCell(currentSheet, currentRow, (short)currentColumn, "", (short)-1);
            if(subTotal != null && subTotal.size() > 0){
                String formula = "SUM(";
                for(int i = 0; i< subTotal.size(); i++){
                    formula += EXCEL_COLUMNS[subTotal.get(i)] + (currentRow+1) + ",";
                }
                formula = formula.substring(0,formula.length()-1);
                formula +=")";

                cell.setCellFormula(formula);
                cell.setCellStyle(styleForecast);
            }

        } else{
            cell = createCell(currentSheet, currentRow, (short)currentColumn, forecast, (short)-1);
            cell.setCellStyle(styleForecast);
        }
        currentColumn++;
        return currentColumn;
    }

    private static void createForecastsRegion(HSSFSheet currentSheet, int startColumn, int endColumn, int currentRow){
        if(startColumn!=endColumn)
            currentSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, startColumn, endColumn));
    }

    private static int createTotalExportOrderCell(HSSFSheet currentSheet, String formula, int currentColumn, int currentRow, boolean isToStyle){
        HSSFCell cell = createCell(currentSheet, currentRow, (short)currentColumn, "", (short)-1);
        cell.setCellValue(0);
        if(!formula.equals(""))
            cell.setCellFormula(formula);
        if(isToStyle)
            cell.setCellStyle(styleForecast);
        else
            cell.setCellStyle(styleOrderTotals);
        currentColumn++;
        return currentColumn;
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
