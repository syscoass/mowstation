/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mowbot.joystick;

import mowbot.*;
import de.hardcode.jxinput.*;
import de.hardcode.jxinput.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
/**
 *
 * @author Justin
 */
public class Joystick
	implements ActionListener{

    JXInputDevice joystick_ = null;
    MowerInterface mower_;

    public final static int HAT_UP = 0;
    public final static int HAT_RIGHT = 9000;
    public final static int HAT_DOWN = 18000;
    public final static int HAT_LEFT = 27000;
    
    public void Joystick()
    {
    }

    public void initJoystick(MowerInterface mower)
    {
        mower_ = mower;
         int cnt = JXInputManager.getNumberOfDevices();

        if(cnt > 0)
        {
            joystick_ = JXInputManager.getJXInputDevice( 0 );
            System.out.println("Found Joystick: "+joystick_.getName());
            mower_.SetJoystickStateLabel(joystick_.getName() + " Connected.");
            new Timer( 50, this ).start();

            System.out.println("Joystick Timer Started.");
        }else{
             System.out.println("No Joysticks found... :( ");
             mower_.SetJoystickStateLabel("No Joystick Found!");
        }
         
    }

    /**
     * Implement ActionListener#actionPerformed().
	 * This is called by the Timer.
     */
	public void actionPerformed( ActionEvent e )
	{
                JXInputManager.updateFeatures();
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					if(joystick_ != null){
                                            for(int i = 0; i < joystick_.getNumberOfButtons(); i++)
                                            {
                                                if(joystick_.getButton(i).getState()==true){
                                                    mower_.ButtonDown(i);
                                                }else{
                                                    mower_.ButtonUp(i);
                                                }
                                            }

                                            for(int a = 0; a < joystick_.getNumberOfAxes(); a++)
                                            {
                                                Axis taxis = joystick_.getAxis(a);

                                                if(taxis != null)
                                                {
                                                    //System.out.println("");
                                                    mower_.UpdateJoystickAxis(a, taxis.getValue());
                                                }
                                            }

                                            for(int d = 0; d < joystick_.getNumberOfDirectionals(); d++){
                                                mower_.SetHatValue(joystick_.getDirectional(d).getDirection(), joystick_.getDirectional(d).getValue());
                                            }
                                        }
				}
			}
		);
	}
}
