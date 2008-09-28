/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.lock.model;

import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.Date;

/**
 * <a href="Lock.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Lock implements Comparable<Lock>, Serializable {

	public Lock() {
	}

	public Lock(
		String uuid, String className, Comparable<?> pk, long userId,
		String owner, boolean inheritable, long expirationTime) {

		_uuid = uuid;
		_className = className;
		_pk = pk;
		_userId = userId;
		_owner = owner;
		_inheritable = inheritable;
		_expirationTime = expirationTime;
		_date = new Date();
	}

	public int compareTo(Lock lock) {
		if (lock == null) {
			return -1;
		}

		int value = getUuid().compareTo(lock.getUuid());

		if (value != 0) {
			return value;
		}

		value = getClassName().compareTo(lock.getClassName());

		if (value != 0) {
			return value;
		}

		value = ((Comparable<Object>)getPrimaryKey()).compareTo(
			lock.getPrimaryKey());

		if (value != 0) {
			return value;
		}

		value = getOwner().compareTo(lock.getOwner());

		if (value != 0) {
			return value;
		}

		value = ((Comparable<Boolean>)isInheritable()).compareTo(
			lock.isInheritable());

		if (value != 0) {
			return value;
		}

		value = getDate().compareTo(lock.getDate());

		if (value != 0) {
			return value;
		}

		return 0;
	}

	public boolean equals(Lock lock) {
		if (lock == null) {
			return false;
		}

		if (getClassName().equals(lock.getClassName()) &&
			getPrimaryKey().equals(lock.getPrimaryKey())) {

			return true;
		}
		else {
			return false;
		}
	}

	public String getClassName() {
		return _className;
	}

	public Date getDate() {
		return _date;
	}

	public long getExpirationTime() {
		return _expirationTime;
	}

	public String getOwner() {
		if (Validator.isNull(_owner)) {
			return String.valueOf(_userId);
		}
		else {
			return _owner;
		}
	}

	public Comparable<?> getPrimaryKey() {
		return _pk;
	}

	public long getUserId() {
		return _userId;
	}

	public String getUuid() {
		return _uuid;
	}

	public int hashCode() {
		return getClassName().hashCode() + getPrimaryKey().hashCode();
	}

	public boolean isExpired() {
		if (_expirationTime <= 0) {
			return false;
		}
		else {
			Date now = new Date();

			if (now.getTime() > (_date.getTime() + _expirationTime)) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	public boolean isInheritable() {
		return _inheritable;
	}

	public void setExpirationTime(long expirationTime) {
		_expirationTime = expirationTime;
		_date = new Date();
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	private String _uuid;
	private String _className;
	private Comparable<?> _pk;
	private long _userId;
	private String _owner;
	private boolean _inheritable;
	private long _expirationTime;
	private Date _date;

}