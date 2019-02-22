package dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryRowDataGateway {

	//constants for delivery
	private int id;

	private int saleId;

	private int paying_client;

	private String address;


	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPaying_client() {
		return paying_client;
	}

	public void setPaying_client(int paying_client) {
		this.paying_client = paying_client;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSaleId() {
		return saleId;
	}

	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}

	//Constructor

	public DeliveryRowDataGateway(int saleId, int paying_client, String address){
		this.saleId = saleId;
		this.paying_client = paying_client;
		this.address = address;
	}


	private static final String INSERT_DELIVERY_SQL = "insert into delivery (id, sale_id, pay_client_id, delivery_address) " 
			+ "values ( DEFAULT, ?, ?, ?) " ;


	public void insert () throws PersistenceException{		
		try(PreparedStatement statement = DataSource.INSTANCE.prepareGetGenKey(INSERT_DELIVERY_SQL)) {
			//set statment arguments
			statement.setInt(1, saleId);
			statement.setInt(2, paying_client);
			statement.setString(3, address);
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
	private static final String GET_DELIVERY_SQL = "select * " +" from delivery " + "where id = ?" ;
	
	public static DeliveryRowDataGateway find (int id) throws PersistenceException {
		try(PreparedStatement statement = DataSource.INSTANCE.prepare(GET_DELIVERY_SQL)){
			
			statement.setInt(1, id);
			try(ResultSet rs = statement.executeQuery()){
				return loadDelivery(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error");
		}	
	}
	
	private static DeliveryRowDataGateway loadDelivery(ResultSet rs) throws RecordNotFoundException{
		try{
			rs.next();
			DeliveryRowDataGateway newDelivery = new DeliveryRowDataGateway(rs.getInt("sale_id"), 
					rs.getInt("pay_client_id"), rs.getString("delivery_address"));
			
			
			newDelivery.id = rs.getInt("id");
			
			return newDelivery;
		}catch(SQLException e){
			throw new RecordNotFoundException("Delivery does not exist", e);
		}
	}
			
	
}
