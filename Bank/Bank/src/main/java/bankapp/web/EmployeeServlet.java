package bankapp.web;

import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import bankapp.bean.account.CustomerDetails;
import bankapp.bean.authentication.*;
import bankapp.bean.errorhandling.ErrorHandling;
import bankapp.database.EmployeeDB;
import bankapp.exception.UserNotFoundException;

public class EmployeeServlet extends HttpServlet {
	

	private EmployeeDB bankDB;

	
	public void init() throws ServletException {
	
		bankDB = (EmployeeDB)getServletContext().getAttribute("bankDB");
		
		if (bankDB == null) throw new UnavailableException("Couldn't get database.");
	}
	
	public void destroy() { 
		
		getServletContext().removeAttribute("bankDB");
		bankDB = null;
		
	}
	
    /**************************************************************************
     * 
     *	DO GET
     * 
     *************************************************************************/
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// Create fresh new session
    	HttpSession session = request.getSession();

		// Authentication Object
		AuthenticationDetails logged = (AuthenticationDetails) session.getAttribute("logged");
		
    	// Current Action
    	String module = request.getParameter("Module");
		
    	// Redirect Dispatcher
		String redirect = "/index.jsp"; 
	
    	// Check if the submit has been pressed
    	if (module != null || logged !=null){
    
    		//
    		// MODULE: Logout
    		//__________________________________________________________________
    		//
    		if(module.equals("LOGOUT")) {
    			
    			// Invalidate Session
    	        if(session != null) {
    	    
    	        	session.invalidate();
    	            session = null;
    	            
    	        }
    	        
    	        // Go Bank to Login Page
    			dispatcher(request,response,"/index.jsp");
    			
    		} 
    		
    		//
    		// MODULE: ADD_CUSTOMER
    		//__________________________________________________________________
    		//
    		else if(module.equals("ADD_CUSTOMER")) {
    		
    			
    			dispatcher(request,response,"/addCustomer.jsp");
    		
    		}
    		
    		//
    		// MODULE: REMOVE_CUSTOMER
    		//__________________________________________________________________
    		//
    		else if(module.equals("REMOVE_CUSTOMER")) {
    			
    			session.setAttribute("allCustomers", bankDB.getAllCustomers());
    			dispatcher(request,response,"/removeCustomer.jsp");
    		
    		}
    		
    		//
    		// MODULE: CREATE_ACCOUNT
    		//__________________________________________________________________
    		//
    		else if(module.equals("CREATE_ACCOUNT")) {
    		
    			
    			dispatcher(request,response,"/createAccount.jsp");
    		
    		}
    		
    		//
    		// MODULE: ADD_ACC_MEM
    		//__________________________________________________________________
    		//
    		else if(module.equals("ADD_ACC_MEM")) {
    		
    			session.setAttribute("allCustomers", bankDB.getAllCustomers());
    			session.setAttribute("allAccounts", bankDB.getAllAccounts());
    			dispatcher(request,response,"/addAccountMember.jsp");
    		
    		}
    		
    		//
    		// MODULE: DELETE_ACCOUNT
    		//__________________________________________________________________
    		//
    		else if(module.equals("DELETE_ACCOUNT")) {
    			
    			session.setAttribute("allAccounts", bankDB.getAllAccounts());
    			dispatcher(request,response,"/deleteAccount.jsp");
    		
    		}
    		
    	} else { dispatcher(request,response,"/index.jsp");	}
    	    	
    }

    

    /**************************************************************************
     * 
     *	DO POST
     * 
     *************************************************************************/
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// Create fresh new session
    	HttpSession session = request.getSession();

		// Authentication Object
		AuthenticationDetails logged = (AuthenticationDetails) session.getAttribute("logged");
		
    	// Current Action
    	String action = request.getParameter("action");

    	// Redirect Dispatcher
		String redirect = "/index.jsp"; 
		
    	// Check if the submit has been pressed
    	if (action != null) {
    			    		
    		if (action.equals("addCustomer")) {
    			
    			// Form Objects
    			String[][] formvars = new String[7][2];
    			formvars[0][0] = "lname";
    			formvars[0][1] = request.getParameter("lname");
    			formvars[1][0] = "fname";
    			formvars[1][1] = request.getParameter("fname");
    			formvars[2][0] = "address";
    			formvars[2][1] = request.getParameter("address");
    			formvars[3][0] = "city";
    			formvars[3][1] = request.getParameter("city");
    			formvars[4][0] = "phone";
    			formvars[4][1] = request.getParameter("phone");
    			formvars[5][0] = "email";
    			formvars[5][1] = request.getParameter("email");
    			formvars[6][0] = "best_time";
    			formvars[6][1] = request.getParameter("best_time");
    			    			
    			// Call the error handling class to check the forms
    			ErrorHandling errorHandler = new ErrorHandling();
    			
    			// Munge the Form variables
    			formvars = errorHandler.mungeForm(formvars);
    			
    			// Check if the form is valid
    			if(errorHandler.isValidForm(formvars)) {
    				
    				String fname = formvars[1][1];
    				String lname = formvars[0][1];
    				String address = formvars[2][1];
    				String city = formvars[3][1];
    				String phone = formvars[4][1];
    				String email = formvars[5][1];
    				String best_time = formvars[6][1];
    				// For now
    				String password = "default";
    				
    				CustomerDetails cd = new CustomerDetails(fname, lname, address, city, phone, email, best_time, password);
    				
    				boolean addedCustomer = bankDB.addCustomer(cd);
    				if (addedCustomer == true) request.setAttribute("GOOD_MSG","New Customer Added!");
    				else request.setAttribute("ERROR_MSG", "Could Not Add Customer!");
    				
    				redirect = "/addCustomer.jsp";
    				
    			} else {
    				
    				String ERROR_MSG = errorHandler.getErrorMsg();   				
    				request.setAttribute("ERROR_MSG", ERROR_MSG);
    				
    				redirect = "/addCustomer.jsp";
    				
    			}
    			
				// Redirect to page
				dispatcher(request,response,redirect);			
    			
    		} else if (action.equals("createAccount")) {
    			
    			int accountTypeInt = 0;
    			int customerId = Integer.parseInt(request.getParameter("cid"));
    			float rateOrOverdraft = Float.parseFloat(request.getParameter("rateOrOverdraft"));
    			
    			String accountType = request.getParameter("accountType");
    			    			
    			if (accountType.equals("Checking")) accountTypeInt = 0;
    			else if (accountType.equals("Savings")) accountTypeInt = 1;
    			
    			if (customerId > 0 && accountType != null && rateOrOverdraft > 0) {
    				
    				boolean accountCreated = bankDB.createAccountForCustomer(customerId, Integer.parseInt(logged.getUserID()), accountTypeInt, rateOrOverdraft);
    				
    				if (accountCreated == true) request.setAttribute("GOOD_MSG", "Account Created!");
    				else request.setAttribute("BAD_MSG", "Invalid Customer ID and/or Rate/Overdraft value");
    				
    			} else {
    				
    				request.setAttribute("ERROR_MSG", "Invalid Form Information!");
    				
    			}
    			
    			redirect = "/createAccount.jsp";

				// Redirect to page
				dispatcher(request,response,redirect);
    			
    		} else if (action.equals("addCustomerToAccount")) {

    			int customerId = Integer.parseInt(request.getParameter("cid"));
    			int accountId = Integer.parseInt(request.getParameter("accountId"));
    			    			    			    			
    			if (customerId > 0 && accountId > 0) {
					   				
    				boolean added = bankDB.addCutomerToAccount(customerId, accountId);    				
    				
    				if (added == true) request.setAttribute("GOOD_MSG", "Customer Added To Account!");
    				else request.setAttribute("ERROR_MSG", "Invalid Customer ID and/or Account ID");
    				
    			} else {
    				
    				request.setAttribute("ERROR_MSG", "Invalid Form Information");
    				
    			}
    			
    			redirect = "/addAccountMember.jsp";

				// Redirect to page
				dispatcher(request,response,redirect);
    			
    		} else if (action.equals("removeCustomer")) {
    			
    			int customerId = Integer.parseInt(request.getParameter("cid"));
    			
    			if (customerId > 0) {
    				
    				boolean removed = bankDB.removeCustomer(customerId);
    				
    				if (removed == true) request.setAttribute("GOOD_MSG", "Customer Removed!");
    				else request.setAttribute("ERROR_MSG", "Invalid Customer ID");
    				
    			} else {
    				
    				request.setAttribute("ERROR_MSG", "Invalid Form Formation");
    				
    			}
    			
    			redirect = "/removeCustomer.jsp";
    			
    			dispatcher(request, response, redirect);
    			
    		} else if (action.equals("deleteAccount")) {
    			
    			int accountId = Integer.parseInt(request.getParameter("accountId"));
    			
    			if (accountId > 0) {
    				
    				boolean removed = bankDB.deleteAccount(accountId);
    				
    				if (removed == true) request.setAttribute("GOOD_MSG", "Account Deleted!");
    				else request.setAttribute("ERROR_MSG", "Invalid Account ID");
    				
    			} else {
    				
    				request.setAttribute("ERROR_MSG", "Invalid Form Formation");
    				
    			}
    			
    			redirect = "/deleteAccount.jsp";
    			
    			dispatcher(request, response, redirect);
    			   			
    		}
    		
    	} else {
    		
    		redirect = "http://www.CampusInteractive.com/";
    		dispatcher(request, response, "");
    		
    	}
    	
    }
    
    /**
     * Dispatcher
     * 
     * @param uri
     * @throws IOException 
     * @throws ServletException 
     */
    private void dispatcher(HttpServletRequest request, HttpServletResponse response, String uri) throws ServletException, IOException {

    	ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(uri);
        rd.forward(request, response);
        
    }


}
