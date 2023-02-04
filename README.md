# COW.API
 
## COW Rest API Setup
1. Install JDK 11
2. Update text file in the root directory of the project named `.env.example` to `.env`.
3. Update these environment variables to the `.env` file:
```
OPERATOR_ID=account_id_in_string_format
OPERATOR_KEY=account_private_key_in_string_format

HEDERA_ENVIRONMENT=environment_in_string_format
MIRROR_NODE_ADDRESS=mirror_node_address_in_string_format

BYTE_CODE=byte_code_in_string_format
```

4. Update text file in the resources directory of the project named `application.properties.example` to `application.properties`.
5. Update these variables to the `application.properties` file for the specific OS:
```
Windows Test - SQL Server Instance:
https://learn.microsoft.com/en-us/sql/relational-databases/databases/create-a-database?view=sql-server-ver16

MacOS Test - MariaDB Instance:
https://mariadb.com/kb/en/documentation/
```
6. Check `pom.xml` to comment/uncomment necessary dependencies depending on machine OS. 
7. Run `SpringHederaStarterProjectApplication`
8. Check `http://localhost:8080/swagger-ui/` to view endpoints available.