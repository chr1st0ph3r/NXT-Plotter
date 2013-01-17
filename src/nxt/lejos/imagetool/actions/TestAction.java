package nxt.lejos.imagetool.actions;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;

import nxt.lejos.plotterinterface.Functions;

public class TestAction extends AbstractAction
{
	//-----------------------------------------------------------------------------
	//-----------------------------Variables---------------------------------------
	//-----------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	
	//-----------------------------------------------------------------------------
	//-----------------------------Constructor(s)----------------------------------
	//-----------------------------------------------------------------------------

	//-----------------------------------------------------------------------------
	//-----------------------------Methods/Functions-------------------------------
	//-----------------------------------------------------------------------------

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Vector<Vector<Integer>> testVector = new Vector<Vector<Integer>>();
		for (int vector=0; vector<4; vector++)
		{
			Vector<Integer> temp = new Vector<Integer>(3);
			temp.add((int)(Math.random()*10));
			temp.add((int)(Math.random()*10));
			temp.add(1);
			for (int i=0; i<temp.size(); i++)
			{
				System.out.print(temp.get(i) + " ");
			}
			System.out.println();
			testVector.add(temp);
		}
		
		Vector<Vector<Integer>> pathData = Functions.calcPathData(testVector);
		for (int i=0; i<pathData.size(); i++)
		{
			for (int j=0; j<pathData.get(i).size(); j++)
			{
				System.out.print(pathData.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}
}
