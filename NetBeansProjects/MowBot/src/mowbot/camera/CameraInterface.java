/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mowbot.camera;

import java.io.*;
import java.net.Socket;
import java.net.URL;
/**
 *
 * @author Justin
 */
public class CameraInterface {

    String ip_;
    int port_;

    public final static int PAN_UP = 0;
    public final static int PAN_UP_STOP = 1;

    public final static int PAN_DOWN = 2;
    public final static int PAN_DOWN_STOP = 3;

   public final static int PAN_LEFT = 4;
   public final static int PAN_LEFT_STOP = 5;

   public final static int PAN_RIGHT = 6;
   public final static int PAN_RIGHT_STOP = 7;

   public final static int CENTER_CAMERA = 25;

   public final static int VERTICAL_PATROL = 26;
   public final static int HORIZON_PATROL = 28;

   private String user_;
   private String pass_;
    //FOSCAM WebCam Interface
    public CameraInterface(String ip, int port, String user, String pass)
    {
        ip_ = ip;
        port_ = port;

        user_ = user;

        pass_ = pass;
    }
    
    public void StopCamera()
    {
        this.runCommand(CameraInterface.PAN_RIGHT_STOP);
        this.runCommand(CameraInterface.PAN_LEFT_STOP);
        this.runCommand(CameraInterface.PAN_UP_STOP);
        this.runCommand(CameraInterface.PAN_RIGHT_STOP);

    }

    public void loadCameraView(){
         try{

            final java.net.URI uri = new java.net.URI("http://"+ip_+":"+port_+"/live.htm"); //videostream.cgi?user="+user_+"&pwd="+pass_);

            if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                    try {
                            desktop.browse(uri);
                    } catch (java.io.IOException e) {
                            // TODO: error handling
                    }
            } else {
                    // TODO: error handling
            }
        }catch(Exception e){

        }
    }

    public void runCommand(int command){
            URL u;
            InputStream is = null;
            DataInputStream dis;
            String s;
            String url = "http://"+ip_+":"+port_+"/decoder_control.cgi?command="+command+"&user="+user_+"&pwd="+pass_;
            try
            {
              u = new URL(url);
              is = u.openStream();
              dis = new DataInputStream(new BufferedInputStream(is));
              while ((s = dis.readLine()) != null)
              {
                System.out.println(s);
              }
            }
            catch (java.net.MalformedURLException mue)
            {
              System.err.println("Ouch - a MalformedURLException happened.");
              mue.printStackTrace();
              System.exit(2);
            }
            catch (java.io.IOException ioe)
            {
              System.err.println("Oops- an IOException happened.");
              ioe.printStackTrace();
              System.exit(3);
            }
            finally
            {
              try
              {
                is.close();
              }
              catch (IOException ioe)
              {
              }
            }
    }
   
}
