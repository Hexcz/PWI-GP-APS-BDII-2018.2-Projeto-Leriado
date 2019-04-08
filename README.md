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

#Configuração Inicial
Este projeto armazena os dados utilizando persistência poliglota, através dos bancos:
 
* Mongo
* Neo4j
* Redis
* PostgreSQL

Para instalar o Mongo no Ubuntu e derivados, execute as seguintes linhas de comando no terminal:

`sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10`

`echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list`

`sudo apt-get update`

`sudo apt-get install -y mongodb-org`


