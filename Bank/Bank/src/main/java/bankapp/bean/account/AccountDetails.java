package bankapp.bean.account;

/**
 * 
 * CSI3317_PROJECT
 *
 * @file AccountDetails.java
 * @package bankapp.bean.account
 * @author Mohamed Mansour @2005
 * @email m0.interactive@gmail.com
 * @web www.m0interactive.com
 * @date 9-Dec-2005
 *
 */
public class AccountDetails {
    private int accountID = 0;
    private float amount = 0;
    private int type = 0;
     
    /**
     * Account Details
     * 
     * @param accountID
     * @param amount
     * @param type
     */
    public AccountDetails(int accountID, float amount, int type ) {
        this.accountID = accountID;
        this.amount = amount;
        this.type = type;
    }

    /**
     * Retrieve the account ID
     * 
     * @return int
     */
    public int getAccountID() {
        return accountID;
    }
    
    /**
     * Retrieve the account type
     * 
     * @return int
     */
    public int getAccountType() {
        return type;
    }
    
    /**
     * Retrieve the Float
     * 
     * @return float
     */
    public float getAmount() {
       return amount;
    }
    
}

