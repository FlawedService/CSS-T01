package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentRowDataGateway {

	//Department's attributes
	
	private int id;
	
	private String name;
	
	private int phoneNumber;
	
	private Iterable<EmployeeRowDataGateway> employees;
	
	private int manager_id;
	
	
	
	
	//Constructor
	public DepartmentRowDataGateway(String name, int phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	
	//Getters and Setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public void setPhoneNumber(int phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	public int getManager_id() {
		return manager_id;
	}
	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}

	public Iterable<EmployeeRowDataGateway> getEmployees() {
		return employees;
	}

	public void setEmployees(Iterable<EmployeeRowDataGateway> employees) {
		this.employees = employees;
	}

	
	
	
	
	
	private static final String INSERT_DEPARTMENT_SQL = 
			"insert into department (id, name, phoneNumber,manager_id) " +
			"values (DEFAULT, ?, ?, 0)";
	
	
	public void insert () throws PersistenceException{		
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_DEPARTMENT_SQL)) {
			//set statment arguments
			statement.setString(1, name);
			statement.setInt(2, phoneNumber);
			//executar o SQl
			statement.executeUpdate();
			//gerar o id automaticamente
			try (ResultSet rs = statement.getGeneratedKeys()) {
				rs.next();
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal Error", e);
		}
	}
	
	
	private static final String GET_DEPARTMENT_SQL = 
			"select id, name, phoneNumber,manager_id "+
			"from department " +
			"where id = ?";	

	public static DepartmentRowDataGateway find (int id) throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepare(GET_DEPARTMENT_SQL)){
			
			statement.setInt(1, id);
			try(ResultSet rs = statement.executeQuery()){
				return loadDep(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error");
		}	
	}
	
	
	
	private static DepartmentRowDataGateway loadDep(ResultSet rs) throws RecordNotFoundException{
		try{
			rs.next();
			DepartmentRowDataGateway newDepartment = new DepartmentRowDataGateway(
					rs.getString("name"), rs.getInt("phoneNumber"));
					newDepartment.id = rs.getInt("id");
					newDepartment.manager_id = rs.getInt("manager_id");
			
				
			return newDepartment;
		}catch(SQLException e){
				throw new RecordNotFoundException("Department does nor exist", e);
			}
		}	
		
	

	
}
