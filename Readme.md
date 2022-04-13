# IndeedBot
#### Jobs scanner

[VIEW DEPLOYMENT](https://indeed-bot.herokuapp.com)

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=
and parses it creating a list of IndeedJob objects containing information about the job title, 
company name, unique "indeed job id" and a link to view the listing. At this point in time data is stored in memory and updated regularly. Data received from these endpoints is a live representation of Indeed job listings.
Objects are available in JSON format through endpoints for front-end consumer.

This Spring Boot powered RESTful API is running on a free Heroku dyno which could sleep occasionally, it has however a scheduled task that hits the custom "/ping" in a specified time interval to keep itself awake. Another scheduled task scans indeed and updates the job list automatically in a specified time interval.
#### Available endpoints:
[/jobs](http://indeed-bot.herokuapp.com/jobs) - retrieves a list of all job objects in JSON format <br>
[/job/{index}](http://indeed-bot.herokuapp.com/job/0) - retrieves a single job object in JSON format
[/jobs/title/{keyword}](http://indeed-bot.herokuapp.com/jobs/title/Java) - retrieves a list of jobs containing keyword in title
[/jobs/company/{keyword}](http://indeed-bot.herokuapp.com/jobs/company/Company) - retrieves a list of jobs containing keyword in company name
