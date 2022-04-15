# IndeedBot
#### Jobs scanner

[VIEW DEPLOYMENT](https://indeed-bot.herokuapp.com)

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=
and parses it creating a list of IndeedJob objects containing information about the job title, 
company name, unique "indeed job id" and a link to view the listing. All data is persisted to Postgres Database running on Heroku and retrieved on application restart. 
Data is exposed through a number of basic endpoints in JSON format.

This Spring Boot powered RESTful API is running on a free Heroku dyno which could sleep occasionally, it has however a scheduled task that hits the custom "/ping" endpoint in a specified time interval to keep itself awake. Another scheduled task scans Indeed.ie and updates the job list periodically.
#### Available endpoints:
[/jobs](http://indeed-bot.herokuapp.com/jobs) - retrieves a list of all job objects in JSON format <br>
[/job/{index}](http://indeed-bot.herokuapp.com/job/0) - retrieves a single job object in JSON format 
<br>[/jobs/title/{keyword}](http://indeed-bot.herokuapp.com/jobs/title/Java) - retrieves a list of jobs containing keyword in title
<br>[/jobs/company/{keyword}](http://indeed-bot.herokuapp.com/jobs/company/Company) - retrieves a list of jobs containing keyword in company name
