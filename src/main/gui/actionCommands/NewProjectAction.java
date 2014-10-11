/**
 * 
 */
package gui.actionCommands;

import gui.GuiConsts;
import helper.Consts;
import helper.ProcGenException;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logic.LogicFacade;

/**
 * @author DINESH THANGAVEL
 *
 */
public class NewProjectAction extends AbstractAction{
	
	JFrame parentWindowFrame;
	
	public NewProjectAction(String string,JFrame parentFrame) {
		super(GuiConsts.NEW_PROJECT_ACTION);
		this.parentWindowFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		System.out.println("Received:" + evt.getActionCommand());
		
		// TODO : Add validation of project name
		Container c = parentWindowFrame.getContentPane();
		JOptionPane prjNamePane = new JOptionPane(GuiConsts.ENTER_PROJECT_NAME,JOptionPane.QUESTION_MESSAGE);
		c.add(prjNamePane);		
		
		String projectName = (String) JOptionPane.showInputDialog(parentWindowFrame, GuiConsts.ENTER_PROJECT_NAME, GuiConsts.PROJECT_NAME, JOptionPane.QUESTION_MESSAGE, null, null, null);
		System.out.println("Project Name:" + projectName);
		
		while(projectName.length()  == 0){
			projectName = (String) JOptionPane.showInputDialog(parentWindowFrame, GuiConsts.PROJECT_NAME_NOT_EMPTY + GuiConsts.ENTER_PROJECT_NAME,GuiConsts.PROJECT_NAME,JOptionPane.QUESTION_MESSAGE,null,null,null);
		}
		
		LogicFacade logicInterface = new LogicFacade();
		try {
			String output = logicInterface.processInput(Consts.CommandInputText.NEW_PROJECT + " " + projectName);
			System.out.println(output);
			
			parentWindowFrame.setTitle(projectName);
			c.remove(prjNamePane);
			c.revalidate();
		} catch (ProcGenException e) {
			JOptionPane.showMessageDialog(parentWindowFrame, e.getMessage());
			e.printStackTrace();
		}
		
	}
}
