package bankapp.database;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import bankapp.bean.account.*;
import bankapp.bean.authentication.AuthenticationDetails;
import bankapp.exception.*;

import java.util.*;

/**
 * 
 * CSI3317_PROJECT
 *
 * @file BankDB.java
 * @package bankapp.database
 * @author Mohamed Mansour @2005
 * @email m0.interactive@gmail.com
 * @web www.m0interactive.com
 * @date 9-Dec-2005
 *
 */
public class BankDB extends DBConnection {

	public BankDB() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Check if customer exists
	 * 
	 * @param cid
	 * @param password
	 * @return AuthenticationDetails
	 * @throws AccountNotFoundException
	 */
	public AuthenticationDetails customerExist(String cid, String password) throws UserNotFoundException {
		
		try {
			// SELECT 
			String selectStatement = "SELECT cid,fname,lname,pass FROM bank.customer WHERE cid = ? AND pass = ?";
			getConnection();
			
			PreparedStatement prepStmt = con.prepareStatement(selectStatement); 
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, cid);
			prepStmt.setString(2, password);
			
			ResultSet rs = prepStmt.executeQuery();
			
			if (rs.next()) {
				AuthenticationDetails ad = new AuthenticationDetails(rs.getString(1),rs.getString(2),rs.getString(3),1,rs.getString(4));
				prepStmt.close();               
				return ad;
			} else {          
				prepStmt.close();
				return null;
			}
		} catch (SQLException ex) {
			throw new UserNotFoundException("Couldn't find User: " + cid + " " + ex.getMessage());
		}
	}
	
	/**
	 * Check if customer exists
	 * 
	 * @param cid
	 * @param password
	 * @return AuthenticationDetails
	 * @throws AccountNotFoundException
	 */
	public AuthenticationDetails employeeExist(String eid, String password) throws UserNotFoundException {
		
		try {
			// SELECT 
			String selectStatement = "SELECT eid,fname,lname,pass FROM bank.employee WHERE eid = ? AND pass = ?";
			getConnection();
			
			PreparedStatement prepStmt = con.prepareStatement(selectStatement); 
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, eid);
			prepStmt.setString(2, password);
			
			ResultSet rs = prepStmt.executeQuery();
			
			if (rs.next()) {
				AuthenticationDetails ad = new AuthenticationDetails(rs.getString(1),rs.getString(2),rs.getString(3),1,rs.getString(4));
				prepStmt.close();               
				return ad;
			} else {          
				prepStmt.close();
				return null;
			}
		} catch (SQLException ex) {
			throw new UserNotFoundException("Couldn't find User: " + eid + " " + ex.getMessage());
		}
	}
	/**
	 * Fetch from the DB the customers information
	 * 
	 * @param cid
	 * @return CustomerDetails
	 * @throws UserNotFoundException
	 */
	public CustomerDetails retrieveCustomerInformation(String cid) throws UserNotFoundException {
		
		try {
			String selectStatement = "SELECT cid,fname,lname,address,city,phone,email,best_time,pass FROM bank.customer WHERE cid = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, cid);
			
			ResultSet rs = prepStmt.executeQuery();
	
		    /**
		     * 
		     * @param firstName
		     * @param lastName
		     * @param address
		     * @param city
		     * @param phone
		     * @param email
		     * @param best_time
		     * @param pass
		     */
			if (rs.next()) {
				CustomerDetails cd = new CustomerDetails(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9));
				
				prepStmt.close();           
				return cd;
			} else {          
				prepStmt.close();
				return null;
			}
		} catch (SQLException ex) {
			throw new UserNotFoundException("Couldn't find User: " + cid + " " + ex.getMessage());
		}
		
	}
	
	/**
	 * Update the user preferences
	 * 
	 * @param customer
	 * @return boolean
	 * @throws UserNotFoundException
	 */
	public boolean updateCustomerPreference(CustomerDetails customer) throws UserNotFoundException {
		try {
			
			// Query for updating customer
			String selectStatement = "UPDATE customer" +
					" SET email = ?, phone = ?, pass = ?, address = ? " +
					"WHERE cid = ?";
			
			// Get the database connection from the Pool of connections
			getConnection();
			
			// Start the prepared statement
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, customer.getEmail());
			prepStmt.setString(2, customer.getPhone());
			prepStmt.setString(3, customer.getPassword());
			prepStmt.setString(4, customer.getAddress());
			prepStmt.setInt(5, customer.getCustomerId());
			
			// Execute the statement
			int rows = prepStmt.executeUpdate();
			
			// Return the connection back to the pool
			prepStmt.close(); 
			
			// Check if update was successfull
			if (rows > 0 ) {
				return true;
			} else {
				return false;
			}	
		} catch (SQLException ex) {
			throw new UserNotFoundException("Couldn't find User: " + customer.getCustomerId() + " " + ex.getMessage());
		}
	}

	/**
	 * Get all the accounts in  a collection
	 * 
	 * @return Collection
	 * @throws AccountsNotFoundException
	 */
	public Collection getAccounts(int customerID) throws AccountsNotFoundException {
		ArrayList account = new ArrayList();
		try {
//			String selectStatement = "SELECT H.acttype, A.account, A.amount " +
//					"FROM bank.customer C " +
//					"NATURAL JOIN bank.holds H " +
//					"NATURAL JOIN bank.account A " +
//					"WHERE C.cid = ? ORDER BY H.acttype,A.account,A.amount";
			
			String selectStatement = "SELECT H.acttype, A.account, A.amount FROM bank.customer C, bank.holds H,bank.account A where C.cid = ? and C.cid =  H.cid and H.account = A.account ORDER BY H.acttype,A.account,A.amount";

			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, customerID);
			
			ResultSet rs = prepStmt.executeQuery();
		
			while (rs.next()) {
				
				AccountSummaryDetails ad = new AccountSummaryDetails(rs.getInt(1),rs.getInt(2),rs.getFloat(3));
				account.add(ad);
			}
			prepStmt.close();
		} catch (SQLException ex) {
			throw new AccountsNotFoundException(ex.getMessage());
		}
		return account;
	}


	/**
	 * Retreive the 
	 * @param TransferAccountDetails
	 * @throws TransferAccountNotFoundException
	 */
	public void transferAmount(TransferAccountDetails trans) throws TransferAccountNotFoundException {  
		try {
			//-- PARAM (AMOUNT,ACCOUNTFROM,ACCOUNTTO,CUSTOMER)
			
			String selectStatement = "SELECT transferMoney(?,?,?,?)";
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);

			prepStmt.setBigDecimal(1, trans.getAmount());
			prepStmt.setInt(2, trans.getFrom());
			prepStmt.setInt(3, trans.getTo());
			prepStmt.setInt(4, trans.getID());
			
			prepStmt.executeQuery();

			// Return the connection back to the pool
			prepStmt.close();  

		} catch (SQLException ex) {
			throw new TransferAccountNotFoundException("Insufficient Funds: " + trans.getAmount()+ " is not enough.");
		}
	}
	
	/**
	 * Retreive the 
	 * @param bill
	 * @throws BillPaymentException
	 */
	public void billPayment(BillAccountDetails bill) throws BillPaymentException, DateException {  
		try {
			//-- PARAM (AMOUNT,ACCOUNT,PAYEE,CUSTOMER,DATE)
			
			String selectStatement = "SELECT payBill(?,?,?,?,?)";
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);

			// Convert to Date
			java.util.Date this_date = bill.getDate();
			java.sql.Date sqldate = new java.sql.Date(this_date.getTime());

			prepStmt.setBigDecimal(1, bill.getAmount());
			prepStmt.setInt(2, bill.getFrom());
			prepStmt.setInt(3, bill.getTo());
			prepStmt.setInt(4, bill.getID());
			prepStmt.setDate(5, sqldate);
			
			prepStmt.executeQuery();

			// Return the connection back to the pool
			prepStmt.close();  

		} catch (SQLException ex) {
			throw new BillPaymentException("Insufficient Funds: " + bill.getAmount()+ " is not enough.");
		} catch (ParseException e) {
			throw new DateException(e.toString());
		}
	}
	

	/**
	 * Gets all the payees from the list
	 * 
	 * @return Collection
	 * @throws PayeeNotFoundException
	 */
	public Collection getAllPayee(int accountId) throws PayeeNotFoundException {
		ArrayList payee = null;
		payee = new ArrayList();
		try {
			
			// Prepare the Prepared Statement
			String selectStatement = "SELECT P.pid,P.payee " +
					"FROM bank.payee P " +
					"NATURAL LEFT JOIN bank.customer_payee C " +
					"WHERE C.cid IS NULL OR C.cid <> ? " +
					"ORDER BY P.payee";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, accountId);
			
			ResultSet rs = prepStmt.executeQuery();
			
			// Go through the whole result set
			while (rs.next()) {
				PayeeDetails pd = new PayeeDetails(rs.getInt(1), rs.getString(2));
				payee.add(pd);
			}
			prepStmt.close();
		} catch (SQLException ex) {
			throw new PayeeNotFoundException(ex.getMessage());
		}
		// Collections.sort(payee);
		return payee;
	}
	
	/**
	 * Add Customers Payee
	 * 
	 * @param payeeID
	 * @param customerID
	 * @return boolean
	 * @throws UserNotFoundException
	 */
	public boolean addCustomerPayee(int payeeID, int customerID) throws UserNotFoundException {
		try {
			
			// Query for inserting payee
			String selectStatement = "INSERT INTO customer_payee VALUES(?,?,current_date)";
			
			// Get the database connection from the Pool of connections
			getConnection();
			
			// Start the prepared statement
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);

			prepStmt.setInt(1, customerID);
			prepStmt.setInt(2, payeeID);
			
			// Execute the statement
			int rows = prepStmt.executeUpdate();
			
			// Return the connection back to the pool
			prepStmt.close(); 
			
			// Check if update was successfull
			if (rows > 0 ) {
				return true;
			} else {
				return false;
			}	
		} catch (SQLException ex) {
			throw new UserNotFoundException("Couldn't find User: " + payeeID + " " + ex.getMessage());
		}
	}

	/**
	 * Del Customers Payee
	 * 
	 * @param payeeID
	 * @param customerID
	 * @return boolean
	 * @throws PayeeNotFoundException
	 */
	public boolean delCustomerPayee(int payeeID, int customerID) throws PayeeNotFoundException {
		try {
			
			// Query for inserting payee
			String selectStatement = "DELETE FROM customer_payee WHERE cid=? AND pid=?";
			
			// Get the database connection from the Pool of connections
			getConnection();
			
			// Start the prepared statement
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);

			prepStmt.setInt(1, customerID);
			prepStmt.setInt(2, payeeID);
			
			// Execute the statement
			int rows = prepStmt.executeUpdate();
			
			// Return the connection back to the pool
			prepStmt.close(); 
			
			// Check if update was successfull
			if (rows > 0 ) {
				return true;
			} else {
				return false;
			}	
		} catch (SQLException ex) {
			throw new PayeeNotFoundException("Couldn't find User: " + payeeID + " " + ex.getMessage());
		}
	}
	
	/**
	 * Returns a list of Customer Payess that they picked
	 * 
	 * @param cid
	 * @return Collection
	 * @throws PayeeNotFoundException
	 */
	public Collection viewCustomerPayee(int cid) throws PayeeNotFoundException {
		ArrayList payee = null;
		payee = new ArrayList();
		try {
			String selectStatement = "SELECT C.pid,P.payee " +
					"FROM bank.customer_payee C " +
					"NATURAL JOIN bank.payee P " +
					"WHERE C.cid = ? ORDER BY P.payee";

			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, cid);

			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				PayeeDetails pd = new PayeeDetails(rs.getInt(1), rs.getString(2));

				payee.add(pd);
			}
			prepStmt.close();
		} catch (SQLException ex) {
			throw new PayeeNotFoundException(ex.getMessage());
		}
		// Collections.sort(payee);
		return payee;
	}
	
	/**
	 * Information about that specific account
	 * 
	 * @param int accountID
	 * @return Collection
	 * @throws AccountsNotFoundException
	 */
	public Collection getAccount(int accountID) throws AccountNotFoundException {
		ArrayList account = new ArrayList();
		try {
			String selectStatement = "SELECT AC.account,A.cid,A.rem_bal,A.amount,A.descr,A.date,A.activity_id " +
					"FROM bank.activity AS A " +
					"JOIN bank.account AS AC ON A.account=AC.account " +
					"WHERE AC.account = ? ORDER BY A.activity_id ASC";
			
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, accountID);
			
			ResultSet rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				AccountActivityDetails aa = new AccountActivityDetails(rs.getInt(1),rs.getInt(2),rs.getFloat(3),rs.getFloat(4),rs.getString(5),rs.getString(6),rs.getInt(7));
				account.add(aa);
			}
			prepStmt.close();
		} catch (SQLException ex) {
			throw new AccountNotFoundException(ex.getMessage());
		}
		return account;
	}
	

	/**
	 * Get the current Account Details!
	 * 
	 * @param accountID
	 * @return AccountActivityDetails
	 * @throws AccountNotFoundException
	 */
	public AccountDetails getCurrentAccount(int accountID, int customerID) throws AccountNotFoundException {
		try {
			String selectStatement = "SELECT A.account,H.acttype,A.amount " +
					"FROM bank.account AS A " +
					"JOIN bank.holds AS H " +
					"	ON H.account = A.account " +
					"WHERE H.account=? AND H.cid=?";
			
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, accountID);
			prepStmt.setInt(2, customerID);
			
			ResultSet rs = prepStmt.executeQuery();
			
			if (rs.next()) {
				AccountDetails ad = new AccountDetails(rs.getInt(1),rs.getFloat(3),rs.getInt(2));
				prepStmt.close();             
				return ad;
			} else {     
				prepStmt.close();
				return null;
			}
		} catch (SQLException ex) {
			throw new AccountNotFoundException("Couldn't find Account: " + accountID + " " + ex.getMessage());
		}
	}
	
	/**
	 * Gets the overdraft
	 * 
	 * @param account
	 * @return
	 * @throws AccountNotFoundException
	 */
	public float getOverDraft(int account) throws AccountNotFoundException {

		float overdraft = 0;
		
		try {
			String selectStatement = "SELECT overdraft FROM bank.checkings WHERE account = ?";
			
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, account);
			
			ResultSet rs = prepStmt.executeQuery();
			
			if (rs.next()) {
				overdraft = rs.getInt(1);
				prepStmt.close(); 
			} else {     
				prepStmt.close();
			}

			return overdraft;
		} catch (SQLException ex) {
			throw new AccountNotFoundException("Couldn't find Account: " + account + " " + ex.getMessage());
		}
	}
}
