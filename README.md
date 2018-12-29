## Cyber Security Base - First Project

The application can be found in the following site: https://github.com/haakksekk/cybSecProj1.
Default username is “admin” and default password is “admin”. Some data will be initialized into the database when the application is started. The base for the application is the cloned cybersecuritybase-project starter code found in https://github.com/cybersecuritybase/cybersecuritybase-project.

This is a report that outlines how the flaws in this application can be identified and fixed. The flaws were chosen from the OWASP top ten list (https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf). One could identify the flaws by using this top ten list as a guide.

### Cross-Site Scripting (XSS) (A7:2017)
The first flaw is that the application is vulnerable to XSS Attacks. The top ten list describes three forms of this flaw which are the following: Reflected XSS, Stored XSS and DOM XSS. This application has the flaw because of an unsecure way of storing user inputs. To create this attack you should use other browser than Chrome because Chrome blocks these attacks by default.

Here are the steps to producing this attack:
1. Enter the site when running the application (http://localhost:8080)
2. Enter an input into the form with a script in it (for example <script>alert(1);</script>)
3. The script is stored and the user has done an XSS attack

The flaw and the problem is that in a secure application any user either shouldn’t be able to send this kind of input, or the application shouldn’t store it. To fix this issue one should change the th:utext into another type of thymeleaf text attribute. Values inputted with th:utext are unescaped and this causes the issue. A more safe attribute would be for example the th:text attribute. 

### Security Misconfiguration (A6:2017)
The second flaw of the application is the vulnerability to the use of Cross-Site Request Forgery (CSRF) attacks. This flaw together with the flaw that enables XSS attacks is a security misconfiguration. This flaw allows attackers to attack users on this site from another site. To identify this flaw one could use the OWASP ZAP tool which helps with identifying security flaws.

To fix the flaw one should remove the line http.csrf().disable(); in the SecurityConfiguration class. One should also fix the flaw that allows XSS attacks as the XSS attacks defeat the defense. These steps would prevent the usage of CSRF attacks on this application.

There is also a misconfiguration with the h2-console (http://localhost:8080/h2-console). The developer of the application has given everyone access to the console and forgotten to change the default password (“”, “”). To fix this flaw one should remove the line .antMatchers("/h2-console/**").permitAll() in the SecurityConfiguration class.

### Broken Authentication (A2:2017)
The application has an admin user with a password, but the password is hard coded and there is no possibility to change it in the application. The creator of the application has also disabled encrypting of passwords. To identify this flaw one can make a lucky guess and figure out that the default username is “admin” and the default password “admin”. The developer has also forgotten to create an authentication for the h2-console and also hasn’t changed the default password of the h2-database (“”, “”) or sometimes (“sa”, “”).

Default usernames and especially passwords count as broken authentication. One should immediately change at least the default password to something else. One could also create a password check to make sure that the password isn’t one of the easiest ones to guess. To fix the issue you should also fix the security misconfiguration issues of the application to make sure that only the admin can access the h2-console.

### Sensitive Data Exposure (A3:2017)
In the application only the admin can view the data submitted by users (http://localhost:8080/signups). However the username and password are easily guessable (“admin”, “admin”). By not changing the default password the developer has caused broken authentication. This flaw also causes sensitive data such as addresses and names of the users to be exposed. To identify this issue one should examine the application and find out that all users can view data that other users have submitted by guessing the easy admin username and password. To fix the issue one should create a proper authentication by changing the default logins. The system would then authenticate users to view only the data they themselves have submitted.

There is also a problem in the class SignupRepository method submitForm and in this line specifically model.addAttribute("name", signupRepository.findFirstByOrderByIdDesc().getName());. This line loads the last name added into the database. However when two users submit just after each other the other one will be shown the other users name. This also leads to sensitive data exposure. To fix this issue you should change the line to be like the line under it (model.addAttribute("address", address);). This change stops the application from fetching the newest data and only shows the data that the user themselves have submitted.

### Insufficient Logging & Monitoring (A10:2017)
The application also suffers from the flaw of insufficient logging & monitoring. This flaw gives attackers lots of time to test and plan their attacks. The application is unable to detect or alert for active attacks in real time or near real time and the application doesn’t generate log messages. This flaw can be fixed by implementing logging features and by testing the security side of the application with OWASP ZAP for example. Testing the application now is a good start to fixing the flaw.

### Injection (A1:2017)
The unescaped text inputted by th:utext attribute causes yet another security issue. The inputted data is unvalidated, unfiltered and unsanitezed. This flaw enables injection in the application. By injection of harmful code into the database an attacker can for example corrupt the database, delete values or combined with other flaws do even more harm. This flaw can be identified by fuzzing or investigating the database by manually doing an injection attack. To fix the issue one should change the html-attribute th:utext into for example th:text. The attribute needs to escape text in order for the application to be safe from this flaw. 
