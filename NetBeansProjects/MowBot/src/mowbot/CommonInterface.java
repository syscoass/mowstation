/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mowbot;

/**
 *
 * @author justin.hawkins
 */
public class CommonInterface {

    public void CommonInterface()
    {}

    public synchronized void writeData(String data){
        System.out.println("Failed to write "+data+" Override this default implementation.");
    }
}
