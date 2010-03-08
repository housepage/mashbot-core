This code goes with Willie Wheeler excellent article Web Services with Spring 2.5 and Apache CXF 2.0.

Note: this assumes you have maven2 installed and on your PATH

** (optional) To get the project setup in eclipse run the following: **

mvn eclipse:eclipse -DdownloadSources=true

Then open eclipse and create a new java project | create project from existing source
and specify this directory.  right click on the project in eclipse and go to properties | java build path | libraries | add variable 

Add a variable called M2_REPO which points to your maven 2 repository

now everything should be OK in eclipse


** To deploy/run: **

mvn jetty:run

-- or --
mvn install and copy the war to your tomcat webapps dir (or wherever you're deploying)

These are now your URLs of interest:

http://localhost:8080/cxf-webservices-1.0/webservices/contactus?wsdl
http://localhost:8080/cxf-webservices-1.0/webservices/contactus/getMessagesAsString


** Running the JUnit client (ContactUsServiceIntegrationTest) **

once you've got the server running (keep it running!), you can also try the JUnit test 
(which does much the same thing as Mr. Wheeler's follow up article on making a client
but then more simply (without the baggage of a webapp).  

Either open a new command prompt and run: mvn test
or run the unit test inside eclipse (or your IDE of choice).

