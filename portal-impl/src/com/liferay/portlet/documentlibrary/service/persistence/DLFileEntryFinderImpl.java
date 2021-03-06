/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.documentlibrary.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.impl.DLFileEntryImpl;
import com.liferay.portlet.documentlibrary.model.impl.DLFileVersionImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.util.Iterator;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class DLFileEntryFinderImpl
	extends BasePersistenceImpl<DLFileEntry> implements DLFileEntryFinder {

	public static final String COUNT_BY_EXTRA_SETTINGS =
		DLFileEntryFinder.class.getName() + ".countByExtraSettings";

	public static final String COUNT_BY_G_F =
		DLFileEntryFinder.class.getName() + ".countByG_F";

	public static final String COUNT_BY_G_U_F =
		DLFileEntryFinder.class.getName() + ".countByG_U_F";

	public static final String FIND_BY_ANY_IMAGE_ID =
		DLFileEntryFinder.class.getName() + ".findByAnyImageId";

	public static final String FIND_BY_EXTRA_SETTINGS =
		DLFileEntryFinder.class.getName() + ".findByExtraSettings";

	public static final String FIND_BY_MISVERSIONED =
		DLFileEntryFinder.class.getName() + ".findByMisversioned";

	public static final String FIND_BY_NO_ASSETS =
		DLFileEntryFinder.class.getName() + ".findByNoAssets";

	public static final String FIND_BY_ORPHANED_FILE_ENTRIES =
		DLFileEntryFinder.class.getName() + ".findByOrphanedFileEntries";

	public static final String FIND_BY_G_F =
		DLFileEntryFinder.class.getName() + ".findByG_F";

	public static final String FIND_BY_G_U_F =
		DLFileEntryFinder.class.getName() + ".findByG_U_F";

	public int countByExtraSettings() throws SystemException {
		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_BY_EXTRA_SETTINGS);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public int countByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_U_F_M(
			groupId, 0, folderIds, null, queryDefinition, false);
	}

	public int countByG_U_F_M(
			long groupId, long userId, List<Long> folderIds, String[] mimeTypes,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_U_F_M(
			groupId, userId, folderIds, mimeTypes, queryDefinition, false);
	}

	public DLFileEntry fetchByAnyImageId(long imageId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_ANY_IMAGE_ID);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(DLFileEntryImpl.TABLE_NAME, DLFileEntryImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(imageId);
			qPos.add(imageId);
			qPos.add(imageId);
			qPos.add(imageId);

			List<DLFileEntry> dlFileEntries = q.list();

			if (!dlFileEntries.isEmpty()) {
				return dlFileEntries.get(0);
			}

			return null;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public int filterCountByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_U_F_M(
			groupId, 0, folderIds, null, queryDefinition, true);
	}

	public List<DLFileEntry> filterFindByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_U_F_M(
			groupId, 0, folderIds, null, queryDefinition, true);
	}

	public DLFileEntry findByAnyImageId(long imageId)
		throws NoSuchFileEntryException, SystemException {

		DLFileEntry dlFileEntry = fetchByAnyImageId(imageId);

		if (dlFileEntry != null) {
			return dlFileEntry;
		}

		throw new NoSuchFileEntryException(
			"No DLFileEntry exists with the imageId " + imageId);
	}

	public List<DLFileEntry> findByExtraSettings(int start, int end)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_EXTRA_SETTINGS);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(DLFileEntryImpl.TABLE_NAME, DLFileEntryImpl.class);

			return (List<DLFileEntry>)QueryUtil.list(
				q, getDialect(), start, end);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<DLFileEntry> findByMisversioned() throws SystemException {
		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_MISVERSIONED);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(DLFileEntryImpl.TABLE_NAME, DLFileEntryImpl.class);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<DLFileEntry> findByNoAssets() throws SystemException {
		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_NO_ASSETS);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(DLFileEntryImpl.TABLE_NAME, DLFileEntryImpl.class);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<DLFileEntry> findByOrphanedFileEntries()
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_ORPHANED_FILE_ENTRIES);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(DLFileEntryImpl.TABLE_NAME, DLFileEntryImpl.class);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<DLFileEntry> findByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_U_F_M(
			groupId, 0, folderIds, null, queryDefinition, false);
	}

	public List<DLFileEntry> findByG_U_F_M(
			long groupId, long userId, List<Long> folderIds, String[] mimeTypes,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_U_F_M(
			groupId, userId, folderIds, mimeTypes, queryDefinition, false);
	}

	protected int doCountByG_U_F_M(
			long groupId, long userId, List<Long> folderIds, String[] mimeTypes,
			QueryDefinition queryDefinition, boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String id = null;

			if (userId <= 0) {
				id = COUNT_BY_G_F;
			}
			else {
				id = COUNT_BY_G_U_F;
			}

			String sql = getFileEntriesSQL(
				id, groupId, folderIds, mimeTypes, queryDefinition,
				inlineSQLHelper);

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (userId > 0) {
				qPos.add(userId);
			}

			qPos.add(queryDefinition.getStatus());

			for (Long folderId : folderIds) {
				qPos.add(folderId);
			}

			if (mimeTypes != null) {
				qPos.add(mimeTypes);
			}

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected List<DLFileEntry> doFindByG_U_F_M(
			long groupId, long userId, List<Long> folderIds, String[] mimeTypes,
			QueryDefinition queryDefinition, boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String id = null;

			if (userId <= 0) {
				id = FIND_BY_G_F;
			}
			else {
				id = FIND_BY_G_U_F;
			}

			String sql = getFileEntriesSQL(
				id, groupId, folderIds, mimeTypes, queryDefinition,
				inlineSQLHelper);

			sql = CustomSQLUtil.replaceOrderBy(
				sql, queryDefinition.getOrderByComparator());

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(DLFileEntryImpl.TABLE_NAME, DLFileEntryImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);

			if (userId > 0) {
				qPos.add(userId);
			}

			qPos.add(queryDefinition.getStatus());

			for (Long folderId : folderIds) {
				qPos.add(folderId);
			}

			if (mimeTypes != null) {
				qPos.add(mimeTypes);
			}

			return (List<DLFileEntry>)QueryUtil.list(
				q, getDialect(), queryDefinition.getStart(),
				queryDefinition.getEnd());
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected String getFileEntriesSQL(
		String id, long groupId, List<Long> folderIds, String[] mimeTypes,
		QueryDefinition queryDefinition, boolean inlineSQLHelper) {

		String tableName = DLFileVersionImpl.TABLE_NAME;

		String sql = CustomSQLUtil.get(id, queryDefinition, tableName);

		if (queryDefinition.getStatus() == WorkflowConstants.STATUS_ANY) {
			sql = StringUtil.replace(sql, "[$JOIN$]", StringPool.BLANK);

			tableName = DLFileEntryImpl.TABLE_NAME;
		}
		else {
			sql = StringUtil.replace(
				sql, "[$JOIN$]",
				CustomSQLUtil.get(
					DLFolderFinderImpl.JOIN_FE_BY_DL_FILE_VERSION));
		}

		if (inlineSQLHelper && InlineSQLHelperUtil.isEnabled()) {
			sql = InlineSQLHelperUtil.replacePermissionCheck(
				sql, DLFileEntry.class.getName(), "DLFileEntry.fileEntryId",
				groupId);
		}

		StringBundler sb = new StringBundler(7);

		if (!folderIds.isEmpty()) {
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(getFolderIds(folderIds, tableName));
			sb.append(StringPool.CLOSE_PARENTHESIS);
		}

		if ((mimeTypes != null) && (mimeTypes.length > 0)) {
			sb.append(WHERE_AND);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(getMimeTypes(mimeTypes, tableName));
			sb.append(StringPool.CLOSE_PARENTHESIS);
		}

		return StringUtil.replace(sql, "[$FOLDER_ID$]", sb.toString());
	}

	protected String getFolderIds(List<Long> folderIds, String tableName) {
		if (folderIds.isEmpty()) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(folderIds.size() * 3 + 1);

		sb.append(StringPool.OPEN_PARENTHESIS);

		for (int i = 0; i < folderIds.size(); i++) {
			sb.append(tableName);
			sb.append(".folderId = ? ");

			if ((i + 1) != folderIds.size()) {
				sb.append(WHERE_OR);
			}
		}

		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	protected String getMimeTypes(String[] mimeTypes, String tableName) {
		if (mimeTypes.length == 0) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(mimeTypes.length * 2 - 1);

		for (int i = 0; i < mimeTypes.length; i++) {
			sb.append(tableName);
			sb.append(".mimeType = ?");

			if ((i + 1) != mimeTypes.length) {
				sb.append(WHERE_OR);
			}
		}

		return sb.toString();
	}

}