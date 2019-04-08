# Leriado
Uma rede social voltada a integrar as comunidades de docência a discência!

## Motivação
Este projeto abrange os projetos propostos pelas disciplinas:

* Programação para Web I
* Banco de Dados II
* Gerência de Projetos de Software
* Análise e Projeto de Sistemas

Ofertadas no curso de Análise e Desenvolvimento de Sistemas, IFPB Campus Cajazeiras, período 2018.2.

## Equipe
1. Ian Carneiro Teixeira de Araújo
2. Isleimar de Souza Oliveira
3. José David Emanoell Feitoza Braga
4. Leanderson Coelho dos Santos
5. Luz de Esperanza Apolo Pereira

# Instalando as Dependências
Este projeto armazena os dados utilizando persistência poliglota, através dos bancos:
 
* Mongo
* Neo4j
* Redis
* PostgreSQL

Instalando o Mongo no Ubuntu e derivados:

`sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10`

`echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list`

`sudo apt-get update`

`sudo apt-get install -y mongodb-org`

Para iniciar o servidor do Mongo pelo terminal:

`mongod`

Para utilizar o Neo4j basta apenas baixar o server no site do Neo4j:

[https://neo4j.com/download-center/#releases]

Após baixar e extrair, iniciar o serviço com o seguinte comando no terminal:

`/caminho/da/pasta/bin/neo4j start`

Instalando o Redis no Ubuntu e derivados:

`sudo apt-get install redis-server`

Para iniciar o servidor do redis, execute o seguinte comando no terminal:

`redis-server`

Por fim para instalar o postgreSQL, executar o seguinte comando:

`sudo apt-get install postgresql postgresql-contrib pgadmin3`

Basta executar o pgadmin3 para iniciar o postgreSQL.

# Criando as Tabelas Necessárias
Para o Leriado funcionar corretamente, ambos PostgreSQL e Neo4j devem contar com as tabelas definidas pelos scripts contidos na pasta "/leriado/src/main/resources/criacao-tabelas/".

#Configurando os Bancos
Após instalar e criar as tabelas, devemos definir as configurações de conexão dos bancos de dados, as quais são mantidas pelo arquivo "/leriado/src/main/resources/config.properties".
O arquivo deve ser estruturado conforme o modelo abaixo:

[[https://ibb.co/cxYWdDb]]
