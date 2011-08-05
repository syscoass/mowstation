/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mowbot;

import mowbot.camera.CameraInterface;
import mowbot.serial.SerialInterface;
import mowbot.joystick.*;
import mowbot.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Justin
 */
public class Main {

    private static final String SERVER_IP = "192.168.1.1";
    private static final int    SERVER_PORT = 3001;

    private static final String    CAMERA_IP = "192.168.1.9";
    private static final int    CAMERA_PORT = 80;
    private static final String CAMERA_USER = "admin";
    private static final String CAMERA_PASSWORD = "helloworld";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here

                CommonInterface main;
                
                /* Uncomment for Direct Serial*/
                //SerialInterface serialInterface_ = new SerialInterface();
		//serialInterface_.initialize(true);

                TcpClient tcpInterface_ = new TcpClient();
                if(!tcpInterface_.initialize(SERVER_IP, SERVER_PORT))
                {
                    JOptionPane.showMessageDialog(null, "Unable to connect to "+SERVER_IP+":"+SERVER_PORT);

                }

                main = tcpInterface_;

                CameraInterface camera = new CameraInterface(CAMERA_IP, CAMERA_PORT, CAMERA_USER, CAMERA_PASSWORD);
                mowbot.joystick.Joystick joystick = new mowbot.joystick.Joystick();
                
		System.out.println("Started");

                MowerInterface mower = new MowerInterface(main, camera);
                mower.setVisible(true);

                camera.loadCameraView();
                
                Thread.sleep(5000);
                joystick.initJoystick(mower);

                //Connect to Mower
                tcpInterface_.writeData("ZZZ");

    }

}
