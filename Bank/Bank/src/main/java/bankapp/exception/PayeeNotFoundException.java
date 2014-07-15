
package bankapp.exception;

/**
 * 
 * CSI3317_PROJECT
 *
 * @file PayeeNotFoundException.java
 * @package bankapp.exception
 * @author Mohamed Mansour @2005
 * @email m0.interactive@gmail.com
 * @web www.m0interactive.com
 * @date 9-Dec-2005
 *
 */
public class PayeeNotFoundException extends Exception {

    public PayeeNotFoundException () { }

    public PayeeNotFoundException (String msg) {
        super(msg);
    } 
}

