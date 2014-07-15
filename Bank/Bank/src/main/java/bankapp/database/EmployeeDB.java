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
public class EmployeeDB extends DBConnection {

	public EmployeeDB() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	  /**
	   * This method adds a customer to the bank's database
	   * 
	   * @param firstName - The first name of the customer being added
	   * @param lastName - The last name of the customer being added
	   * @param address - The adress of the customer being added
	   * @param city - The city the new customer lives in
	   * @param phone - The phone number of the new customer
	   * @param email - The email address of the new customer
	   * @param bestTime - The best time the customer can come in
	   * 
	   * @return boolean - A variable indicating the customer has been added or not
	   */
	  
	  public boolean addCustomer(CustomerDetails customerDetails) {
		  
		  String sqlAddCustomerString = "INSERT INTO bank.customer(pass, lname, fname, address, city, phone, email, best_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		  
		  try {
			  
			  PreparedStatement sqlAddCustomerStatement = con.prepareStatement(sqlAddCustomerString);
			  
			  sqlAddCustomerStatement.setString(1, customerDetails.getPassword());
			  sqlAddCustomerStatement.setString(2, customerDetails.getLastName());
			  sqlAddCustomerStatement.setString(3, customerDetails.getFirstName());
			  sqlAddCustomerStatement.setString(4, customerDetails.getAddress());
			  sqlAddCustomerStatement.setString(5, customerDetails.getCity());
			  sqlAddCustomerStatement.setString(6, customerDetails.getPhone());
			  sqlAddCustomerStatement.setString(7, customerDetails.getEmail());
			  sqlAddCustomerStatement.setString(8, customerDetails.getBestTime());
			  
			  sqlAddCustomerStatement.executeUpdate();
			  sqlAddCustomerStatement.close();
			  		  
		  } catch (SQLException sqlEx) { return false; }
		  
		  return true;
		  
	  }
	  
	  
	  /** 
	   * This method removes a customer from the bank's database
	   * 
	   * @param customerId - The id of the customer
	   * 
	   * @return boolean - A variable indicating the customer has been removed or not
	   */
	  public boolean removeCustomer(int customerId) {
		  
		  int count;
		  
		  String sqlRemoveCustomerString = "DELETE FROM customer WHERE cid=?";
		  		 		  
		  try {
	
			  PreparedStatement sqlRemoveCustomerStatement = con.prepareStatement(sqlRemoveCustomerString);
			  
			  sqlRemoveCustomerStatement.setInt(1, customerId);
			  
			  sqlRemoveCustomerStatement.executeUpdate();
			  sqlRemoveCustomerStatement.close();
			  			  
		  } catch (SQLException sqlEx) { return false; }
				  
		  return true;
		  
	  }

	  
	  /** 
	   * This method creates an account for a user. Upon doing so, the 
	   * emoployee creating the account will be able designated as the
	   * account-supervisor/loan-supervison based on the type of account
	   * being created
	   * 
	   * @param customerId - The id of the customer the account is being created for
	   * @param employeeId - The id of the employee the
	   * @param accountType - The type of account being created (0 - Savings Account, 1 - Checking Account)
	   * @param specVariable - This can represent if the savings rate if it's a savings account, and it can represent the overdraft if it's a checking account
	   *
	   * @return boolean - A variable indicating the customer her been removed or not 
	   */
	  public boolean createAccountForCustomer(int customerId, int employeeId, int accountType, float specVariable) {
		  
		  String sqlCreateAccountForCustomerString = "INSERT INTO bank.account(amount) VALUES(?)";
		  String sqlSetCustomerAsHoldingAccountString = "INSERT INTO bank.holds VALUES (?, ?)";
		  String sqlCreateSpecificAccountForCustomerString;
		  String sqlSetCustomerLoanSupervisorString = "INSERT INTO bank.acc_sup VALUES(?, ?, ?)";
		  
		  if (accountType == 0) { sqlCreateSpecificAccountForCustomerString = "INSERT INTO bank.savings VALUES(?, ?)"; }
		  else { sqlCreateSpecificAccountForCustomerString = "INSERT INTO bank.checking VALUES(?, ?)"; }
		  		  
		  try {
			  
			  // Creating the account in the account table
			  PreparedStatement sqlCreateAccountForCustomerStatement = con.prepareStatement(sqlCreateAccountForCustomerString);
			  
			  sqlCreateAccountForCustomerStatement.setInt(1, 0);
			  		  
			  sqlCreateAccountForCustomerStatement.executeUpdate();
			  sqlCreateAccountForCustomerStatement.close();
			  
			  int accountId = getAccountId();
			  
			  // Linking the user and the account in the holds table
			  PreparedStatement sqlSetCustomerAsHoldingAccountStatement = con.prepareStatement(sqlSetCustomerAsHoldingAccountString);
			  makeCustomerHoldAccount(sqlSetCustomerAsHoldingAccountStatement, customerId, accountId);
			  
			  // Linking the account into the savings/checking account table
			  PreparedStatement sqlCreateSpecificAccountForCustomerStatement = con.prepareStatement(sqlCreateSpecificAccountForCustomerString);
			  specifyAccount(sqlCreateSpecificAccountForCustomerStatement, accountId, specVariable);
			  		  		  
			  // Setting the employee as the account supervisor
			  PreparedStatement sqlSetCustomerLoanSupervisorStatement = con.prepareStatement(sqlSetCustomerLoanSupervisorString);
			  setAccountSupervisor(sqlSetCustomerLoanSupervisorStatement, accountId, customerId, employeeId);
			  		  
		  } catch (SQLException sqlEx) { return false; }
		  
		  return true;
		  
	  }
	  
	  
	  /**
	   * This method gets the account id of the very last account added into the system
	   * 
	   * @return int - The id of the newly added account based on the MAX() sql function
	   */
	  public int getAccountId() {
		  
		  String sqlGetMaxIdString = "SELECT MAX(account) FROM account";
		  int maxId;
		  
		  try {
			  
			  Statement sqlGetMaxIdStatement = con.createStatement();
			  ResultSet sqlGetMaxIdResultSet = sqlGetMaxIdStatement.executeQuery(sqlGetMaxIdString);
			  
			  if (sqlGetMaxIdResultSet.next()) {
				  
				  maxId = sqlGetMaxIdResultSet.getInt("account");
				  
			  } else { maxId = -1; }		  
			  
		  } catch (SQLException sqlEx) { maxId = -1; }
		  
		  return maxId;
		  
	  }
	  
	  
	  /**
	   * This method allows us to link a customer to an account
	   * 
	   * @param insertStatement - The statement indicating the insert query statement
	   * @param customerId - The customer id of the customer being associated to an account
	   * @param accountId - The account id of the account being associated to a customer
	   */
	  public void makeCustomerHoldAccount(PreparedStatement insertStatement, int customerId, int accountId) {
		  
		  try  {
		  
			  insertStatement.setInt(1, customerId);
			  insertStatement.setInt(2, accountId);
		  		  
			  insertStatement.executeUpdate();
			  insertStatement.close();
		  
		  } catch (SQLException sqlEx) {}
		  
	  }
	  
	  
	  /**
	   * This method allows for specifying what type of account is being created
	   *  
	   * @param insert - The statement indicating the insert query statement
	   * @param accountId - The account id of the account being specified
	   * @param specVariable - Either the rate(for Savings Accounts) or the overdraft(for Checking Accounts)
	   */
	  public void specifyAccount(PreparedStatement insertStatement, int accountId, float specVariable) {
		  	  
		  try {
			  
			  insertStatement.setInt(1, accountId);
			  insertStatement.setFloat(2, specVariable);
		  		  
			  insertStatement.executeUpdate();
			  insertStatement.close();
		  
		  } catch (SQLException sqlEx) {}
		  
	  }
	  
	  /**
	   * This method allows for the setting of a supervisor to a customers account
	   * 
	   * @param insert - The statement indicating the insert query statement
	   * @param accountId - The account id of the account being supervised
	   * @param customerId - The customer id for which the account is being supervised
	   * @param employeeId - The employee supervising the account
	   */
	  public void setAccountSupervisor(PreparedStatement insertStatement, int accountId, int customerId, int employeeId) {
		  
		  try {
			  
			  insertStatement.setInt(1, accountId);
			  insertStatement.setInt(2, customerId);
			  insertStatement.setInt(3, employeeId);
			  		  
			  insertStatement.executeUpdate();
			  insertStatement.close();
			  
		  } catch (SQLException sqlEx) {}
		  	  
	  }
	  /**
	   * This method adds a customer to an account
	   * 
	   * @param customerId - The id of the customer being added to the account
	   * @param accountId - The id of the account the user is being added to
	   * 
	   * @return boolean - A variable indicating the customer has been added to the account
	   */
	  public boolean addCutomerToAccount(int customerId, int accountId) {
		  
		  String addCustomerToAccountString = "INSERT INTO holds VALUES(?, ?)";
		  
		  try {
			  
			  PreparedStatement addCustomerToAccountStatement = con.prepareStatement(addCustomerToAccountString);
			  
			  makeCustomerHoldAccount(addCustomerToAccountStatement, customerId, accountId);
			  
		  } catch (SQLException sqlEx) { return false; }
				  
		  return true;
		  
	  }
	  
	  
	  /**
	   * This method adds customers to an account
	   * 
	   * @param accountId - The id of the account being deleted
	   * 
	   * @return boolean - A variable indicating the account has been deleted
	   */
	   public boolean deleteAccount(int accountId) {

		   String sqlRemoveAccountString = "DELETE FROM account WHERE account=?";
			  
		   try {
				  
			  PreparedStatement sqlRemoveAccountStatement = con.prepareStatement(sqlRemoveAccountString);
			  
			  sqlRemoveAccountStatement.setInt(1, accountId);
			  
			  sqlRemoveAccountStatement.executeUpdate();
			  sqlRemoveAccountStatement.close();
			  
		   } catch (SQLException sqlEx) { return false; }
					  
		   return true;
	   
	   }
	   
	   /**
	    * This method returns an array filled with all the account ids
	    * that exist in the database
	    * 
	    * @return Vector - A vector filled with all the account ids
	    */
	   public Vector getAllAccounts() {
		   
		  String sqlGetAllAccountsString = "SELECT account FROM account";
		  Vector accountVector = new Vector();
			  
		  try {
			  
			  Statement sqlGetAllAccountsStatement = con.createStatement();
			  ResultSet sqlGetAllAccountsResultSet = sqlGetAllAccountsStatement.executeQuery(sqlGetAllAccountsString);
			  
			  while (sqlGetAllAccountsResultSet.next()) {
				  
				 accountVector.add(Integer.valueOf(sqlGetAllAccountsResultSet.getInt("account")));
				  
			  } 		  
			  
		  } catch (SQLException sqlEx) { }
		  
		  return accountVector;
		   
	   }

	   
	   /**
	    * This method returns an array filled with the
	    * customer ids of all the customers that exist 
	    * in the database
	    * 
	    * @return Vector - A vector filled with all the cid's
	    */
	   public Vector getAllCustomers() {
		   
			  String sqlGetAllCustomersString = "SELECT cid FROM customer";
			  Vector customerVector = new Vector();
				  
			  try {
				  
				  Statement sqlGetAllCustomersStatement = con.createStatement();
				  ResultSet sqlGetAllCustomersResultSet = sqlGetAllCustomersStatement.executeQuery(sqlGetAllCustomersString);
				  
				  while (sqlGetAllCustomersResultSet.next()) {
					  
					 customerVector.add(Integer.valueOf(sqlGetAllCustomersResultSet.getInt("cid")));
					  
				  } 		  
				  
			  } catch (SQLException sqlEx) { }
			  
			  return customerVector;
			   
		   }
}
