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

package nl.finalist.liferay.lam.admin.service.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ChangelogLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ChangelogLocalService
 * @generated
 */
@ProviderType
public class ChangelogLocalServiceWrapper implements ChangelogLocalService,
	ServiceWrapper<ChangelogLocalService> {
	public ChangelogLocalServiceWrapper(
		ChangelogLocalService changelogLocalService) {
		_changelogLocalService = changelogLocalService;
	}

	/**
	* Adds the changelog to the database. Also notifies the appropriate model listeners.
	*
	* @param changelog the changelog
	* @return the changelog that was added
	*/
	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog addChangelog(
		nl.finalist.liferay.lam.admin.service.model.Changelog changelog) {
		return _changelogLocalService.addChangelog(changelog);
	}

	/**
	* Creates a new changelog with the primary key. Does not add the changelog to the database.
	*
	* @param installed_rank the primary key for the new changelog
	* @return the new changelog
	*/
	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog createChangelog(
		int installed_rank) {
		return _changelogLocalService.createChangelog(installed_rank);
	}

	/**
	* Deletes the changelog from the database. Also notifies the appropriate model listeners.
	*
	* @param changelog the changelog
	* @return the changelog that was removed
	*/
	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog deleteChangelog(
		nl.finalist.liferay.lam.admin.service.model.Changelog changelog) {
		return _changelogLocalService.deleteChangelog(changelog);
	}

	/**
	* Deletes the changelog with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog that was removed
	* @throws PortalException if a changelog with the primary key could not be found
	*/
	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog deleteChangelog(
		int installed_rank)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _changelogLocalService.deleteChangelog(installed_rank);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _changelogLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _changelogLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _changelogLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link nl.finalist.liferay.lam.admin.service.model.impl.ChangelogModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _changelogLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link nl.finalist.liferay.lam.admin.service.model.impl.ChangelogModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _changelogLocalService.dynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _changelogLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _changelogLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog fetchChangelog(
		int installed_rank) {
		return _changelogLocalService.fetchChangelog(installed_rank);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _changelogLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the changelog with the primary key.
	*
	* @param installed_rank the primary key of the changelog
	* @return the changelog
	* @throws PortalException if a changelog with the primary key could not be found
	*/
	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog getChangelog(
		int installed_rank)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _changelogLocalService.getChangelog(installed_rank);
	}

	/**
	* Returns a range of all the changelogs.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link nl.finalist.liferay.lam.admin.service.model.impl.ChangelogModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of changelogs
	* @param end the upper bound of the range of changelogs (not inclusive)
	* @return the range of changelogs
	*/
	@Override
	public java.util.List<nl.finalist.liferay.lam.admin.service.model.Changelog> getChangelogs(
		int start, int end) {
		return _changelogLocalService.getChangelogs(start, end);
	}

	/**
	* Returns the number of changelogs.
	*
	* @return the number of changelogs
	*/
	@Override
	public int getChangelogsCount() {
		return _changelogLocalService.getChangelogsCount();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		return _changelogLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _changelogLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _changelogLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Updates the changelog in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param changelog the changelog
	* @return the changelog that was updated
	*/
	@Override
	public nl.finalist.liferay.lam.admin.service.model.Changelog updateChangelog(
		nl.finalist.liferay.lam.admin.service.model.Changelog changelog) {
		return _changelogLocalService.updateChangelog(changelog);
	}

	@Override
	public ChangelogLocalService getWrappedService() {
		return _changelogLocalService;
	}

	@Override
	public void setWrappedService(ChangelogLocalService changelogLocalService) {
		_changelogLocalService = changelogLocalService;
	}

	private ChangelogLocalService _changelogLocalService;
}