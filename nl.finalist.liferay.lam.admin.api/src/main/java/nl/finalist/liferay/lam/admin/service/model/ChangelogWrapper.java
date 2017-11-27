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

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import aQute.bnd.annotation.ProviderType;

/**
 * <p>
 * This class is a wrapper for {@link Changelog}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Changelog
 * @generated
 */
@ProviderType
public class ChangelogWrapper implements Changelog, ModelWrapper<Changelog> {
	public ChangelogWrapper(Changelog changelog) {
		_changelog = changelog;
	}

	@Override
	public Class<?> getModelClass() {
		return Changelog.class;
	}

	@Override
	public String getModelClassName() {
		return Changelog.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("installed_rank", getInstalled_rank());
		attributes.put("version", getVersion());
		attributes.put("description", getDescription());
		attributes.put("type", getType());
		attributes.put("checksum", getChecksum());
		attributes.put("script", getScript());
		attributes.put("installed_by", getInstalled_by());
		attributes.put("installed_on", getInstalled_on());
		attributes.put("execution_time", getExecution_time());
		attributes.put("success", getSuccess());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Integer installed_rank = (Integer)attributes.get("installed_rank");

		if (installed_rank != null) {
			setInstalled_rank(installed_rank);
		}

		String version = (String)attributes.get("version");

		if (version != null) {
			setVersion(version);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		Integer checksum = (Integer)attributes.get("checksum");

		if (checksum != null) {
			setChecksum(checksum);
		}

		String script = (String)attributes.get("script");

		if (script != null) {
			setScript(script);
		}

		String installed_by = (String)attributes.get("installed_by");

		if (installed_by != null) {
			setInstalled_by(installed_by);
		}

		Date installed_on = (Date)attributes.get("installed_on");

		if (installed_on != null) {
			setInstalled_on(installed_on);
		}

		Integer execution_time = (Integer)attributes.get("execution_time");

		if (execution_time != null) {
			setExecution_time(execution_time);
		}

		Boolean success = (Boolean)attributes.get("success");

		if (success != null) {
			setSuccess(success);
		}
	}

	@Override
	public java.lang.Object clone() {
		return new ChangelogWrapper((Changelog)_changelog.clone());
	}

	@Override
	public int compareTo(Changelog changelog) {
		return _changelog.compareTo(changelog);
	}

	/**
	* Returns the checksum of this changelog.
	*
	* @return the checksum of this changelog
	*/
	@Override
	public int getChecksum() {
		return _changelog.getChecksum();
	}

	/**
	* Returns the description of this changelog.
	*
	* @return the description of this changelog
	*/
	@Override
	public java.lang.String getDescription() {
		return _changelog.getDescription();
	}

	/**
	* Returns the execution_time of this changelog.
	*
	* @return the execution_time of this changelog
	*/
	@Override
	public int getExecution_time() {
		return _changelog.getExecution_time();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _changelog.getExpandoBridge();
	}

	/**
	* Returns the installed_by of this changelog.
	*
	* @return the installed_by of this changelog
	*/
	@Override
	public java.lang.String getInstalled_by() {
		return _changelog.getInstalled_by();
	}

	/**
	* Returns the installed_on of this changelog.
	*
	* @return the installed_on of this changelog
	*/
	@Override
	public Date getInstalled_on() {
		return _changelog.getInstalled_on();
	}

	/**
	* Returns the installed_rank of this changelog.
	*
	* @return the installed_rank of this changelog
	*/
	@Override
	public int getInstalled_rank() {
		return _changelog.getInstalled_rank();
	}

	/**
	* Returns the primary key of this changelog.
	*
	* @return the primary key of this changelog
	*/
	@Override
	public int getPrimaryKey() {
		return _changelog.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _changelog.getPrimaryKeyObj();
	}

	/**
	* Returns the script of this changelog.
	*
	* @return the script of this changelog
	*/
	@Override
	public java.lang.String getScript() {
		return _changelog.getScript();
	}

	/**
	* Returns the success of this changelog.
	*
	* @return the success of this changelog
	*/
	@Override
	public boolean getSuccess() {
		return _changelog.getSuccess();
	}

	/**
	* Returns the type of this changelog.
	*
	* @return the type of this changelog
	*/
	@Override
	public java.lang.String getType() {
		return _changelog.getType();
	}

	/**
	* Returns the version of this changelog.
	*
	* @return the version of this changelog
	*/
	@Override
	public java.lang.String getVersion() {
		return _changelog.getVersion();
	}

	@Override
	public int hashCode() {
		return _changelog.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _changelog.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _changelog.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _changelog.isNew();
	}

	/**
	* Returns <code>true</code> if this changelog is success.
	*
	* @return <code>true</code> if this changelog is success; <code>false</code> otherwise
	*/
	@Override
	public boolean isSuccess() {
		return _changelog.isSuccess();
	}

	@Override
	public void persist() {
		_changelog.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_changelog.setCachedModel(cachedModel);
	}

	/**
	* Sets the checksum of this changelog.
	*
	* @param checksum the checksum of this changelog
	*/
	@Override
	public void setChecksum(int checksum) {
		_changelog.setChecksum(checksum);
	}

	/**
	* Sets the description of this changelog.
	*
	* @param description the description of this changelog
	*/
	@Override
	public void setDescription(java.lang.String description) {
		_changelog.setDescription(description);
	}

	/**
	* Sets the execution_time of this changelog.
	*
	* @param execution_time the execution_time of this changelog
	*/
	@Override
	public void setExecution_time(int execution_time) {
		_changelog.setExecution_time(execution_time);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_changelog.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_changelog.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_changelog.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the installed_by of this changelog.
	*
	* @param installed_by the installed_by of this changelog
	*/
	@Override
	public void setInstalled_by(java.lang.String installed_by) {
		_changelog.setInstalled_by(installed_by);
	}

	/**
	* Sets the installed_on of this changelog.
	*
	* @param installed_on the installed_on of this changelog
	*/
	@Override
	public void setInstalled_on(Date installed_on) {
		_changelog.setInstalled_on(installed_on);
	}

	/**
	* Sets the installed_rank of this changelog.
	*
	* @param installed_rank the installed_rank of this changelog
	*/
	@Override
	public void setInstalled_rank(int installed_rank) {
		_changelog.setInstalled_rank(installed_rank);
	}

	@Override
	public void setNew(boolean n) {
		_changelog.setNew(n);
	}

	/**
	* Sets the primary key of this changelog.
	*
	* @param primaryKey the primary key of this changelog
	*/
	@Override
	public void setPrimaryKey(int primaryKey) {
		_changelog.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_changelog.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the script of this changelog.
	*
	* @param script the script of this changelog
	*/
	@Override
	public void setScript(java.lang.String script) {
		_changelog.setScript(script);
	}

	/**
	* Sets whether this changelog is success.
	*
	* @param success the success of this changelog
	*/
	@Override
	public void setSuccess(boolean success) {
		_changelog.setSuccess(success);
	}

	/**
	* Sets the type of this changelog.
	*
	* @param type the type of this changelog
	*/
	@Override
	public void setType(java.lang.String type) {
		_changelog.setType(type);
	}

	/**
	* Sets the version of this changelog.
	*
	* @param version the version of this changelog
	*/
	@Override
	public void setVersion(java.lang.String version) {
		_changelog.setVersion(version);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<Changelog> toCacheModel() {
		return _changelog.toCacheModel();
	}

	@Override
	public Changelog toEscapedModel() {
		return new ChangelogWrapper(_changelog.toEscapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _changelog.toString();
	}

	@Override
	public Changelog toUnescapedModel() {
		return new ChangelogWrapper(_changelog.toUnescapedModel());
	}

	@Override
	public java.lang.String toXmlString() {
		return _changelog.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ChangelogWrapper)) {
			return false;
		}

		ChangelogWrapper changelogWrapper = (ChangelogWrapper)obj;

		if (Objects.equals(_changelog, changelogWrapper._changelog)) {
			return true;
		}

		return false;
	}

	@Override
	public Changelog getWrappedModel() {
		return _changelog;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _changelog.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _changelog.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_changelog.resetOriginalValues();
	}

	private final Changelog _changelog;
}