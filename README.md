elasticbeanstalk-dashboard
==========================

A realtime ElasticBeanstalk dashboard, built with Java using the Atmosphere and Spring frameworks.

![elasticbeanstalk dashboard](https://s3-eu-west-1.amazonaws.com/nw-test-images/elasticbeanstalk-dashboard.png "ElasticBeanstalk Dashboard")

Installation
------

Open a terminal and run the following

``` BASH
git clone git@github.com:drei01/elasticbeanstalk-dashboard.git
```

```BASH
cd elasticbeanstalk-dashboard
```


``` BASH
mvn eclipse:eclipse
```

This will generate the settings files so you can import the project to eclipse.

AWS Credentials
------
AWS credentials can be provided as VM arguments like the following when running on tomcat:

```
-DawsKey=ACCESS_KEY -DawsSecret=ACCESS_SECRET
```


