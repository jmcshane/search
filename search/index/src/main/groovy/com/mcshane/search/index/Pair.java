package com.mcshane.search.index;

import java.io.Serializable;
import java.util.function.Function;

public class Pair<L, R> implements Serializable {

	private static final long serialVersionUID = 2554869628365228637L;

	protected L left; // first member of pair
	protected R right; // second member of pair

	public Pair(L left, R right) {
		setLeft(left);
		setRight(right);
	}

	public void setLeft(L left) {
		this.left = left;
	}

	public void setRight(R right) {
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}
	
	public <K,V> Pair<K,V> map(Function<L,K> leftFunc, Function<R,V> rightFunc)  {
		return new Pair<K,V>(leftFunc.apply(this.left), rightFunc.apply(this.right));
	}

	@Override
	public String toString() {
		return String.format("Pair [left=%s, right=%s]", left, right);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pair))
			return false;
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	
}
