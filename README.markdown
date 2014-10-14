# memcached session manager - High Availability Options

## High Availability Options:
Project based on Magro's Memcached Session Manager, but considering we the Lifecycle of the Application detached from the Session Distribution functionality, therefore the features to implement are as follow:

- High Availability on Client-Side:

We should provide several configuration options for number of retries, timeouts, etc. Application Lifecycle should be able to survive and not have a Single Point of Failure, given the Cluster is the only entry point we wish the App Instances to be able to survive with a Disaster Recovery Strategy.
	
	* If Cluster connectivity is lost, fall back to using Near Cache / Local storage
	* If Cluster data becomes corrupt or any error is detected, fall back to using Near Cache / Local storage
	* Retries/Reconnect mechanisms
	* If Reconnection is achieved, dump all Near Cache / Local storage and start using Distribution as if new

- Serialization High Availability:

We should provide several configuration options for the Application Developer to decide the Serialization Tolerance policy. This comes up when having many developers on an application using several libs, frameworks and other and tracking the use of the session may be difficult. E.g. Spring Security uses the session to store User data, some objects are non serializable but Spring knows how to recover from them being Null.
	
	* If Serialization of an attribute fails, tag session as Local and don't try to Distribute it, keep trying with other sessions. Log Warning.
	* If Serialization of an attribute fails, stop Serialization and Distribution completely. Log Warning.
	* If Serialization of an attribute fails, stop Application with Error (default behavior)
	* If Serialization of an attribute fails, save and log Black List of non-serializable attributes and leave attribute as null. Distribute rest of the Session. 

- Distributed Listeners Support:

According to the Java Servlet SPEC we have two main Interfaces to have listeners, javax.servlet.http.HttpSessionListener and javax.servlet.http.HttpSessionAttributeListener to listen to events of Creation, Destruction and Attributes add, update and remove. We want to create a basic infrastructure to handle these in a Distributed way:
	
	* When a Listener is registered in the web.xml file of the Application, we need to create an Event Listener in the cluster.
	Details TBD

- Review JSESSIONID creation mechanism:

Even if the collision chance using SecureRandom() is pretty low, we can lock the creation of the JSESSIONID in the Cluster for all client Applications thus making sure there are no collisions at all and then create a simple loop in the event there is a repeated entry.

- Granularity of Updates

Give the option to the Application Developer to send the entire Session as a whole or send only manage Deltas. This brings lots of complexity as now we need to handle all pre-existing features, how to handle Distributed Expiration if we have lots of Entries in the Cluster for any single Session? Benefits include: less starvation scenarios, less Network traffic, better locking (per Attribute instead of per Session). 
Steps and Details TBD

Following is the original Description for memcached-session-manager:

[![Build Status](https://jenkins.inoio.de/job/memcached-session-manager%20master/badge/icon)](https://jenkins.inoio.de/job/memcached-session-manager%20master/)


memcached-session-manager is a tomcat session manager that keeps sessions in memcached, for highly available, scalable and fault tolerant web applications. It supports both sticky and non-sticky configurations, and is currently working with tomcat 6.x and 7.x. For sticky sessions session failover (tomcat crash) is supported, for non-sticky sessions this is the default (a session is served by default by different tomcats for different requests). Also memcashed failover (memcached crash) is supported via migration of sessions. There shall also be no single point of failure, so when a memcached fails the session will not be lost (but either be available in tomcat or in another memcached).

The project home page is located at [googlecode.com](http://code.google.com/p/memcached-session-manager/).

## Installation and Configuration
Basically you must put the spymemcached jar and the memcached-session-manager jar into tomcat's lib folder.
Additionally you must set the Manager class and add some configuration attributes. This is described in detail in the [SetupAndConfiguration wiki page](http://code.google.com/p/memcached-session-manager/wiki/SetupAndConfiguration).

## Where to get help
Checkout the [wiki](http://code.google.com/p/memcached-session-manager/w/list) for documentation, contact the [mailing list](http://groups.google.com/group/memcached-session-manager) or [submit an issue](http://code.google.com/p/memcached-session-manager/issues/list).

## How to contribute
If you want to contribute to this project you can fork the [sources on github](https://github.com/magro/memcached-session-manager), make your changes and submit a pull request. Alternatively you can [submit an issue](http://code.google.com/p/memcached-session-manager/issues/list) with a patch attached. Or you start on the [mailing list](http://groups.google.com/group/memcached-session-manager) and we'll see how we can work together.

## Samples
There's a [github project](https://github.com/magro/msm-sample-webapp) that has various memcached-session-manager example configurations, both sticky and non-sticky, with tomcat 6 and tomcat7, with wicket or openwebbeans and more. Just checkout the different branches and see if there's s.th. interesting for you.
