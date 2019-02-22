package SaleSys;

import presentation.CustomerService;
import presentation.DepartmentService;
import presentation.EmployeeService;
import presentation.SaleService;
import business.ApplicationException;
import business.CustomerTransactionScripts;
import business.SaleTransactionScripts;
import dataaccess.DataSource;
import dataaccess.PersistenceException;

public class SaleSys {

	private CustomerService customerService;
	private SaleService saleService;
	private DepartmentService departmentService;
	private EmployeeService employeeService;

	public void run() throws ApplicationException {
		// Connects to the database
		try {
			DataSource.INSTANCE.connect("jdbc:derby:data/derby/cssdb;create=false", "SaleSys", "");
			customerService = new CustomerService(new CustomerTransactionScripts());
			saleService = new SaleService(new SaleTransactionScripts());
		} catch (PersistenceException e) {
			throw new ApplicationException("Error connecting database", e);
		}
	}
	
	public void stopRun()  {
		// Closes the database connection
		DataSource.INSTANCE.close();
	}

	public CustomerService getCustomerService() {
		return customerService;
	}
	
	public SaleService getSaleService() {
		return saleService;
	}
	
	public DepartmentService getDepartmentService(){
		return departmentService;
	}
	
	public EmployeeService getEmployeeService(){
		return employeeService;
	}
}
