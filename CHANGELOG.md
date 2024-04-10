## Changelog
#### Version 0.0.1-SNAPSHOT:
- Initial version
- set up basic project structure: spring security, postgres, sse, virtual thread support, and singleThread task executor for blocking tasks.
- added webmvc config for cors handling
- set up jpa user repository for userDetailService custom implementation, with email as username.

#### Version 1.0.0-SNAPSHOT:
- fixed bugs in spring security and @PreAuthorize annotation and methods using it. 
- fixed sse bugs.
- set up database loading with initial data.
- added docker-compose file for fast loading postgres database.
- added dockerfile for building the docker image.
- added timerEvents for managing all timers in server side, letting the client stay stupid.
- todo: manage and test bets interface in client and server side.