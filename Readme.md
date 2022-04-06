# IndeedBot
#### Java jobs scanner
This is an experimental project to explore Java ecosystem of technologies for web development. 
This RESTful API retrieves html from https://ie.indeed.com/jobs?q=java&l=cork&sort=date&filter=0&start=
and parses it creating a list of IndeedJob objects containing information about the job title, 
company name, unique "job id" and a link to view the listing.
Data is exposed in JSON format through endpoints for front-end consumer
#### Endpoints:
/jobs - retrieves a list of all job objects in JSON format <br>
/job/{index} - retrieves a single job object in JSON format