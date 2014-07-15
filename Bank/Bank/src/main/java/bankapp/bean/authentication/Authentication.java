/**
 * 
 */
package bankapp.bean.authentication;

import bankapp.database.*;
import bankapp.exception.UserNotFoundException;

/**
 * 
 * CSI3317_PROJECT
 *
 * @file Authentication.java
 * @package bankapp.bean.authentication
 * @author Mohamed Mansour @2005
 * @email m0.interactive@gmail.com
 * @web www.m0interactive.com
 * @date 9-Dec-2005
 *
 */
public class Authentication
{
	BankDB connect;
	
	public Authentication(BankDB connect_temp) {
		connect = connect_temp;
	}
	
	/**
	 * Check if user exists in the database 
	 * 
	 * @param id
	 * @param password
	 * @param type
	 * @return AuthenticationDetails
	 * @throws UserNotFoundException
	 */
	public AuthenticationDetails checkIfUserExist(String id, String password, int type) throws UserNotFoundException {
		// Initialize the current result	
		AuthenticationDetails result = null;
		
		// Appropriate Actions of user Type
		switch(type) {
			case 1:
				result = connect.customerExist(id,password);
				break;
			case 2:
				result = connect.employeeExist(id,password);
				break;
		}

		if (result != null)
			result.setType(type);
		
		return result;
		
	}
}
