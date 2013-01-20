package nxt.lejos.plotterinterface;

import lejos.nxt.Motor;

import nxt.lejos.data.Constants;
import nxt.lejos.data.Constants.MotorDirections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *	Christopher Ottens
 *	2013
 */

public class MotorController
{
	//-----------------------------------------------------------------------------
	//------------------------Variables--------------------------------------------
	//-----------------------------------------------------------------------------

	private static MotorController instance = null;
	
	//Logger
	private static final Logger logger = LoggerFactory.getLogger(MotorController.class.getName());
	
	private boolean dummyMode = false;
	private boolean isDrawing = false;
	
	private int xPos = 0;
	private int yPos = 0;
	
	//-----------------------------------------------------------------------------
	//------------------------Constructor(s)---------------------------------------
	//-----------------------------------------------------------------------------

	private MotorController()
	{		
		logger.debug("instanziiert");
		
		this.dummyMode = Constants.DUMMY_MODE;
		if (this.dummyMode)
		{
			logger.info("Dummy-Modus");
		}
		else
		{
			this.initMotorZ();
		}
	}
	
	//-----------------------------------------------------------------------------
	//------------------------Methods/Functions------------------------------------
	//-----------------------------------------------------------------------------
	
	public static MotorController getInstance()
	{
		if (instance == null)
		{
			instance = new MotorController();
		}
		return instance;
	}
	
	private void initMotorZ()
	{
		Motor.C.resetTachoCount();
	}
	
	private int getXPos()
	{
		this.xPos = Motor.A.getTachoCount();
		return this.xPos;
	}
	
	private int getYPos()
	{
		this.yPos = Motor.B.getTachoCount();
		return this.yPos;
	}
	
	public boolean isDrawing()
	{
		return this.isDrawing;
	}
	
	public void moveToStartPosition()
	{
		logger.info("Zeichenvorrichtung faehrt Startposition an");

		boolean xMotorRuns = false;
		boolean yMotorRuns = false;
		
		Motor.A.setSpeed(360);
		Motor.B.setSpeed(360);
		
		if (!LimitSensors.getInstance().limitXreached())
		{
			Motor.A.forward();
			xMotorRuns = true;
		}
		
		if (!LimitSensors.getInstance().limitYreached())
		{
			Motor.B.backward();
			yMotorRuns = true;
		}
		
		while (xMotorRuns || yMotorRuns)
		{	
			if (LimitSensors.getInstance().limitXreached() && xMotorRuns)
			{
				Motor.A.stop();
				Motor.A.resetTachoCount();
				xMotorRuns = false;
			}
			
			if (LimitSensors.getInstance().limitYreached() && yMotorRuns)
			{
				Motor.B.stop();
				Motor.B.resetTachoCount();
				yMotorRuns = false;
			}
		}
		logger.info("Schlitten in Ausgangsposition: " + this.getXPos() + "/" + this.getYPos());
	}
	
	public void moveToPoint(int x, int y, int speedX, int speedY)
	{
		logger.info(x + "/" + y + " wird angefahren, Geschwindigkeit: " + speedX + "/" + speedY);
		logger.info("Schlittenposition: " + this.getXPos() + "/" + this.getYPos());
	}
	
	public void draw(boolean b)
	{
		if (b)
		{
			if (isDrawing)
			{
				logger.error("Zeichenvorrichtung ist bereits unten!");
			}
			else
			{
				logger.info("Zeichenvorrichtung wird abgesenkt");
				
				Motor.C.setSpeed(50);
				Motor.C.rotateTo(-50, true);
				
				isDrawing = true;
			}
		}
		else
		{
			if (isDrawing)
			{
				logger.info("Zeichenvorrichtung wird hochgefahren");
				
				Motor.C.setSpeed(50);
				Motor.C.rotateTo(0, true);
				Motor.C.resetTachoCount();
				
				isDrawing = false;
			}
			else
			{
				logger.error("Zeichenvorrichtung ist bereits oben!");
			}
		}
	}
	
	public void move(MotorDirections direction)
	{
		logger.info("Schlitten faehrt nach " + direction);
		
		switch (direction)
		{
			case LEFT_UP:
				Motor.A.backward();
				Motor.B.forward();
				break;
			case UP:
				Motor.A.backward();
				break;
			case RIGHT_UP:
				Motor.A.backward();
				Motor.B.backward();
				break;
			case LEFT:
				Motor.B.forward();
				break;
			case RIGHT:
				Motor.B.backward();
				break;
			case LEFT_DOWN:
				Motor.A.forward();
				Motor.B.forward();
				break;
			case DOWN:
				Motor.A.forward();
				break;
			case RIGHT_DOWN:
				Motor.A.forward();
				Motor.B.backward();
				break;
			default:
				logger.error("Invalide Richtungsangabe, sollte niemals auftreten");
				break;
		}
		
		logger.debug("Motor(en) in Bewegung");
	}
	
	public void stop()
	{
		logger.info("Motor(en) stoppen");
		
		Motor.A.stop();
		Motor.B.stop();
		
		logger.debug("Motor(en) gestoppt");
		
		logger.info("Schlittenposition: " + this.getXPos() + "/" + this.getYPos());
	}
}
