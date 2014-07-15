package bankapp.database;

import junit.framework.Assert;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bankapp.bean.account.CustomerDetails;
import bankapp.bean.authentication.AuthenticationDetails;
import bankapp.database.BankDB;


public class BankDBTest{
	private static final long serialVersionUID = 1L;
	
	private static BankDB bankDB; 
		
	@BeforeClass
	public static void setUp() throws Exception{
		System.out.println("==>> inside setup");
		bankDB = new BankDB();
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		System.out.println("==>> inside tearDown");
		bankDB.remove();
	}
	
	@Test
	public void testCustomerExist() throws Exception{
		System.out.println("==>> inside testCustomerExist");
		String custId = "1";
		String pwd = "111";
		int type = 1;
		String firstName = "Mohamed";
		String lastName = "Mansour";

		AuthenticationDetails authDetails = bankDB.customerExist(custId, pwd);
		
		System.out.println("==>> user id = "+authDetails.getUserID());
		System.out.println("==>> pwd = "+authDetails.getPassword());
		System.out.println("==>> account type = "+authDetails.getType());
		System.out.println("==>> first name = "+authDetails.getFirstName());
		System.out.println("==>> last name = "+authDetails.getLastName());
		
		Assert.assertNotNull(authDetails);
		Assert.assertEquals(authDetails.getUserID(), custId);
		Assert.assertEquals(authDetails.getType(), type);
		Assert.assertEquals(authDetails.getFirstName(), firstName);
		Assert.assertEquals(authDetails.getLastName(), lastName);
	}

	@Test
	public void testEmployeeExist() throws Exception{
		System.out.println("==>> inside testEmployeeExist");
		String empId = "2";
		String pwd = "123";
		int type = 1;
		String firstName = "Iluju";
		String lastName = "Kiringa";

		AuthenticationDetails authDetails = bankDB.employeeExist(empId, pwd);
		
		System.out.println("==>> user id = "+authDetails.getUserID());
		System.out.println("==>> pwd = "+authDetails.getPassword());
		System.out.println("==>> account type = "+authDetails.getType());
		System.out.println("==>> first name = "+authDetails.getFirstName());
		System.out.println("==>> last name = "+authDetails.getLastName());
		
		Assert.assertNotNull(authDetails);
		Assert.assertEquals(authDetails.getUserID(), empId);
		Assert.assertEquals(authDetails.getType(), type);
		Assert.assertEquals(authDetails.getFirstName(), firstName);
		Assert.assertEquals(authDetails.getLastName(), lastName);
			
	}

	@Test
	public void testRetrieveCustomerInformation() throws Exception{
		System.out.println("==>> inside testCustomerExist");
		String custId = "1";
		String email = "m0.interactive@gmail.com";
		String city = "Ottawa";
		String firstName = "Mohamed";
		String lastName = "Mansour";

		CustomerDetails custDetails = bankDB.retrieveCustomerInformation(custId);
		
		System.out.println("==>> user id = "+custDetails.getCustomerId());
		System.out.println("==>> pwd = "+custDetails.getCity());
		System.out.println("==>> account type = "+custDetails.getEmail());
		System.out.println("==>> first name = "+custDetails.getFirstName());
		System.out.println("==>> last name = "+custDetails.getLastName());
		
		Assert.assertNotNull(custDetails);
		Assert.assertEquals(custDetails.getCustomerId(), custId);
		Assert.assertEquals(custDetails.getCity(), city);
		Assert.assertEquals(custDetails.getEmail(), email);
		Assert.assertEquals(custDetails.getFirstName(), firstName);
		Assert.assertEquals(custDetails.getLastName(), lastName);
	}



	
}
