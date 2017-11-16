CREATE TABLE tx_helfenkannjeder_domain_model_organisationtype (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	namedisplay varchar(255) DEFAULT '' NOT NULL,
	acronym varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	picture blob,
	teaser blob,
	logos blob,
	pseudo tinyint(4) DEFAULT '0' NOT NULL,
	group_template_categories int(11) DEFAULT '0' NOT NULL,
	fegroup int(11) DEFAULT '0' NOT NULL,
	registerable tinyint(4) DEFAULT '0' NOT NULL,
	hide_in_result tinyint(4) DEFAULT '0' NOT NULL,
	dummy_organisation int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_organisation (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	organisationtype int(11) DEFAULT '0' NOT NULL,
	website varchar(255) DEFAULT '' NOT NULL,
	street varchar(255) DEFAULT '' NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	zipcode varchar(20) DEFAULT '0' NOT NULL,
	umbrellaorganisation varchar(255) DEFAULT '' NOT NULL,
	nationalassociation varchar(255) DEFAULT '' NOT NULL,
	addressappendix varchar(255) DEFAULT '' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	telephone varchar(255) DEFAULT '' NOT NULL,
	contactpersons int(11) DEFAULT '0' NOT NULL,
	logo blob,
	pictures blob,
	feuser int(11) DEFAULT '0' NOT NULL,
	groups int(11) DEFAULT '0' NOT NULL,
	workinghours int(11) DEFAULT '0' NOT NULL,
	employees int(11) DEFAULT '0' NOT NULL,
	matrices int(11) DEFAULT '0' NOT NULL,
	defaultaddress int(11) DEFAULT '0' NOT NULL,
	addresses int(11) DEFAULT '0' NOT NULL,
	hash varchar(255) DEFAULT '' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,
	is_dummy tinyint(4) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	t3ver_oid int(11) DEFAULT '0' NOT NULL,
	t3ver_id int(11) DEFAULT '0' NOT NULL,
	t3ver_wsid int(11) DEFAULT '0' NOT NULL,
	t3ver_label varchar(30) DEFAULT '' NOT NULL,
	t3ver_state tinyint(4) DEFAULT '0' NOT NULL,
	t3ver_stage tinyint(4) DEFAULT '0' NOT NULL,
	t3ver_count int(11) DEFAULT '0' NOT NULL,
	t3ver_tstamp int(11) DEFAULT '0' NOT NULL,
	t3_origuid int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_organisationdraft (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	organisationtype int(11) DEFAULT '0' NOT NULL,
	website varchar(255) DEFAULT '' NOT NULL,
	street varchar(255) DEFAULT '' NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	zipcode varchar(20) DEFAULT '0' NOT NULL,
	umbrellaorganisation varchar(255) DEFAULT '' NOT NULL,
	nationalassociation varchar(255) DEFAULT '' NOT NULL,
	addressappendix varchar(255) DEFAULT '' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	telephone varchar(255) DEFAULT '' NOT NULL,
	contactpersons int(11) DEFAULT '0' NOT NULL,
	logo blob,
	pictures blob,
	feuser int(11) DEFAULT '0' NOT NULL,
	groups int(11) DEFAULT '0' NOT NULL,
	workinghours int(11) DEFAULT '0' NOT NULL,
	employees int(11) DEFAULT '0' NOT NULL,
	matrices int(11) DEFAULT '0' NOT NULL,
	defaultaddress int(11) DEFAULT '0' NOT NULL,
	addresses int(11) DEFAULT '0' NOT NULL,
	request int(11) DEFAULT '0' NOT NULL,
	requesttime int(11) DEFAULT '0' NOT NULL,
	supporter int(11) DEFAULT '0' NOT NULL,
	hash varchar(255) DEFAULT '' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,
	remind_last int(11) DEFAULT '0' NOT NULL,
	remind_count int(11) DEFAULT '0' NOT NULL,
	is_dummy tinyint(4) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	t3ver_oid int(11) DEFAULT '0' NOT NULL,
	t3ver_id int(11) DEFAULT '0' NOT NULL,
	t3ver_wsid int(11) DEFAULT '0' NOT NULL,
	t3ver_label varchar(30) DEFAULT '' NOT NULL,
	t3ver_state tinyint(4) DEFAULT '0' NOT NULL,
	t3ver_stage tinyint(4) DEFAULT '0' NOT NULL,
	t3ver_count int(11) DEFAULT '0' NOT NULL,
	t3ver_tstamp int(11) DEFAULT '0' NOT NULL,
	t3_origuid int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_group (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	name varchar(255) DEFAULT '' NOT NULL,
	website varchar(255) DEFAULT '' NOT NULL,
	workinghours text NOT NULL,
	description text NOT NULL,
	minimum_age int(11) DEFAULT '0' NOT NULL,
	maximum_age int(11) DEFAULT '0' NOT NULL,
	matrix int(11) DEFAULT '0' NOT NULL,
	street varchar(255) DEFAULT '' NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	zipcode varchar(20) DEFAULT '0' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	contactpersons int(11) DEFAULT '0' NOT NULL,
	template int(11) DEFAULT '0' NOT NULL,
	sort int(11) DEFAULT '0' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_groupdraft (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	name varchar(255) DEFAULT '' NOT NULL,
	website varchar(255) DEFAULT '' NOT NULL,
	workinghours text NOT NULL,
	description text NOT NULL,
	minimum_age int(11) DEFAULT '0' NOT NULL,
	maximum_age int(11) DEFAULT '0' NOT NULL,
	matrix int(11) DEFAULT '0' NOT NULL,
	street varchar(255) DEFAULT '' NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	zipcode varchar(20) DEFAULT '0' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	contactpersons int(11) DEFAULT '0' NOT NULL,
	template int(11) DEFAULT '0' NOT NULL,
	sort int(11) DEFAULT '0' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_grouptemplatecategory (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisationtype int(11) DEFAULT '0' NOT NULL,
	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	group_templates int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,
	sort int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_grouptemplate (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	grouptemplatecategory int(11) DEFAULT '0' NOT NULL,
	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	suggestion text NOT NULL,
	minimum_age int(11) DEFAULT '0' NOT NULL,
	maximum_age int(11) DEFAULT '0' NOT NULL,
	matrix int(11) DEFAULT '0' NOT NULL,
	isdefault tinyint(4) DEFAULT '0' NOT NULL,
	isoptional tinyint(4) DEFAULT '0' NOT NULL,
	isfeature tinyint(4) DEFAULT '0' NOT NULL,
	sort int(11) DEFAULT '0' NOT NULL,
	group_of_group_template int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_activityfield (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	activities int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_activity (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	keywords text NOT NULL,
	words int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_employee (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	iscontact int(11) DEFAULT '0' NOT NULL,
	rank varchar(256) DEFAULT '' NOT NULL,
	surname varchar(256) DEFAULT '' NOT NULL,
	prename varchar(256) DEFAULT '' NOT NULL,
	headline varchar(256) DEFAULT '' NOT NULL,
	motivation text NOT NULL,
	birthday int(11) DEFAULT '0' NOT NULL,
	pictures blob,
	mail varchar(256) DEFAULT '' NOT NULL,
	street varchar(256) DEFAULT '' NOT NULL,
	city varchar(256) DEFAULT '' NOT NULL,
	telephone varchar(256) DEFAULT '' NOT NULL,
	mobilephone varchar(256) DEFAULT '' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_employeedraft (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	iscontact int(11) DEFAULT '0' NOT NULL,
	rank varchar(256) DEFAULT '' NOT NULL,
	surname varchar(256) DEFAULT '' NOT NULL,
	prename varchar(256) DEFAULT '' NOT NULL,
	headline varchar(256) DEFAULT '' NOT NULL,
	motivation text NOT NULL,
	birthday int(11) DEFAULT '0' NOT NULL,
	pictures blob,
	mail varchar(256) DEFAULT '' NOT NULL,
	street varchar(256) DEFAULT '' NOT NULL,
	city varchar(256) DEFAULT '' NOT NULL,
	telephone varchar(256) DEFAULT '' NOT NULL,
	mobilephone varchar(256) DEFAULT '' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_activityfield_activity_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_workinghour_group_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_workinghourdraft_groupdraft_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_organisatondraft_contactperson_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_organisaton_contactperson_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_group_contactperson_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_groupdraft_contactperson_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_matrixfield (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	matrix int(11) DEFAULT '0' NOT NULL,
	organisation int(11) DEFAULT '0' NOT NULL,
	activity int(11) DEFAULT '0' NOT NULL,
	activityfield int(11) DEFAULT '0' NOT NULL,
	grade smallint(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_matrix (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	organisation int(11) DEFAULT '0' NOT NULL,
	matrixfields int(11) DEFAULT '0' NOT NULL,
	feuser int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_workinghour (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	day int(11) DEFAULT '0' NOT NULL,
	starttimehour int(11) DEFAULT '0' NOT NULL,
	stoptimehour int(11) DEFAULT '0' NOT NULL,
	starttimeminute int(11) DEFAULT '0' NOT NULL,
	stoptimeminute int(11) DEFAULT '0' NOT NULL,
	groups int(11) DEFAULT '0' NOT NULL,
	addition text NOT NULL,
	address int(11) DEFAULT '0' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_workinghourdraft (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	day int(11) DEFAULT '0' NOT NULL,
	starttimehour int(11) DEFAULT '0' NOT NULL,
	stoptimehour int(11) DEFAULT '0' NOT NULL,
	starttimeminute int(11) DEFAULT '0' NOT NULL,
	stoptimeminute int(11) DEFAULT '0' NOT NULL,
	groups int(11) DEFAULT '0' NOT NULL,
	addition text NOT NULL,
	address int(11) DEFAULT '0' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_address (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	addressappendix varchar(255) DEFAULT '' NOT NULL,
	street varchar(255) DEFAULT '' NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	zipcode varchar(20) DEFAULT '0' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	telephone varchar(255) DEFAULT '' NOT NULL,
	website varchar(255) DEFAULT '' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_addressdraft (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	organisation int(11) DEFAULT '0' NOT NULL,
	addressappendix varchar(255) DEFAULT '' NOT NULL,
	street varchar(255) DEFAULT '' NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	zipcode varchar(20) DEFAULT '0' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	telephone varchar(255) DEFAULT '' NOT NULL,
	website varchar(255) DEFAULT '' NOT NULL,
	reference int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_backer (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	degree varchar(255) DEFAULT '' NOT NULL,
	prename varchar(255) DEFAULT '' NOT NULL,
	surname varchar(255) DEFAULT '' NOT NULL,
	company varchar(255) DEFAULT '' NOT NULL,
	status varchar(255) DEFAULT '' NOT NULL,
	thumbnail varchar(255) DEFAULT '' NOT NULL,
	picture varchar(255) DEFAULT '' NOT NULL,
	type int(11) DEFAULT '0' NOT NULL,
	description text NOT NULL,
	sort int(11) DEFAULT '0' NOT NULL,
	since int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_registerorganisationprogress (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	modified int(11) DEFAULT '0' NOT NULL,
	sessionid varchar(255) DEFAULT '' NOT NULL,
	agreement tinyint(4) DEFAULT '0' NOT NULL,
	organisationtype int(11) DEFAULT '0' NOT NULL,
	typename varchar(11) DEFAULT '' NOT NULL,
	typeacronym varchar(11) DEFAULT '' NOT NULL,
	typedescription text NOT NULL,
	city varchar(255) DEFAULT '' NOT NULL,
	longitude float(10) DEFAULT '0.000000' NOT NULL,
	latitude float(10) DEFAULT '0.000000' NOT NULL,
	department varchar(255) DEFAULT '' NOT NULL,
	organisationname varchar(255) DEFAULT '' NOT NULL,
	username varchar(255) DEFAULT '' NOT NULL,
	password varchar(255) DEFAULT '' NOT NULL,
	password2 varchar(255) DEFAULT '' NOT NULL,
	password_saved tinyint(4) DEFAULT '0',
	mail varchar(255) DEFAULT '' NOT NULL,
	feuser int(11) DEFAULT '0' NOT NULL,
	organisation int(11) DEFAULT '0' NOT NULL,
	laststep int(11) DEFAULT '0' NOT NULL,
	finisheduntil int(11) DEFAULT '0' NOT NULL,
	mail_hash varchar(255) DEFAULT '' NOT NULL,
	supporter int(11) DEFAULT '0' NOT NULL,
	surname varchar(255) DEFAULT '' NOT NULL,
	prename varchar(255) DEFAULT '' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_helfomat (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	questions int(11) DEFAULT '0' NOT NULL,
	used int(11) DEFAULT '0' NOT NULL,
	method int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_helfomatquestion (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	helfomat int(11) DEFAULT '0' NOT NULL,
	question varchar(255) DEFAULT '' NOT NULL,
	description text NOT NULL,
	positive int(11) DEFAULT '0' NOT NULL,
	negative int(11) DEFAULT '0' NOT NULL,
	positivenot int(11) DEFAULT '0' NOT NULL,
	negativenot int(11) DEFAULT '0' NOT NULL,
	group_positive int(11) DEFAULT '0' NOT NULL,
	group_negative int(11) DEFAULT '0' NOT NULL,
	group_positivenot int(11) DEFAULT '0' NOT NULL,
	group_negativenot int(11) DEFAULT '0' NOT NULL,
	sort int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_domain_model_groupofgrouptemplate (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	name varchar(255) DEFAULT '' NOT NULL,
	group_templates int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	deleted tinyint(4) DEFAULT '0' NOT NULL,
	hidden tinyint(4) DEFAULT '0' NOT NULL,
	sys_language_uid int(11) DEFAULT '0' NOT NULL,
	l18n_parent int(11) DEFAULT '0' NOT NULL,
	l18n_diffsource mediumblob NOT NULL,
	access_group int(11) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomatquestion_positive_activity_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomatquestion_negative_activity_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);


CREATE TABLE tx_helfenkannjeder_helfomatquestion_positive_not_activity_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomatquestion_negative_not_activity_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomatquestion_positive_gogt_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomatquestion_negative_gogt_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);


CREATE TABLE tx_helfenkannjeder_helfomatquestion_positive_not_gogt_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomatquestion_negative_not_gogt_mm (
	uid int(11) NOT NULL auto_increment,
	pid int(11) DEFAULT '0' NOT NULL,

	uid_local int(11) DEFAULT '0' NOT NULL,
	uid_foreign int(11) DEFAULT '0' NOT NULL,
	sorting int(11) DEFAULT '0' NOT NULL,
	sorting_foreign int(11) DEFAULT '0' NOT NULL,

	tstamp int(11) DEFAULT '0' NOT NULL,
	crdate int(11) DEFAULT '0' NOT NULL,
	hidden tinyint(3) DEFAULT '0' NOT NULL,

	PRIMARY KEY (uid)
);

CREATE TABLE tx_helfenkannjeder_helfomat_question_organisation_mapping_cache (
	questionUid int(11) DEFAULT '0' NOT NULL,
	organisationUid int(11) DEFAULT '0' NOT NULL,
	found int(11) DEFAULT '0' NOT NULL,
	np int(11) DEFAULT '0' NOT NULL,
	pn int(11) DEFAULT '0' NOT NULL,
	nn int(11) DEFAULT '0' NOT NULL,
	pp int(11) DEFAULT '0' NOT NULL,
	yes int(11) DEFAULT '0' NOT NULL,
	no int(11) DEFAULT '0' NOT NULL,
	question varchar(255) DEFAULT '' NOT NULL,
	helfomat int(11) DEFAULT '0' NOT NULL,
	organisationname varchar(255) DEFAULT '' NOT NULL,
	gtgmapo int(11) DEFAULT '0' NOT NULL
);