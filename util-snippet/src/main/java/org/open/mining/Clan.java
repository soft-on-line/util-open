package org.open.mining;

import java.util.Set;

public interface Clan
{
	public boolean contains(int data_index);
	
	public Set<Integer> getDataIndex();
	public void setDataIndex(Set<Integer> dataIndex);
}
