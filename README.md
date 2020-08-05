<h1 align="center">Welcome to Portal Cadastro 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.1-blue.svg?cacheSeconds=2592000" />
  <a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html" target="_blank">
    <img alt="Java" src="https://img.shields.io/badge/java-8-blue" />
  </a>
  <a href="https://spring.io/projects/spring-boot" target="_blank">
    <img alt="Spring" src="https://img.shields.io/badge/spring%20boot-2.2.1-blue" />  
  </a>
  <a href="https://www.mysql.com/" target="_blank">
    <img alt="Mysql" src="https://img.shields.io/badge/mysql-8-blue" />  
  </a>  
  <a href="https://twitter.com/macahyba" target="_blank">
    <img alt="Twitter: macahyba" src="https://img.shields.io/twitter/follow/macahyba.svg?style=social" />
  </a>
</p>

> Application responsible for managing the repair workflow and invoice issuing for a service workshop.  <br />
> Backend developed using `Java 8` and Spring `Boot 2.2.1` exposing a REST Api. Database of choice was `MySQL 8.0` <br />

To download the project:

```
git clone https://github.com/Macahyba/portal-cadastro.git
cd portal-cadastro
```

There is a `application.properties` file under `resources` folder, to be changed as needed.

On the working folder, download the dependencies and build the project

```
mvn dependency: tree
mvn install
```

The `jar` file can be found under `target/portal-cadastro-0.0.1-SNAPSHOT.jar`

```
java -jar target/portal-cadastro-0.0.1-SNAPSHOT.jar
```

For the e-mail sending function work properly, it is needed to create OAuth credentials according the article:

https://developers.google.com/gmail/api/quickstart/java

The credentials must be defined under `mail.credentials`.

A frontend for this application can be found under the project

https://github.com/Macahyba/portal-cadastro-frontend

## Author

👤 **Thiago Macahyba**

* Twitter: [@macahyba](https://twitter.com/macahyba)
* Github: [@macahyba](https://github.com/macahyba)
* LinkedIn: [@thiagomacahyba](https://linkedin.com/in/thiagomacahyba)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/Macahyba/portal-cadastro/issues). 

## Show your support

Give a ⭐️ if this project helped you!
