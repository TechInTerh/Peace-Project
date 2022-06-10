```sh
docker run --name mysql -e MYSQL_ROOT_PASSWORD=sqladmin -e MYSQL_DATABASE=alerts -p 3306:3306 -p 33060:33060 mysql:5
```
```shell
node index.js
```
