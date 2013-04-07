package org.open.mining.apriori;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 存储频繁项集
 * @author Lam
 */
class FrequentItemTree {

	FrequentItemTree() {
		root = null;
	}

	/**
	 * 向二叉树中插入数据
	 * @param thePattern 字符串
	 * @param lev 项集的层数
	 */
	public void insert(String thePattern, int lev) {
		//root=insert(thePattern,lev,root);
		root = insert2(thePattern, lev, root);
	}

	/**
	 * 使用了常规比较方法的insert操作
	 * 在节点t下insert
	 * @param thePattern 要插入的数据
	 * @param lev 层数
	 * @param t 节点
	 * @return
	 */
	protected FrequentNode insert(String thePattern, int lev, FrequentNode t) {
		if (t == null)
			t = new FrequentNode(thePattern, lev);
		else {
			if (thePattern.compareTo(t.pattern) < 0)
				t.left = insert(thePattern, lev, t.left);
			else if (thePattern.compareTo(t.pattern) > 0)
				t.right = insert(thePattern, lev, t.right);
			else
				;
		}
		return t;
	}

	/**
	 * 使用了特殊比较方法的insert操作
	 * 向节点t插入数据
	 * @param thePattern
	 * @param lev
	 * @param t
	 * @return
	 */
	private FrequentNode insert2(String thePattern, int lev, FrequentNode t) {
		if (t == null)
			t = new FrequentNode(thePattern, lev);
		else {
			if (compare(thePattern, t.pattern) < 0)
				t.left = insert2(thePattern, lev, t.left);

			else if (compare(thePattern, t.pattern) > 0)
				t.right = insert2(thePattern, lev, t.right);

			else
				//存在相同元素,不操作
				;
		}
		return t;
	}

	/**
	 * 在整个树中查找指定的字符串模式
	 * @param pattern
	 * @return
	 */
	public boolean contains(String pattern) {

		return contains2(pattern, root);
	}

	/**
	 * 使用常规比较方法的contians
	 * 在指定节点t下查找指定的字符串模式thePattern
	 * @param thePattern
	 * @param t
	 * @return
	 */
	private boolean contains(String thePattern, FrequentNode t) {
		if (t == null)
			return false;
		else {
			if (thePattern.compareTo(t.pattern) < 0)
				return contains(thePattern, t.left);
			else if (thePattern.compareTo(t.pattern) > 0)
				return contains(thePattern, t.right);
			else
				return true;
		}
	}

	/**
	 * 使用了特殊比较方法的contains
	 * 在指定节点下查找指定模式
	 * @param thePattern 模式字符串
	 * @param t
	 * @return
	 */
	private boolean contains2(String thePattern, FrequentNode t) {
		if (t == null)
			return false;
		else {
			if (compare(thePattern, t.pattern) < 0)
				return contains(thePattern, t.left);
			else if (compare(thePattern, t.pattern) > 0)
				return contains(thePattern, t.right);
			else
				return true;
		}
	}

	/**
	 * 输出二叉树存储的结果
	 */
	public void printTree() {
		System.out.println("频繁项集列表:");
		printTree(root);
		System.out.println("结果同时保存到文件\"FrequentItems_List.txt\"");
	}

	/**
	 * 显示指定节点下的所有频繁项集
	 * @param t
	 */
	private void printTree(FrequentNode t) {
		if (t != null) {
			printTree(t.left);
			System.out.print("第" + t.level + "层频繁项集:" + t.pattern);
			System.out.println();
			printTree(t.right);
		}
	}

	/**
	 * 遍历整棵树,将结果保存到文件中
	 * @throws IOException
	 */
	public void saveTree() throws IOException {
		File outputFile = new File("FrequentItems_List.txt");
		if (outputFile.exists())
			outputFile.delete();
		FileOutputStream fout = new FileOutputStream(outputFile);
		PrintWriter pWriter = new PrintWriter(fout);
		pWriter.println("频繁项集列表:");
		saveTree(root, pWriter);
		pWriter.flush();
		pWriter.close();
	}

	/**
	 * 将节点t下的内容保存到文件中
	 * @param t 节点
	 * @param pWriter 文件输出流
	 */
	private void saveTree(FrequentNode t, PrintWriter pWriter) {
		if (t != null) {
			saveTree(t.left, pWriter);
			pWriter.print("第" + t.level + "层频繁项集:" + t.pattern);
			pWriter.println();
			saveTree(t.right, pWriter);
		}
	}

	/**
	 * 特殊的比较方法
	 * 字符串长度较长的比较大
	 * @param p1 字符串1
	 * @param p2 字符串2
	 * @return 大于0,等于0,小于0
	 */
	private int compare(String p1, String p2) {
		int len1 = p1.length();
		int len2 = p2.length();
		if (len1 < len2)
			return -1;
		else if (len1 > len2)
			return 1;
		else {
			return p1.compareTo(p2);
		}
	}

	FrequentNode root;
}

/**
 * 节点类
 * @author Lam
 */
class FrequentNode {

	FrequentNode(String thePattern, int theLevel) {
		this(thePattern, theLevel, null, null);
	}

	FrequentNode(String thePattern, int theLevel, FrequentNode lt, FrequentNode rt) {
		pattern = thePattern;
		level = theLevel;
		//hashcode=code;
		left = lt;
		right = rt;
	}

	//int hashcode;
	String       pattern;
	int          level;
	FrequentNode left;
	FrequentNode right;
}

/**
 * 可以比较链表大小的数组型链表
 * @author Lam
 * @param <AnyType>
 */
class SArrayList<AnyType> extends ArrayList<String> {

	/**
     * 
     */
	private static final long serialVersionUID = 7429775912226160957L;

	/**
	 * 比较两个链表字符串的大小
	 * @param other
	 * @return
	 */
	public int compareTo(SArrayList<?> other) {
		int i = 0;
		while (i < this.size() && i < other.size()) {
			String s1 = this.get(i).toString();
			String s2 = other.get(i).toString();
			if (s1.compareTo(s2) > 0)
				return 1;
			else if (s1.compareTo(s2) < 0)
				return -1;
			else
				i++;
		}
		if (i < this.size()) //this字符串比较长
			return 1;
		else if (i < other.size()) //other字符串比较长
			return -1;
		else
			return 0; //两个字符串长度相等
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < this.size(); i++)
			s += this.get(i);
		return s;
	}

}

/**
 * 项集
 * @author Lam
 */
class ItemSet {

	ItemSet(String s) {
		pattern = new SArrayList<String>();
		pattern.add(s);
		//pattern=s;
		counts = 1;
	}

	ItemSet(SArrayList<String> list) {
		pattern = list;
		counts = 1;
	}

	SArrayList<String> pattern;
	//String pattern;
	int                counts;
}

/**
 * @author Lam
 */
public class Apriori {

	public Apriori() {
		ItemSets = new ArrayList<String>();
		this.freHs = new HashSet<String>();
		this.iTree = new FrequentItemTree();
	}

	/**
	 * 读取数据集
	 * 不要求数据集的每一行是已经排序的
	 * @param fileName
	 * @throws Exception
	 */
	public void ReadData() throws Exception {
		//File dataFile=new File(fileName);

		//		FileInputStream fin=new FileInputStream(dataFile);
		BufferedReader cin = new BufferedReader(new InputStreamReader(Apriori.class.getResourceAsStream("data.in")));
		String line = cin.readLine();
		while (line != null) {
			ItemSets.add(line);
			line = cin.readLine();
		}
		//ItemSets.add(null);
	}

	/**
	 * 程序运行接口方法
	 * @param fileName 数据文件地址
	 * @param minsup 最小支持度
	 * @throws Exception
	 */
	public void startApriori(int minsup) throws Exception {
		//读取数据
		ReadData();
		ArrayList<ItemSet> sets = new ArrayList<ItemSet>();
		//获取第一层频繁项集
		sets = this.getL1Set();
		sets = this.genFrequentSet(sets, minsup);
		//对项集排序
		sets = this.sortArray(sets);

		int k = 1;
		sets = this.genkLSet(sets, k);
		sets = this.checkSubSet(sets);
		sets = this.countItemSet(sets);
		sets = this.genFrequentSet(sets, minsup);
		//将第一层频繁项集排序,作为生成第二层的输入,以后生成的都是有顺序的
		sets = this.sortArray(sets);
		//当频繁项集不为空时,获取第k层频繁项集
		while (sets.size() != 0) {
			k++;
			sets = this.genkLSet(sets, k); //生成k层项集
			sets = this.checkSubSet(sets); //检查n-1子集
			sets = this.countItemSet(sets); //对项集计数
			sets = this.genFrequentSet(sets, minsup);
			//	sets=this.sortArray(sets); //此处不用再排序
		}
		//显示结果
		showResult();
	}

	/**
	 * 显示所有频繁项集
	 */
	private void showResult() throws IOException {
		iTree.printTree();
		iTree.saveTree();
	}

	/**
	 * 返回模式所在ItemSet的下标
	 * @param array 项集
	 * @param pattern 字符串模式
	 * @return
	 */
	private int contains(ArrayList<ItemSet> array, String pattern) {
		int i = 0;
		for (; i < array.size(); i++) {
			ItemSet item = array.get(i);
			if (item.pattern.toString().equals(pattern))
				return i;
		}
		//找不到指定模式
		return -1;
	}

	/**
	 * 扫描数据集的每一行,
	 * 对项集中模式出现的次数计数
	 * @param array k层项集(删除了其n-1子集不是频繁项集的部分)
	 * @return 更新后的项集
	 */
	private ArrayList<ItemSet> countItemSet(ArrayList<ItemSet> array) {
		for (ItemSet item : array) {
			//对于ItemSets(存储了输入文件数据的结构)的每一行,判断是否出现item的模式字符串
			for (String s : ItemSets) {
				int j = 0;
				while (j < item.pattern.size() && s.indexOf(item.pattern.get(j)) != -1)
					j++;
				if (j == item.pattern.size()) //整个字符串中的每一部分都出现
					item.counts++;
			}
			item.counts--;
		}
		return array;
	}

	/**
	 * 删除n-1项子集非频繁集的项集
	 * @param superSet 项集
	 * @return 频繁集的超集
	 */
	private ArrayList<ItemSet> checkSubSet(ArrayList<ItemSet> superSet) {
		ArrayList<ItemSet> newSet = new ArrayList<ItemSet>();//存储结果的项集表
		for (ItemSet item : superSet) {
			//如果这个项集的所有子集都是频繁的,则将它加到新的集合中
			if (checkN_1SubSet(item))
				newSet.add(item);
		}
		superSet = newSet;
		return superSet;
	}

	/**
	 * 利用哈希表测试项集的每一个n-1组合是否是频繁项
	 * @param item 项集
	 * @return
	 */
	private boolean checkN_1SubSet(ItemSet item) {
		//ht 存储了所有频繁项集的全局哈希表
		HashSet<?> hs = this.freHs;
		int n = item.pattern.size();
		int[] pattern = new int[n - 1]; //字符0~n-2
		for (int i = 0; i < n - 1; i++) {
			pattern[i] = i;
		}
		String testPattern = "";
		for (int k = 0; k < pattern.length; k++)
			testPattern += item.pattern.get(pattern[k]);
		if (!hs.contains(testPattern)) //测试第一个n-1组合01234……n-2
			return false;
		//测试剩下的n-1组合
		for (int i = n - 2; i >= 0; i--) {
			int[] newpattern = pattern.clone(); //存储新的n-1序列

			for (int j = i; j <= n - 2; j++) {
				newpattern[j]++;
			}
			testPattern = "";
			for (int k = 0; k < newpattern.length; k++)
				testPattern += item.pattern.get(newpattern[k]);
			if (!hs.contains(testPattern))
				return false;
		}
		//所有的n-1子集都是频繁项
		return true;
	}

	/**
	 * 返回第一层的ItemSet(频繁集的超集)
	 * 没有经过筛选
	 * @return
	 */
	private ArrayList<ItemSet> getL1Set() {
		ArrayList<ItemSet> L1Set = new ArrayList<ItemSet>();
		for (String s : ItemSets) {
			if (s != null) {
				String[] elements = s.split(",");
				for (String e : elements) {
					int index = contains(L1Set, e);
					if (index != -1) {
						L1Set.get(index).counts++;
					} else {
						ItemSet item = new ItemSet(e);
						L1Set.add(item);
					}
				}
			}
		}
		return L1Set;
	}

	/**
	 * 生成k层项集
	 * @param array k-1层项集
	 * @param level 层数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<ItemSet> genkLSet(ArrayList<ItemSet> array, int level) {
		ArrayList<ItemSet> resultArray = new ArrayList<ItemSet>();
		ArrayList<ItemSet> oldarray = array;
		if (level == 1) //生成第二层的项集
		{
			for (int i = 0; i < oldarray.size() - 1; i++) {

				SArrayList<String> ls = oldarray.get(i).pattern;
				//简单的将两个模式拼凑起来
				for (int j = i + 1; j < array.size(); j++) {
					SArrayList<String> tmp = new SArrayList<String>();
					for (int pi = 0; pi < ls.size(); pi++)
						tmp.add(ls.get(pi));
					SArrayList<String> ls2 = array.get(j).pattern;
					for (int pi2 = 0; pi2 < ls2.size(); pi2++)
						tmp.add(ls2.get(pi2));
					ItemSet item = new ItemSet(tmp);
					resultArray.add(item);
				}
			}
		} else {
			//生成两层以上的项集
			for (int i = 0; i < oldarray.size() - 1; i++) {
				SArrayList<String> list = oldarray.get(i).pattern;
				for (int j = i + 1; j < array.size(); j++) {
					SArrayList<String> list2 = array.get(j).pattern;
					int lev = 0;
					while (list.get(lev).compareTo(list2.get(lev)) == 0)
						lev++;
					//有level个元素是相同的
					if (lev == level - 1 && list.get(lev).compareTo(list2.get(lev)) < 0) {
						SArrayList<String> newList = (SArrayList<String>) list.clone();

						newList.add(list2.get(lev));
						ItemSet item = new ItemSet(newList);
						resultArray.add(item);
					}
				}
			}
		}

		return resultArray;

	}

	/**
	 * 将项集转换为频繁集
	 * @param superSet 频繁项集的超集
	 * @param minsup 最小支持度计数
	 * @return
	 */
	private ArrayList<ItemSet> genFrequentSet(ArrayList<ItemSet> superSet, int minsup) {
		ArrayList<ItemSet> array = new ArrayList<ItemSet>();
		HashSet<String> hs = this.freHs;
		for (ItemSet item : superSet) {
			if (item.counts >= minsup) {
				//将频繁项存储到哈希表中
				hs.add(item.pattern.toString());
				//	System.out.print(item.pattern+",");
				//将频繁项存储到二叉树中
				iTree.insert(item.pattern.toString(), item.pattern.size());
				//存储到项集列表,作为生成下一层项集的依据
				array.add(item);
			}
		}
		//	System.out.println();
		return array;
	}

	/**
	 * 显示频繁集(测试用)
	 */
	public void showFrequentItemSet() {
		showItemSet(genFrequentSet(this.sortArray(getL1Set()), 3));
	}

	/**
	 * 显示第一层集合(测试用)
	 */
	public void showL1Set() {
		showItemSet(getL1Set());
	}

	/**
	 * 显示项集合(测试用)
	 */
	private void showItemSet(ArrayList<ItemSet> array) {

		for (int i = 0; i < array.size(); i++) {
			System.out.println(array.get(i).pattern + " " + array.get(i).counts);
		}

	}

	/**
	 * 对项集中的链表按字符串大小进行排序
	 * @param array
	 * @return
	 */
	ArrayList<ItemSet> sortArray(ArrayList<ItemSet> array) {
		for (int i = 0; i < array.size(); i++) {
			int j = i;
			ItemSet tmp = array.get(i);
			for (; j > 0 && tmp.pattern.compareTo(array.get(j - 1).pattern) < 0; j--) {
				array.set(j, array.get(j - 1));
			}
			array.set(j, tmp);
		}
		return array;
	}

	/**
	 * 显示排序后的ItemSet集合(测试用)
	 */
	public void showSortedArray() {
		ArrayList<ItemSet> show = sortArray(getL1Set());
		for (int i = 0; i < show.size(); i++)
			System.out.println(show.get(i).pattern + " " + show.get(i).counts);
	}

	//存储初始的数据集
	ArrayList<String> ItemSets;
	//确认频繁项集是否存在的哈希表
	HashSet<String>   freHs;
	//存储频繁项集的二叉树
	FrequentItemTree  iTree;

	/**
	 * 测试数据
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO 自动生成方法存根
		Apriori rd = new Apriori();

		System.out.println("输入最小支持度:");
		int minsup = 0;

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String sup = reader.readLine();
		while (minsup == 0) {
			try {
				minsup = Integer.parseInt(sup);
			}
			catch (Exception ex) {
				System.out.println("输入数据不合法!重新输入:");
				sup = reader.readLine();
				continue;
			}
			if (minsup < 1)
				System.out.println("支持度最小为1,重新输入:");

		}
		rd.startApriori(minsup);

	}

}
