package com.kingbase.db.core.util;

/**
 * 
 * @author jpliu
 *
 */
public final class KBBooleanFlag {
	private boolean flag = false;
	
	public KBBooleanFlag() {
	}

	public KBBooleanFlag(boolean flag) {
		this.flag = flag;
	}

	public synchronized boolean getFlag() {
		return flag;
	}

	public synchronized void setFlag(boolean flag) {
		this.flag = flag;
	}

}