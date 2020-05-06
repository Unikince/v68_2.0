/**
 * 
 */
package com.zyhy.common_server.util;

import java.util.List;

/**
 * @author linanjun
 *
 */
public class PageBean<T> {
	
	// 当前页
	private Integer currentPage = 1;
	// 每页显示的总条数
	private Integer pageSize = 10;
	// 总条数
	private Integer totalNum;
	// 是否有下一页
	private Integer isMore;
	// 总页数
	private Integer totalPage;
	// 开始索引
	private Integer startIndex;
	// 结束索引
	private Integer endIndex;
	// 分页结果
	private List<T> items;

	public PageBean() {
		super();
	}

	public PageBean(Integer currentPage, Integer pageSize, List<T> items) {
		super();
		this.items = items;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalNum = items == null ? 0 : items.size();
		this.totalPage = (this.totalNum + this.pageSize - 1) / this.pageSize;
		this.startIndex = (this.currentPage - 1) * this.pageSize;
		this.endIndex = currentPage * pageSize > this.totalNum ? this.totalNum : currentPage * pageSize;
		this.isMore = this.currentPage >= this.totalPage ? 0 : 1;
	}
	
	/**
	 * 查询分页数据
	 * @return
	 */
	public List<T> selectPageInfo(){
		if (endIndex == 0) {
			return items;
		}
		return items.subList(startIndex, endIndex);
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getIsMore() {
		return isMore;
	}

	public void setIsMore(Integer isMore) {
		this.isMore = isMore;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
