package com.pos.pointOfSale.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.pos.pointOfSale.model.OrderModel;
import com.pos.pointOfSale.model.PointOfSale;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@Repository("PointOfSaleDao")
public class PointOfSaleDaoImpl implements PointOfSaleDao {

	private SimpleJdbcCall simpleJdbcCall;
	private JdbcTemplate jdbcTemplate;
	CipherUtils oCipherUtils = new CipherUtils();
	RemoveNull oRemoveNull = new RemoveNull();
	@Autowired
	private DataSource dataSource;

	public PointOfSale saveItem(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_ITEM_SAVE");
			Map<String, Object> inParamMap = new HashMap<String, Object>();

			inParamMap.put("P_ITEM_ID", oCipherUtils.decrypt(pointOfSale.getEncItemId()));
			inParamMap.put("P_ITEM_NAME", pointOfSale.getItemName());
			inParamMap.put("P_ITEM_PRICE", pointOfSale.getItemPrice());
			inParamMap.put("P_UPDATE_BY", pointOfSale.getUpdateBy());
			inParamMap.put("P_UPDATE_DATE", pointOfSale.getUpdateDate());

			// inParamMap.put("P_UPDATE_DATE", student.getUpdateDate());

			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			oPointOfSale.setMessage((String) outParamMap.get("P_MESSAGE"));
			oPointOfSale.setmCode((String) outParamMap.get("P_MESSAGE_CODE"));
			// System.out.println("studentId " + oStudent.getStudentId());
		} catch (Exception ex) {
			oPointOfSale.setMessage("Error Saving Record !!!");
			oPointOfSale.setmCode("0000");
			ex.printStackTrace();
		}
		return oPointOfSale;
	}

	public List<PointOfSale> getItemList(PointOfSale pointOfSale) throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oItemList = new ArrayList<PointOfSale>();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("SELECT ITEM_ID, ITEM_CODE, ITEM_NAME, ITEM_PRICE ");
		sBuilder.append(" FROM L_ITEM ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		//System.out.println("bbb" + sBuilder);

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale.setEncItemId(oCipherUtils.encrypt(String.valueOf(row.get("ITEM_ID"))));
			oPointOfSale.setItemId(String.valueOf(row.get("ITEM_ID")));
			oPointOfSale.setItemCode(String.valueOf(row.get("ITEM_CODE")));
			oPointOfSale.setItemName(String.valueOf(row.get("ITEM_NAME")));
			oPointOfSale.setItemPrice(String.valueOf(row.get("ITEM_PRICE")));
			oItemList.add(oPointOfSale);

			// System.out.println("abc"+
			// oCipherUtils.encrypt(String.valueOf(row.get("EMPLOYEE_ID"))));

		}

		return oItemList;
	}

	public PointOfSale saveItemConfig(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_ITEM_CONFIG_SAVE");
			Map<String, Object> inParamMap = new HashMap<String, Object>();

			inParamMap.put("P_ENC_ITEM_ID", oCipherUtils.decrypt(oRemoveNull.nullRemove(pointOfSale.getEncItemId())));
			inParamMap.put("P_ITEM_ID", pointOfSale.getItemId());
			inParamMap.put("P_PRODUCT_ID", pointOfSale.getProductId());
			inParamMap.put("P_QUANTITY", pointOfSale.getQuantity());
			inParamMap.put("P_UNIT_ID", pointOfSale.getUnitId());
			inParamMap.put("P_UPDATE_BY", pointOfSale.getUpdateBy());
			inParamMap.put("P_UPDATE_DATE", pointOfSale.getUpdateDate());
			
			//System.out.println("pointOfSale.getEncItemId() =  " + oCipherUtils.decrypt(pointOfSale.getEncItemId()));


			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			oPointOfSale.setMessage((String) outParamMap.get("P_MESSAGE"));
			oPointOfSale.setmCode((String) outParamMap.get("P_MESSAGE_CODE"));
			
		} catch (Exception ex) {
			oPointOfSale.setMessage("Error Saving Record !!!");
			oPointOfSale.setmCode("0000");
			ex.printStackTrace();
		}
		return oPointOfSale;
	}

	public List<PointOfSale> getItemConfigList(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oItemConfigList = new ArrayList<PointOfSale>();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(" SELECT ITEM_ID, ");
		sBuilder.append(" M.PRODUCT_ID, ");
		sBuilder.append(" (SELECT PRODUCT_NAME FROM L_PRODUCT WHERE PRODUCT_ID = M.PRODUCT_ID) PRODUCT_NAME, ");
		sBuilder.append(" QUANTITY, ");
		sBuilder.append(" M.UNIT_ID, ");
		sBuilder.append(" (SELECT UNIT_NAME FROM L_UNIT WHERE UNIT_ID = M.UNIT_ID) UNIT_NAME ");
		sBuilder.append(" FROM ITEM_CONFIG M ");
		sBuilder.append(" WHERE ITEM_ID = :itemId ");
		// sBuilder.append(" AND TRUNC(UPDATE_DATE) =
		// TO_DATE(:updateDate,'DD/MM/YYYY') ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("itemId", pointOfSale.getItemId());
		// paramSource.addValue("updateDate", pointOfSale.getUpdateDate());

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale.setEncItemId(oCipherUtils.encrypt(String.valueOf(row.get("ITEM_ID"))));
			oPointOfSale.setItemId(String.valueOf(row.get("ITEM_ID")));
			oPointOfSale.setProductId(String.valueOf(row.get("PRODUCT_ID")));
			oPointOfSale.setProductName(String.valueOf(row.get("PRODUCT_NAME")));
			oPointOfSale.setQuantity(String.valueOf(row.get("QUANTITY")));
			oPointOfSale.setUnitId(String.valueOf(row.get("UNIT_ID")));
			oPointOfSale.setUnitName(String.valueOf(row.get("UNIT_NAME")));
			oItemConfigList.add(oPointOfSale);

			// System.out.println("abc"+
			// oCipherUtils.encrypt(String.valueOf(row.get("EMPLOYEE_ID"))));

		}

		return oItemConfigList;
	}

	public PointOfSale getOrderInfo(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oOrderInfo = new PointOfSale();
		ArrayList list = new ArrayList();
		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();

			CallableStatement oCallStmt = conn.prepareCall("{call PRO_ORDER_INFO(?,?,?,?,?,?,?)}");
			oCallStmt.setString(1, pointOfSale.getItemId());
			oCallStmt.setString(2, pointOfSale.getQuantity());
			oCallStmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			oCallStmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			oCallStmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			//oCallStmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			oCallStmt.registerOutParameter(6, java.sql.Types.VARCHAR);
			oCallStmt.registerOutParameter(7, java.sql.Types.VARCHAR);

			oCallStmt.execute();
			

			oOrderInfo.setmCode(oCallStmt.getString(3));
			oOrderInfo.setMessage(oCallStmt.getString(4));
			oOrderInfo.setItemName(oCallStmt.getString(5));
			oOrderInfo.setItemPrice(oCallStmt.getString(6));
			oOrderInfo.setSubTotal(oCallStmt.getString(7));
			//oOrderInfo.setSubTotal(oCallStmt.getString(5));

/*			System.out.println("3 " + oOrderInfo.getItemName());
			System.out.println("4 " + oOrderInfo.getItemPrice());
			System.out.println("5 " + oOrderInfo.getSubTotal());*/

			/*
			 * try { simpleJdbcCall = new
			 * SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_ORDER_INFO");
			 * Map<String, Object> inParamMap = new HashMap<String, Object>();
			 * 
			 * inParamMap.put("P_ITEM_ID", pointOfSale.getItemId());
			 * inParamMap.put("P_QUANTITY",
			 * Integer.parseInt(pointOfSale.getQuantity()));
			 * 
			 * // inParamMap.put("P_UPDATE_DATE", student.getUpdateDate());
			 * 
			 * Map<String, Object> outParamMap = simpleJdbcCall.execute(new
			 * MapSqlParameterSource().addValues(inParamMap));
			 * 
			 * oOrderInfo.setMessage((String) outParamMap.get("P_MESSAGE"));
			 * oOrderInfo.setmCode((String) outParamMap.get("P_MESSAGE_CODE"));
			 * oOrderInfo.setOrderPrice(()(outParamMap.get("P_ORDER_PRICE")));
			 * 
			 * //oReceiptInfo.setAmountWithoutVat(Integer.toString(oCallStmt.
			 * getInt(42)));
			 * 
			 * System.out.println("price " + oOrderInfo.getItemPrice());
			 */
		} catch (Exception ex) {
			oOrderInfo.setMessage("Error Creating Order !");
			oOrderInfo.setmCode("0000");
			ex.printStackTrace();
		}

		return oOrderInfo;
	}

	public PointOfSale saveOrder(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);

		PointOfSale oPointOfSale = new PointOfSale();

		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			// CallableStatement oCallStmt = null;
			// String sResult = "";

			/*****************************
			 * Receipt Data Manipulation
			 ********************************/

			StructDescriptor orderRecDescriptor = StructDescriptor.createDescriptor("ORDERARRAY", conn);
			ArrayDescriptor orderArrayDescriptor = ArrayDescriptor.createDescriptor("ORDERARRAYTAB", conn);

			List<OrderModel> orderList = pointOfSale.getOrderModelList();

			int orderArraySize = 0;

			if (orderList != null && orderList.size() > 0) {
				for (OrderModel oOrderModel : orderList) {
					if ((oOrderModel.getItemId() != null && oOrderModel.getItemId().length() > 0)
							&& (oOrderModel.getQuantity() != null && oOrderModel.getQuantity().length() > 0)
							&& (oOrderModel.getItemPrice() != null && oOrderModel.getItemPrice().length() > 0)
							&& (oOrderModel.getSubTotal() != null && oOrderModel.getSubTotal().length() > 0)) {
						orderArraySize = orderArraySize + 1;
						
						 System.out.println("getEncItemOrderId() DE :  "+ oCipherUtils.decrypt(oRemoveNull.nullRemove(oOrderModel.getEncItemOrderId())));
					}
				}
			}
			
			if (orderArraySize == 0) {
				oPointOfSale.setMessage("No Order Found !!");
				oPointOfSale.setmCode("0000");
				return oPointOfSale;
			}
			
			if (orderArraySize > 10) {
				oPointOfSale.setMessage("Maximum 10 Orders Can Be Generated In One Receipt !!");
				oPointOfSale.setmCode("0000");
				return oPointOfSale;
			}

			//System.out.println("orderArraySize = " + orderArraySize);

			Object[] order_array_of_records = new Object[orderArraySize];

			Object[] order_java_record_array = new Object[5];
			int i = 0;

			if (null != orderList && orderList.size() > 0) {
				for (OrderModel oOrderModel : orderList) {
					if ((oOrderModel.getItemId() != null && oOrderModel.getItemId().length() > 0)
							&& (oOrderModel.getQuantity() != null && oOrderModel.getQuantity().length() > 0)
							&& (oOrderModel.getItemPrice() != null && oOrderModel.getItemPrice().length() > 0)
							&& (oOrderModel.getSubTotal() != null && oOrderModel.getSubTotal().length() > 0)) {

						// We create a record by filling an array and then casting it into an Oracle type
						order_java_record_array[0] = oCipherUtils.decrypt(oRemoveNull.nullRemove(oOrderModel.getEncItemOrderId()));
						order_java_record_array[1] = oOrderModel.getItemId();
						order_java_record_array[2] = oOrderModel.getQuantity();
						order_java_record_array[3] = oOrderModel.getItemPrice();
						order_java_record_array[4] = oOrderModel.getSubTotal();
						
						 System.out.println("oOrderModel.getEncItemOrderId() DECR :  "+ oCipherUtils.decrypt(oRemoveNull.nullRemove(oOrderModel.getEncItemOrderId())));

						// cast the java arrays into the Oracle record type for the input record
						STRUCT oracle_record = new STRUCT(orderRecDescriptor, conn, order_java_record_array);
						// store the oracle_record into the array of records which will be passed to the function
						order_array_of_records[i] = oracle_record;
						i = i + 1;
						/*
						 * System.out.println("java_record_array[0] " +
						 * jointOwner_java_record_array[0]); System.out.println(
						 * "java_record_array[1] " +
						 * jointOwner_java_record_array[1]); System.out.println(
						 * "java_record_array[2] " +
						 * jointOwner_java_record_array[2]); System.out.println(
						 * "java_record_array[3] " +
						 * jointOwner_java_record_array[3]); System.out.println(
						 * "java_record_array[4] " +
						 * jointOwner_java_record_array[4]); System.out.println(
						 * "java_record_array[5] " +
						 * jointOwner_java_record_array[5]); System.out.println(
						 * "java_record_array[6] " +
						 * jointOwner_java_record_array[6]); System.out.println(
						 * "java_record_array[7] " +
						 * jointOwner_java_record_array[7]);
						 */
						// System.out.println("java_record_array[0] " +
						// java_record_array[0]);
					}

				}
			}

			ARRAY order_array = new ARRAY(orderArrayDescriptor, conn, order_array_of_records);

			CallableStatement oCallStmt = dataSource.getConnection().prepareCall(
					"{call PRO_ORDER_SAVE(?,?,?,?,?,?,?)}");
			
			oCallStmt.setObject(1, oCipherUtils.decrypt(oRemoveNull.nullRemove(pointOfSale.getEncOrderId())));
			oCallStmt.setObject(2,order_array);
			oCallStmt.setObject(3, pointOfSale.getTableNo());
			oCallStmt.setObject(4, pointOfSale.getEmployeeId());
			oCallStmt.setObject(5, pointOfSale.getUpdateBy());
			oCallStmt.registerOutParameter(6, java.sql.Types.CHAR);
			oCallStmt.registerOutParameter(7, java.sql.Types.CHAR);

			oCallStmt.execute();

			oPointOfSale.setmCode(oCallStmt.getString(6));
			oPointOfSale.setMessage(oCallStmt.getString(7));

		} catch (Exception e) {
			e.printStackTrace();
			oPointOfSale.setMessage("ERROR SAVING RECORD !!!" + e);
			oPointOfSale.setmCode("0000");
		}
		return oPointOfSale;
	}
	
	

	public PointOfSale getOrderPrice(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);

		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append("SELECT SUM (ITEM_PRICE) ITEM_PRICE FROM ORDER_MANAGEMENT WHERE ORDER_ID = :orderId ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue("orderId", pointOfSale.getOrderId());

		 //System.out.println(sBuilder);

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			oPointOfSale.setItemPrice(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_PRICE"))));
		}
		return oPointOfSale;
	}

	public PointOfSale saveDiscount(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_DISCOUNT_SAVE");
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("P_DISCOUNT_ID", oCipherUtils.decrypt(pointOfSale.getEncDiscountId()));
			inParamMap.put("P_ORDER_NO", pointOfSale.getOrderId());
			inParamMap.put("P_PRICE_AMOUNT", pointOfSale.getOrderPrice());
			inParamMap.put("P_DISCOUNT_AMOUNT", pointOfSale.getDiscountAmount());
			inParamMap.put("P_UPDATE_BY", pointOfSale.getUpdateBy());
			inParamMap.put("P_REFERENCE_BY_ID", pointOfSale.getReferenceById());

			// inParamMap.put("P_UPDATE_DATE", student.getUpdateDate());

			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			oPointOfSale.setMessage((String) outParamMap.get("P_MESSAGE"));
			oPointOfSale.setmCode((String) outParamMap.get("P_MESSAGE_CODE"));
			// System.out.println("studentId " + oStudent.getStudentId());
		} catch (Exception ex) {
			oPointOfSale.setMessage("Error Saving Record !!!");
			oPointOfSale.setmCode("0000");
			ex.printStackTrace();
		}
		return oPointOfSale;
	}

	public List<PointOfSale> getDiscountList(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oDiscountList = new ArrayList<PointOfSale>();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(" SELECT DISCOUNT_ID, ");
		sBuilder.append(" ORDER_ID, ");
		sBuilder.append(" PRICE_AMOUNT, ");
		sBuilder.append(" DISCOUNT_AMOUNT, ");
		sBuilder.append(" UPDATE_BY, ");
		sBuilder.append(" TO_CHAR(UPDATE_DATE,'DD/MM/YYYY') UPDATE_DATE, ");
		sBuilder.append(" REFERENCE_BY, ");
		sBuilder.append(" (SELECT EMPLOYEE_NAME FROM EMPLOYEE WHERE EMPLOYEE_ID = D.REFERENCE_BY) REFERENCE_BY_NAME ");
		sBuilder.append(" FROM DISCOUNT D ");
		sBuilder.append(" WHERE TRUNC (UPDATE_DATE) BETWEEN TO_DATE (:fromDate, 'dd/mm/yyyy') AND  TO_DATE (:toDate, 'dd/mm/yyyy') ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fromDate", pointOfSale.getFromDate());
		paramSource.addValue("toDate", pointOfSale.getToDate());
		
		if (pointOfSale.getOrderId() != null && pointOfSale.getOrderId().length() > 0) {
			sBuilder.append(" AND ORDER_ID = :orderId ");
			paramSource.addValue("orderId", pointOfSale.getOrderId());
		} 
		if(pointOfSale.getReferenceById() != null && pointOfSale.getReferenceById().length() > 0) {
			sBuilder.append(" AND REFERENCE_BY = :referenceById ");
			paramSource.addValue("referenceById", pointOfSale.getReferenceById());
		} 
		//System.out.println(sBuilder);
		//System.out.println("productId " + inventory.getProductId());

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);
		
		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale.setEncDiscountId(oRemoveNull.nullRemove(oCipherUtils.encrypt((String.valueOf(row.get("DISCOUNT_ID"))))));
			oPointOfSale.setDiscountId(oRemoveNull.nullRemove((String.valueOf(row.get("DISCOUNT_ID")))));
			oPointOfSale.setEncOrderId(oRemoveNull.nullRemove(oCipherUtils.encrypt((String.valueOf(row.get("ORDER_ID"))))));
			oPointOfSale.setOrderId(oRemoveNull.nullRemove((String.valueOf(row.get("ORDER_ID")))));
			oPointOfSale.setOrderPrice(oRemoveNull.nullRemove((String.valueOf(row.get("PRICE_AMOUNT")))));
			oPointOfSale.setDiscountAmount(oRemoveNull.nullRemove((String.valueOf(row.get("DISCOUNT_AMOUNT")))));
			oPointOfSale.setUpdateDate(oRemoveNull.nullRemove((String.valueOf(row.get("UPDATE_DATE")))));
			oPointOfSale.setUpdateBy(oRemoveNull.nullRemove((String.valueOf(row.get("UPDATE_BY")))));
			oPointOfSale.setReferenceByName(oRemoveNull.nullRemove((String.valueOf(row.get("REFERENCE_BY_NAME")))));
			oDiscountList.add(oPointOfSale);
		}
		return oDiscountList;
	}

	public PointOfSale getDiscountInfo(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);

		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append(" SELECT DISCOUNT_ID, ");
		sBuilder.append(" ORDER_ID, ");
		sBuilder.append(" PRICE_AMOUNT, ");
		sBuilder.append(" DISCOUNT_AMOUNT, ");
		sBuilder.append(" REFERENCE_BY ");
		sBuilder.append(" FROM DISCOUNT D ");
		sBuilder.append(" WHERE DISCOUNT_ID = :encDiscountId ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("encDiscountId", oCipherUtils.decrypt(pointOfSale.getEncDiscountId()));

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			oPointOfSale.setEncDiscountId(oCipherUtils.encrypt(String.valueOf(row.get("DISCOUNT_ID"))));
			oPointOfSale.setDiscountId(oRemoveNull.nullRemove(String.valueOf(row.get("DISCOUNT_ID"))));
			oPointOfSale.setOrderId(oRemoveNull.nullRemove(String.valueOf(row.get("ORDER_ID"))));
			oPointOfSale.setOrderPrice(oRemoveNull.nullRemove(String.valueOf(row.get("PRICE_AMOUNT"))));
			oPointOfSale.setDiscountAmount(oRemoveNull.nullRemove(String.valueOf(row.get("DISCOUNT_AMOUNT"))));
			oPointOfSale.setReferenceById(oRemoveNull.nullRemove(String.valueOf(row.get("REFERENCE_BY"))));
		}

		return oPointOfSale;
	}

	public List<PointOfSale> getOrderInfoList(PointOfSale pointOfSale) throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oOrderInfoList = new ArrayList<PointOfSale>();
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		StringBuilder sBuilder = new StringBuilder();

		try {
			sBuilder.append(" SELECT ITEM_ID, ");
			sBuilder.append(" (SELECT ITEM_NAME FROM L_ITEM WHERE ITEM_ID = OM.ITEM_ID) ITEM_NAME, ");
			sBuilder.append(" QUANTITY, ");
			sBuilder.append(" ITEM_PRICE ");
			sBuilder.append(" FROM ORDER_MANAGEMENT OM ");
			sBuilder.append(" WHERE ORDER_ID = :encOrderId ");

			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("encOrderId", oCipherUtils.decrypt(pointOfSale.getEncOrderId()));
			// System.out.println(sBuilder);

			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);
			

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {

				PointOfSale oPointOfSale = new PointOfSale();
				oPointOfSale.setItemName(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_NAME"))));
				oPointOfSale.setQuantity(oRemoveNull.nullRemove(String.valueOf(row.get("QUANTITY"))));
				oPointOfSale.setItemPrice(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_PRICE"))));
				// oRegistrationRemoveNull.removeNullRegistration(oRegistration);
				oOrderInfoList.add(oPointOfSale);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oOrderInfoList;
	}

	public List<PointOfSale> getOrderList(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oOrderList = new ArrayList<PointOfSale>();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(" SELECT ORDER_ID, ");
		sBuilder.append(" ITEM_ID, ");
		sBuilder.append(" (SELECT ITEM_NAME FROM L_ITEM WHERE ITEM_ID = OM.ITEM_ID) ITEM_NAME, ");
		sBuilder.append(" QUANTITY, ");
		sBuilder.append(" ITEM_PRICE, ");
		sBuilder.append(" UPDATE_BY, ");
		sBuilder.append(" TO_CHAR(UPDATE_DATE,'DD/MM/YYYY') UPDATE_DATE ");
		sBuilder.append(" FROM ORDER_MANAGEMENT OM ");
		sBuilder.append(" WHERE TRUNC (UPDATE_DATE) BETWEEN TO_DATE (:fromDate, 'dd/mm/yyyy') AND  TO_DATE (:toDate, 'dd/mm/yyyy') ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("fromDate", pointOfSale.getFromDate());
		paramSource.addValue("toDate", pointOfSale.getToDate());
		
		if (pointOfSale.getOrderId() != null && pointOfSale.getOrderId().length() > 0) {
			sBuilder.append(" AND ORDER_ID = :orderId ");
			paramSource.addValue("orderId", pointOfSale.getOrderId());
		}
		if (pointOfSale.getUpdateBy() != null && pointOfSale.getUpdateBy().length() > 0) {
			sBuilder.append(" AND UPDATE_BY = :updateBy ");
			paramSource.addValue("updateBy", pointOfSale.getUpdateBy());
		}
		//System.out.println(sBuilder);
		//System.out.println("productId " + inventory.getProductId());

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);
		
		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale.setEncOrderId(oRemoveNull.nullRemove(oCipherUtils.encrypt((String.valueOf(row.get("ORDER_ID"))))));
			oPointOfSale.setOrderId(oRemoveNull.nullRemove((String.valueOf(row.get("ORDER_ID")))));
			oPointOfSale.setItemName(oRemoveNull.nullRemove((String.valueOf(row.get("ITEM_NAME")))));
			oPointOfSale.setQuantity(oRemoveNull.nullRemove((String.valueOf(row.get("QUANTITY")))));
			oPointOfSale.setOrderPrice(oRemoveNull.nullRemove((String.valueOf(row.get("ITEM_PRICE")))));
			oPointOfSale.setUpdateDate(oRemoveNull.nullRemove((String.valueOf(row.get("UPDATE_DATE")))));
			oPointOfSale.setUpdateBy(oRemoveNull.nullRemove((String.valueOf(row.get("UPDATE_BY")))));
			oOrderList.add(oPointOfSale);
		}
		return oOrderList;
	}

	public List<PointOfSale> getPendingOrderList(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oOrderList = new ArrayList<PointOfSale>();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(" SELECT ORDER_ID, ");
		sBuilder.append(" ORDER_NO, ");
		sBuilder.append(" TABLE_NO, ");
		sBuilder.append(" TO_CHAR(ORDER_DATE,'DD/MM/YYYY HH12:MI:SS AM') ORDER_DATE, ");
		sBuilder.append(" WAITER_ID, ");
		sBuilder.append(" (SELECT KNOWN_AS FROM EMPLOYEE WHERE EMPLOYEE_ID = OM.WAITER_ID) WAITER_NAME, ");
		sBuilder.append(" FINALIZED_YN, ");
		sBuilder.append(" COMPLETED_YN, ");
		sBuilder.append(" (SELECT KNOWN_AS FROM EMPLOYEE WHERE EMPLOYEE_ID = OM.UPDATE_BY) UPDATE_BY ");
		sBuilder.append(" FROM ORDER_MANAGEMENT OM ");
		sBuilder.append(" WHERE  (COMPLETED_YN = 'N' OR COMPLETED_YN IS NULL) ");
		sBuilder.append(" AND (ORDER_CANCELED_YN = 'N' OR ORDER_CANCELED_YN IS NULL) ");
		sBuilder.append(" ORDER BY ORDER_DATE DESC ");
		
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();

		System.out.println("order list : " + sBuilder);
		//System.out.println("productId " + inventory.getProductId());

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);
		
		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			PointOfSale oPointOfSale = new PointOfSale();
			oPointOfSale.setEncOrderId(oRemoveNull.nullRemove(oCipherUtils.encrypt((String.valueOf(row.get("ORDER_ID"))))));
			oPointOfSale.setOrderNo(oRemoveNull.nullRemove((String.valueOf(row.get("ORDER_NO")))));
			oPointOfSale.setTableNo(oRemoveNull.nullRemove((String.valueOf(row.get("TABLE_NO")))));
			oPointOfSale.setOrderDate(oRemoveNull.nullRemove((String.valueOf(row.get("ORDER_DATE")))));
			oPointOfSale.setEmployeeId(oRemoveNull.nullRemove((String.valueOf(row.get("WAITER_ID")))));
			oPointOfSale.setEmployeeName(oRemoveNull.nullRemove((String.valueOf(row.get("WAITER_NAME")))));
			oPointOfSale.setFinalizedYn(oRemoveNull.nullRemove((String.valueOf(row.get("FINALIZED_YN")))));
			oPointOfSale.setCompletedYn(oRemoveNull.nullRemove((String.valueOf(row.get("COMPLETED_YN")))));
			oPointOfSale.setUpdateBy(oRemoveNull.nullRemove((String.valueOf(row.get("UPDATE_BY")))));
			oOrderList.add(oPointOfSale);
		}
		return oOrderList;
	}

	public List<PointOfSale> getPendingOrderInfoList(PointOfSale pointOfSale) throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oPendingOrderInfoList = new ArrayList<PointOfSale>();
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		StringBuilder sBuilder = new StringBuilder();

		try {
			
			sBuilder.append(" SELECT ITEM_ORDER_ID, ");
			sBuilder.append(" ORDER_ID, ");
			sBuilder.append(" ITEM_ID, ");
			sBuilder.append(" (SELECT ITEM_NAME FROM L_ITEM WHERE ITEM_ID = IO.ITEM_ID) ITEM_NAME, ");
			sBuilder.append(" QUANTITY, ");
			sBuilder.append(" ITEM_PRICE, ");
			sBuilder.append(" SUB_TOTAL, ");
			sBuilder.append(" UPDATE_BY, ");
			sBuilder.append(" UPDATE_DATE ");
			sBuilder.append(" FROM ITEM_ORDER IO ");
			sBuilder.append(" WHERE ORDER_ID = :encOrderId ");

			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("encOrderId", oCipherUtils.decrypt(pointOfSale.getEncOrderId()));
			// System.out.println(sBuilder);

			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);
			

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {

				PointOfSale oPointOfSale = new PointOfSale();
				oPointOfSale.setEncItemOrderId(oCipherUtils.encrypt(String.valueOf(row.get("ITEM_ORDER_ID"))));
				oPointOfSale.setItemOrderId(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_ORDER_ID"))));
				oPointOfSale.setEncItemId(oCipherUtils.encrypt(String.valueOf(row.get("ITEM_ID"))));
				oPointOfSale.setItemId(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_ID"))));
				oPointOfSale.setItemName(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_NAME"))));
				oPointOfSale.setQuantity(oRemoveNull.nullRemove(String.valueOf(row.get("QUANTITY"))));
				oPointOfSale.setItemPrice(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_PRICE"))));
				oPointOfSale.setSubTotal(oRemoveNull.nullRemove(String.valueOf(row.get("SUB_TOTAL"))));
				// oRegistrationRemoveNull.removeNullRegistration(oRegistration);
				oPendingOrderInfoList.add(oPointOfSale);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oPendingOrderInfoList;
	}

	public PointOfSale cancelOrder(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		
		try {
		/*	simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_CANCEL_ORDER");
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("P_ORDER_ID", oCipherUtils.decrypt(pointOfSale.getEncOrderId()));

			// inParamMap.put("P_UPDATE_DATE", student.getUpdateDate());

			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			oPointOfSale.setMessage((String) outParamMap.get("P_MESSAGE"));
			oPointOfSale.setmCode((String) outParamMap.get("P_MESSAGE_CODE"));
			// System.out.println("studentId " + oStudent.getStudentId());
			 */
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append(" UPDATE ORDER_MANAGEMENT SET ORDER_CANCELED_YN = 'Y'  ");
			sBuilder.append(" WHERE ORDER_ID = :orderId  ");
			
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("orderId", oCipherUtils.decrypt(oRemoveNull.nullRemove(pointOfSale.getEncOrderId())));
			
			npjt.update(sBuilder.toString(), paramSource);
			
			oPointOfSale.setMessage("Order Caceled Successfully");
			oPointOfSale.setmCode("1111");
				
				} catch (Exception ex) {
			oPointOfSale.setMessage("Error Canceling Record !!!");
			oPointOfSale.setmCode("0000");
			ex.printStackTrace();
		}
		return oPointOfSale;
	}

	public List<PointOfSale> getOrderEditList(PointOfSale pointOfSale) throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<PointOfSale> oOrderEditList = new ArrayList<PointOfSale>();
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);
		StringBuilder sBuilder = new StringBuilder();

		try {
			sBuilder.append(" SELECT ITEM_ORDER_ID, ");
			sBuilder.append(" ORDER_ID, ");
			sBuilder.append(" ITEM_ID, ");
			sBuilder.append(" (SELECT ITEM_NAME FROM L_ITEM WHERE ITEM_ID = IO.ITEM_ID) ITEM_NAME, ");
			sBuilder.append(" QUANTITY, ");
			sBuilder.append(" ITEM_PRICE, ");
			sBuilder.append(" SUB_TOTAL, ");
			sBuilder.append(" UPDATE_BY, ");
			sBuilder.append(" UPDATE_DATE ");
			sBuilder.append(" FROM ITEM_ORDER IO ");
			sBuilder.append(" WHERE ORDER_ID = :encOrderId ");

			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue("encOrderId", oCipherUtils.decrypt(pointOfSale.getEncOrderId()));
			// System.out.println(sBuilder);
			sBuilder.append(" ORDER BY ITEM_ORDER_ID ASC ");

			List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);
			

			for (@SuppressWarnings("rawtypes")
			Map row : rows) {

				PointOfSale oPointOfSale = new PointOfSale();
				oPointOfSale.setEncItemOrderId(oCipherUtils.encrypt(String.valueOf(row.get("ITEM_ORDER_ID"))));
				oPointOfSale.setEncOrderId(oCipherUtils.encrypt(String.valueOf(row.get("ORDER_ID"))));
				oPointOfSale.setOrderId(oRemoveNull.nullRemove(String.valueOf(row.get("ORDER_ID"))));
				oPointOfSale.setTableNo(oRemoveNull.nullRemove(String.valueOf(row.get("TABLE_NO"))));
				oPointOfSale.setItemId(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_ID"))));
				oPointOfSale.setItemName(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_NAME"))));
				oPointOfSale.setQuantity(oRemoveNull.nullRemove(String.valueOf(row.get("QUANTITY"))));
				oPointOfSale.setItemPrice(oRemoveNull.nullRemove(String.valueOf(row.get("ITEM_PRICE"))));
				oPointOfSale.setSubTotal(oRemoveNull.nullRemove(String.valueOf(row.get("SUB_TOTAL"))));
				// oRegistrationRemoveNull.removeNullRegistration(oRegistration);
				oOrderEditList.add(oPointOfSale);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oOrderEditList;
	}

	public PointOfSale getOrderTotalAmount(String encOrderId) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);

		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append(" SELECT SUM (SUB_TOTAL) ORDER_TOTAL_AMOUNT FROM ITEM_ORDER WHERE ORDER_ID = :encOrderId ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("encOrderId", oCipherUtils.decrypt(encOrderId));

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			oPointOfSale.setOrderTotalAmount(oRemoveNull.nullRemove(String.valueOf(row.get("ORDER_TOTAL_AMOUNT"))));
		}

		return oPointOfSale;
	}

	public PointOfSale saveOrderFinalize(PointOfSale pointOfSale) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		PointOfSale oPointOfSale = new PointOfSale();
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_ORDER_FINALIZE");
			Map<String, Object> inParamMap = new HashMap<String, Object>();

			inParamMap.put("P_ORDER_ID", oCipherUtils.decrypt(oRemoveNull.nullRemove(pointOfSale.getEncOrderId())));
			inParamMap.put("P_PAYABLE_AMOUNT", pointOfSale.getOrderTotalAmount());
			inParamMap.put("P_CASH_PAY_AMOUNT", pointOfSale.getCashPayAmount());
			inParamMap.put("P_CARD_PAY_AMOUNT", pointOfSale.getCardPayAmount());
			inParamMap.put("P_DISCOUNT_AMOUNT", pointOfSale.getDiscountAmount());
			inParamMap.put("P_NET_PAY_AMOUNT", pointOfSale.getNetPayableAmount());
			inParamMap.put("P_UPDATE_BY", pointOfSale.getUpdateBy());
			
			System.out.println("ORDER iD : " + oCipherUtils.decrypt(oRemoveNull.nullRemove(pointOfSale.getEncOrderId())));

			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			oPointOfSale.setMessage((String) outParamMap.get("P_MESSAGE"));
			oPointOfSale.setmCode((String) outParamMap.get("P_MESSAGE_CODE"));
			// System.out.println("studentId " + oStudent.getStudentId());
		} catch (Exception ex) {
			oPointOfSale.setMessage("Error Saving Record !!!");
			oPointOfSale.setmCode("0000");
			ex.printStackTrace();
		}
		return oPointOfSale;
	}

}
