# COW.API

## Description

Welcome to COW Rest API this is the first step to decentralize food supply chains.

## Installation

1. Install JDK 17.
2. Create a Hedera Account in Hedera Portal `https://portal.hedera.com/register`. (if you don't own a Hedera account already)
3. Create text file in the root directory of the project named `.env` and add the following information.

```
#Hedera Configurations
OPERATOR_ID=account_id_in_string_format
OPERATOR_KEY=account_private_key_in_string_format

#Project Configuration
PROJECT_PATH=project_path_in_string_format
```

4. Install a database service and configure a user to access it.
5. Create text file in the resources folder of the project named `application.properties`.

* MacOS application.properties tested with MariaDB

```
spring.datasource.url=datasource_url_in_string_format
spring.datasource.username=datasource_username_in_string_format
spring.datasource.password=datasource_pwd_in_string_format
spring.datasource.driver-class-name=driver_class_in_string_format

jwt.secret=jwt_secret_in_string_format
```

Read more in https://mariadb.com/kb/en/documentation/

* Windows OS application.properties tested with SQLServer

```
spring.datasource.url=datasource_url_in_string_format
spring.datasource.username=datasource_username_in_string_format
spring.datasource.password=datasource_pwd_in_string_format
spring.datasource.driver-class-name=driver_class_in_string_format

spring.jpa.show-sql=jpa_show_sql_bool
spring.jpa.hibernate.ddl-auto=hibernate_ddl_auto_bool
spring.jpa.properties.hibernate.format_sql=hibernate_format_sql_in_string_format
spring.jpa.properties.hibernate.dialect=hibernate_dialect_in_string_format

jwt.secret=jwt_secret_in_string_format
```

Read more in https://learn.microsoft.com/en-us/sql/relational-databases/databases/create-a-database?view=sql-server-ver16

6. Check `pom.xml` to comment/uncomment necessary dependencies depending on OS.
7. Run `COWApplication`
8. Check `http://localhost:8080/swagger-ui/` to view endpoints available.

#### NOTE:

A .env.example is provided to help the setup of the project.

An application.properties.example is provided to help the setup of the project.

## Contributing

We appreciate and welcome contributions! To contribute to the project, please follow these guidelines:

1. Fork the repository.
2. Create a new branch.
3. Make your changes and ensure they are appropriately documented.
4. Test your changes thoroughly.
5. Submit a pull request, describing the changes you have made.
6. Our team will review your contribution, provide feedback if necessary, and merge it once it meets the required standards.

## Contact

If you have any questions, suggestions, or feedback, please feel free to contact us:

- Email: [bernardo.figueiredo@outlook.com]
- GitHub: [bernardo-rf](https://github.com/bernardo-rf)
- Trello: [cow-api](https://trello.com/b/5CnlL9nG/cow-project)

