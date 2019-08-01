package com.pos.accounts.service;

import java.util.List;

import com.pos.accounts.model.EmployeeMonthlyConsumption;
import com.pos.accounts.model.SalaryProcessModel;
import com.pos.accounts.model.SupplierInfo;


public interface AccountsService {
	
	public List<EmployeeMonthlyConsumption> getEmpMonthlyConsumption(EmployeeMonthlyConsumption employeeMonthlyConsumption);
	
	public EmployeeMonthlyConsumption getConsumeInfo(EmployeeMonthlyConsumption employeeMonthlyConsumption);
	
	public EmployeeMonthlyConsumption empMonthlyConsumptionSave(EmployeeMonthlyConsumption employeeMonthlyConsumption);

	public List<SalaryProcessModel> salaryProcessSetup(SalaryProcessModel salaryProcessModel);
	
	public List<SalaryProcessModel> salaryProcessedSearch(SalaryProcessModel salaryProcessModel);
	
	public SalaryProcessModel grossSalary(SalaryProcessModel salaryProcessModel);
	
	public SalaryProcessModel salaryProcessSave(SalaryProcessModel salaryProcessModel);
	
	public SalaryProcessModel salaryProcessFinalize(SalaryProcessModel salaryProcessModel);
	
	public List<SupplierInfo> supplierList(SupplierInfo supplierInfo);
	
	public SupplierInfo saveSupplier(SupplierInfo supplierInfo);
	
	public SupplierInfo getSupplierInfo(SupplierInfo supplierInfo);
	
	
}
