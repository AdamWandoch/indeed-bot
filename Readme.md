# IndeedBot Scanner
###### Jobs scanner

[![BUILD](https://github.com/AdamWandoch/indeed-bot/workflows/BUILD/badge.svg)](https://github.com/AdamWandoch/indeed-bot/actions/workflows/maven.yml)

SINGLE BOT DEPLOYED ON HEROKU AFTER MY AWS ACCOUNT HAS BEEN SUSPENDED

[HEROKU]("http://indeed-bot.herokuapp.com/") | 

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from [HERE](https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=) and parses it creating a list of IndeedJob objects containing information about the job title,
            company name, unique "indeed job id" and a link to view the listing. 

All data is persisted to Postgres Database hosted on HEROKU and is available in JSON format through [indeed-bot-api](https://github.com/AdamWandoch/indeed-bot-api)

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
