# IndeedBot
###### Jobs scanner

[![BUILD](https://github.com/AdamWandoch/indeed-bot/workflows/BUILD/badge.svg)](https://github.com/AdamWandoch/indeed-bot/actions/workflows/maven.yml)

/// THIS SECTION NEEDS UPDATE WHEN NEW INSTANCE GROUP IS DEPLOYED
VIEW DEPLOYMENT : [HEROKU](https://indeed-bot.herokuapp.com) < | >
[AWS](https://ru4umr3xja.eu-west-1.awsapprunner.com)

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from [HERE](ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=) and parses it creating a list of IndeedJob objects containing information about the job title,
            company name, unique "indeed job id" and a link to view the listing. 

All data is persisted to Postgres Database hosted on AWS RDS and is available in JSON format through [indeed-bot-api](https://github.com/AdamWandoch/indeed-bot-api)
        A number of instances of this bot is deployed in different environments and is scanning Indeed.ie in a staggered pattern to avoid captcha block and to maintain as up-to-date job list in the database as possible.

#### To run the project locally:
 * install PostgreSQL
 * create a local postgres database (I'm using pgAdmin for local postgres administration)
 * install Java 11 or higher
 * git clone this repository
 * add system environment variables:
    * POSTGRES_USERNAME set value to your local postgres username
    * POSTGRES_PASSWORD set value to your local postgres password
    * SPRING_PROFILE set value to dev
 * rename application-dev-sample.properties to application-dev.properties
 * update application-dev.properties spring.datasource.url property with your database name (I used indeed-db)
 * install Maven or use your favourite IDE that has Maven support (I recommend IntelliJ IDEA)
 * build, run and let me know what can we improve :)
#### What's next?
Check [indeed-bot-api](https://github.com/AdamWandoch/indeed-bot-api) and find out how to access collected data.
