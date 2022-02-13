# Searchly---Search-Engine-Refactored
Introductory Programming Final Project 2021, using Java programming language


# Group 18
* Ryan Williams
* Lukas Dannebrog Jensen
* Bence Kovacs
* Ausrine Kriucokaite

# Introduction
> This document reports on the search engine project that we developed during the Intro-ductory Programming course at the IT University of Copenhagen. In Sections 1–5 we willreport on our solutions to the mandatory tasks posed in the project description.  The de-scription for each solution is roughly split up into the following parts:
- Task:A short review on the task that we had to solve.

- Approach:An informal, high-level description of how we solved the task.

- Solution:A detailed technical description of our solution to the task.

- Reflections:Thoughts about solution, including known bugs or shortcomings.


> The source code of our project is handed in as a single zip file calledgroup18.zip.  ThedirectoryGP21-group18contains the project-files that solves the mandatory tasks. All thecode was written by us or contained in the template, some of the hashcode methods weregenerated were generated by VS-code’s source action command. Our code is also availableon ITU’s Github:https://github.itu.dk/mahv/GP21-group18.

# Statement of Contribution

> Given the experience and required knowledge for this task, we have decided to establish a team lead position who would be responsible for the overall architecture and who would act as a lead developer in case any issues occurred. The rest of the team focused on more hands-on experience while coding the solution.  We have decided to split the mandatorytasks based on the level of individual experience, so that everyone was equally challenged and gained the most out of this project.

> Lukas took on the team lead role and guided team members through the assigned tasks.Bence took the lead in implementing Task 4 - Refined Queries and Task 5 - Ranking Al-gorithm together with Lukas.  Ryan and Ausrine took the lead in implementing Task 3 -Inverted Index and Task 2 - Modifying the Basic Search Engine together with Lukas. Refactoring phase was completed as a whole group in order to make sure that everyone got familiar with the code. It is important to note that everyone participated in group discussions and contributed to developing solutions for every task as well as committed part ofthe code for this project to the repository.

# Task 1: Refactoring

> The given task was to refactor the existing code to establish a cleaner code base and prepare it for the implementation of new features. 
#### Approach
> We have decided to introduce new classes for our initial prototype to achieve higher cohesiveness, less coupling and reduce overall complexity within the system. In addition, we have decided to extract parts of the code into separate methods, rename variables and discarded the use of local variable type inference (\texttt{var}), where the data type was not obvious,  to improve code readability.
#### Solution
> We have gone through several iterations of the class diagram. We started out with a class diagram that could support task 2, then gradually updated it to support the rest of the required functionality. The final version of initial prototype can be seen in figure 1.
#### Solution
> We decided to break down the system into separate parts that could be represented as an individual object with it's own behaviour and attributes. Also, we wanted to make sure that the new architecture could support new features developed in tasks 2-5. Therefore, we have introduced 5 additional classes that all combined create an improved version of the search engine:


- Page.java- represents a Page object. It is responsible for storing all the given infor-mation in the data files: a title, an url and content, as well as modifying the title and url fields into a JSON format.

- WebSet.java- represents a set of Pages. It is responsible for enabling search engineto support more complex search queries by creating intersections and unions between sets, as well as unifying multiple websets.

- Ranker.java- represents a Ranker object.   It is responsible for ranking matched queries by utilizing a term frequency-inverse document score (tf-idf ).

- QueryHandler.java- represents a QueryHandler object.  It is responsible for the processing of search queries and matching them with the pages by utilizing the Inverted Index.

- InvertedIndex.java- represents data structure based on the inverted index.  It is responsible for storing a mapping of search queries and corresponding pages.

- WebServer.java- represents a WebServer object. It is responsible for creating, from file,  processing and delivering web pages,  it also takes the http request,  that will trigger the search.
