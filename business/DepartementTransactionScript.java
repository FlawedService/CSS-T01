package business;

import dataaccess.DepartmentEmployeeRowDataGateway;
import dataaccess.DepartmentRowDataGateway;
import dataaccess.EmployeeRowDataGateway;
import dataaccess.PersistenceException;

public class DepartementTransactionScript {

	/**
	 * Creates a new department for a given company identified by its id
	 * 
	 * @param id The ID number of the company the department belongs to
	 * @return The id of the new department.
	 * @throws ApplicationException When the department does not exist or 
	 * when an unexpected SQL error occurs.
	 */
	public void NewDepartment(String name, int phoneNumber)
			throws ApplicationException{
		try {
			DepartmentRowDataGateway newDepart = new DepartmentRowDataGateway(name, phoneNumber);
			newDepart.insert();
			
		} catch (Exception e) {
			throw new ApplicationException( "Internal error adding new Department! ", e);
		}
	}

	/**
	 * Switches a Employee from one Department to another
	 * 
	 * @param id The ID number of the company the department belongs to
	 * @param employeeId the ID of the employee to be Department manager
	 * @throws PersistenceException 
	 * @throws ApplicationException 
	 */
	public void SwitchEmployee(int id, int employeeId) throws PersistenceException, ApplicationException{
		try {
			DepartmentEmployeeRowDataGateway employee = new DepartmentEmployeeRowDataGateway(employeeId, id);
			employee.UpdateEmployeeDepart();
			
		} catch (PersistenceException e) {
			throw new ApplicationException(" Internal error Switching employees! ", e);

		}
	}

	public void addEmployeeToDepartment(EmployeeRowDataGateway employee, DepartmentRowDataGateway department) throws PersistenceException {
		DepartmentEmployeeRowDataGateway departEmplo = new DepartmentEmployeeRowDataGateway(employee.getId(), department.getId());
		departEmplo.insert();
	}

	/*
	/**
	 * Selects a Department Manager for that Department
	 * 
	 * @param id The ID number of the company the department belongs to
	 * @param employeeId the ID of the employee to be Department manager
	 * @return The id of the new Manager.
	 * @throws ApplicationException 

	public int DepartmentManager (int id, int employeeId) throws ApplicationException{
		try{

			if(id == 0 || employeeId == 0)
				throw new ApplicationException("departamento nao valid0");

			DepartmentManagerRowDataGateway manager = new DepartmentManagerRowDataGateway(id, employeeId);

			manager.insert();
		}catch (PersistenceException e) {
			throw new ApplicationException("Error", e);
		}
		return employeeId;
	}*/
}
