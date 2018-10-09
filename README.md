# AWS Lambda Java with MySQL Integration using Hibernate

This repository shows how to create Lambda function using Java8 runtime and integrate it with MySQL RDS using Hibernate.
- Create Lambda function using Java8 runtime
- Integrate Hibernate framework to connect it to MySQL RDS instance
- Multiple lambda services in single lambda project
- Package, Deploy and Test lambda


## Create Lambda function using Java8 runtime

AWS Lambda functions cab be created in multiple platform. AWS lambda supports Java8 runtime. This repository contains maven based Java8 project with necessary dependencies.

* AWS Lambda Core dependency for Java
```
<dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-lambda-java-core</artifactId>
            <version>1.2.0</version>
 </dependency>
```

* Hibernate Core and MySQL Connector dependency
```
<dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.3.6.Final</version>
</dependency>

<dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.3.6.Final</version>
</dependency>
```

* JSON manipulation dependencies
```
<dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>1.1.1</version>
 </dependency>
 
 <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.5</version>
</dependency>
```


## Integrate Hibernate framework to connect with MySQL RDS instance


Hibernate is standard JPA framework now-a-days to connect Java application to database. `com.abhishek.core.HibernateUtil.getSessionFactory()` creates hibernate session factory using `hibernate.cfg.xml` hibernate configuration. You can take now object-to-relational framework advantage. 



## Multiple lambda services in single lambda project

This repository is designed with simple architecture(for reference only) in mind that can be used to create multiple Lambda services using single lambda project. If you are developing REST web services and plan to host it using lambda services then you can use this architecture pattern.

- `com.abhishek.services` : All REST web services will be put in this package. Each service class is dedicated for single REST web services. For example, `com.abhishek.services.employee.SaveEmployeeService` is lambda function to save Employee. 

- `com.abhishek.core.entities` : All core entities will be put in this package. For example, `com.abhishek.core.entities.Employee` is annotated with `@Entity`.

## Package, Deploy and Test lambda

* ### Package

This is maven based project and can be package using following command. 
```
mvn clean package
```
Thanks to `maven-shade-plugin` which will package all the depedent libraries and create single artifact jar.
After successfully running command, it will generate `<artifactId>-<version>.jar` in `target` folder. For example `lambda-1.0.0.jar`.

* ### Deploy

`lambda-1.0.0.jar` is final artifact that needs to be uploaded in AWS lambda function using various options provided by AWS lambda as `Function Package`. You can also upload this to s3 bucket and configure AWS lambda to take from that bucket.

* ### Test

AWS lambda provides mechanism to test Lambda function from AWS lambda console itself. Before running it, we have to configure following environment variables which are used in Lambda project in your AWS lambda service and specify `com.abhishek.services.employee.SaveEmployeeService::handleRequest` handler. 

`RDS_DB_NAME`
`RDS_HOSTNAME`
`RDS_PASSWORD`
`RDS_USERNAME`

When lambda functions runs, all the environment variables will be passed to lambda function and you can retrieve it using `System.getenv(Constant.RDS_HOSTNAME)`. This way you can avoid hard coding important configuration values to code.

Create `Employee` table in MySQL database with id, name, designation columns before running test event.

Once all configuration is done, you can create Test event and pass following request body data to run this lambda function.

```
{
  "body": "{\"name\": \"Final\", \"designation\": \"Test\"}"
}
```
If everything is configured and coded properly, it should run successfully and new record will be created in MySQL database.

###### NOTE 
- Configuring AWS lambda and MySQL RDS is out of scope
- Request and Response format of JSON is designed in such a way that you can trigger this lambda function with AWS API gateway using lambda proxy integration.
