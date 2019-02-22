package presentation;

import business.ApplicationException;
import business.DepartementTransactionScript;
import dataaccess.PersistenceException;

public class DepartmentService {

		private DepartementTransactionScript departmentTS;
		
		public DepartmentService(DepartementTransactionScript departmentTS) {
			this.departmentTS = departmentTS;
			
		}
		
		public void NewDepartment(String name, int phoneNumber) throws ApplicationException {
			
			departmentTS.NewDepartment( name, phoneNumber);
		}

		public void SwitchEmployee(int id, int employeeId) throws PersistenceException, ApplicationException{
			departmentTS.SwitchEmployee(id, employeeId);
			
		}

}

