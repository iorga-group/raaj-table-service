package com.iorga.iraj.service;

import java.util.Map;

public class SearchScope {
	private Long currentPage;
	private Long countPerPage;
	private Map<String, String> sorting;

	public Long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Long page) {
		this.currentPage = page;
	}
	public Long getCountPerPage() {
		return countPerPage;
	}
	public void setCountPerPage(Long count) {
		this.countPerPage = count;
	}
	public Map<String, String> getSorting() {
		return sorting;
	}
	public void setSorting(Map<String, String> sorting) {
		this.sorting = sorting;
	}
}
