package com.gsc.mkformularios.service.impl.pvm;

import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.repository.toynet.LexusRetailerRepository;
import com.gsc.mkformularios.repository.toynet.ToyotaRetailerRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.utils.ExcelUtils3Month;
import com.gsc.mkformularios.utils.PVMReportQueries3Month;
import com.sc.commons.exceptions.SCErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

import static com.gsc.mkformularios.utils.ExcelUtils3Month.*;

@Component
@RequiredArgsConstructor
public class PVM3MonthReport {

    public static final String[] ptMonths= new String[]{"", "Janeiro","Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro", "Janeiro" ,"Fevereiro"};

    private final DbContext dbContext;
    private final LexusRetailerRepository lexusRetailerRepository;
    private final ToyotaRetailerRepository toyotaRetailerRepository;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;

    public void setDataSourceContext(Long client){
        if (client == 2L) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == 1L){
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    public HSSFWorkbook executeForToyota(int year, int month, boolean isCAMember) {
        return execute(String.valueOf(year), String.valueOf(month), "Toyota", isCAMember);
    }

    public HSSFWorkbook executeForLexus(int year, int month, boolean isCAMember) {
        return execute(String.valueOf(year), String.valueOf(month), "Lexus", isCAMember);
    }





    private HSSFWorkbook execute(String year, String month, String brand, boolean isCAMember) {
        return createExcelWb(year, month, brand, isCAMember);
    }



    private HSSFWorkbook createExcelWb(String year, String month, String brand, boolean isCAMember) {

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet platesSheet = workBook.createSheet("Tot. Matríc.");
        HSSFSheet salesSheet = workBook.createSheet("Tot. Vendas");
        HSSFSheet vdvcSheet = workBook.createSheet("Tot. VDVC");

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null ;
        List<Map<String,Object>> rs = new ArrayList<>();
        List<Map<String,Object>> rs2 = new ArrayList<>();

        initStyles(workBook);

        try {
            List<RetailDealerDTO> vecRetailerDealers = getRetailerDealers(brand, isCAMember);
            List<String[]> vecPVMCarModel = getPVMCarModels(brand, year, month, "PASSAGEIROS");

//            String platesSql = PVMReportQueries.getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", false);
//            String frotasPlatesSql = PVMReportQueries.getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", true);
//
//            String salesSql = PVMReportQueries.getSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", false);
//            String frotasSalesSql = PVMReportQueries.getSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", true);
//
//            String totalPlatesPassageirosSql = PVMReportQueries.getTotalPlatesSql( vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
//            String totalFrotasPlatesPassageirosSql = PVMReportQueries.getTotalPlatesFrotasSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
//
//            String totalPlatesComerciaisSql = PVMReportQueries.getTotalPlatesSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
//            String totalFrotasPlatesComerciaisSql = PVMReportQueries.getTotalPlatesFrotasSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
//
//            String totalSalesPassageirosSql = PVMReportQueries.getTotalSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
//            String totalFrotasSalesPassageirosSql = PVMReportQueries.getTotalSalesFrotasSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
//
//            String totalSalesComerciaisSql = PVMReportQueries.getTotalSalesSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
//            String totalFrotasSalesComerciaisSql = PVMReportQueries.getTotalSalesFrotasSql(vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
//
//            String vdvcSql = PVMReportQueries.getVDVCSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
//            String totalVDVCPassageirosSql = PVMReportQueries.getTotalVDVCSql(vecRetailerDealers, year, month, "PASSAGEIROS");
//            String totalVDVCComerciaisSql = PVMReportQueries.getTotalVDVCSql( vecRetailerDealers, year, month, "COMERCIAIS");

            ExcelUtils3Month.createSheetTitle(workBook, platesSheet, "PREVISÃO DE MATRíCULAS - " + ptMonths[Integer.parseInt(month)].toUpperCase());
            ExcelUtils3Month.createSheetTitle(workBook, salesSheet , "PREVISÃO DE VENDAS - " + ptMonths[Integer.parseInt(month)].toUpperCase());
            ExcelUtils3Month.createSheetTitle(workBook, vdvcSheet , "PREVISÃO DE VEICULOS DE DEMONSTRAÇÃO / CORTESIA - " + ptMonths[Integer.parseInt(month)].toUpperCase());


            //TODO change 1L constant for CONSTANT
            if (brand.equalsIgnoreCase("Toyota")) {
                this.setDataSourceContext(1L);
            } else if (brand.equalsIgnoreCase("Lexus")) {
                this.setDataSourceContext(2L);
            }

//			------ MATRICULAS ----- MATRICULAS ----- MATRICULAS ----- MATRICULAS ----- MATRICULAS ----- MATRICULAS ----- MATRICULAS
            this.createPlates(vecRetailerDealers, vecPVMCarModel, workBook, platesSheet, year, month, brand, isCAMember);


//------ VENDAS ----- VENDAS ----- VENDAS ----- VENDAS ----- VENDAS ----- VENDAS ----- VENDAS ---------------------------------------------
            this.createVendas(vecRetailerDealers, vecPVMCarModel, workBook, salesSheet, year, month, brand, isCAMember);

// VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC

            this.createVDVC(vecRetailerDealers, vecPVMCarModel, vdvcSheet, year, month, brand);

//VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC   VDVC   -  VDVC


        } catch (SCErrorException e) {
            System.out.println(">>>>>>>>>>" + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(">>>>>>>>>>" + e.getMessage());
        }
        return workBook;
    }


    private void createPlates( List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel,
                              HSSFWorkbook workBook, HSSFSheet platesSheet, String year,String month, String brand, boolean isCAMember) throws SQLException, SCErrorException {

        List<Map<String,Object>> rs = new ArrayList<>();
        List<Map<String,Object>> rs2 = new ArrayList<>();
        String platesSql = PVMReportQueries3Month.getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", false);
        String frotasPlatesSql = PVMReportQueries3Month.getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", true);
        String totalPlatesPassageirosSql = PVMReportQueries3Month.getTotalPlatesSql( vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
        String totalFrotasPlatesPassageirosSql = PVMReportQueries3Month.getTotalPlatesFrotasSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
        String totalPlatesComerciaisSql = PVMReportQueries3Month.getTotalPlatesSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
        String totalFrotasPlatesComerciaisSql = PVMReportQueries3Month.getTotalPlatesFrotasSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(platesSql);
        int currentRow = 1;


        //Inicio Constru��o Folha Matriculas PASSAGEIROS
        List<String> metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());
        createSheetPlatesSubTitle(currentRow, workBook, platesSheet, metaData, month, "PASSAGEIROS");
        currentRow = 4;
        int topRow = currentRow+1;

        int i=0;
        for (Map<String,Object> rsCurrent:rs) {
            createPlatesLine(workBook, platesSheet, rsCurrent, currentRow++, metaData.size());
            i++;
        }


        //Linha SubTotal
        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            //Linha Total
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, true, false);
        } else {
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, false, false);

            rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasPlatesSql);

            int countMetadataColumns =  metaData.size();
            int countExecutions = 0;
            List cellsToChange= null;

            for (Map<String,Object>  rs2Current :rs2) {
                if(countExecutions>0){
                    List currCells = createPlatesLine(workBook, platesSheet, rs2Current, currentRow++, metaData.size());
                    for(int cnt = 0; cnt< currCells.size(); cnt++){
                        HSSFCell currCell = (HSSFCell) currCells.get(cnt);
                        HSSFCell cellToChange = (HSSFCell) cellsToChange.get(cnt);
                        double curValue = currCell.getNumericCellValue() + cellToChange.getNumericCellValue();
                        cellToChange.setCellValue(curValue);
                        currCell.setCellValue(0);
                    }
                } else {
                    cellsToChange = createPlatesLine(workBook, platesSheet, rs2Current, currentRow++, metaData.size());
                }
                countExecutions ++;
            }
            platesSheet.addMergedRegion(new CellRangeAddress(currentRow-2,  currentRow-1, countMetadataColumns-5, countMetadataColumns-5));
            platesSheet.addMergedRegion(new CellRangeAddress(currentRow-2,  currentRow-1, countMetadataColumns-3, countMetadataColumns-3));
            platesSheet.addMergedRegion(new CellRangeAddress(currentRow-2,  currentRow-1, countMetadataColumns-1, countMetadataColumns-1));

            //Linha Total
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), true, false, false );
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, false, true);
        }

        currentRow += 2;

        vecPVMCarModel 		= getPVMCarModels(brand, year, month, "COMERCIAIS");

        platesSql = PVMReportQueries3Month.getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS", false);
        frotasPlatesSql = PVMReportQueries3Month.getPlatesSql(vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS", true);

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(platesSql);

        //Inicio Constru��o Folha Matriculas COMERCIAIS
        metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        ExcelUtils3Month.createSheetPlatesSubTitle(currentRow, workBook, platesSheet, metaData, month, "COMERCIAIS");
        currentRow += 3;
        topRow = currentRow+1;
        for (Map<String, Object> rsCurrent: rs) {
            createPlatesLine(workBook, platesSheet, rsCurrent, currentRow++, metaData.size());
        }

        //Linha SubTotal
        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            //Linha Total
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, true, false);
        } else {

            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, false, false);

            rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasPlatesSql);

            int countExecutions = 0;
            List cellsToChange= null;
            for (Map<String,Object>  rs2Current :rs2) {
                if(countExecutions>0){
                    List currCells = createPlatesLine(workBook, platesSheet, rs2Current, currentRow++, metaData.size());
                    for(int cnt = 0; cnt< currCells.size(); cnt++){
                        HSSFCell currCell = (HSSFCell) currCells.get(cnt);
                        HSSFCell cellToChange = (HSSFCell) cellsToChange.get(cnt);
                        double curValue = currCell.getNumericCellValue() + cellToChange.getNumericCellValue();
                        cellToChange.setCellValue(curValue);
                        currCell.setCellValue(0);
                    }
                } else {
                    cellsToChange = createPlatesLine(workBook, platesSheet, rs2Current, currentRow++, metaData.size());
                }
                countExecutions ++;
            }

            platesSheet.addMergedRegion(new CellRangeAddress(currentRow-2,  currentRow-1, metaData.size()-5, metaData.size()-5));
            platesSheet.addMergedRegion(new CellRangeAddress(currentRow-2,  currentRow-1, metaData.size()-3, metaData.size()-3));
            platesSheet.addMergedRegion(new CellRangeAddress(currentRow-2,  currentRow-1, metaData.size()-1, metaData.size()-1));

            //Linha Total
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), true, false, false);
            createPlatesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, false, true);
        }

        currentRow += 3;

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalPlatesPassageirosSql);

        rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalPlatesComerciaisSql);

        metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        //Inicio Constru��o Folha Matriculas TOTAIS
        createSheetTotalPlatesSalesSubTitle("PLATES", currentRow, workBook, platesSheet, month);

        currentRow += 3;
        topRow = currentRow+1;
        for(int ik=0;ik<rs.size();ik++) {
            Map<String, Object> currentRs = rs.get(ik);
            Map<String, Object> currentRs2 = rs2.get(ik);
            createTotalPlatesSalesLine(workBook, platesSheet, currentRs, currentRs2, currentRow++, metaData.size());
        }

        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            //Linha Total
            createPlatesSalesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(),
                    false, true, false);
        } else {
            //Linha SubTotal
            createPlatesSalesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, false, false);

            rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalFrotasPlatesPassageirosSql);

            rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalFrotasPlatesComerciaisSql);

            metaData = new ArrayList<>();
            if(!rs.isEmpty())
                metaData =  new ArrayList<>(rs.get(0).keySet());

            for(int ik=0;ik<rs.size();ik++) {
                Map<String, Object> currentRs = rs.get(ik);
                Map<String, Object> currentRs2 = rs2.get(ik);
                createTotalPlatesSalesLine(workBook, platesSheet, currentRs, currentRs2, currentRow++, metaData.size());
            }

            //Linha Total
            createPlatesSalesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), true, false, false);
            createPlatesSalesTotalLine(topRow, workBook, platesSheet, currentRow++, metaData.size(), false, false, true);
        }

    }


    private void createVendas(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel,
                              HSSFWorkbook workBook, HSSFSheet salesSheet, String year,String month, String brand, boolean isCAMember) throws SCErrorException, SQLException {

        String salesSql = PVMReportQueries3Month.getSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", false);
        String frotasSalesSql = PVMReportQueries3Month.getSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS", true);
        String totalSalesPassageirosSql = PVMReportQueries3Month.getTotalSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
        String totalSalesComerciaisSql = PVMReportQueries3Month.getTotalSalesSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
        String totalFrotasSalesComerciaisSql = PVMReportQueries3Month.getTotalSalesFrotasSql(vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
        String totalFrotasSalesPassageirosSql = PVMReportQueries3Month.getTotalSalesFrotasSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");

        List<Map<String,Object>> rs = new ArrayList<>();
        List<Map<String,Object>> rs2 = new ArrayList<>();
        //Descri��o Asterisco


        int currentRow = 1;

        //Inicio Constru��o Folha Vendas

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(salesSql);
        List<String> metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        createSheetSalesSubTitle(currentRow, workBook, salesSheet, metaData, month, "PASSAGEIROS");
        currentRow = 4;
        int topRow = currentRow+1;

        for(Map<String,Object> currentRs: rs) {
            createSalesLine(workBook, salesSheet, currentRs, currentRow++, metaData.size());
        }
//			Linha SubTotal
        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, true, false);
        } else {
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, false, false);
            rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasSalesSql);

            for(Map<String,Object> currentRs2: rs2) {
                createSalesLine(workBook, salesSheet, currentRs2, currentRow++, metaData.size());
            }

            //Linha Total
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), true, false, false);
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, false, true);
        }

        currentRow += 2;

        vecPVMCarModel 		= getPVMCarModels(brand, year, month, "COMERCIAIS");

        salesSql = PVMReportQueries3Month.getSalesSql(vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS", false);
        frotasSalesSql = PVMReportQueries3Month.getSalesSql( vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS", true);

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(salesSql);


        //Inicio Constru��o Folha Vendas COMERCIAIS
        metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());
        createSheetSalesSubTitle(currentRow, workBook, salesSheet, metaData, month, "COMERCIAIS");
        currentRow += 3;
        topRow = currentRow+1;
        for(Map<String,Object> currentRs: rs) {
            createSalesLine(workBook, salesSheet, currentRs, currentRow++, metaData.size());
        }


        //Linha SubTotal
        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, true, false);
        } else {
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, false, false);
            rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(frotasSalesSql);

            for(Map<String,Object> currentRs2: rs2) {
                createSalesLine(workBook, salesSheet, currentRs2, currentRow++,  metaData.size());
            }

            //Linha Total
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), true, false, false);
            createSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, false, true);
        }

        currentRow += 3;

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalSalesPassageirosSql);
        rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalSalesComerciaisSql);

        metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        //Inicio Constru��o Folha Vendas TOTAIS
        createSheetTotalPlatesSalesSubTitle("SALES", currentRow, workBook, salesSheet, month);

        currentRow += 3;
        topRow = currentRow+1;

        for(int ik=0;ik<rs.size();ik++) {
            Map<String, Object> currentRs = rs.get(ik);
            Map<String, Object> currentRs2 = rs2.get(ik);
            createTotalPlatesSalesLine(workBook, salesSheet, currentRs, currentRs2, currentRow++, metaData.size());
        }

        if(isCAMember && brand.equalsIgnoreCase("Toyota")) {
            createPlatesSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, true, false);
        } else {
            createPlatesSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, false, false);

            rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalFrotasSalesPassageirosSql);
            rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalFrotasSalesComerciaisSql);
            metaData = new ArrayList<>();
            if(!rs.isEmpty())
                metaData =  new ArrayList<>(rs.get(0).keySet());

            for(int ik=0;ik<rs.size();ik++) {
                Map<String, Object> currentRs = rs.get(ik);
                Map<String, Object> currentRs2 = rs2.get(ik);
                createTotalPlatesSalesLine(workBook, salesSheet, currentRs, currentRs2, currentRow++, metaData.size());
            }

            //Linha Total
            createPlatesSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), true, false, false);
            createPlatesSalesTotalLine(topRow, workBook, salesSheet, currentRow++, metaData.size(), false, false, true);
        }

    }

    private void createVDVC(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel,
                            HSSFSheet vdvcSheet, String year,String month, String brand) throws SCErrorException, SQLException {

        String vdvcSql = PVMReportQueries3Month.getVDVCSql(vecRetailerDealers, vecPVMCarModel, year, month, "PASSAGEIROS");
        String totalVDVCPassageirosSql = PVMReportQueries3Month.getTotalVDVCSql(vecRetailerDealers, year, month, "PASSAGEIROS");
        String totalVDVCComerciaisSql = PVMReportQueries3Month.getTotalVDVCSql( vecRetailerDealers, year, month, "COMERCIAIS");

        List<Map<String,Object>> rs = new ArrayList<>();
        List<Map<String,Object>> rs2 = new ArrayList<>();
        int currentRow = 1;

        //Inicio Constru��o Folha VDVC
        //Inicio Constru��o Folha VDVC PASSAGEIROS

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(vdvcSql);
        List<String> metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        createSheetVDVCSubTitle(currentRow, vdvcSheet, metaData, month, "PASSAGEIROS");
        currentRow = 4;
        int topRow = currentRow+1;
        for (Map<String, Object> currentRs: rs) {
            createVDVCLine(vdvcSheet, currentRs, currentRow++, metaData.size());
        }

        createVDVCTotalLine(topRow, vdvcSheet, currentRow++, metaData.size());

        //Inicio Constru��o Folha VDVC COMERCIAIS

        currentRow += 2;

        vecPVMCarModel 		= getPVMCarModels(brand, year, month, "COMERCIAIS");

        vdvcSql = PVMReportQueries3Month.getVDVCSql(vecRetailerDealers, vecPVMCarModel, year, month, "COMERCIAIS");
        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(vdvcSql);
        metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        createSheetVDVCSubTitle(currentRow, vdvcSheet, metaData, month, "COMERCIAIS");
        currentRow += 3;
        topRow = currentRow+1;
        for (Map<String, Object> currentRs: rs) {
            createVDVCLine(vdvcSheet, currentRs, currentRow++, metaData.size());
        }
        createVDVCTotalLine(topRow, vdvcSheet, currentRow++, metaData.size());

        //Inicio Constru��o Folha VDVC TOTAIS
        currentRow += 3;

        rs = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalVDVCPassageirosSql);
        rs2 = pvmMonthlyReportRepository.getPVMMonthReportPlates(totalVDVCComerciaisSql);

        metaData = new ArrayList<>();
        if(!rs.isEmpty())
            metaData =  new ArrayList<>(rs.get(0).keySet());

        createSheetTotalVDVCSubTitle(currentRow, vdvcSheet, month);

        currentRow += 3;
        topRow = currentRow+1;

        for(int ik=0;ik<rs.size();ik++) {
            Map<String, Object> currentRs = rs.get(ik);
            Map<String, Object> currentRs2 = rs2.get(ik);
            createTotalVDVCLine(vdvcSheet, currentRs, currentRs2, currentRow++, metaData.size());
        }

        createTotalTotalVDVCLine(topRow, vdvcSheet, currentRow++);

        for(int c=3; c<26; c++){
            vdvcSheet.setColumnWidth((short)c, (short)1500);
        }
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

    private List<String[]> getPVMCarModels(String brand, String year, String month, String typeModel) throws SCErrorException, SQLException {
        if (brand.equalsIgnoreCase("Lexus"))
            this.setDataSourceContext(2L);

        List<String[]> vecResult = new ArrayList<>();

        month = ("00" + month);
        month = month.substring(month.length()-2);

        String dtFrom = year+"-"+month+"-01";

        List<PVMCarmodel> pvmCarmodels = pvmCarmodelRepository.getPVMCarModels(typeModel, dtFrom);

        for (PVMCarmodel carModel: pvmCarmodels) {
            String type = carModel.getType();
            String id = String.valueOf(carModel.getId());
            String name = carModel.getName();
            vecResult.add(new String[]{type, "M1-" + name, id});
            vecResult.add(new String[]{type, "D1-" + name, id});
            vecResult.add(new String[]{type, "M2-" + name, id});
            vecResult.add(new String[]{type, "D2-" + name, id});
            vecResult.add(new String[]{type, "M3-" + name, id});
        }
        return vecResult;
    }

}
