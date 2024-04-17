## README
please refer the relevant md files for more information on the projects architecture and design patterns, and the algorithms in use for matching teams and periods.
- MATCHING.md - for the algorithmic part of the project.
- ARCHITECTURE.md - for the projects architecture and design patterns.

### Requirements
- java 21 lts - for using spring boot 3.2.3 with virtual threads enabled. 
- postgres - for the database.
- nodejs for client side, if not using docker.
- docker - for running the project with docker.
- no need for maven, since I included the maven wrapper(3.9.6) inside the project.

### How to run the project
1. without docker: 
- install and open postgres, using postgres user, and initialize `mydb` database, with 1-6 password.
- clone the [repo](https://github.com/benayat/winner-system.git).
- run `./mvnw clean package -DskipTests` && `./mvnw spring-boot:run`.
- clone the [ui repo](https://github.com/benayat/winner-system-ui.git), and inside run `npm install` and `npm start`.

2. using docker:
- clone the [ui repo](https://github.com/benayat/winner-system-ui.git) and follow the instructions in the README to build a docker image.
- update the docker-compose.yml file with the correct UI image name.
- run `docker-compose up`, and when it's ready, go to localhost:3000 in the browser.