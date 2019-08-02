package com.pos.reports.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.aes.protection.CipherUtils;
import com.pos.common.RemoveNull;
import com.pos.inventory.model.Inventory;
import com.pos.pointOfSale.model.PointOfSale;
import com.pos.reports.model.ReportModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Repository("ReportDao")
public class ReportDaoImpl implements ReportDao{

	private SimpleJdbcCall simpleJdbcCall;
	private JdbcTemplate jdbcTemplate;
	CipherUtils oCipherUtils = new CipherUtils();
	RemoveNull oRemoveNull = new RemoveNull();
	
	@Autowired
	private DataSource dataSource;

	
	
	public ReportModel searchReportDetails(ReportModel reportModel) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("select JESPER_NAME from report_list where report_code = :reportCode");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("reportCode", reportModel.getReportCode());
			
			//System.out.println("reportCode " + reportModel.getReportCode());
			
			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {
				reportModel.setJesperName(String.valueOf(row.get("JESPER_NAME")));
				
				//System.out.println("JESPER_NAME " + reportModel.getJesperName());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return reportModel;
	}

	public JRDataSource orderFinalizeData(ReportModel reportModel) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<ReportModel> list = new ArrayList<ReportModel>();
		try {
			NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
			StringBuilder sBuilder = new StringBuilder();

			sBuilder.append(" SELECT ORDER_ID, ORDER_NO, ");
			sBuilder.append(" (SELECT ITEM_NAME FROM L_ITEM WHERE ITEM_ID = OM.ITEM_ID) ITEM_NAME, ");
			sBuilder.append(" QUANTITY, ");
			sBuilder.append(" ITEM_PRICE, ");
			sBuilder.append(" UPDATE_BY, ");
			sBuilder.append(" UPDATE_DATE ");
			sBuilder.append(" FROM ORDER_MANAGEMENT OM ");
			sBuilder.append(" WHERE OM.ORDER_ID = :orderId ");

			MapSqlParameterSource paramSource = new MapSqlParameterSource();

			paramSource.addValue("orderId", oCipherUtils.decrypt(reportModel.getEncOrderId()));
			

			// System.out.println(sBuilder);

			// paramSource.addValue("regNo", reportModel.getRegistrationNo());

			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {
				ReportModel oReportModel = new ReportModel();
				// oReportModel.setEncRegisterId(oCipherUtils.encrypt(String.valueOf(row.get("REGISTER_ID"))));
				oReportModel.setOrderId(oRemoveNull.nullRemove(String.valueOf(row.get("ORDER_ID"))));
				oReportModel.setOrderNo(oRemoveNull.nullRemove(String.valueOf(row.get("ORDER_NO"))));
				oReportModel.setItemName(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_NAME"))));
				oReportModel.setQuantity(oRemoveNull.nullRemove(String.valueOf(row.get("QUANTITY"))));
				oReportModel.setItemPrice(Integer.parseInt(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_PRICE")))));
				oReportModel.setUpdateBy(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_BY"))));
				oReportModel.setUpdateDate(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_DATE"))));
				
				//System.out.println("price " + reportModel.getItemPrice());
				
				list.add(oReportModel);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JRDataSource ds = new JRBeanCollectionDataSource(list);

		return ds;
	}

	public JRDataSource inventoryReportData(ReportModel reportModel) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<ReportModel> list = new ArrayList<ReportModel>();
		try {
			NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
			StringBuilder sBuilder = new StringBuilder();

			sBuilder.append(" SELECT INVENTORY_ID, ");
			sBuilder.append(" (SELECT INVENTORY_TYPE_NAME FROM L_INVENTORY_TYPE WHERE INVENTORY_TYPE_ID = IM.INVENTORY_TYPE_ID) INVENTORY_TYPE_NAME, ");
			sBuilder.append(" (SELECT PRODUCT_NAME FROM L_PRODUCT WHERE PRODUCT_ID = IM.PRODUCT_ID) PRODUCT_NAME, ");
			sBuilder.append(" QUANTITY, ");
			sBuilder.append(" (SELECT UNIT_NAME FROM L_UNIT WHERE UNIT_ID = IM.UNIT_ID) UNIT_NAME, ");
			sBuilder.append(" PRICE, ");
			//sBuilder.append(" (SELECT KNOWN_AS FROM EMPLOYEE WHERE EMPLOYEE_ID = IM.UPDATE_BY) EMPLOYEE_NAME, ");
			sBuilder.append(" PROGRAM_YN, ");
			sBuilder.append(" PROGRAM_ID, ");
			sBuilder.append(" UPDATE_BY, ");
			sBuilder.append(" TO_CHAR(UPDATE_DATE,'DD/MM/YYYY') UPDATE_DATE ");
			sBuilder.append(" FROM INVENTORY_MASTER IM ");
			sBuilder.append(" WHERE TRUNC(UPDATE_DATE) BETWEEN TO_DATE(:fromDate,'DD/MM/YYYY') AND TO_DATE(:toDate,'DD/MM/YYYY') ");

			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			
			if (reportModel.getInventoryTypeId() != null && reportModel.getInventoryTypeId() != "") {
				sBuilder.append(" AND INVENTORY_TYPE_ID = :inventoryTypeId ");
				paramSource.addValue("inventoryTypeId", reportModel.getInventoryTypeId());
			} else if (reportModel.getProductId() != null && reportModel.getProductId() != "") {
				sBuilder.append(" AND PRODUCT_ID = :productId ");
				paramSource.addValue("productId", reportModel.getProductId());
			}

			

			paramSource.addValue("fromDate", reportModel.getFromDate());
			paramSource.addValue("toDate", reportModel.getToDate());
			

			// System.out.println(sBuilder);

			// paramSource.addValue("regNo", reportModel.getRegistrationNo());

			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {
				ReportModel oReportModel = new ReportModel();
				// oReportModel.setEncRegisterId(oCipherUtils.encrypt(String.valueOf(row.get("REGISTER_ID"))));
				oReportModel.setInventoryId(oRemoveNull.nullRemove(String.valueOf(row.get("INVENTORY_ID"))));
				oReportModel.setInventoryTypeName(oRemoveNull.nullRemove(String.valueOf(row.get("INVENTORY_TYPE_NAME"))));
				oReportModel.setProductName(oRemoveNull.nullRemove(String.valueOf(row.get("PRODUCT_NAME"))));
				oReportModel.setQuantity(oRemoveNull.nullRemove(String.valueOf(row.get("QUANTITY"))));
				oReportModel.setUnitName(oRemoveNull.nullRemove(String.valueOf(row.get("UNIT_NAME"))));
				oReportModel.setItemPrice(Integer.parseInt(oRemoveNull.nullRemove(String.valueOf(row.get("PRICE")))));
				oReportModel.setEmployeeName(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_BY"))));
				oReportModel.setUpdateBy(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_BY"))));
				oReportModel.setUpdateDate(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_DATE"))));
				
				//System.out.println("price " + reportModel.getItemPrice());
				
				list.add(oReportModel);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JRDataSource ds = new JRBeanCollectionDataSource(list);

		return ds;
	}

	public JRDataSource posReportData(ReportModel reportModel) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<ReportModel> list = new ArrayList<ReportModel>();
		try {
			NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
			StringBuilder sBuilder = new StringBuilder();

			sBuilder.append(" SELECT ORDER_ID, ");
			sBuilder.append(" (SELECT ITEM_NAME FROM L_ITEM WHERE ITEM_ID = OM.ITEM_ID) ITEM_NAME, ");
			sBuilder.append(" QUANTITY, ");
			sBuilder.append(" ITEM_PRICE, ");
			sBuilder.append(" UPDATE_BY, ");
			sBuilder.append(" TO_CHAR(UPDATE_DATE,'DD/MM/YYYY') UPDATE_DATE ");
			sBuilder.append(" FROM ORDER_MANAGEMENT OM ");
			sBuilder.append(" WHERE TRUNC(UPDATE_DATE) BETWEEN TO_DATE(:fromDate,'DD/MM/YYYY') AND TO_DATE(:toDate,'DD/MM/YYYY') ");

			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			
			if (reportModel.getItemId() != null && reportModel.getItemId() != "") {
				sBuilder.append(" AND ITEM_ID = :itemId ");
				paramSource.addValue("itemId", reportModel.getItemId());
			}

			sBuilder.append(" ORDER BY ORDER_ID ");

			paramSource.addValue("fromDate", reportModel.getFromDate());
			paramSource.addValue("toDate", reportModel.getToDate());
			

			// System.out.println(sBuilder);

			// paramSource.addValue("regNo", reportModel.getRegistrationNo());

			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {
				ReportModel oReportModel = new ReportModel();
				oReportModel.setOrderId(oRemoveNull.nullRemove(String.valueOf(row.get("ORDER_ID"))));
				oReportModel.setItemName(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_NAME"))));
				oReportModel.setQuantity(oRemoveNull.nullRemove(String.valueOf(row.get("QUANTITY"))));
				oReportModel.setItemPrice(Integer.parseInt(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_PRICE")))));
				oReportModel.setUpdateBy(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_BY"))));
				oReportModel.setUpdateDate(oRemoveNull.nullRemove(String.valueOf(row.get("UPDATE_DATE"))));
				
				//System.out.println("price " + reportModel.getItemPrice());
				
				list.add(oReportModel);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JRDataSource ds = new JRBeanCollectionDataSource(list);

		return ds;
	}

	public String viewPdfReport(String fileName, HttpServletRequest request, HttpServletResponse response,
			Map parameters) throws JRException, IOException, SQLException, ClassNotFoundException, Exception {
		String sResult = "";
		HttpSession session = request.getSession();
		ServletContext servletContext = session.getServletContext();
		
		//System.out.println("path " + servletContext.getRealPath(fileName));
		
		File reportFile = new File(servletContext.getRealPath(fileName));
		Connection conn = dataSource.getConnection();

		//System.out.println("........Coon......" + conn);

		/*
		 * Connection conn = DriverManager.getConnection(
		 * "jdbc:oracle:thin:@192.168.78.79:1521:orcl", "btcl", "btcl");
		 */

		System.out.println("parameters..................       " + parameters.size());

		byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conn);
		if (bytes.length > 380) {
			sResult = "Found";
			
			//System.out.println(bytes.length);
		}

		// if (conn != null) try { conn.close(); } catch (Exception e)
		// {e.printStackTrace();}
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();

		return sResult;
	}

	public JRDataSource costAnalysisReportData(ReportModel reportModel) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<ReportModel> list = new ArrayList<ReportModel>();
		ReportModel oReportModel = new ReportModel();
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_DATE_WISE_COST_ANALYSIS");
			Map<String, Object> inParamMap = new HashMap<String, Object>();

			inParamMap.put("P_COST_ANALYSIS_REPORT_TYPE", reportModel.getCostAnalysisReportType());
			inParamMap.put("P_YEAR", reportModel.getYear());
			inParamMap.put("P_MONTH", reportModel.getMonth());
			inParamMap.put("P_FROM_DATE", reportModel.getFromDate());
			inParamMap.put("P_TO_DATE", reportModel.getToDate());

			// inParamMap.put("P_UPDATE_DATE", student.getUpdateDate());

			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			
			oReportModel.setEmployeeSalary(oRemoveNull.nullRemove((String) outParamMap.get("P_EMPLOYEE_SALARY")));
			oReportModel.setInventoryCost(oRemoveNull.nullRemove((String) outParamMap.get("P_INVENTORY")));
			oReportModel.setDiscountCost(oRemoveNull.nullRemove((String) outParamMap.get("P_DISCOUNT")));
			oReportModel.setWastageCost(oRemoveNull.nullRemove((String) outParamMap.get("P_WASTAGE")));
			oReportModel.setTotalCost(oRemoveNull.nullRemove((String) outParamMap.get("P_TOTAL_COST")));
			oReportModel.setTotalOrderPrice(oRemoveNull.nullRemove((String) outParamMap.get("P_ORDER_PRICE")));
			oReportModel.setEmployeeSalary(oRemoveNull.nullRemove((String) outParamMap.get("P_EMPLOYEE_SALARY")));
			oReportModel.setNetIncome(oRemoveNull.nullRemove((String) outParamMap.get("P_NET_INCOME")));
			
			//System.out.println("P_EMPLOYEE_SALARY " + oReportModel.getEmployeeSalary());
			//System.out.println("P_TOTAL_COST " + oReportModel.getTotalCost());
			//System.out.println("P_ORDER_PRICE " + oReportModel.getTotalOrderPrice());
			
			list.add(oReportModel);
			
		} catch (Exception ex) {
			oReportModel.setMessage("Error Printing Report !!!");
			oReportModel.setmCode("0000");
			ex.printStackTrace();
		}
		JRDataSource ds = new JRBeanCollectionDataSource(list);

		return ds;
	}
}
