package org.open;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 键值工具包类
 * @author 覃芝鹏
 * @param <T>
 * @version $Id: KeyValue.java,v 1.10 2008/11/26 07:38:39 moon Exp $
 */
public class KeyValue<K, V> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8927412034289738827L;

	/**
	 * 键队列
	 */
	private List<K>           keys             = new ArrayList<K>();

	/**
	 * 值队列
	 */
	private List<V>           values           = new ArrayList<V>();

	/**
	 * 设置Key是否唯一
	 */
	private boolean           isKeyUnique      = false;

	public KeyValue() {

	}

	public KeyValue(boolean isKeyUnique) {
		this.isKeyUnique = isKeyUnique;
	}

	/**
	 * 压入键值
	 * @param key 键
	 * @param value 值
	 * @return 压入成功与否
	 */
	public boolean put(K key, V value) {
		if (isKeyUnique) {
			int index = keys.indexOf(key);
			if (index == -1) {
				return keys.add(key) && values.add(value);
			} else {
				keys.set(index, key);
				values.set(index, value);
				return true;
			}
		} else {
			return keys.add(key) && values.add(value);
		}
	}

	/**
	 * 根据键 返回 值
	 * @param key 键
	 * @return value 值
	 */
	public V getValue(K key) {
		int index = keys.indexOf(key);
		if (-1 != index) {
			return values.get(index);
		} else {
			return null;
		}
	}

	/**
	 * @return 所有的值队列
	 */
	public List<V> getValues() {
		return values;
	}

	/**
	 * 根据值 返回 键
	 * @param value 值
	 * @return key 键
	 */
	public K getKey(K value) {
		int index = values.indexOf(value);
		if (-1 != index) {
			return keys.get(index);
		} else {
			return null;
		}
	}

	/**
	 * clear keys and values;
	 */
	public void clear() {
		keys.clear();
		values.clear();
	}

	public int size() {
		return keys.size();
	}

	public boolean removeByKey(K k) {
		int index = keys.indexOf(k);
		if (index == -1) {
			return false;
		} else {
			return (keys.remove(index) != null) && (values.remove(index) != null);
		}
	}

	public boolean removeByValue(V v) {
		int index = values.indexOf(v);
		if (index == -1) {
			return false;
		} else {
			return (keys.remove(index) != null) && (values.remove(index) != null);
		}
	}

	public K getKey(int index) {
		return keys.get(index);
	}

	public V getValue(int index) {
		return values.get(index);
	}

	/**
	 * @return 返回所有的键队列
	 */
	public List<K> getKeys() {
		return keys;
	}

	/**
	 * 查看队列是否为空
	 * @return true空，false非空。
	 */
	public boolean isEmpty() {
		return (keys.size() == 0) && (values.size() == 0);
	}

	/**
	 * 返回根据 值队列排序后对应的 键队列
	 * @param reversed true递减排列，false递增排列。
	 * @return List 键队列
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<K> getKeysOrderByValues(boolean reversed) {
		List _values = new ArrayList<V>();
		_values.addAll(values);

		Collections.sort(_values);

		List buf = new ArrayList();
		buf.addAll(values);

		List _keys = new ArrayList(keys.size());
		for (int i = 0; i < _values.size(); i++) {
			Object tmp = _values.get(i);
			int b = buf.indexOf(tmp);
			buf.set(b, null);
			_keys.add(keys.get(b));
		}

		if (reversed) {
			Collections.reverse(_keys);
		}

		return _keys;
	}

	/**
	 * @see #getKeysOrderByValues(boolean)
	 */
	public List<K> getKeysOrderByValuesAsc() {
		return getKeysOrderByValues(false);
	}

	/**
	 * @see #getKeysOrderByValues(boolean)
	 */
	public List<K> getKeysOrderByValuesDesc() {
		return getKeysOrderByValues(true);
	}

	public String toString() {
		return "[Keys:[" + keys.toString() + "]Values:[" + values.toString() + "]]";
	}

}
