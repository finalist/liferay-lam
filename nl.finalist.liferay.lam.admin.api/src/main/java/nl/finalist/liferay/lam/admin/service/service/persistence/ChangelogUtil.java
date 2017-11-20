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

package nl.finalist.liferay.lam.admin.service.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.osgi.util.ServiceTrackerFactory;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import nl.finalist.liferay.lam.admin.service.model.Changelog;

import org.osgi.util.tracker.ServiceTracker;

import java.util.List;

/**
 * The persistence utility for the changelog service. This utility wraps {@link nl.finalist.liferay.lam.admin.service.service.persistence.impl.ChangelogPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ChangelogPersistence
 * @see nl.finalist.liferay.lam.admin.service.service.persistence.impl.ChangelogPersistenceImpl
 * @generated
 */
@ProviderType
public class ChangelogUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(Changelog changelog) {
		getPersistence().clearCache(changelog);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Changelog> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Changelog> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Changelog> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Changelog> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Changelog update(Changelog changelog) {
		return getPersistence().update(changelog);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Changelog update(Changelog changelog,
		ServiceContext serviceContext) {
		return getPersistence().update(changelog, serviceContext);
	}

	/**
	* Caches the changelog in the entity cache if it is enabled.
	*
	* @param changelog the changelog
	*/
	public static void cacheResult(Changelog changelog) {
		getPersistence().cacheResult(changelog);
	}

	/**
	* Caches the changelogs in the entity cache if it is enabled.
	*
	* @param changelogs the changelogs
	*/
	public static void cacheResult(List<Changelog> changelogs) {
		getPersistence().cacheResult(changelogs);
	}

	/**
	* Creates a new changelog with the primary key. Does not add the changelog to the database.
	*
	* @param installed_rank the primary key for the new changelog
	* @return the new changelog
	*/
	public static Changelog create(int installed_rank) {
		return getPersistence().create(installed_rank);
	}

	/**
	* Removes the changelog with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog that was removed
	* @throws NoSuchChangelogException if a changelog with the primary key could not be found
	*/
	public static Changelog remove(int installed_rank)
		throws nl.finalist.liferay.lam.admin.service.exception.NoSuchChangelogException {
		return getPersistence().remove(installed_rank);
	}

	public static Changelog updateImpl(Changelog changelog) {
		return getPersistence().updateImpl(changelog);
	}

	/**
	* Returns the changelog with the primary key or throws a {@link NoSuchChangelogException} if it could not be found.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog
	* @throws NoSuchChangelogException if a changelog with the primary key could not be found
	*/
	public static Changelog findByPrimaryKey(int installed_rank)
		throws nl.finalist.liferay.lam.admin.service.exception.NoSuchChangelogException {
		return getPersistence().findByPrimaryKey(installed_rank);
	}

	/**
	* Returns the changelog with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog, or <code>null</code> if a changelog with the primary key could not be found
	*/
	public static Changelog fetchByPrimaryKey(int installed_rank) {
		return getPersistence().fetchByPrimaryKey(installed_rank);
	}

	public static java.util.Map<java.io.Serializable, Changelog> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the changelogs.
	*
	* @return the changelogs
	*/
	public static List<Changelog> findAll() {
		return getPersistence().findAll();
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
	public static List<Changelog> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
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
	public static List<Changelog> findAll(int start, int end,
		OrderByComparator<Changelog> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
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
	public static List<Changelog> findAll(int start, int end,
		OrderByComparator<Changelog> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findAll(start, end, orderByComparator, retrieveFromCache);
	}

	/**
	* Removes all the changelogs from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of changelogs.
	*
	* @return the number of changelogs
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static ChangelogPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ChangelogPersistence, ChangelogPersistence> _serviceTracker =
		ServiceTrackerFactory.open(ChangelogPersistence.class);
}