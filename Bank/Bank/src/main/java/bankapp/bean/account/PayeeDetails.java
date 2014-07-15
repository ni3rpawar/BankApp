package bankapp.bean.account;

/**
 * 
 * CSI3317_PROJECT
 *
 * @file PayeeDetails.java
 * @package bankapp.bean.account
 * @author Mohamed Mansour @2005
 * @email m0.interactive@gmail.com
 * @web www.m0interactive.com
 * @date 9-Dec-2005
 *
 */
public class PayeeDetails {
    private int payeeID = 0;
    private String payeeName = null;
     


    /**
     * Payee Details
     * 
     * @param id
     * @param name
     */
    public PayeeDetails(int id, String name ) {
        this.payeeID = id;
        this.payeeName = name;
    }

    /**
     * Retrieve the Payee ID
     * 
     * @return int
     */
    public int getPayeeID() {
        return payeeID;
    }
    
    /**
     * Retrieve the Payee Name
     * 
     * @return String
     */
    public String getPayeeName() {
       return payeeName;
    }
    
}

