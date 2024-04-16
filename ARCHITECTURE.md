## Architecture summary and considerations
In this part, I'll discuss the projects architecture and design patterns, excluding obvious things.
- The basic architecture of the project is based on the Model-View-Controller (MVC) design pattern, which in our case it's model, controller, service and repository.
- all configuration beans are defined in `config` package - the cache, cors, webMvc(for cors), cors(for security), schedulers, and spring security config.
- in aspect package, I used aop to encrypt password before sending it to the database, and before returning the userProfile back to client, to avoid security issues.
- in the factory package, I added a Sse factory to create and handle the lifecycle of both secure and insecure ssh connections.

#### spring security
I used spring security to secure the application, and I used the following configurations:
- a custom, JPA based userDetailsService to load the user by username, and I used a custom password encoder to encode the password, and set both up in an AuthenticationManager bean. 
- I disabled csrf since it's a simple dev project, and I used cors to allow calls from the UI. 
- declared all secure and unsecure endpoint, in favor of readability. 
- added a remember me service to keep the client logged in after refreshing the page.
- set up a logout handler, to invalidate the session and remove/complete the user emitter.

#### event handling mechanism
In order to avoid race conditions and complexities, I seperated the game running mechanism from the events sending to the client. 
- The season run in SeasonRunnerService in an async method, seperate from the main method. then, to send an event to the client - I first sent it to an applicationEventPublisher, and handled it elsewhere.
- I used an abstract SseEvent class, and all relevant events classes extend it. The events are sent to the client with a scheduller, and a ConcurrentLinkedQueue to handle large volume of concurrent reads, with a single way out.
