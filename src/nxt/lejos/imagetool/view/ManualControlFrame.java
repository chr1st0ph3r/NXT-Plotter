package nxt.lejos.imagetool.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nxt.lejos.data.Constants;
import nxt.lejos.data.Constants.MotorDirections;
import nxt.lejos.imagetool.actions.listener.TimedKeyAdapter;
import nxt.lejos.imagetool.view.components.DirectionButton;
import nxt.lejos.plotterinterface.MotorController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualControlFrame extends JDialog
{
	//-----------------------------------------------------------------------------
	//-----------------------------Variables---------------------------------------
	//-----------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	
	//kein singleton, dient zum externen Zugriff auf die Checkbox wenn malen per Tastendruck gestartet wird
	//ausserst unelegant, mir fiel grad nichts besseres ein
	private static ManualControlFrame instance = null;
	
	//Logger
	private static final Logger logger = LoggerFactory.getLogger(ManualControlFrame.class.getName());
	
	//ButtonPanel
	private JPanel buttonPanel = new JPanel();

	private DirectionButton leftUpButton = new DirectionButton(MotorDirections.LEFT_UP);
	private DirectionButton upButton = new DirectionButton(MotorDirections.UP);
	private DirectionButton rightUpButton = new DirectionButton(MotorDirections.RIGHT_UP);
	private DirectionButton leftButton = new DirectionButton(MotorDirections.LEFT);
	private DirectionButton rightButton = new DirectionButton(MotorDirections.RIGHT);
	private DirectionButton leftDownButton = new DirectionButton(MotorDirections.LEFT_DOWN);
	private DirectionButton downButton = new DirectionButton(MotorDirections.DOWN);
	private DirectionButton rightDownButton = new DirectionButton(MotorDirections.RIGHT_DOWN);
	
	//ControlPanel
	private JPanel controlPanel = new JPanel();
	
	private JButton startPosButton = new JButton("Startposition anfahren");
	private JCheckBox drawCheckBox = new JCheckBox("malen");

	//-----------------------------------------------------------------------------
	//-----------------------------Constructor(s)----------------------------------
	//-----------------------------------------------------------------------------

	public ManualControlFrame()
	{
		logger.debug("instanziiert");
		
		instance = this;
		
		this.initBehaviour();
		this.initAppearance();
		this.initContent();
		this.initKeyListener();
		
		this.pack();
		this.setLocationRelativeTo(ProgramFrame.getInstance());
		this.setVisible(true);
	}
	
	//-----------------------------------------------------------------------------
	//-----------------------------Methods/Functions-------------------------------
	//-----------------------------------------------------------------------------
	
	private void initBehaviour()
	{
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	private void initAppearance()
	{
		this.setTitle("Manuelle Steuerung");
		
		this.setSize(300, 300);
		this.setResizable(false);
		
		this.setLayout(new BorderLayout());
	}
	
	private void initContent()
	{
		//ButtonPanel
		this.buttonPanel.setLayout(new GridLayout(3, 3));
		
		this.buttonPanel.add(this.leftUpButton);
		this.buttonPanel.add(this.upButton);
		this.buttonPanel.add(this.rightUpButton);
		this.buttonPanel.add(this.leftButton);
		this.buttonPanel.add(new JPanel());
		this.buttonPanel.add(this.rightButton);
		this.buttonPanel.add(this.leftDownButton);
		this.buttonPanel.add(this.downButton);
		this.buttonPanel.add(this.rightDownButton);
		
		this.add(buttonPanel, BorderLayout.CENTER);
		
		//ControlPanel
		this.controlPanel.setLayout(new FlowLayout());
		
		this.controlPanel.add(this.startPosButton);
		this.startPosButton.addActionListener(new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MotorController.getInstance().moveToStartPosition();
			}
		});
		
		//DrawCheckBox mit Actionlistener
		this.controlPanel.add(this.drawCheckBox);
		
		this.drawCheckBox.addActionListener(new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//Die draw() bekommt gleich den boolean-Wert ob die Checkbox gesetzt ist
				MotorController.getInstance().draw(drawCheckBox.isSelected());
			}
		});
		//Ueberpfuefe Position der Malvorrichtung und setze CheckBox entsprechend
		this.drawCheckBox.setSelected(MotorController.getInstance().isDrawing());
		
		this.add(this.controlPanel, BorderLayout.SOUTH);
		
		//Border
		this.buttonPanel.setBorder(Constants.PREFERED_BORDER_TYPE);
		this.controlPanel.setBorder(Constants.PREFERED_BORDER_TYPE);
	}
	
	private void initKeyListener()
	{
		TimedKeyAdapter keyListener = new TimedKeyAdapter(true, 10);

		//Wird alles Komponenten der beiden Panels hinzugefügt
		for (Component component: this.buttonPanel.getComponents())
		{
			component.addKeyListener(keyListener);
		}
		for (Component component: this.controlPanel.getComponents())
		{
			component.addKeyListener(keyListener);
		}
	}
	
	public void setCheckBox(boolean b)
	{
		this.drawCheckBox.setSelected(b);
	}
	
	public static ManualControlFrame getInstance()
	{
		return instance;
	}
}
