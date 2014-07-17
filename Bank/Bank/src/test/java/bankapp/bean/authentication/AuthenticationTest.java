package bankapp.bean.authentication;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bankapp.database.BankDB;


public class AuthenticationTest{
	private static final long serialVersionUID = 1L;
	
	private static BankDB bankDB; 
	private static Authentication auth; 
	
	@BeforeClass
	public static void setUp() throws Exception{
		System.out.println("==>> inside setup");
		bankDB = new BankDB();
		auth = new Authentication(bankDB);
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		System.out.println("==>> inside tearDown");
		auth = null;
		bankDB.remove();
	}
	
	@Test
	public void testCheckIfUserExist() throws Exception{
		System.out.println("==>> inside testCheckIfUserExist");
		String userId = "1";
		String pwd = "111";
		int type = 1;
		String firstName = "Mohamed";
		String lastName = "Mansour";

		AuthenticationDetails authDetails = auth.checkIfUserExist(userId, pwd, type);
		
		System.out.println("==>> user id = "+authDetails.getUserID());
		System.out.println("==>> pwd = "+authDetails.getPassword());
		System.out.println("==>> account type = "+authDetails.getType());
		System.out.println("==>> first name = "+authDetails.getFirstName());
		System.out.println("==>> last name = "+authDetails.getLastName());
		
		Assert.assertNotNull(authDetails);
		Assert.assertEquals(authDetails.getUserID(), userId);
		Assert.assertEquals(authDetails.getType(), type);
		Assert.assertEquals(authDetails.getFirstName(), firstName);
		Assert.assertEquals(authDetails.getLastName(), lastName);
		
			
	}
/*
	@Test
	public void testCheckIfAnotherUserExist() throws Exception{
		System.out.println("==>> inside testCheckIfUserExist");
		String userId = "2";
		String pwd = "111";
		int type = 1;
		String firstName = "Mohamed";
		String lastName = "Mansour";

		AuthenticationDetails authDetails = auth.checkIfUserExist(userId, pwd, type);
		
		System.out.println("==>> user id = "+authDetails.getUserID());
		System.out.println("==>> pwd = "+authDetails.getPassword());
		System.out.println("==>> account type = "+authDetails.getType());
		System.out.println("==>> first name = "+authDetails.getFirstName());
		System.out.println("==>> last name = "+authDetails.getLastName());
		
		Assert.assertNotNull(authDetails);
		Assert.assertEquals(authDetails.getUserID(), userId);
		Assert.assertEquals(authDetails.getType(), type);
		Assert.assertEquals(authDetails.getFirstName(), firstName);
		Assert.assertEquals(authDetails.getLastName(), lastName);
		
			
	}
	*/
}
