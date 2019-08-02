package com.pos.reports.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pos.reports.model.ReportModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

public interface ReportDao {
	
	public ReportModel searchReportDetails(ReportModel reportModel);
	
	public JRDataSource orderFinalizeData(ReportModel reportModel);
	
	public JRDataSource inventoryReportData(ReportModel reportModel);
	
	public JRDataSource posReportData(ReportModel reportModel);
	
	public JRDataSource costAnalysisReportData(ReportModel reportModel);
	
	public String viewPdfReport(String fileName, HttpServletRequest request, HttpServletResponse response,
			Map parameters) throws JRException, IOException, SQLException, ClassNotFoundException, Exception;

}