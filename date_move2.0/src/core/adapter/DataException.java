/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.adapter;

/**
 *
 * @author Lection <yujw@linkinways.com>
 */
public class DataException extends Exception{
    private Object errorObject;

    public DataException(Object errorObject) {
        this.errorObject = errorObject;
    }
    public DataException(Object errorObject,String message) {
        super(message);
        this.errorObject = errorObject;
    }

    public Object getErrorObject() {
        return errorObject;
    }
}
