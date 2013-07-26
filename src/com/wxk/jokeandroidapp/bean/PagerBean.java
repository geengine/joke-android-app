package com.wxk.jokeandroidapp.bean;

import java.util.ArrayList;
import java.util.List;

public class PagerBean<E> {
	private Integer index;
	private Integer size;
	private long totalPage;
	private long totalSize;
	private List<E> result;

	public PagerBean() {
		this.setIndex(0);
		this.setSize(0);
		this.setTotalPage(0);
		this.setTotalSize(0);
		this.setResult(new ArrayList<E>());
	}

	public PagerBean(int page, int size, long totalSize) {
		this.setIndex(page);
		this.setSize(size);
		this.setTotalPage((totalSize + size - 1) / size);
		this.setTotalSize(totalSize);
		this.setResult(new ArrayList<E>());
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {

		this.totalSize = totalSize;
		if (totalSize > 0 && this.getSize() > 0)
			this.totalPage = ((totalSize + this.getSize() - 1) / this.getSize());
	}

	public List<E> getResult() {
		return result;
	}

	public void setResult(List<E> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "page: " + this.getIndex() + "  size: " + this.getSize()
				+ "  totalPage: " + this.getTotalPage() + "  totalSize: "
				+ this.getTotalSize() + "  result.size(): "
				+ (result != null ? this.getResult().size() : 0);
	}
}
