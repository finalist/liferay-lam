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

package nl.finalist.liferay.lam.admin.service.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the Changelog service. Represents a row in the &quot;LAM_Changelog&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ChangelogModel
 * @see nl.finalist.liferay.lam.admin.service.model.impl.ChangelogImpl
 * @see nl.finalist.liferay.lam.admin.service.model.impl.ChangelogModelImpl
 * @generated
 */
@ImplementationClassName("nl.finalist.liferay.lam.admin.service.model.impl.ChangelogImpl")
@ProviderType
public interface Changelog extends ChangelogModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link nl.finalist.liferay.lam.admin.service.model.impl.ChangelogImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Changelog, Integer> INSTALLED_RANK_ACCESSOR = new Accessor<Changelog, Integer>() {
			@Override
			public Integer get(Changelog changelog) {
				return changelog.getInstalled_rank();
			}

			@Override
			public Class<Integer> getAttributeClass() {
				return Integer.class;
			}

			@Override
			public Class<Changelog> getTypeClass() {
				return Changelog.class;
			}
		};
}