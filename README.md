<h1> VMware has ended active development of this project, this repository will no longer be updated.</h1><br>Spring Music
============

This is a sample application that forks from spring-music to show some basic attributes around HTTP session state in Cloud Foundry. Refer to the originating project for specific info on spring-music usage.

In this demo, you will be able to demonstrate:
* Sticky sessions in Cloud Foundry
* What happens with application instance failure when there is / is not a HTTP Session store service bound to the app

## Running the application on Cloud Foundry

When running on Cloud Foundry, the application will detect the type of database service bound to the application
(if any). If a service of one of the supported types (MySQL, Postgres, Oracle, MongoDB, or Redis) is bound to the app, the
appropriate Spring profile will be configured to use the database service. The connection strings and credentials
needed to use the service will be extracted from the Cloud Foundry environment.

If no bound services are found containing any of these values in the name, then the `in-memory` profile will be used.

If more than one service containing any of these values is bound to the application, the application will throw an
exception and fail to start.

After installing the 'cf' [command-line interface for Cloud Foundry](http://docs.cloudfoundry.com/docs/using/managing-apps/cf/),
targeting a Cloud Foundry instance, and logging in, the application can be built and pushed using these commands:

~~~
$ ./gradlew assemble

$ cf push
...
requested state: started
instances: 1/1
usage: 512M x 1 instances
urls: spring-music--sf.cfapps.io
...
~~~

The application will be pushed using settings in the provided `manifest.yml` file. The output from the command will
show the URL that has been assigned to the application.

## Running the application for the first time

OK, now we're ready to test out the application a bit - use the hostname provided in your 'cf push' console output to access your application. Note the information icon (a circle with an "i" in it) at the top right? Great. Click on it - it displays what services you are bound to, app profiles you're using but also which application instance index number your application is running on [0,....,n]. Since you haven't scaled your application yet, it shows instance "0". There is also a number indicating the number of album changes you have performed (add, delete, update) in this session. Right now it says "0" as well - try creating some fanciful albums (I recommend Air Supply or Jefferson Starship, as you are running this in the cloud). After refreshing the page, your Mod count should increment.
Let's try scaling the application up to 2 instances:

~~~
$ cf scale spring-music -i 2
~~~

Return to your browser and make note of the instance your session is running on - now browse to http://YOUR_HOST_NAME/#/errors and click the "Kill" button - great, now our first instance is totally hosed, check out its status at http://console.run.pivotal.io - you might have to be quick. Refresh the page - you should your index has changed (you're running on a separate instance now) and your modification count is zeroed out. That sucks!

### Creating and binding services

Using the provided manifest, the application will be created without an external database (in the `in-memory` profile) or an in-memory HTTP Session store.
You can create and bind services to the application using the information below.

#### Binding a session store

This example anticipates that you will be using GemFire as an HTTP Session store, but will also work with any store that replaces the session manager in Tomcat.

~~~
$ cf create-service <> <> <>
$ cf bind-service <> <>
$ cf restart
~~~

#### Changing bound services

To test the application with different services, you can simply stop the app, unbind a service, bind a different
database service, and start the app:

~~~
$ cf unbind-service <app name> <service name>
$ cf bind-service <app name> <service name>
$ cf restart
~~~

#### Database drivers

Database drivers for MySQL, Postgres, MongoDB, and Redis are included in the project. To connect to an Oracle database,
you will need to download the appropriate driver (e.g. from http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-112010-090769.html?ssSourceSiteId=otnjp),
add the driver .jar file to the `src/main/webapp/WEB-INF/lib` directory in the project, and re-build the
application .war file using `./gradlew assemble`.
