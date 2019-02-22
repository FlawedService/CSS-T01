package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * An in-memory representation of a row gateway for the products composing a sale.	
 * 
 * @author 
 * @version 
 *
 */
public class DepartmentEmployeeRowDataGateway {
	
	// 1. Department employee attributes 
	
	private int id;
	private int departmentId;
	private int employeeId;

	
	// 2. constructor

	public DepartmentEmployeeRowDataGateway(int employeeid, int departmentId) {
		this.departmentId = departmentId;
		this.employeeId = employeeid;
		
		
	}
	// 3. getters and setters
	public int getEmployeeId() {
		return employeeId;
	}
	public int departmentId(){
		return departmentId;
	}

	public int getId() {
		return id;
	}
	
	
	
	/**
	 * The insert employee in a department SQL statement
	 */
	private static final String INSERT_EMPLOYEE_DEPARTMENT_SQL = 
			"insert into saleproduct(id,employee_id,department_id) " +
			"values(DEFAULT,?,?)"; 

	
	/**
	 * The select the employee of a department by department Id SQL statement
	 */
	private static final String GET_EMPLOYEE_DEPARTMENT_SQL = 
			"select id, employee_id, department_id"+
			"from departmentemployee "+
			"where department_id = ?";		

	

	
	// 4. interaction with the repository (a relational database in this simple example)

	private static final String UPDATE_EMPLOYEE_DEPART = 
			"update department_employee " + 
					"set department_id = ? " +
					"where id = ? ";
	
	
	public void UpdateEmployeeDepart() throws PersistenceException{
		try(PreparedStatement statement = DataSource.INSTANCE.prepare(UPDATE_EMPLOYEE_DEPART)){
			statement.setInt(1, departmentId);
			statement.setInt(2, id);
			statement.executeUpdate();
			
		}catch (SQLException e) {
			throw new PersistenceException( "Error on Update Department Employee! ", e);
		}
		
	}

	/**
	 * Inserts the record in the DeparmentEmployee
	 * @throws SQLException 
	 */
	public void insert () throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_EMPLOYEE_DEPARTMENT_SQL)){
			
			statement.setInt(1, employeeId);
			statement.setInt(2, departmentId);
		
			statement.executeUpdate();
			try(ResultSet rs = statement.getGeneratedKeys()){
				rs.next();
				id = rs.getInt(1);
			}
		}catch(SQLException e){
			throw new PersistenceException("Internal Error", e);
		}
	}
	
	
	/**
	 * Gets the employees of a department by its department id 
	 * 
	 * @param saleId The sale id to get the products of
	 * @return The set of products that compose the sale
	 * @throws PersistenceException When there is an error obtaining the
	 *         information from the database.
	 */
	public static Set<DepartmentEmployeeRowDataGateway> findDepartmentEMployees (int departmentId) throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(GET_EMPLOYEE_DEPARTMENT_SQL)){
			
			statement.setInt(1, departmentId);
			
			try(ResultSet rs = statement.executeQuery()){
				return loademployees(rs);
			}
		}catch(SQLException e){
			throw new PersistenceException("Internal error");
		}
	}
	
	
	
	private static Set<DepartmentEmployeeRowDataGateway> loademployees(ResultSet rs) throws SQLException{
		Set<DepartmentEmployeeRowDataGateway> result = new HashSet<DepartmentEmployeeRowDataGateway>();
		while(rs.next()){
			DepartmentEmployeeRowDataGateway newDepEmp = new DepartmentEmployeeRowDataGateway(rs.getInt("saleId"), rs.getInt("productId"));
			newDepEmp.id = rs.getInt("id");
			result.add(newDepEmp);
		}
		return result;
	}
}
