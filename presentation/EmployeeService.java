package presentation;

import business.ApplicationException;
import business.EmployeeTransactionScript;
import dataaccess.PersistenceException;

public class EmployeeService {

	
	public EmployeeTransactionScript employeeTS;
	
	public EmployeeService(EmployeeTransactionScript employeeTS) {
		this.employeeTS = employeeTS;
	}
	
	
	public void addNewEmployeeToSale (int id, int saleId, int departmentId) throws PersistenceException, ApplicationException{
		employeeTS.addNewEmployeeToSale(id, saleId, departmentId);
	}

	
}
