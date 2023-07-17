# HelfenKannJeder project: Helf-O-Mat

This project is the [Helf-O-Mat](http://helf-o-mat.de) project of HelfenKannJeder.
It is a single page application based on Angular.

## Build

To build the project, change to the `helfomat-web-ui` directory and enter the following commands:

```bash
npm install
npm build build-prod:kiosk
```

## Requirements

Node and NPM are required to run the project.

## Deployment

For the deployment we are providing docker containers at [Docker Hub](https://hub.docker.com/u/helfenkannjeder).
The container names are aligned to the names of the maven modules. 

## Development

Type `npm run start`

The frontend is running at `http://localhost:4200/`.

### Users

To sign-in to the application you need to go to [Keycloak](http://localhost:8085/auth/admin/master/console/#/realms/helfomat/users) and create a user.
The username for the admin console is `admin` and the password is `secret`.
After pressing the `Add user` button, you need to give the user a username.
Once the user is created you need to give the user a password in the `Credentials` tab.
In the `Role Mapping` you can assign one of the Helf-O-Mat roles (e.g. `helfomat_admin`).

### Offline Usage (Unsupported)

For offline usage you can start the spring boot app with the parameter `--spring.profiles.active=offline`.
The frontend can be started with `npm run start:offline`.

## License

GNU GENERAL PUBLIC LICENSE Version 3
