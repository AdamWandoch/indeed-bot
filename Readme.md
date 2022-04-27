# IndeedBot Scanner
###### Jobs scanner

[![BUILD](https://github.com/AdamWandoch/indeed-bot/workflows/BUILD/badge.svg)](https://github.com/AdamWandoch/indeed-bot/actions/workflows/maven.yml)

VIEW AWS DEPLOYMENTS: (SOME MAY BE TEMPORARILY OFFLINE)

[VIRGINIA](http://indeedbotvirginia-env.eba-4sf63gic.us-east-1.elasticbeanstalk.com/) | 
[OHIO](http://indeedbotohio-env.eba-niszmpdp.us-east-2.elasticbeanstalk.com/) | 
[CALIFORNIA](http://indeedbotcalifornia-env.eba-mhqpsqdy.us-west-1.elasticbeanstalk.com/) |
[OREGON](http://indeedbotoregon-env.eba-daaqgedz.us-west-2.elasticbeanstalk.com/) |
[CAPE TOWN](http://indeedbotcapetown-env.eba-md74kpmh.af-south-1.elasticbeanstalk.com/) |
[HONG KONG](http://indeedbothongkong-env.eba-swjz6jbt.ap-east-1.elasticbeanstalk.com/) |
[MUMBAI](http://indeedbotmumbai-env.eba-k83bwiia.ap-south-1.elasticbeanstalk.com/) |
[OSAKA](http://indeedbotosaka-env.eba-7cpbujbn.ap-northeast-3.elasticbeanstalk.com/) |
[SEOUL](http://indeedbotseoul-env.eba-e5uicp37.ap-northeast-2.elasticbeanstalk.com/) |
[SINGAPORE](http://indeedbotsingapore-env.eba-5m52rter.ap-southeast-1.elasticbeanstalk.com/) |
[SYDNEY](http://indeedbotsydney-env.eba-rp7xuxxm.ap-southeast-2.elasticbeanstalk.com/) |
[TOKYO](http://indeedbottokyo-env.eba-xmsejbh8.ap-northeast-1.elasticbeanstalk.com/) |
[CANADA](http://indeedbotcanada-env.eba-jxu6x6ue.ca-central-1.elasticbeanstalk.com/) |
[FRANKFURT](http://indeedbotfrankfurt-env.eba-qpbqcwxw.eu-central-1.elasticbeanstalk.com/) |
[IRELAND](http://indeedbotireland-env.eba-cckhqftf.eu-west-1.elasticbeanstalk.com/) |
[LONDON](http://indeedbotlondon-env.eba-cevpj4x9.eu-west-2.elasticbeanstalk.com/) |
[MILAN](http://indeedbotmilan-env.eba-emvxupbh.eu-south-1.elasticbeanstalk.com/) |
[PARIS](http://indeedbotparis-env.eba-qdcmu7p2.eu-west-3.elasticbeanstalk.com/) |
[STOCKHOLM](http://indeedbotstockholm-env.eba-y6zw9bvv.eu-north-1.elasticbeanstalk.com/) |
[BAHRAIN](http://indeedbotbahrain-env.eba-tzhqvrds.me-south-1.elasticbeanstalk.com/) |
[SAO PAULO](http://indeedbotsaopaulo-env.eba-3kg9h3bq.sa-east-1.elasticbeanstalk.com/) |

This is an experimental project to explore Java ecosystem of technologies for web development.

The application retrieves html from [HERE](https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=) and parses it creating a list of IndeedJob objects containing information about the job title,
            company name, unique "indeed job id" and a link to view the listing. 

All data is persisted to Postgres Database hosted on AWS RDS and is available in JSON format through [indeed-bot-api](https://github.com/AdamWandoch/indeed-bot-api)
        A number of instances of this bot is deployed in different AWS regions(listed above) and is scanning Indeed.ie in a staggered pattern using cron scheduling to avoid captcha block and to maintain as up-to-date job list in the database as possible.

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
