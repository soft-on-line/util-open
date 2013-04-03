package org.open;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 单态设计 存储后台各种消息（出错、运行状态等等）的处理类
 *
 * @author 覃芝鹏
 * @version $Id: Message.java,v 1.1 2008/07/30 07:56:07 moon Exp $
 */
public class Message {

    /**
     * 设置默认容量为1000
     */
    private static int                               capacity    = 1000;

    /**
     * 用于存放后台各种消息的库（默认1000条）
     */
    // private List<String> msgList = new ArrayList<String>(1000);
    private volatile LinkedList<String>              msgList     = new LinkedList<String>();

    /**
     * 用于指定池子的消息库
     */
    private volatile Map<String, LinkedList<String>> poolMsgList = new HashMap<String, LinkedList<String>>();

    /**
     * 单态设计，取得类实例
     */
    public static Message                            instance    = new Message();

    private Message() {
        msgList.add("欢迎光临!");
    }

    /**
     * @param list
     * @param input
     */
    private static void in(LinkedList<String> list, String input) {
        while (list.size() > capacity) {
            list.removeFirst();
        }
        list.addLast(input);
    }

    /**
     * 用于后台各种函数 压入消息
     *
     * @param input 消息字符串
     */
    public synchronized void in(String input) {
        in(msgList, input);
    }

    /**
     * 用于后台各种函数向指定的消息池 压入消息
     *
     * @param poolName 消息池
     * @param input
     */
    public synchronized void in(String poolName, String input) {
        if (null == poolName) {
            in(input);
        } else {
            LinkedList<String> data = poolMsgList.get(poolName);
            if (null == data) {
                data = new LinkedList<String>();
                poolMsgList.put(poolName, data);
            }
            in(data, input);
        }
    }

    /**
     * @param data
     * @param index
     * @return
     */
    private static String[] out(List<String> data, int size) {
        if (null == data || data.size() <= 0 || size <= 0) {
            return new String[0];
        }
        //
        if (size < data.size()) {
            return data.subList(data.size() - 1 - size, data.size() - 1).toArray(new String[size]);
        } else {
            return data.toArray(new String[data.size()]);
        }
    }

    /**
     * 根据给定位置 取出消息队列中消息
     *
     * @param index 消息池中 消息下标（消息位置）
     * @return 2长度字符串数组 1对应消息所处位置 2对应消息体
     */
    public String[] out(int size) {
        return out(msgList, size);
    }

    /**
     * @param poolName
     * @param index
     * @return
     */
    public String[] out(String poolName, int size) {
        if (null == poolName) {
            return out(size);
        }
        LinkedList<String> data = poolMsgList.get(poolName);
        if (null == data) {
            return new String[0];
        }
        return out(data, size);
    }

    public String[] out() {
        return msgList.toArray(new String[msgList.size()]);
    }

    public String[] out(String poolName) {
        LinkedList<String> data = poolMsgList.get(poolName);
        if (null == data) {
            return new String[0];
        } else {
            return data.toArray(new String[data.size()]);
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        Message.capacity = capacity;
    }

}
