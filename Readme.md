# IndeedBot
#### Jobs scanner

[VIEW DEPLOYMENT](https://indeed-bot.herokuapp.com)

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=
and parses it creating a list of IndeedJob objects containing information about the job title, 
company name, unique "indeed job id" and a link to view the listing. All data is persisted to Postgres Database running on Heroku and retrieved on application restart. 
Data is exposed through a number of basic endpoints in JSON format.

This Spring Boot powered RESTful API is running on a free Heroku dyno which could sleep occasionally, it has however a scheduled task that hits the custom "/ping" endpoint in a specified time interval to keep itself awake. Another scheduled task scans Indeed.ie and updates the job list periodically.
#### Available endpoints serving JSON:
[/job/{index}](http://indeed-bot.herokuapp.com/job/0) - retrieves a single job object <br>
[/jobs](http://indeed-bot.herokuapp.com/jobs) - retrieves a list of all job objects <br>
[/jobs/sort/id](http://indeed-bot.herokuapp.com/jobs/sort/id) - retrieves a list of all job objects sorted by ID <br>
[/jobs/sort/title](http://indeed-bot.herokuapp.com/jobs/sort/title) - retrieves a list of all job objects in sorted by title <br>
[/jobs/sort/company](http://indeed-bot.herokuapp.com/jobs/sort/company) - retrieves a list of all job objects in sorted by company <br>
[/jobs/title/{keyword}](http://indeed-bot.herokuapp.com/jobs/title/software) - retrieves a list of jobs containing keyword in title <br>
[/jobs/title/{keyword}/sort/id](http://indeed-bot.herokuapp.com/jobs/title/software/sort/id) - retrieves a list of jobs containing keyword in title sorted by ID <br>
[/jobs/title/{keyword}/sort/title](http://indeed-bot.herokuapp.com/jobs/title/software/sort/title) - retrieves a list of jobs containing keyword in title sorted by title <br>
[/jobs/title/{keyword}/sort/company](http://indeed-bot.herokuapp.com/jobs/title/software/sort/company) - retrieves a list of jobs containing keyword in title sorted by company <br>
[/jobs/company/{keyword}](http://indeed-bot.herokuapp.com/jobs/company/reperio) - retrieves a list of jobs containing keyword in company name <br>
[/jobs/company/{keyword}/sort/id](http://indeed-bot.herokuapp.com/jobs/company/reperio/sort/id) - retrieves a list of jobs containing keyword in company name sorted by ID <br>
[/jobs/company/{keyword}/sort/title](http://indeed-bot.herokuapp.com/jobs/company/reperio/sort/title) - retrieves a list of jobs containing keyword in company name sorted by title <br>
[/jobs/company/{keyword}/sort/company](http://indeed-bot.herokuapp.com/jobs/company/reperio/sort/company) - retrieves a list of jobs containing keyword in company name sorted by company <br>

