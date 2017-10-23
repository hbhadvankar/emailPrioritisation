# emailPrioritisation
##This project consists of Web service API's which user will be using for managing priorities of their emails

###Instructions of pre requisite softwares.

- You need to install Jdk 1.8. Please refer below link to download and install jdk 1.8
[http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html]
- Download and Install Apache tomcat 7 server from below
[https://tomcat.apache.org/download-70.cgi]
- Download Apache Ant on your machine with the help of below link.
[http://ant.apache.org/bindownload.cgi]
Extract the package downloaded from above to a folder and set path in environment variable as below.
<path-up-to-ant-install-directory>/bin
once environment variable is set you can verify ant installation by typing "ant" or "ant -v" in command
prompt.
- Download mongoDB from below
[http://downloads.mongodb.org/win32/mongodb-win32-x86_64-2008plus-ssl-3.2.1-signed.msi?_ga=2.4690291.133099765.1508707694-1628380916.1508446564]
- Install mongoDB from below
[https://docs.mongodb.com/v3.2/installation/]
- Download and install Postman from below link.
[https://www.getpostman.com/docs/postman/launching_postman/installation_and_updates]

###Build and Deployment Procedure

- Refer below github url for email prioritisation project. You can either clone or download the source.
[https://github.com/hbhadvankar/emailPrioritisation]
- Once the project is available on your local machine, you need to build the project to create a war file. To
create a war file follow below steps.
  - Go to the project's root directory from command prompt and type “ant” command. If apache-ant is
installed on your machine, this command will build the war file for this project.
  - You will see the status of the build in your console. Once the build is successful the war file will be
created in the dist folder of the project. Please refer screenshot below.
- Once you have created a war you can deploy the same on the Apache-tomcat server which you have
downloaded and installed.
- Copy the war file into the webapps folder of Apache-tomcat server and start the server.
- You can follow the same procedure for build and deployment using eclipse as well.
  - You can right click on the build.xml mentioned above and click on run as “Ant build”.
  - This will build the war file. Copy the war file into the webapps folder of Apache-tomcat server and
start the server.
- Once your apache-tomcat server is completely up and running, open the postman application to test the
web services API calls.
- The project directory contains 2 postman collections mentioned below. Please import them to test the API
calls.
  - CRUDPriorityServiceCalls.postman_collection
  - EmailServiceCalls.postman_collection
- Also project directory contains JSON data. Please use them to test the API's.
