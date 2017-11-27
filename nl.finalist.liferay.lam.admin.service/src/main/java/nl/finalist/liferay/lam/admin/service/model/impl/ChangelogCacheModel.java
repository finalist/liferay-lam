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

package nl.finalist.liferay.lam.admin.service.model.impl;

import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import aQute.bnd.annotation.ProviderType;
import nl.finalist.liferay.lam.admin.service.model.Changelog;

/**
 * The cache model class for representing Changelog in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see Changelog
 * @generated
 */
@ProviderType
public class ChangelogCacheModel implements CacheModel<Changelog>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ChangelogCacheModel)) {
			return false;
		}

		ChangelogCacheModel changelogCacheModel = (ChangelogCacheModel)obj;

		if (installed_rank == changelogCacheModel.installed_rank) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, installed_rank);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{installed_rank=");
		sb.append(installed_rank);
		sb.append(", version=");
		sb.append(version);
		sb.append(", description=");
		sb.append(description);
		sb.append(", type=");
		sb.append(type);
		sb.append(", checksum=");
		sb.append(checksum);
		sb.append(", script=");
		sb.append(script);
		sb.append(", installed_by=");
		sb.append(installed_by);
		sb.append(", installed_on=");
		sb.append(installed_on);
		sb.append(", execution_time=");
		sb.append(execution_time);
		sb.append(", success=");
		sb.append(success);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Changelog toEntityModel() {
		ChangelogImpl changelogImpl = new ChangelogImpl();

		changelogImpl.setInstalled_rank(installed_rank);

		if (version == null) {
			changelogImpl.setVersion(StringPool.BLANK);
		}
		else {
			changelogImpl.setVersion(version);
		}

		if (description == null) {
			changelogImpl.setDescription(StringPool.BLANK);
		}
		else {
			changelogImpl.setDescription(description);
		}

		if (type == null) {
			changelogImpl.setType(StringPool.BLANK);
		}
		else {
			changelogImpl.setType(type);
		}

		changelogImpl.setChecksum(checksum);

		if (script == null) {
			changelogImpl.setScript(StringPool.BLANK);
		}
		else {
			changelogImpl.setScript(script);
		}

		if (installed_by == null) {
			changelogImpl.setInstalled_by(StringPool.BLANK);
		}
		else {
			changelogImpl.setInstalled_by(installed_by);
		}

		if (installed_on == Long.MIN_VALUE) {
			changelogImpl.setInstalled_on(null);
		}
		else {
			changelogImpl.setInstalled_on(new Date(installed_on));
		}

		changelogImpl.setExecution_time(execution_time);
		changelogImpl.setSuccess(success);

		changelogImpl.resetOriginalValues();

		return changelogImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		installed_rank = objectInput.readInt();
		version = objectInput.readUTF();
		description = objectInput.readUTF();
		type = objectInput.readUTF();

		checksum = objectInput.readInt();
		script = objectInput.readUTF();
		installed_by = objectInput.readUTF();
		installed_on = objectInput.readLong();

		execution_time = objectInput.readInt();

		success = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeInt(installed_rank);

		if (version == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(version);
		}

		if (description == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (type == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(type);
		}

		objectOutput.writeInt(checksum);

		if (script == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(script);
		}

		if (installed_by == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(installed_by);
		}

		objectOutput.writeLong(installed_on);

		objectOutput.writeInt(execution_time);

		objectOutput.writeBoolean(success);
	}

	public int installed_rank;
	public String version;
	public String description;
	public String type;
	public int checksum;
	public String script;
	public String installed_by;
	public long installed_on;
	public int execution_time;
	public boolean success;
}