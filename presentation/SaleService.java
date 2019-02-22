package presentation;

import java.sql.SQLException;

import business.ApplicationException;
import business.SaleTransactionScripts;
import dataaccess.PersistenceException;


public class SaleService {

	private SaleTransactionScripts saleTS;

	public SaleService(SaleTransactionScripts saleTS) {
		this.saleTS = saleTS;
	}
	
	public int newSale(int vat, int employeeId) throws ApplicationException {
		return saleTS.newSale(vat, employeeId);
	}

	public void addProductToSale(int saleId, int productCode, int qty) 
			throws ApplicationException {
		saleTS.addProductToSale(saleId, productCode, qty);
	}

	public double getSaleDiscount(int saleId) throws ApplicationException {
		return saleTS.getSaleDiscount(saleId);
	}
	
	public void saleDelivery(int saleid, String address) throws PersistenceException, ApplicationException, SQLException {
		saleTS.saleDelivery(saleid, address);
	}
}
