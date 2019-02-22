package business;
import dataaccess.PersistenceException;
import dataaccess.RecordNotFoundException;
import dataaccess.SaleRowDataGateway;

import java.util.Date;

import dataaccess.CustomerRowDataGateway;
import dataaccess.EmployeeRowDataGateway;

public class EmployeeTransactionScript {

	/**
	 * Adds a new employee to the sale. It checks that there is no other employee in the system
	 * with the same ID.
	 * 
	 * @param id The ID number of the employee
	 * @param saleId The ID of the sale
	 * @param departmentID the ID of the department responsible for the employee
	 * @throws PersistenceException 
	 * @throws ApplicationException 
	 */
	public void addNewEmployeeToSale (int id, int saleId, int departmentId) throws PersistenceException, ApplicationException{

		//checks if the sale is OPEN
		if (SaleRowDataGateway.find(saleId).getStatus() != SaleStatus.OPEN){
			throw new ApplicationException(" The sale is closed, cannot insert employee in a closed sale! ");
		}
		//checks if the sale has already another employee assigned to it
		// if != 0 then there is a employee on the case
		if (SaleRowDataGateway.find(saleId).getEmployeeId() != 0){
			throw new ApplicationException(" There is already another employee associated with the current sale! ");
		}

		try {
			//gets the employee given its current id
			EmployeeRowDataGateway employee = new EmployeeRowDataGateway(id, departmentId);
			CustomerRowDataGateway customer = CustomerRowDataGateway.find(saleId);
			SaleRowDataGateway sale = new SaleRowDataGateway(customer.getCustomerId(), new Date(), employee.getId());
			
			//adds employee to the sale
			SaleRowDataGateway.find(saleId);
			employee.setEmployeeStatus(EmployeeStatus.IN_SALE);
			employee.insert();
			sale.insert();
					
		} catch (RecordNotFoundException e) {
			throw new ApplicationException(" There is no Employee with the given ID ", e);
		}
	}
	
	
	
}
