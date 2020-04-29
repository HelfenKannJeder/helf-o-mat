# HelfenKannJeder project: Helf-O-Mat

[![Build Status](https://travis-ci.org/HelfenKannJeder/helf-o-mat.svg?branch=master)](https://travis-ci.org/HelfenKannJeder/helf-o-mat)

This project is the [Helf-O-Mat](http://helf-o-mat.de) project of HelfenKannJeder.
It is a single page application based on Spring Boot and Angular.

## Build

Everything is available as maven task and the project can be build with `./mvnw -P helfomat-web,helfomat-web-ui,helfomat-import clean package` on the project root level.
There is an embedded node environment which will build the frontend.

To build only one of the different packages, please go ahead and specify the profile accordingly (`-P`):
* `helfomat-import` is going to build the backend service, the artifact is going to be placed in `helfomat-import/target`
* `helfomat-web` is going to build the backend service, the artifact is going to be placed in `helfomat-web/target`
* `helfomat-web-ui` is going to build the backend service, the artifact is going to be placed in `helfomat-web-ui/target`

## Requirements

For the execution of the web app there are two dependencies:

* PostgreSQL Database (or another JDBC database if you change the configuration)
* Elasticsearch 6.x

For the execution of the import app there are three dependencies:

* PostgreSQL Database (or another JDBC database if you change the configuration)
* MySQL (or another JDBC database if you change the configuration)
* Elasticsearch 6.x

The MySQL Database is the legacy database which is necessary since we are importing data from the old HelfenKannJeder V1.
For the basic setup you need the following custom TYPO3 databases tables:
* tx_helfenkannjeder_domain_model_address
* tx_helfenkannjeder_domain_model_employee
* tx_helfenkannjeder_domain_model_group
* tx_helfenkannjeder_domain_model_grouptemplate
* tx_helfenkannjeder_domain_model_grouptemplatecategory
* tx_helfenkannjeder_domain_model_organisation
* tx_helfenkannjeder_domain_model_organisationtype
* tx_helfenkannjeder_domain_model_workinghour
* tx_helfenkannjeder_organisaton_contactperson_mm
* tx_helfenkannjeder_group_contactperson_mm
* tx_helfenkannjeder_workinghour_group_mm

The schema can be found [in the legacy repository](https://github.com/HelfenKannJeder/TYPO3-helfen_kann_jeder/blob/master/ext_tables.sql)

When you would like to have a quick start for the import, please reach out to [vzickner](mailto:valentin.zickner(at)helfenkannjeder(dot)de) for a database export including organizations.

## Deployment

For the deployment we are providing docker containers at [Docker Hub](https://hub.docker.com/u/helfenkannjeder).
The container names are aligned to the names of the maven modules. 

## Development

For your convinience there is a [docker-compose.yml](helfomat-docker/docker-compose.yml) file available.
This file is starting all required dependencies.
For development mode you can start `helfomat-web` like any normal spring boot applications.
You should use the spring profile `local` to enable the default connectivity to the docker containers.
Inside the frontend project `helfomat-web-ui` you can execute `npm run start` to run it in development mode.
This command is automatically proxying the API to the backend service.

You can reach the API at `http://localhost:8080/`.
The frontend is running at `http://localhost:4200/`.

### Offline Usage (Unsupported)

For offline usage you can start the spring boot app with the parameter `--spring.profiles.active=offline`.
The frontend can be started with `npm run start:offline`.

## License

GNU GENERAL PUBLIC LICENSE Version 3
