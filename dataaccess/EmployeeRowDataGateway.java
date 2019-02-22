/**
 * 
 */
package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import business.EmployeeStatus;

/**
 * @author fc44308
 *
 */
public class EmployeeRowDataGateway {

	// employee atributes

	private int id;

	private int departmentId;

	private String employeeStatus;

	//private boolean manager + method

	/**
	 * Constructor, creates a new Employee gives its id and departmentId
	 * 
	 */

	public EmployeeRowDataGateway(int id, int departmentId){
		this.id = id;
		this.departmentId = departmentId;
		setEmployeeStatus(EmployeeStatus.AVAILABLE);
	}

	// Constants for EmplyeeStatus

	private static final String IN_SALE = "I";
	private static final String AVAILABLE = "A";

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId){
		this.departmentId = departmentId;
	}

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(EmployeeStatus employeeStatus) {
		this.employeeStatus = employeeStatus == EmployeeStatus.AVAILABLE ? AVAILABLE : IN_SALE;
	}


	private static final String INSERT_EMPLOYEE_SQL = 
			"insert into employee (id, departmentId, employeeStatus)"
					+ "values (DEFAULT,?,?)";


	public void insert() throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_EMPLOYEE_SQL)){
			statement.setInt(1, departmentId);
			statement.setString(2, AVAILABLE);

			statement.executeUpdate();

			try(ResultSet rs = statement.getGeneratedKeys()){
				rs.next();
				id = rs.getInt(1);
			}
		}catch(SQLException e){
			throw new PersistenceException("error in insert employee" , e);
		}
	}


	private static final String	GET_EMPLOYEE_BY_ID_SQL =
			"select * " +
					"from employee " +
					"where id = ?";


	public static EmployeeRowDataGateway find (int id) throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_EMPLOYEE_BY_ID_SQL)) {			
			// set statement arguments
			statement.setInt(1, id);
			// executes SQL
			try (ResultSet rs = statement.executeQuery()) {
				// creates a new customer from the data retrieved from the database
				return loadEmployee(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error getting a customer by its id", e);
		}
	}
	private static EmployeeRowDataGateway loadEmployee(ResultSet rs) throws RecordNotFoundException {
		try {
			rs.next();
			EmployeeRowDataGateway newEmployee = new EmployeeRowDataGateway(rs.getInt("employee_id"),rs.getInt("department_id"));

			newEmployee.id = rs.getInt("id");
			return newEmployee;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Customer does not exist", e);
		}
	}
}

