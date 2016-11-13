package portfolio1;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

//Look at example 6 for Add/Remove code
@SuppressWarnings("serial")
public class DataModel<E> extends AbstractListModel<E> 
{

	public ArrayList<E> arrListData = new ArrayList<E>();
	
	@Override
	public E getElementAt(int index) 
	{		
		return arrListData.get(index);
	}

	@Override
	public int getSize() 
	{		
		return arrListData.size();
	}

	public void addItem(E newItem)
	{
		arrListData.add(newItem);
		this.fireIntervalAdded(this,  arrListData.size() - 1, arrListData.size() - 1);
	}
	
	public void removeItem(int index)
	{
		arrListData.remove(index);
		this.fireIntervalRemoved(this,  arrListData.size() - 1, arrListData.size() - 1);
	}
	
}
