package com.wxk.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UniqueList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(E object) {
		if (!this.contains(object)) {
			return super.add(object);
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean ret = false;
		if (null != collection) {

			Iterator<? extends E> iterator = collection.iterator();
			while (iterator.hasNext()) {
				boolean r = this.add(iterator.next());
				if (r == true) {
					ret = true;
				}
			}
		}
		return ret;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		if (null != collection) {
			int tmp = index;
			Iterator<? extends E> iterator = collection.iterator();
			while (iterator.hasNext()) {
				E e = iterator.next();
				if (!this.contains(e)) {
					this.add(tmp, e);
					tmp++;
				}
			}
			return tmp > 0;
		}
		return false;
	}

}
