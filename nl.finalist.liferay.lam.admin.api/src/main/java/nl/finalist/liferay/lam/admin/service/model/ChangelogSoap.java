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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class ChangelogSoap implements Serializable {
	public static ChangelogSoap toSoapModel(Changelog model) {
		ChangelogSoap soapModel = new ChangelogSoap();

		soapModel.setInstalled_rank(model.getInstalled_rank());
		soapModel.setVersion(model.getVersion());
		soapModel.setDescription(model.getDescription());
		soapModel.setType(model.getType());
		soapModel.setChecksum(model.getChecksum());
		soapModel.setScript(model.getScript());
		soapModel.setInstalled_by(model.getInstalled_by());
		soapModel.setInstalled_on(model.getInstalled_on());
		soapModel.setExecution_time(model.getExecution_time());
		soapModel.setSuccess(model.getSuccess());

		return soapModel;
	}

	public static ChangelogSoap[] toSoapModels(Changelog[] models) {
		ChangelogSoap[] soapModels = new ChangelogSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ChangelogSoap[][] toSoapModels(Changelog[][] models) {
		ChangelogSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ChangelogSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ChangelogSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ChangelogSoap[] toSoapModels(List<Changelog> models) {
		List<ChangelogSoap> soapModels = new ArrayList<ChangelogSoap>(models.size());

		for (Changelog model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ChangelogSoap[soapModels.size()]);
	}

	public ChangelogSoap() {
	}

	public int getPrimaryKey() {
		return _installed_rank;
	}

	public void setPrimaryKey(int pk) {
		setInstalled_rank(pk);
	}

	public int getInstalled_rank() {
		return _installed_rank;
	}

	public void setInstalled_rank(int installed_rank) {
		_installed_rank = installed_rank;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		_version = version;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public int getChecksum() {
		return _checksum;
	}

	public void setChecksum(int checksum) {
		_checksum = checksum;
	}

	public String getScript() {
		return _script;
	}

	public void setScript(String script) {
		_script = script;
	}

	public String getInstalled_by() {
		return _installed_by;
	}

	public void setInstalled_by(String installed_by) {
		_installed_by = installed_by;
	}

	public Date getInstalled_on() {
		return _installed_on;
	}

	public void setInstalled_on(Date installed_on) {
		_installed_on = installed_on;
	}

	public int getExecution_time() {
		return _execution_time;
	}

	public void setExecution_time(int execution_time) {
		_execution_time = execution_time;
	}

	public boolean getSuccess() {
		return _success;
	}

	public boolean isSuccess() {
		return _success;
	}

	public void setSuccess(boolean success) {
		_success = success;
	}

	private int _installed_rank;
	private String _version;
	private String _description;
	private String _type;
	private int _checksum;
	private String _script;
	private String _installed_by;
	private Date _installed_on;
	private int _execution_time;
	private boolean _success;
}