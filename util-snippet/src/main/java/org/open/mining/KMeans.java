package org.open.mining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.open.Algorithm;
import org.open.util.OperationUtil;

/**
 * 用于数据聚类 k-means 算法
 * @author 覃芝鹏
 * @version $Id: KMeans.java,v 1.14 2008/08/04 06:13:21 moon Exp $
 */
public final class KMeans implements ClusterAlgorithm
{
	private double[][] data;
	
	private int k;
	
	public KMeans(double[][] data,int k)
	{
		this.data = data;
		this.k = k;
		if(1>data.length){
			throw new KMeansException("data first-dimension's length must big than 0.");
		}
		if(1>data[0].length){
			throw new KMeansException("data second-dimension's length must big than 0.");
		}
		if(k>data.length){
			throw new KMeansException("cluster k must not big than data first-dimension's length.");
		}
	}
	
	private class _Clan implements Clan
	{
		private Set<Integer> dataIndex = new HashSet<Integer>();
		
		private double[] averageData;
		
		public _Clan(int index,double[] data)
		{
			this.dataIndex.add(index);
			this.averageData = data;
		}
		
		private void addDadaIndex(int data_index)
		{
			this.dataIndex.add(data_index);
			balancing();
		}
		
		public boolean contains(int data_index){
			return dataIndex.contains(data_index);
		}
		
		private void balancing()
		{
			for(int i=0;i<averageData.length;i++)
			{
				double sum = 0;
				Iterator<Integer> e = dataIndex.iterator();
				while(e.hasNext()){
					int dataIndex = e.next();
					sum += data[dataIndex][i];
				}
				averageData[i] = sum/averageData.length;
			}
		}
		
		public Set<Integer> getDataIndex() { return dataIndex; }
		public void setDataIndex(Set<Integer> dataIndex) { this.dataIndex = dataIndex; }
	}
	
	private class KClan
	{
		_Clan[] clan;
		
		Map<Integer,_Clan> map = new HashMap<Integer,_Clan>();
		
		public KClan(double[][] data,int k)
		{
			clan = new _Clan[k];
			
			double[] center = Algorithm.average(data);
			Double[] scoreData = new Double[data.length];
			for(int i=0;i<data.length;i++){
				scoreData[i] = Algorithm.distanceCos(center, data[i]);
			}
			
			List<Integer> dataIndex = OperationUtil.sort(scoreData);
			
//			for(int i=0;i<k;i++)
//			{
//				int tmp = data.length/k;
//				int s_i = tmp*i;
//				int e_i = tmp*i+tmp;
//				for(int j=s_i;j<e_i;j++)
//				{
//					clan[i].addDadaIndex(dataIndex.get(j));
//				}
//			}
			
			for(int i=0;i<k;i++)
			{
//				int data_index = RandomUtil.nextInt(data.length);
//				int data_index = i;
				int tmp = data.length/k;
				int s_i = tmp*i;
				int data_index = dataIndex.get(s_i);
				clan[i] = new _Clan(data_index,data[data_index]);
				map.put(data_index, clan[i]);
			}
			
//			clan[0] = new Clan(0,data[0]);
//			map.put(0, clan[0]);
//			
//			clan[1] = new Clan(5,data[5]);
//			map.put(5, clan[1]);
//			
//			clan[2] = new Clan(10,data[10]);
//			map.put(10, clan[2]);
//			
//			clan[3] = new Clan(15,data[15]);
//			map.put(15, clan[3]);
		}
		
		public int whereDataIndexInClan(int data_index)
		{
			for(int i=0;i<clan.length;i++)
			{
				Clan _clan = clan[i];
				if(_clan == null){
					return -1;
				}else{
					if(_clan.contains(data_index)){
						return i;
					}
				}
			}
			return -1;
		}
		
//		public boolean matchDataIndex(int clan_index,int data_index)
//		{
//			Clan _clan = map.get(data_index);
//			if(_clan == null){
//				return false;
//			}else{
//				return _clan.equals(clan[clan_index]);
//			}
//		}
		

		
		public void clusting(int clan_index,int data_index)
		{
			//得到data_index以前所在的族，然后删除。
			_Clan _clan = map.get(data_index);
			if(_clan != null){
				_clan.dataIndex.remove(data_index);
			}
			
			//新的索引族加入该data_index。
//			clan[clan_index].dataIndex.add(data_index);
			
//			clan[clan_index].data = balancing(data,clan[clan_index].dataIndex);
			clan[clan_index].addDadaIndex(data_index);
			map.put(data_index, clan[clan_index]);
		}
	}
	
	private Clan[] clusting(KClan kclan)
	{
		int data_index = 0;
		Set<Integer> matched = new HashSet<Integer>();
		while(true)
		{
			if(data_index >= data.length){
				data_index = 0;
			}
			
			double min_distance = Double.MAX_VALUE;
//			double max_distance = Double.MIN_VALUE;
			int clan_index = 0;
			for(int i=0;i<k;i++)
			{
				double distance = Algorithm.distanceCos(data[data_index],kclan.clan[i].averageData);
//				double distance = Algorithm.distanceVariance(data[data_index],kclan.clan[i].averageData);
				System.out.println("Clan "+i+" distance is "+distance);
				if(distance < min_distance){
//				if(distance < max_distance){
					min_distance = distance;
//					max_distance = distance;
					clan_index = i;
				}
			}
			
//			boolean match = kclan.matchIndex(clan_index, data_index);
			int match = kclan.whereDataIndexInClan(data_index);
			
			System.out.println("match:"+match+" clan_index:"+clan_index+" data_index:"+data_index+" min_distance:"+min_distance);
//			System.out.println("match:"+match+" clan_index:"+clan_index+" data_index:"+data_index+" max_distance:"+max_distance);
			for(int i=0;i<kclan.clan.length;i++)
			{
				System.out.println("clan "+i+kclan.clan[i].dataIndex);
			}
			System.out.println(matched);
			
			if(match==clan_index){
				matched.add(data_index);
				if(matched.size() == data.length){
					break;
				}
			}else{
				kclan.clusting(clan_index, data_index);
			}
			
			data_index++;
		}
		
		return kclan.clan;
	}
	
	public Clan[] run()
	{
		return clusting(new KClan(data,k));
	}

}
