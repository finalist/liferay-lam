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

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import nl.finalist.liferay.lam.admin.service.exception.NoSuchChangelogException;
import nl.finalist.liferay.lam.admin.service.model.Changelog;

/**
 * The persistence interface for the changelog service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see nl.finalist.liferay.lam.admin.service.service.persistence.impl.ChangelogPersistenceImpl
 * @see ChangelogUtil
 * @generated
 */
@ProviderType
public interface ChangelogPersistence extends BasePersistence<Changelog> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ChangelogUtil} to access the changelog persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the changelog in the entity cache if it is enabled.
	*
	* @param changelog the changelog
	*/
	public void cacheResult(Changelog changelog);

	/**
	* Caches the changelogs in the entity cache if it is enabled.
	*
	* @param changelogs the changelogs
	*/
	public void cacheResult(java.util.List<Changelog> changelogs);

	/**
	* Creates a new changelog with the primary key. Does not add the changelog to the database.
	*
	* @param installed_rank the primary key for the new changelog
	* @return the new changelog
	*/
	public Changelog create(int installed_rank);

	/**
	* Removes the changelog with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog that was removed
	* @throws NoSuchChangelogException if a changelog with the primary key could not be found
	*/
	public Changelog remove(int installed_rank) throws NoSuchChangelogException;

	public Changelog updateImpl(Changelog changelog);

	/**
	* Returns the changelog with the primary key or throws a {@link NoSuchChangelogException} if it could not be found.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog
	* @throws NoSuchChangelogException if a changelog with the primary key could not be found
	*/
	public Changelog findByPrimaryKey(int installed_rank)
		throws NoSuchChangelogException;

	/**
	* Returns the changelog with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog, or <code>null</code> if a changelog with the primary key could not be found
	*/
	public Changelog fetchByPrimaryKey(int installed_rank);

	@Override
	public java.util.Map<java.io.Serializable, Changelog> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the changelogs.
	*
	* @return the changelogs
	*/
	public java.util.List<Changelog> findAll();

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
	public java.util.List<Changelog> findAll(int start, int end);

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
	public java.util.List<Changelog> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Changelog> orderByComparator);

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
	public java.util.List<Changelog> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Changelog> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the changelogs from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of changelogs.
	*
	* @return the number of changelogs
	*/
	public int countAll();
}