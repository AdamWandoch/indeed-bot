# IndeedBot Scanner
###### Jobs scanner

[![BUILD](https://github.com/AdamWandoch/indeed-bot/workflows/BUILD/badge.svg)](https://github.com/AdamWandoch/indeed-bot/actions/workflows/maven.yml)

VIEW AWS DEPLOYMENTS: (SOME TEMPORARILY NOT ONLINE)

[VIRGINIA](http://indeedbotvirginia-env.eba-4sf63gic.us-east-1.elasticbeanstalk.com/) | 
[OHIO](http://indeedbotohio-env.eba-niszmpdp.us-east-2.elasticbeanstalk.com/) | 
[CALIFORNIA](http://indeedbotcalifornia-env.eba-mhqpsqdy.us-west-1.elasticbeanstalk.com/) |
[OREGON](http://indeedbotoregon-env.eba-daaqgedz.us-west-2.elasticbeanstalk.com/) |
[CAPE TOWN]() |
[HONG KONG]() |
[MUMBAI]() |
[OSAKA]() |
[SEOUL]() |
[SINGAPORE]() |
[SYDNEY]() |
[TOKYO]() |
[CANADA]() |
[FRANKFURT]() |
[IRELAND]() |
[LONDON]() |
[MILAN]() |
[PARIS]() |
[STOCKHOLM]() |
[BAHRAIN]() |
[SAO PAULO]() |

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from [HERE](https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=) and parses it creating a list of IndeedJob objects containing information about the job title,
            company name, unique "indeed job id" and a link to view the listing. 

All data is persisted to Postgres Database hosted on AWS RDS and is available in JSON format through [indeed-bot-api](https://github.com/AdamWandoch/indeed-bot-api)
        A number of instances of this bot is deployed in different AWS regions(Virginia, Ohio, Oregon, Tokyo, Ireland) and is scanning Indeed.ie in a staggered pattern using cron scheduling to avoid captcha block and to maintain as up-to-date job list in the database as possible.

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
