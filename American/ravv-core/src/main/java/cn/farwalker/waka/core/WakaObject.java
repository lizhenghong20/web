package cn.farwalker.waka.core;

import java.io.Serializable;

public class WakaObject<T> implements Serializable {

	private static final long serialVersionUID = -2881786932192317944L;

	private T value;
	public WakaObject() {
        value = null;
    }
	public WakaObject(T t) {
		value = t;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public String toString(){
	    return (value==null?"null":value.toString());
	}
}
