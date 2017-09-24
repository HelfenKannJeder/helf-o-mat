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

- Disable in the BatchConfiguration.java the `importOrganisationFromJpa` step. Then you
  only need a database connection and not the content. Disadvantage: You have currently only
  THW organisations in your repository.
- Ask [valentinz](mailto:valentin.zickner(at)helfenkannjeder(dot)de) for an database export

## Deployment

The deployment can be done by running the Spring services as normal applications. For the
frontend there is currently also a Spring application, but it will be removed soon.
You need to setup a reverse proxy for the api.

## Development

For development you can start `web` and `import` like normal applications. For the frontend
project `client` you can execute `npm run start` to have it in development mode. The API
is automatically redirected to the backend. 

## License

tbd