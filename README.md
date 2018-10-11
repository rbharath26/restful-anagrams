# restful-anagrams

## Description

This project is to develop a RESTful WebService in Java using Spring MVC & REST, that use Embedded Tomcat. 

Given a local storage containing the dictionary and a search phrase provided as parameter of the call, returns all the combinations of words that are anagrams of the search phrase.

  - Letter  case does not matter.
  - Special characters should be ignored.
  - Ignore short dictionary words (those having only one or two letters)
  
An anagram is a type of word play, the result of rearranging the letters of a word or phrase to produce a new word or phrase, using all the original letters exactly once; for example, the word anagram can be rearranged into nag-a-ram.
 
Examples:

- William Shakespeare = I am a weakish speller
- Helmuth Elsner = me run the shell
- Computer = mute proc = CPU metro
- Visual Studio = loud USA visit = it’s usual void
- Wareneingang = gain new range = earn gang wine = eager wanning = aging earn new

Clone it and either load into your favorite IDE or use maven directly.

## Using an IDE to run the web application

You can run the system in your IDE by running main class _AnagramsApplication_. This class is a Spring Boot application using embedded Tomcat. In Spring Tool Suite use `Run As ... Spring Boot App` otherwise just run each as a Java application - each has a static `main()` entry point. You can use `mvn spring-boot:run` on his folder.

## The Angular client to test the service

The Angular project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.6.5.

To run this client it is necessary to have the [npm](https://www.npmjs.com/) installed and execute into the folder `angular-anagrams` the command `npm install`. After that you can execute `ng build` to build the project. The build artifacts will be stored in the `dist/` directory (in the directory `/dist` you can see a this build) 

To run the application, execute `npm start` or `ng serve` for a developer server. Navigate to [http://localhost:4200/](http://localhost:4200/). The application will automatically reload if you change any of the source files.

In this url, you can see a form with two fields:

- One of them to introduce a dictionary file
- The other to introduce a word or phrase 

Then, click on the button and wait to see the result, the list of anagrams found or a warning message if there is no anagrams.
