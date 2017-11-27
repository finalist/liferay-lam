create table LAM_Changelog (
	installed_rank INTEGER not null primary key,
	version VARCHAR(75) null,
	description VARCHAR(75) null,
	type VARCHAR(75) null,
	checksum INTEGER,
	script VARCHAR(75) null,
	installed_by VARCHAR(75) null,
	installed_on DATE null,
	execution_time INTEGER,
	success BOOLEAN
);