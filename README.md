## README
please refer the relevant md files for more information on the projects architecture and design patterns, and the algorithms in use for matching teams and periods.
- MATCHING.md - for the algorithmic part of the project.
- ARCHITECTURE.md - for the projects architecture and design patterns.

### Requirements
- java 21 lts - for using spring boot 3.2.3 with virtual threads enabled. 
- postgres - for the database.
- nodejs for client side, if not using docker.
- docker - for running the project with docker.
- no need for maven, since I included the maven wrapper inside the project.

### How to run the project
1. without docker: 
- install and open postgres, using postgres user, and initialize `mydb` database, with 1-6 password.
- clone the repo.
- run `./mvnw clean package` in the root server directory.
- run `java -jar target/winner-system-1.0.0-SNAPSHOT.jar` in the root server directory.
- go to client directory, and run "npm install" and "npm start".

2. using docker:
- clone the repo.
- run `docker-compose up`, and after a minute, go to localhost:3000 in the browser.