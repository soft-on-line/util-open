package org.open;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: PageModel
 * </p>
 * <p>
 * Description: *分页组件，用于产生分页效果
 * </p>
 * 
 * @author 覃芝鹏
 * @time 2008-01—01
 * @version $Id: PageModel.java,v 1.9 2009/06/16 02:19:12 moon Exp $
 */
public class PageModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7154049317850503299L;

    /**
     * 定义每页数量
     */
    private int               pageSize;

    /**
     * 定义当前页
     */
    private int               page;

    /**
     * 定义select值
     */
    private int               itemCount;

    /**
     * 搜索时间
     */
    private String            time;

    /**
     * 分页链接条显示的页码链接数
     */
    private int               linkNum          = 5;

    /**
     * 定义select值
     */
    private List<Integer>     listPageNo;

    /**
     * PageSize = 10;Page = 1;
     */
    public PageModel() {
        pageSize = 10;
        page = 1;
    }

    /**
     * Page = 1;
     * 
     * @param pageSize 每页大小
     */
    public PageModel(int pageSize) {
        this.pageSize = pageSize;
        this.page = 1;
    }

    /**
     * @param pageSize 每页大小
     * @param page 当前页
     */
    public PageModel(int pageSize, int page) {
        this.pageSize = pageSize;
        this.page = page;
    }

    public int getPageCount() {
        return (getItemCount() + getPageSize() - 1) / getPageSize();
    }

    public int getPreviousPage() {
        return Math.min(getPageCount(), (Math.max(1, getPage() - 1)));
    }

    public int getNextPage() {
        return Math.min(getPageCount(), (Math.max(1, getPage() + 1)));
    }

    public List<Integer> getListPageNo() {
        listPageNo = new ArrayList<Integer>();

        if (getLinkNum() == 0) {
            return listPageNo;
        }

        int _linkNum = getLinkNum();
        int half_link_num = _linkNum / 2;

        int _page = getPage();
        int _pageCount = getPageCount();

        _page = Math.max(1, _page);
        _page = Math.min(_page, _pageCount);

        // 给当前页面重新赋值，调整有可能应页面设置过来的越界页面值
        this.page = _page;

        if (_page > 0 && _page <= half_link_num) {
            for (int i = 1; i <= _linkNum && i <= _pageCount; i++) {
                listPageNo.add(i);
            }
        } else if (_page > _pageCount - half_link_num) {
            for (int i = Math.max(_pageCount - _linkNum + 1, 1); i <= _pageCount; i++) {
                listPageNo.add(i);
            }
        } else {
            for (int i = -half_link_num; i <= half_link_num && (_page + i) <= (_pageCount); i++) {
                listPageNo.add(_page + i);
            }
        }

        return listPageNo;
    }

    /**
     * @return 三分制显示 格式
     */
    public String getFormatItemCount() {
        return new DecimalFormat("###,###").format(itemCount);
    }

    public void setPage(int page) {
        this.page = page;
        // //没有数据的时候
        // if(getItemCount() == 0)
        // {
        // this.page = page;
        // }
        // else
        // {
        // //给当前页面重新赋值，调整有可能应页面设置过来的越界页面值
        // this.page = Math.min(Math.max(1, page),getPageCount());
        // }
    }

    public int getPage() {
        this.page = Math.max(this.page, 1);
        return this.page;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setLinkNum(int linkNum) {
        this.linkNum = linkNum;
    }

    public int getLinkNum() {
        return linkNum;
    }

    public static <T> List<T> pagination(List<T> list, PageModel pageModel) {
        if (list == null) {
            return null;
        }

        pageModel.setItemCount(list.size());

        int fromIndex = (pageModel.getPage() - 1) * pageModel.getPageSize();
        int toIndex = pageModel.getPage() * pageModel.getPageSize();

        return list.subList(Math.max(0, fromIndex), Math.min(toIndex, list.size()));
    }

    @Override
    public String toString() {
        return "PageModel [itemCount=" + itemCount + ", linkNum=" + linkNum + ", listPageNo=" + listPageNo + ", page="
               + page + ", pageSize=" + pageSize + ", time=" + time + "]";
    }

}
