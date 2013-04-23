package com.iorga.irajblank.model.service;


public abstract class PaginatedResquest {

	private Integer currentPage;
	private Integer pageSize;

	private String orderByPath;
	private String orderByDirection;

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
	public String getOrderByDirection() {
		return orderByDirection;
	}
	public void setOrderByDirection(String orderByDirection) {
		this.orderByDirection = orderByDirection;
	}
	public String getOrderByPath() {
		return orderByPath;
	}
	public void setOrderByPath(String orderByPath) {
		this.orderByPath = orderByPath;
	}


}
