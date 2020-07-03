## Portal Cadastro

Application responsible for managing the repair workflow and invoice issuing for a service workshop.  

Backend developed using `Java 8` and Spring `Boot 2.2.1` exposing a REST Api. Database of choice was `MySQL 8.0`

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

-----------------------------------------------------------------------------

Aplicação responsável por controlar o fluxo de reparos em uma oficina e emitir orçamentos para serviços.

Backend desenvolvido em `Java 8` com `Spring Boot 2.2.1` expondo uma Api REST. O banco de dados escolhido foi `MySQL 8.0`

Para obter o projeto execute:

```
git clone https://github.com/Macahyba/portal-cadastro.git
cd portal-cadastro
```

Existe um arquivo `application.properties` na pasta `resources`, modifique como necessitar. 


Já na pasta de trabalho, baixe as dependências necessárias e efetue o build do projeto

```
mvn dependency: tree
mvn install
```

O arquivo `jar` gerado pode ser encontrado em `target/portal-cadastro-0.0.1-SNAPSHOT.jar`

```
java -jar target/portal-cadastro-0.0.1-SNAPSHOT.jar
```

Para que o envio de e-mails funcione corretamente, é necessário que criem-se credenciais OAuth conforme indicado no artigo:

https://developers.google.com/gmail/api/quickstart/java

As credenciais devem ser apontadas na variável `mail.credentials`.

Um frontend para essa aplicação pode ser encontrado no projeto

https://github.com/Macahyba/portal-cadastro-frontend
