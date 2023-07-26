package com.gsc.mkformularios.service.impl.pvm;

import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PVMCarmodelForecast;
import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toynet.LexusRetailerRepository;
import com.gsc.mkformularios.repository.toynet.ToyotaRetailerRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.utils.ExcelUtils1Month;
import com.gsc.mkformularios.utils.ExcelUtils3Month;
import com.gsc.mkformularios.utils.PVMReportQueries1Month;
import com.gsc.mkformularios.utils.PVMUtil;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DataBaseTasks;
import com.sc.commons.utils.DateTimerTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

import static com.gsc.mkformularios.service.impl.pvm.PVMReportStyles.*;
import static com.gsc.mkformularios.service.impl.pvm.PVMReportStyles.getStyleVariation;
//import static com.gsc.mkformularios.utils.ExcelUtils.*;
//import static com.gsc.mkformularios.utils.ExcelUtils.createVDVCTotalLine;
import static com.gsc.mkformularios.utils.ExcelUtils1Month.*;
import static com.gsc.mkformularios.utils.PVMReportQueries1Month.*;

@Component
@RequiredArgsConstructor
@Log4j
public class PVM1MonthReport {

    private final DbContext dbContext;
    private final LexusRetailerRepository lexusRetailerRepository;
    private final ToyotaRetailerRepository toyotaRetailerRepository;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;

    public static Hashtable<Integer, Integer> SUB_TOTAL_COLUMNS = null;
    public static Hashtable<Integer, Integer> BUDGET_COLUMNS = null;

    public void setDataSourceContext(Long client){
        if (client == 2L) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == 1L){
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    public HSSFWorkbook execute(int year, int month, String brand, boolean isCAMember) throws SCErrorException {
        return createExcelWb(String.valueOf(year), String.valueOf(month), brand, isCAMember);
    }

    private HSSFWorkbook createExcelWb(String year, String month, String brand, boolean isCAMember) throws SCErrorException {
        //TODO change 1L constant for CONSTANT
        if (brand.equalsIgnoreCase("Toyota")) {
            this.setDataSourceContext(1L);
        } else if (brand.equalsIgnoreCase("Lexus")) {
            this.setDataSourceContext(2L);
        }
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet platesSheet = workBook.createSheet("Tot. Matríc.");
        HSSFSheet salesSheet = workBook.createSheet("Tot. Vendas");
        HSSFSheet vdvcSheet = workBook.createSheet("Tot. VDVC");
        HSSFSheet contractsSheet = null;
        if (brand.equalsIgnoreCase("Lexus"))
            contractsSheet = workBook.createSheet("Tot. Contratos");

        List<Map<String,Object>> rs = new ArrayList<>();
        List<Map<String,Object>> rs2 = new ArrayList<>();
        try {
            List<RetailDealerDTO> vecRetailerDealers 	= getRetailerDealers(brand, isCAMember);
            Vector<String[]> pvmCarModels = PVMUtil.getPVMCarModels(brand, year, month);
            List<String[]> vecPVMCarModel 		= getPVMCarModels(brand, year, month);
            List<PVMCarmodelForecast> vecForecasts 		= new ArrayList<>();
            if(brand.equals("Toyota")){
                String dtFrom = CalculatePVMDtFrom(year, month);
                vecForecasts = pvmMonthlyReportRepository.getPVMCarModelsForecasts(year, month, dtFrom);
            }

            String platesSql = getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month);
            log.debug("...............................................");
            log.debug("platesSql:" + platesSql);
            log.debug("...............................................");

            String frotasPlatesSql = PVMReportQueries1Month.getFrotasPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month);
            log.debug("...............................................");
            log.debug("frotasPlatesSql:" + frotasPlatesSql);
            log.debug("...............................................");

            String salesSql = getSalesSql(vecRetailerDealers, vecPVMCarModel, year, month);
            log.debug("...............................................");
            log.debug("salesSql:" + salesSql);
            log.debug("...............................................");
            String frotasSalesSql= getFrotasSalesSql(vecRetailerDealers, vecPVMCarModel, year, month);
            log.debug("...............................................");
            log.debug("frotasSalesSql:" + frotasSalesSql);
            log.debug("...............................................");

            String vdvcSql = getVDVCSql(vecRetailerDealers, vecPVMCarModel, year, month);

            String contractsSql = getContractsSql(vecRetailerDealers, vecPVMCarModel, year, month);

            SUB_TOTAL_COLUMNS  = new Hashtable<Integer, Integer>();
            BUDGET_COLUMNS = new Hashtable<Integer, Integer>();

            initStyles(workBook);

            createSheetTitle(platesSheet,     "PREVISÃO DE MATRÍCULAS - "	+ DateTimerTasks.ptMonths[Integer.parseInt(month)-1].toUpperCase());
            createSheetTitle(salesSheet ,     "PREVISÃO DE VENDAS - " 		+ DateTimerTasks.ptMonths[Integer.parseInt(month)-1].toUpperCase());
            createSheetTitle(vdvcSheet, "PREVISÃO DE VEICULOS DE DEMONSTRAÇÃO / CORTESIA - " + DateTimerTasks.ptMonths[Integer.parseInt(month)-1].toUpperCase());
            if (brand.equalsIgnoreCase("Lexus"))
                createSheetTitle(contractsSheet , "PREVISÃO DE CONTRATOS - " 	+ DateTimerTasks.ptMonths[Integer.parseInt(month)-1].toUpperCase());


            rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(platesSql);


            //Inicio Constru��o Folha Matriculas
            List<String> metaData = new ArrayList<>();
            if(!rs.isEmpty())
                metaData =  new ArrayList<>(rs.get(0).keySet());
            createSheetPlatesSubTitle(platesSheet, metaData);

            int currentRow = 3;
            for (Map<String, Object> rsCurrent: rs) {
                createPlatesLine(platesSheet, rsCurrent, currentRow++, metaData.size());
            }


            //Linha SubTotal
            if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
                createPlatesTotalLine(platesSheet, currentRow++, metaData.size(), false, true, false);
            } else {
                createPlatesTotalLine(platesSheet, currentRow++, metaData.size(), false, false, false);
                rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasPlatesSql);

                int lineCount = 0;
                List cellsToChange= null;
                for (Map<String, Object> rs2Current: rs2) {
                    if(lineCount>0){
                        List currCells = createPlatesLine(platesSheet, rs2Current, currentRow++, metaData.size(), false);
                        for(int cnt = 0; cnt< currCells.size(); cnt++){
                            HSSFCell currCell = (HSSFCell) currCells.get(cnt);
                            HSSFCell cellToChange = (HSSFCell) cellsToChange.get(cnt);

                            double curValue = cellToNumber(currCell) + cellToNumber(cellToChange);
                            cellToChange.setCellValue(curValue);
                            currCell.setCellValue(0);
                        }
                    } else {
                        cellsToChange = createPlatesLine(platesSheet, rs2Current, currentRow++, metaData.size(), false);
                    }
                    lineCount ++;
                }
                Iterator<Integer> itBudgets  = BUDGET_COLUMNS.keySet().iterator();
                while(itBudgets.hasNext()){
                    Integer budgetCol = itBudgets.next();
                    Short budgetColShort = budgetCol.shortValue();
                    platesSheet.addMergedRegion(new CellRangeAddress(currentRow - 2, currentRow - 1, budgetColShort, budgetColShort));
                }
                createPlatesTotalLine(platesSheet, currentRow++, metaData.size(), true, false, false);
                //Linha Total
                createPlatesTotalLine(platesSheet, currentRow++, metaData.size(), false, false, true);
            }

            //Descri��o Asterisco
            ExcelUtils3Month.createCell(platesSheet, ++currentRow, (short)2, "* Valores obtidos através do orçamento", (short)-1);

            platesSheet.setColumnWidth((short)1, (short) (3 * 256));
            platesSheet.setColumnWidth((short)2, (short) (40 * 256));

            //=========================================================================================================//
            //=========================================================================================================//
            //=========================================================================================================//

            //Inicio Constru��o Folha Vendas
            SUB_TOTAL_COLUMNS  = new Hashtable<Integer, Integer>();
            rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(salesSql);

            metaData = new ArrayList<>();
            if(!rs.isEmpty())
                metaData =  new ArrayList<>(rs.get(0).keySet());

            createSheetSalesSubTitle(salesSheet, metaData);

            currentRow = 3;
            for (Map<String, Object> rsCurrent: rs) {
                createSalesLine(salesSheet, rsCurrent, currentRow++, metaData.size());
            }

            //Linha SubTotal
            if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
                createSalesTotalLine(salesSheet, currentRow++, metaData.size(), false, true, false);
            } else {
                createSalesTotalLine(salesSheet, currentRow++, metaData.size(), false, false, false);

                rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasSalesSql);
                for (Map<String, Object> rs2Current: rs2) {
                    createSalesLine(salesSheet, rs2Current, currentRow++, metaData.size());
                }

                createSalesTotalLine(salesSheet, currentRow++, metaData.size(), true, false, false);
                //Linha Total
                createSalesTotalLine(salesSheet, currentRow++, metaData.size(), false, false, true);
            }

            if(brand.equals("Toyota")){
                // Linha de totais agrupados
                createExportOrderTotalLines(salesSheet, vecForecasts, currentRow++);

                currentRow++;
                // Linha de form01
                createForecastsLines(salesSheet, vecForecasts, currentRow++);

                currentRow++;
                // Linha de total diferencial
                createForecastsTotalLines(salesSheet, vecForecasts, currentRow++);
            }

            salesSheet.setColumnWidth((short)1, (short) (3 * 256));
            salesSheet.setColumnWidth((short)2, (short) (40 * 256));

            if (brand.equalsIgnoreCase("Lexus")) {
                //Inicio Constru��o Folha Contratos
                SUB_TOTAL_COLUMNS  = new Hashtable<Integer, Integer>();

                rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(contractsSql);
                metaData = new ArrayList<>();
                if(!rs.isEmpty())
                    metaData =  new ArrayList<>(rs.get(0).keySet());

                createSheetContractsSubTitle(contractsSheet, metaData);
                currentRow = 3;
                for (Map<String, Object> rsCurrent: rs) {
                    createContractsLine(contractsSheet, rsCurrent, currentRow++, metaData.size());
                }


                String frotasContractsSql = getLexusFrotasContractsSql(vecRetailerDealers, vecPVMCarModel, year, month);

                createContractsTotalLine(contractsSheet, currentRow++, metaData.size(), false, false, false);
                rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasContractsSql);
                for (Map<String, Object> rs2Current: rs2) {
                    createContractsLine(contractsSheet, rs2Current, currentRow++, metaData.size());
                }

                //Linha Total
                createContractsTotalLine(contractsSheet, currentRow++, metaData.size(), true, false, false);
                createContractsTotalLine(contractsSheet, currentRow++, metaData.size(), true, false, true);
                contractsSheet.setColumnWidth((short)1, (short) (3 * 256));
                contractsSheet.setColumnWidth((short)2, (short) (40 * 256));
            }

            //Inicio Constru��o Folha VDVC


            SUB_TOTAL_COLUMNS  = new Hashtable<Integer, Integer>();

            rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(vdvcSql);
            metaData = new ArrayList<>();
            if(!rs.isEmpty())
                metaData =  new ArrayList<>(rs.get(0).keySet());

            createVDVCSheetSubTitle(vdvcSheet, metaData);

            currentRow = 3;
            for (Map<String,Object> rsCurrent:rs) {
                createVDVCLine(vdvcSheet, rsCurrent, currentRow++, metaData.size());
            }


            createVDVCTotalLine(vdvcSheet, currentRow++, metaData.size());

            vdvcSheet.setColumnWidth((short)1, (short) (3 * 256));
            vdvcSheet.setColumnWidth((short)2, (short) (40 * 256));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SCErrorException("GeneratePVMReport.createExcelWb", e.getMessage(), e);
        }
        return workBook;
    }

    private List<RetailDealerDTO> getRetailerDealers(String brand, boolean isCAMember) throws SCErrorException, SQLException {
        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            return toyotaRetailerRepository.findAllOrderByDesigCaMember();
        } else if (brand.equalsIgnoreCase("Toyota")) {
            return toyotaRetailerRepository.findAllOrderByDesig();
        } else if (brand.equalsIgnoreCase("Lexus")){
            return lexusRetailerRepository.findAllOrderByDesig();
        }
        return new ArrayList<>();
    }

    private List<String[]> getPVMCarModels(String brand, String year, String month) throws SQLException {
        if (brand.equalsIgnoreCase("Lexus"))
            this.setDataSourceContext(2L);

        List<String[]> vecResult = new ArrayList<>();

        month = ("00" + month);
        month = month.substring(month.length()-2);

        String dtFrom = CalculatePVMDtFrom(year, month);

        List<PVMCarmodel> pvmCarmodels = pvmCarmodelRepository.getPVMCarModels1(dtFrom);

        for (PVMCarmodel carModel: pvmCarmodels) {
            String type = carModel.getType();
            String id = String.valueOf(carModel.getId());
            String name = carModel.getName();
            vecResult.add(new String[]{type, name, id});

        }
        return vecResult;
    }

    public static String CalculatePVMDtFrom(String year, String month) {

//		String dtFrom = year + "-" + month + "-01";

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String dtFrom = DateTimerTasks.fmtDT.format(cal.getTime());

        return dtFrom;
    }

    private static Double cellToNumber(HSSFCell value) {

        if(value.getCellType() == CellType.STRING) {
            return fromStringToDouble(value.getStringCellValue());
        } else if(value.getCellType() == CellType.NUMERIC){
            return value.getNumericCellValue();
        } else {
            return (double) 0;
        }
    }

    private static Double fromStringToDouble(String value){
        if (value != null) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                return (double) 0;
            }
        } else {
            return (double) 0;
        }
    }


}
