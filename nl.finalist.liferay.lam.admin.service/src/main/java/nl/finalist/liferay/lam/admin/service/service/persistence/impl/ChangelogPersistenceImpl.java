/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package nl.finalist.liferay.lam.admin.service.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.spring.extender.service.ServiceReference;

import nl.finalist.liferay.lam.admin.service.exception.NoSuchChangelogException;
import nl.finalist.liferay.lam.admin.service.model.Changelog;
import nl.finalist.liferay.lam.admin.service.model.impl.ChangelogImpl;
import nl.finalist.liferay.lam.admin.service.model.impl.ChangelogModelImpl;
import nl.finalist.liferay.lam.admin.service.service.persistence.ChangelogPersistence;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the changelog service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ChangelogPersistence
 * @see nl.finalist.liferay.lam.admin.service.service.persistence.ChangelogUtil
 * @generated
 */
@ProviderType
public class ChangelogPersistenceImpl extends BasePersistenceImpl<Changelog>
	implements ChangelogPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link ChangelogUtil} to access the changelog persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = ChangelogImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
			ChangelogModelImpl.FINDER_CACHE_ENABLED, ChangelogImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
			ChangelogModelImpl.FINDER_CACHE_ENABLED, ChangelogImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
			ChangelogModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public ChangelogPersistenceImpl() {
		setModelClass(Changelog.class);
	}

	/**
	 * Caches the changelog in the entity cache if it is enabled.
	 *
	 * @param changelog the changelog
	 */
	@Override
	public void cacheResult(Changelog changelog) {
		entityCache.putResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
			ChangelogImpl.class, changelog.getPrimaryKey(), changelog);

		changelog.resetOriginalValues();
	}

	/**
	 * Caches the changelogs in the entity cache if it is enabled.
	 *
	 * @param changelogs the changelogs
	 */
	@Override
	public void cacheResult(List<Changelog> changelogs) {
		for (Changelog changelog : changelogs) {
			if (entityCache.getResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
						ChangelogImpl.class, changelog.getPrimaryKey()) == null) {
				cacheResult(changelog);
			}
			else {
				changelog.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all changelogs.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(ChangelogImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the changelog.
	 *
	 * <p>
	 * The {@link EntityCache} and {@link FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Changelog changelog) {
		entityCache.removeResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
			ChangelogImpl.class, changelog.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Changelog> changelogs) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Changelog changelog : changelogs) {
			entityCache.removeResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
				ChangelogImpl.class, changelog.getPrimaryKey());
		}
	}

	/**
	 * Creates a new changelog with the primary key. Does not add the changelog to the database.
	 *
	 * @param installed_rank the primary key for the new changelog
	 * @return the new changelog
	 */
	@Override
	public Changelog create(int installed_rank) {
		Changelog changelog = new ChangelogImpl();

		changelog.setNew(true);
		changelog.setPrimaryKey(installed_rank);

		return changelog;
	}

	/**
	 * Removes the changelog with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param installed_rank the primary key of the changelog
	 * @return the changelog that was removed
	 * @throws NoSuchChangelogException if a changelog with the primary key could not be found
	 */
	@Override
	public Changelog remove(int installed_rank) throws NoSuchChangelogException {
		return remove((Serializable)installed_rank);
	}

	/**
	 * Removes the changelog with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the changelog
	 * @return the changelog that was removed
	 * @throws NoSuchChangelogException if a changelog with the primary key could not be found
	 */
	@Override
	public Changelog remove(Serializable primaryKey)
		throws NoSuchChangelogException {
		Session session = null;

		try {
			session = openSession();

			Changelog changelog = (Changelog)session.get(ChangelogImpl.class,
					primaryKey);

			if (changelog == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchChangelogException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(changelog);
		}
		catch (NoSuchChangelogException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected Changelog removeImpl(Changelog changelog) {
		changelog = toUnwrappedModel(changelog);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(changelog)) {
				changelog = (Changelog)session.get(ChangelogImpl.class,
						changelog.getPrimaryKeyObj());
			}

			if (changelog != null) {
				session.delete(changelog);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (changelog != null) {
			clearCache(changelog);
		}

		return changelog;
	}

	@Override
	public Changelog updateImpl(Changelog changelog) {
		changelog = toUnwrappedModel(changelog);

		boolean isNew = changelog.isNew();

		Session session = null;

		try {
			session = openSession();

			if (changelog.isNew()) {
				session.save(changelog);

				changelog.setNew(false);
			}
			else {
				changelog = (Changelog)session.merge(changelog);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			finderCache.removeResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY);
			finderCache.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		entityCache.putResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
			ChangelogImpl.class, changelog.getPrimaryKey(), changelog, false);

		changelog.resetOriginalValues();

		return changelog;
	}

	protected Changelog toUnwrappedModel(Changelog changelog) {
		if (changelog instanceof ChangelogImpl) {
			return changelog;
		}

		ChangelogImpl changelogImpl = new ChangelogImpl();

		changelogImpl.setNew(changelog.isNew());
		changelogImpl.setPrimaryKey(changelog.getPrimaryKey());

		changelogImpl.setInstalled_rank(changelog.getInstalled_rank());
		changelogImpl.setVersion(changelog.getVersion());
		changelogImpl.setDescription(changelog.getDescription());
		changelogImpl.setType(changelog.getType());
		changelogImpl.setChecksum(changelog.getChecksum());
		changelogImpl.setScript(changelog.getScript());
		changelogImpl.setInstalled_by(changelog.getInstalled_by());
		changelogImpl.setInstalled_on(changelog.getInstalled_on());
		changelogImpl.setExecution_time(changelog.getExecution_time());
		changelogImpl.setSuccess(changelog.isSuccess());

		return changelogImpl;
	}

	/**
	 * Returns the changelog with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the changelog
	 * @return the changelog
	 * @throws NoSuchChangelogException if a changelog with the primary key could not be found
	 */
	@Override
	public Changelog findByPrimaryKey(Serializable primaryKey)
		throws NoSuchChangelogException {
		Changelog changelog = fetchByPrimaryKey(primaryKey);

		if (changelog == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchChangelogException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return changelog;
	}

	/**
	 * Returns the changelog with the primary key or throws a {@link NoSuchChangelogException} if it could not be found.
	 *
	 * @param installed_rank the primary key of the changelog
	 * @return the changelog
	 * @throws NoSuchChangelogException if a changelog with the primary key could not be found
	 */
	@Override
	public Changelog findByPrimaryKey(int installed_rank)
		throws NoSuchChangelogException {
		return findByPrimaryKey((Serializable)installed_rank);
	}

	/**
	 * Returns the changelog with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the changelog
	 * @return the changelog, or <code>null</code> if a changelog with the primary key could not be found
	 */
	@Override
	public Changelog fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
				ChangelogImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		Changelog changelog = (Changelog)serializable;

		if (changelog == null) {
			Session session = null;

			try {
				session = openSession();

				changelog = (Changelog)session.get(ChangelogImpl.class,
						primaryKey);

				if (changelog != null) {
					cacheResult(changelog);
				}
				else {
					entityCache.putResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
						ChangelogImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
					ChangelogImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return changelog;
	}

	/**
	 * Returns the changelog with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param installed_rank the primary key of the changelog
	 * @return the changelog, or <code>null</code> if a changelog with the primary key could not be found
	 */
	@Override
	public Changelog fetchByPrimaryKey(int installed_rank) {
		return fetchByPrimaryKey((Serializable)installed_rank);
	}

	@Override
	public Map<Serializable, Changelog> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, Changelog> map = new HashMap<Serializable, Changelog>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			Changelog changelog = fetchByPrimaryKey(primaryKey);

			if (changelog != null) {
				map.put(primaryKey, changelog);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
					ChangelogImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (Changelog)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_CHANGELOG_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append((int)primaryKey);

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (Changelog changelog : (List<Changelog>)q.list()) {
				map.put(changelog.getPrimaryKeyObj(), changelog);

				cacheResult(changelog);

				uncachedPrimaryKeys.remove(changelog.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(ChangelogModelImpl.ENTITY_CACHE_ENABLED,
					ChangelogImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the changelogs.
	 *
	 * @return the changelogs
	 */
	@Override
	public List<Changelog> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the changelogs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ChangelogModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of changelogs
	 * @param end the upper bound of the range of changelogs (not inclusive)
	 * @return the range of changelogs
	 */
	@Override
	public List<Changelog> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the changelogs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ChangelogModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of changelogs
	 * @param end the upper bound of the range of changelogs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of changelogs
	 */
	@Override
	public List<Changelog> findAll(int start, int end,
		OrderByComparator<Changelog> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the changelogs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link ChangelogModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of changelogs
	 * @param end the upper bound of the range of changelogs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of changelogs
	 */
	@Override
	public List<Changelog> findAll(int start, int end,
		OrderByComparator<Changelog> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<Changelog> list = null;

		if (retrieveFromCache) {
			list = (List<Changelog>)finderCache.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_CHANGELOG);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_CHANGELOG;

				if (pagination) {
					sql = sql.concat(ChangelogModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Changelog>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<Changelog>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				finderCache.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the changelogs from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Changelog changelog : findAll()) {
			remove(changelog);
		}
	}

	/**
	 * Returns the number of changelogs.
	 *
	 * @return the number of changelogs
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_CHANGELOG);

				count = (Long)q.uniqueResult();

				finderCache.putResult(FINDER_PATH_COUNT_ALL, FINDER_ARGS_EMPTY,
					count);
			}
			catch (Exception e) {
				finderCache.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return ChangelogModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the changelog persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		entityCache.removeCache(ChangelogImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;
	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;
	private static final String _SQL_SELECT_CHANGELOG = "SELECT changelog FROM Changelog changelog";
	private static final String _SQL_SELECT_CHANGELOG_WHERE_PKS_IN = "SELECT changelog FROM Changelog changelog WHERE installed_rank IN (";
	private static final String _SQL_COUNT_CHANGELOG = "SELECT COUNT(changelog) FROM Changelog changelog";
	private static final String _ORDER_BY_ENTITY_ALIAS = "changelog.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Changelog exists with the primary key ";
	private static final Log _log = LogFactoryUtil.getLog(ChangelogPersistenceImpl.class);
}