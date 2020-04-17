# HelfenKannJeder project: Helf-O-Mat

[![Build Status](https://travis-ci.org/HelfenKannJeder/helf-o-mat.svg?branch=master)](https://travis-ci.org/HelfenKannJeder/helf-o-mat)

This project is the rewrite of the current [Helf-O-Mat](http://helf-o-mat.de) project of
HelfenKannJeder. Since the old version was written in PHP with TYPO3, the new version is
a single page application based on Spring and Angular.

## Build

For the build everything is integrated into Maven tasks and it can be simply build with
`mvn clean package` on the upper level. Inside a node environment is downloaded and
installed for the frontend.

## Requirements

For the execution there are two dependencies:

- MySQL Database (or another JDBC database if you change the configuration)
- Elasticsearch 2.4.x

The MySQL Database is necessary since we would like to be compatible with the old version.
For the basic setup you need some TYPO3 databases. Unfortunately, we are currently using
for testing data which is similar to the production data, so we haven't published a script
for that. To avoid that, you have two different options:

- Remove the dependency to the `helfomat-infrastructure-typo3` module from the `helfomat-web`
  module. Afterwards you only need a database connection and not the content.
  Disadvantage: You have currently only THW organizations in your repository.
- Ask [valentinz](mailto:valentin.zickner(at)helfenkannjeder(dot)de) for an database export

## Deployment

The deployment can be done by running the Spring services as normal applications.
You need to setup a reverse proxy for the api.

For automated deployment you can use the ansible project inside `helfomat-deployment`.
For the execution you can call the ansible playbook inside `local.yml`.
There are by default two inventories files, one for production and the other for development.
A sample to execute the development:

```ansible-playbook -i inventories/development/hosts.yml local.yml```

For the execution of the production you must encrypt the vault values:

```ansible-playbook -i inventories/production/hosts.yml --vault-id @prompt local.yml```

## Development

For development you can start `helfomat-web` like normal applications, you should use the
spring profile `dev`, this will enable logging to the console. For the frontend
project `helfomat-web-ui` you can execute `npm run start` to have it in development mode.
The API is automatically redirected to the backend.

For offline usage add the parameter `--spring.profiles.active=offline`, then the backend
will automatically mock all application which are require network.

You can reach the application at `http://localhost:8080/helf-o-mat/` or 
with the frontend running separate at `http://localhost:4200/`

## License

GNU GENERAL PUBLIC LICENSE Version 3
