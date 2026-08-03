/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.sales.report.service;

import com.storrity.storrity.sales.report.dto.DailySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.MonthlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.QuarterlySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.SalesReportQueryParams;
import com.storrity.storrity.sales.report.dto.WeeklySalesSummaryDto;
import com.storrity.storrity.sales.report.dto.YearlySalesSummaryDto;
import com.storrity.storrity.sales.report.reposiitory.SalesReportRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Seun Owa
 */
@Service
public class SalesReportServiceImpl implements SalesReportService{
    
    private final SalesReportRepository salesReportRepository;

    @Autowired
    public SalesReportServiceImpl(SalesReportRepository salesReportRepository) {
        this.salesReportRepository = salesReportRepository;
    }

    @Override
    public List<DailySalesSummaryDto> dailySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.dailySalesSummary(params);
    }

    @Override
    public List<WeeklySalesSummaryDto> weeklySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.weeklySalesSummary(params);
    }

    @Override
    public List<MonthlySalesSummaryDto> monthlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.monthlySalesSummary(params);
    }

    @Override
    public List<QuarterlySalesSummaryDto> quarterlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.quarterlySalesSummary(params);
    }

    @Override
    public List<YearlySalesSummaryDto> yearlySalesSummary(SalesReportQueryParams params) {
        return salesReportRepository.yearlySalesSummary(params);
    }

}
