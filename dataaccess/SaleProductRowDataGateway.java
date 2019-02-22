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
public class SaleProductRowDataGateway {
	
	// 1. Sale product attributes 
	
	
	/**
	 * The insert product in a sale SQL statement
	 */
	private static final String INSERT_PRODUCT_SALE_SQL = 
			"insert into saleproduct(id,qty,product_id,sale_id) " +
			"values(DEFAULT,?,?,?)"; 

	
	/**
	 * The select the products of a sale by sale Id SQL statement
	 */
	private static final String GET_SALE_PRODUCTS_SQL = 
			"select id, qty, product_id, sale_id "+
			"from saleproduct "+
			"where sale_id = ?";		

	private int id;
	private int saleId;
	private int productId;
	private double qty;
	
	// 2. constructor


	// 3. getters and setters
	public int getProductId() {
		return productId;
	}
	public int getsaleId(){
		return saleId;
	}

	public double getQty() {
		return qty;
	}
	public int getId() {
		return id;
	}
	


	// 4. interaction with the repository (a relational database in this simple example)

	public SaleProductRowDataGateway(int id, int productId, double qty) {
		this.saleId = id;
		this.productId = productId;
		this.qty = qty;
		
		
	}

	/**
	 * Inserts the record in the products sale 
	 * @throws SQLException 
	 */
	public void insert () throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_PRODUCT_SALE_SQL)){
			
			statement.setDouble(1, qty);
			statement.setInt(2, productId);
			statement.setInt(3, saleId);
		
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
	 * Gets the products of a sale by its sale id 
	 * 
	 * @param saleId The sale id to get the products of
	 * @return The set of products that compose the sale
	 * @throws PersistenceException When there is an error obtaining the
	 *         information from the database.
	 */
	public static Set<SaleProductRowDataGateway> findSaleProducts (int saleId) throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(GET_SALE_PRODUCTS_SQL)){
			
			statement.setInt(1, saleId);
			
			try(ResultSet rs = statement.executeQuery()){
				return loadsaleproducts(rs);
			}
		}catch(SQLException e){
			throw new PersistenceException("Internal error");
		}
	}
	
	
	
	private static Set<SaleProductRowDataGateway> loadsaleproducts(ResultSet rs) throws SQLException{
		Set<SaleProductRowDataGateway> result = new HashSet<SaleProductRowDataGateway>();
		while(rs.next()){
			SaleProductRowDataGateway newSaleProd = new SaleProductRowDataGateway(rs.getInt("saleId"), rs.getInt("productId"), rs.getInt("qty"));
			newSaleProd.id = rs.getInt("id");
			result.add(newSaleProd);
		}
		return result;
	}



	

	
	



	

	
	

}
