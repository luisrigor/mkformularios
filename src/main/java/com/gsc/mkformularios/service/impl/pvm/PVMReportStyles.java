package com.gsc.mkformularios.service.impl.pvm;

import com.gsc.mkformularios.utils.ExcelUtils3Month;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class PVMReportStyles {

    public static HSSFCellStyle getTitleStyleLevel1(HSSFWorkbook wb) {
        HSSFCellStyle styleTitleStyleLevel1 = wb.createCellStyle();
        styleTitleStyleLevel1.setAlignment(HorizontalAlignment.LEFT);

        HSSFFont fontTitle = wb.createFont();
        fontTitle.setFontName("Times New Roman");
        fontTitle.setFontHeightInPoints((short) 14);
        fontTitle.setBold(true);

        styleTitleStyleLevel1.setFont(fontTitle);
        return styleTitleStyleLevel1;
    }

    public static HSSFCellStyle getTitleStyleLevel2(HSSFWorkbook wb) {
        HSSFCellStyle styleTitleStyleLevel2 = wb.createCellStyle();
        styleTitleStyleLevel2.setAlignment(HorizontalAlignment.LEFT);

        HSSFFont fontTitle = getFontTitleLevel2Style(wb);

        styleTitleStyleLevel2.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitleStyleLevel2.setAlignment(HorizontalAlignment.CENTER);

        styleTitleStyleLevel2.setBorderBottom(BorderStyle.THIN);
        styleTitleStyleLevel2.setBorderLeft(BorderStyle.THIN);
        styleTitleStyleLevel2.setBorderTop(BorderStyle.THIN);
        styleTitleStyleLevel2.setBorderRight(BorderStyle.THIN);

        styleTitleStyleLevel2.setFont(fontTitle);
        return styleTitleStyleLevel2;
    }
    public static HSSFCellStyle getDetailsStyleLeft(HSSFWorkbook wb) {
        HSSFCellStyle styleDetailsStyleLeft = wb.createCellStyle();
        styleDetailsStyleLeft.setAlignment(HorizontalAlignment.LEFT);
        styleDetailsStyleLeft.setFont(getDetailsFont(wb));
        return styleDetailsStyleLeft;
    }
    public static HSSFCellStyle getDetailsStyleCenterText(HSSFWorkbook wb) {
        HSSFCellStyle styleDetailsStyleCenterText = wb.createCellStyle();
        styleDetailsStyleCenterText.setAlignment(HorizontalAlignment.CENTER);
        styleDetailsStyleCenterText.setFont(getDetailsFont(wb));
        return styleDetailsStyleCenterText;
    }
    public static HSSFCellStyle getDetailsStyleCenterNumber(HSSFWorkbook wb) {
        HSSFCellStyle styleDetails = wb.createCellStyle();
        styleDetails.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDetails.setAlignment(HorizontalAlignment.CENTER);
        styleDetails.setFont(getDetailsFont(wb));
        return styleDetails;
    }
    public static HSSFFont getFontTitleLevel2Style(HSSFWorkbook wb) {
        HSSFFont fontTitle = wb.createFont();
        fontTitle.setFontName("Arial");
        fontTitle.setFontHeightInPoints((short) 8);
        fontTitle.setBold(true);
        return fontTitle;
    }
    public static HSSFFont getDetailsFont(HSSFWorkbook wb) {
        return ExcelUtils3Month.getArial(wb, 10);
    }

    public static HSSFCellStyle getBorderStyleLeft(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleLeft(currentWorkBook);
        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.DOTTED);
        return currentStyle;
    }

    public static HSSFCellStyle getBorderStyleCenterNumber(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);
        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.DOTTED);
        return currentStyle;
    }
    public static HSSFCellStyle getBorderStyleCenterYellowFilled(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);


        HSSFFont fontTitle = getFontTitleLevel2Style(currentWorkBook);
        currentStyle.setFont(fontTitle);

        currentStyle.setBorderTop(BorderStyle.THIN);
        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.THIN);

        currentStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        currentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return currentStyle;
    }
    public static HSSFCellStyle getBorderStyleCenterGreenFilled(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);


        HSSFFont fontTitle = getFontTitleLevel2Style(currentWorkBook);
        currentStyle.setFont(fontTitle);

        currentStyle.setBorderTop(BorderStyle.THIN);
        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.THIN);

        currentStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        currentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return currentStyle;
    }
    public static HSSFCellStyle getBorderStyleCenterBlueFilled(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);

        HSSFFont fontTitle = getFontTitleLevel2Style(currentWorkBook);
        currentStyle.setFont(fontTitle);

        currentStyle.setBorderTop(BorderStyle.THIN);
        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.THIN);

        currentStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
        currentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return currentStyle;
    }

    public static HSSFCellStyle getBorderStyleCenterYellowDotted(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);

        HSSFFont fontTitle = getFontTitleLevel2Style(currentWorkBook);
        currentStyle.setFont(fontTitle);

        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.DOTTED);

        currentStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        currentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return currentStyle;
    }
    public static HSSFCellStyle getBorderStyleCenterGreenDotted(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);

        HSSFFont fontTitle = getFontTitleLevel2Style(currentWorkBook);
        currentStyle.setFont(fontTitle);

        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.DOTTED);

        currentStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        currentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return currentStyle;
    }
    public static HSSFCellStyle getBorderStyleCenterBlueDotted(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);

        HSSFFont fontTitle = getFontTitleLevel2Style(currentWorkBook);
        currentStyle.setFont(fontTitle);

        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.THIN);

        currentStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
        currentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return currentStyle;
    }

    public static HSSFCellStyle getStyleVariation(HSSFWorkbook currentWorkBook) {
        HSSFCellStyle currentStyle = getDetailsStyleCenterNumber(currentWorkBook);

        currentStyle.setBorderLeft(BorderStyle.THIN);
        currentStyle.setBorderRight(BorderStyle.THIN);
        currentStyle.setBorderTop(BorderStyle.THIN);
        currentStyle.setBorderBottom(BorderStyle.THIN);

        HSSFDataFormat format = currentWorkBook.createDataFormat();
        currentStyle.setDataFormat(format.getFormat("0.0"));

        return currentStyle;
    }
}
