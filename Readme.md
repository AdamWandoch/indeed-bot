#IndeedBot
####Java jobs scanner
This is an experimental project to explore Java web development stack. The process
retrieves html from https://ie.indeed.com/jobs?q=java&l=cork&sort=date&filter=0&start=
and parses it creating a list of IndeedJob objects. 
#### Endpoints:
/jobs - retrieves a list of all job objects in JSON format <br>
/job/{index} - retrieves a single job object in JSON format