# Audition API

The purpose of this Spring Boot application is to test general knowledge of SpringBoot, Java, Gradle etc. It is created for hiring needs of our company but can be used for other purposes.

## Overarching expectations & Assessment areas

<pre>
This is not a university test. 
This is meant to be used for job applications and MUST showcase your full skillset. 
<b>As such, PRODUCTION-READY code must be written and submitted. </b> 
</pre>

- clean, easy to understand code
- good code structures
- Proper code encapsulation
- unit tests with minimum 80% coverage.
- A Working application to be submitted.
- Observability. Does the application contain Logging, Tracing and Metrics instrumentation?
- Input validation.
- Proper error handling.
- Ability to use and configure rest template. We allow for half-setup object mapper and rest template
- Not all information in the Application is perfect. It is expected that a person would figure these out and correct.
  
## Getting Started

### Prerequisite tooling

- Any Springboot/Java IDE. Ideally IntelliJIdea.
- Java 17
- Gradle 8
  
### Prerequisite knowledge

- Java
- SpringBoot
- Gradle
- Junit

### Importing Google Java codestyle into INtelliJ

```
- Go to IntelliJ Settings
- Search for "Code Style"
- Click on the "Settings" icon next to the Scheme dropdown
- Choose "Import -> IntelliJ Idea code style XML
- Pick the file "google_java_code_style.xml" from root directory of the application
__Optional__
- Search for "Actions on Save"
    - Check "Reformat Code" and "Organise Imports"
```

---
**NOTE** -
It is  highly recommended that the application be loaded and started up to avoid any issues.

---

## Audition Application information

This section provides information on the application and what the needs to be completed as part of the audition application.

The audition consists of multiple TODO statements scattered throughout the codebase. The applicants are expected to:

- Complete all the TODO statements.
- Add unit tests where applicants believe it to be necessary.
- Make sure that all code quality check are completed.
- Gradle build completes sucessfully.
- Make sure the application if functional.

## Submission process
Applicants need to do the following to submit their work: 
- Clone this repository
- Complete their work and zip up the working application. 
- Applicants then need to send the ZIP archive to the email of the recruiting manager. This email be communicated to the applicant during the recruitment process. 

  
---
## Additional Information based on the implementation

Below TODO tasks are done - 

1. Controller and Service implemetation.
2. Configuration of RestTemplate and ObjectMapper.
3. Injection of openTelemetry trace and span Ids in the response headers in RestCallLoggingInterceptor.java.
4. Actuator setting for info and health endpoints.
5. Logger and Exception handling.
6. Junit test cases for positive senarios only.

Kindly note, further code optimizations are possible, these are not done due to time constraint. Also, code coverage can be increased by writing negative test cases. Since Gradle and IntelliJ IDEA are new for me, so consumed a significant time to convert pom.xml based Maven project.

I pushed two repositories for each-
"https://github.com/sushilshakya-github/audition_api" is for Gradle and IntelliJIdea and 
https://github.com/sushilshakya-github/audition-api for Maven and STS(Eclipse).
      
