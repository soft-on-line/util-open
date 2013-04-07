package org.open.mining;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.open.Algorithm;
import org.open.util.OperationUtil;

public class KSector implements ClusterAlgorithm {

	private double[][] data;

	private int        k;

	public KSector(double[][] data, int k) {
		this.data = data;
		this.k = k;
	}

	private class _Clan implements Clan {

		private Set<Integer> dataIndex = new HashSet<Integer>();

		//		private double[] averageData;

		public _Clan() {

		}

		//		public _Clan(int index,double[] data)
		//		{
		//			this.dataIndex.add(index);
		//			this.averageData = data;
		//		}

		private void addDadaIndex(int data_index) {
			this.dataIndex.add(data_index);
			//balancing();
		}

		public boolean contains(int data_index) {
			return dataIndex.contains(data_index);
		}

		//		private void balancing()
		//		{
		//			for(int i=0;i<averageData.length;i++)
		//			{
		//				double sum = 0;
		//				Iterator<Integer> e = dataIndex.iterator();
		//				while(e.hasNext()){
		//					int dataIndex = e.next();
		//					sum += data[dataIndex][i];
		//				}
		//				averageData[i] = sum/averageData.length;
		//			}
		//		}

		public Set<Integer> getDataIndex() {
			return dataIndex;
		}

		public void setDataIndex(Set<Integer> dataIndex) {
			this.dataIndex = dataIndex;
		}
	}

	private class KClan {

		_Clan[] clan;

		//		Map<Integer,_Clan> map = new HashMap<Integer,_Clan>();

		public KClan(double[][] data, int k) {
			clan = new _Clan[k];
			for (int i = 0; i < k; i++) {
				clan[i] = new _Clan();
			}

			//			clan[0] = new _Clan(0,data[0]);
			//			map.put(0, clan[0]);
			//			
			//			clan[1] = new _Clan(5,data[5]);
			//			map.put(5, clan[1]);
			//			
			//			clan[2] = new _Clan(10,data[10]);
			//			map.put(10, clan[2]);
			//			
			//			clan[3] = new _Clan(15,data[15]);
			//			map.put(15, clan[3]);
		}

		//		public int whereDataIndexInClan(int data_index)
		//		{
		//			for(int i=0;i<clan.length;i++)
		//			{
		//				_Clan _clan = clan[i];
		//				if(_clan == null){
		//					return -1;
		//				}else{
		//					if(_clan.contains(data_index)){
		//						return i;
		//					}
		//				}
		//			}
		//			return -1;
		//		}
		//		
		//		public void clusting(int clan_index,int data_index)
		//		{
		//			//得到data_index以前所在的族，然后删除。
		//			_Clan _clan = map.get(data_index);
		//			if(_clan != null){
		//				_clan.dataIndex.remove(data_index);
		//			}
		//			
		//			//新的索引族加入该data_index。
		//			clan[clan_index].addDadaIndex(data_index);
		//			map.put(data_index, clan[clan_index]);
		//		}
	}

	private Clan[] clusting(KClan kclan) {
		_Clan[] clan = kclan.clan;
		double[] center = Algorithm.average(data);
		//		double[] center = data[10];
		Double[] scoreData = new Double[data.length];
		for (int i = 0; i < data.length; i++) {
			scoreData[i] = Algorithm.distanceCos(center, data[i]);
		}
		System.out.println("Center:" + OperationUtil.toList(center));
		System.out.println("Score:" + OperationUtil.toList(scoreData));
		System.out.println("ScoreData:" + OperationUtil.sort(scoreData));

		List<Integer> dataIndex = OperationUtil.sort(scoreData);

		for (int i = 0; i < k; i++) {
			int tmp = data.length / k;
			int s_i = tmp * i;
			int e_i = tmp * i + tmp;
			for (int j = s_i; j < e_i; j++) {
				clan[i].addDadaIndex(dataIndex.get(j));
			}
		}

		return clan;
	}

	public Clan[] run() {
		return clusting(new KClan(data, k));
	}
}
