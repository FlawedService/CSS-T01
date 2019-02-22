package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//import javax.xml.crypto.Data;

import business.SaleStatus;

/**
 * An in-memory representation of a customer table record. 
 *	
 * Notes:
 * 1. See the notes for the CustomerRowGateway class
 * 
 * 2. Java makes available two Date classes (in fact, more in Java 8, 
 * but we will address it later (with JPA)): one in the package 
 * java.util, which is the one we normally use, and another in
 * java.sql, which is a subclass of java.util.date and that 
 * transforms the milliseconds representation according to the
 * "Date type of databases". For more information refer to 
 * http://download.oracle.com/javase/6/docs/api/java/sql/Date.html.
 * 
 * 3. When creating a new sale, we only pass the date and customer id 
 * parameters to the constructor. Moreover, attribute open is always 
 * set to 'O'. The remaining attributes are either set automatically (id) 
 * or when closing the sale (totalSale and totalDiscount). Also, the open 
 * attribute is set to 'C' upon payment. 
 * 
 * @author fmartins
 * @Version 1.2 (13/02/2015)
 *
 */
public class SaleRowDataGateway {

	// Sale attributes 


	
	/**
	 * The insert sale SQL statement
	 */
	
	private String status;

	private double discount;

	private int id;
	
	private double total;

	private int customerId;

	private java.sql.Date date;
	
	private int employeeId;


	/**
	 * Constants for mapping discount types with discount ids
	 */
	
	private static final String OPEN = "O" ;
	private static final String CLOSED = "C";
	// Constants for conversion of status

	// 1. constructor 

	/**
	 * Creates a new sale given the customer Id and the date it occurs.
	 * 
	 * @param customerId The customer Id the sale belongs to
	 * @param date The date the sale took place
	 * @param employeeId the employeed id associated with the sale.
	 */
	public SaleRowDataGateway(int customerId, Date data, int employeeId) {
		this.customerId = customerId;
		this.date = new java.sql.Date(data.getTime());
		this.employeeId = employeeId;
		setStatus(SaleStatus.OPEN);
	}

	// 2. getters and setters

	public int getId() {
		return id;
	}

	public SaleStatus getStatus() {
		return SaleStatusType(status);
	}
	private static SaleStatus SaleStatusType(String status){
		return status.equals(OPEN) ? SaleStatus.OPEN: SaleStatus.CLOSED;
	}
	public void setStatus(SaleStatus status) {
		this.status = status == SaleStatus.OPEN ? OPEN : CLOSED;
	}
	


	public int getCustomerId() {
		return customerId;
	}

	public double getDiscount() {
		return discount;			

	}
	public Date getDate() {
		return date;
	}
	
	public double getTotal(){
		return total;
	}
	
	public int getEmployeeId() {
		return employeeId;
	}


	// 3. interaction with the repository (a memory map in this simple example)

	
	private static final String INSERT_SALE_SQL = 
			"insert into sale (id, date, total, discount_total, status,customer_id, employee_id)" +
					"values (DEFAULT,?,?,?,?,?,?)"; 

	private static final String PENDING_SALE_SQL = 
			"select id, date, total,discount_total,status,customer_id, employee_id "+
					"from sale " +
					"where status = O"; 

	
	/**
	 * Stores the information in the repository
	 * @throws SQLException 
	 */
	public void insert () throws PersistenceException{		
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_SALE_SQL)) {
			//set statment arguments
			statement.setDate(1,date);
			statement.setDouble(2, total);
			statement.setDouble(3, discount);
			statement.setString(4, OPEN);
			statement.setInt(5, customerId);
			statement.setInt(6, employeeId);
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

	/**
	 * The select a sale by Id SQL statement
	 */
	private static final String GET_SALE_SQL = 
			"select id, date, total,discount_total,status,customer_id, employee_id "+
					"from sale " +
					"where id = ?"; 
	
	/**
	 * Gets a sale by its id 
	 * 
	 * @param id The sale id to search for
	 * @return The new object that represents an in-memory sale
	 * @throws PersistenceException In case there is an error accessing the database.
	 * 
	 */
	public static SaleRowDataGateway find (int id) throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepare(GET_SALE_SQL)){
			
			statement.setInt(1, id);
			try(ResultSet rs = statement.executeQuery()){
				return loadSale(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error");
		}	
	}
	
	public static Set<SaleRowDataGateway> pendingsales() throws SQLException, PersistenceException{
		Set<SaleRowDataGateway> result = new HashSet<SaleRowDataGateway>();
		ResultSet rs = findPendentSales();
		while(rs.next()){
			SaleRowDataGateway newsale = new SaleRowDataGateway(rs.getInt("customer_id"), 
					rs.getDate("date"), rs.getInt("employee_id"));
			newsale.id = rs.getInt("id");
			newsale.discount = rs.getDouble("discount_total");
			newsale.status = rs.getString("status");
			newsale.total = rs.getDouble("total");
			result.add(newsale);
		}
		return result;
	}
	
	/**
	 * Creates a sale from a result set retrieved from the database.
	 * 
	 * @param rs The result set with the information to create the sale.
	 * @return A new sale loaded from the database.
	 * @throws RecordNotFoundException In case the result set is empty.
	 */
	private static SaleRowDataGateway loadSale(ResultSet rs) throws RecordNotFoundException{
		try{
			rs.next();
			SaleRowDataGateway newsale = new SaleRowDataGateway(rs.getInt("customer_id"), rs.getDate("date"), 
					rs.getInt("employee_id"));
			newsale.id = rs.getInt("id");
			newsale.discount = rs.getDouble("discount_total");
			newsale.status = rs.getString("status");
			newsale.total = rs.getDouble("total");
			
			return newsale;
		}catch(SQLException e){
			throw new RecordNotFoundException("Sale does not exist", e);
		}
	}
	
	
	private static ResultSet findPendentSales() throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(PENDING_SALE_SQL)){
			
			try(ResultSet rs = statement.executeQuery()){
				return rs;
			}
		}catch(SQLException e){
			throw new PersistenceException("Internal error");
		}
	}
	
}
