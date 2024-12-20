
# Balcão UFF

## Descrição

O **Balcão UFF** é um sistema de anúncios voltado para a comunidade universitária da UFF. Ele visa facilitar a troca, venda ou doação de bens e serviços entre alunos, professores e funcionários da universidade. O sistema permite que os usuários se autentiquem com suas contas da UFF e criem, busquem e interajam com anúncios. Além disso, promove um sistema de avaliação e comunicação entre os usuários, incentivando a confiança e a segurança nas transações.

Este projeto está sendo desenvolvido como parte do curso de Gerência de Projetos e Manutenção de Software (GPMS). O código está sendo gerido de acordo com as práticas de desenvolvimento ágil, com controle de versões e processos de integração contínua.

## Tecnologias

### Backend
- **Java 17** com **Spring Boot**: Framework principal para o backend.
- **Maven**: Gerenciamento de dependências e build do projeto.
- **Flyway**: Para gerenciamento e versionamento do banco de dados.
- **PostgreSQL**: Banco de dados relacional utilizado.
- **Swagger**: Documentação automática da API.
- **JUnit**: Para testes unitários e de integração.

### Frontend
- **React**: Framework utilizado para o frontend.
- **Vite**: Ferramenta de build rápida para o frontend.
- **TypeScript**: Superset do JavaScript, garantindo tipagem estática no frontend.

### Infraestrutura
- **Docker**: Utilizado para containerizar o backend.
- **Docker Compose**: Para orquestração de múltiplos containers (backend + banco de dados).

## Pré-requisitos

Antes de rodar o projeto, certifique-se de que você tem as seguintes ferramentas instaladas:

- **Docker**: [Instalação do Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Instalação do Docker Compose](https://docs.docker.com/compose/install/)
- **Java 17** (caso precise compilar o código): [Instalação do Java](https://adoptium.net/)
- **Maven** (para compilar o código, se necessário): [Instalação do Maven](https://maven.apache.org/install.html)

## Como rodar o projeto

### 1. Clone o repositório

Clone o repositório para sua máquina local:

```bash
git clone https://github.com/Roberto-deP-Martins/Balcao-UFF
cd balcao-uff
```

### 2. Build do Backend

Se você fez alterações no código ou precisa compilar o backend pela primeira vez, execute o comando Maven para gerar o arquivo JAR:

```bash
mvn clean package
```

Esse comando irá gerar o arquivo JAR na pasta `target/` do seu projeto.

### 3. Build do Frontend

Para rodar o frontend (React), acesse a pasta do frontend e execute:

```bash
cd frontend
npm install
npm run dev
```

O servidor de desenvolvimento do frontend estará disponível em `http://localhost:5173`.

### 4. Subir o Backend com Docker

Para rodar o backend (Spring Boot) junto com o banco de dados PostgreSQL usando Docker, utilize o comando:

```bash
docker-compose up -d
```

Este comando vai:
- Construir e iniciar os contêineres Docker para o backend e o banco de dados.
- Subir a aplicação na porta `8080`.

**Importante**: O banco de dados está configurado para rodar na porta `5433` no seu host local, portanto, ao configurar a URL de conexão, utilize:

```bash
jdbc:postgresql://localhost:5433/emprestimo_livros
```

### 5. Acessar a Aplicação

Depois de iniciar os contêineres Docker, a aplicação estará disponível na URL:

```
http://localhost:8080
```

Para acessar a documentação da API (Swagger), basta acessar:

```
http://localhost:8080/swagger-ui.html
```

### 6. Banco de Dados

O banco de dados PostgreSQL está configurado no Docker, e você pode acessá-lo com as seguintes credenciais:

- **Usuário**: `postgres`
- **Senha**: `1996`
- **Banco de dados**: `emprestimo_livros`
- **Porta**: `5433` (alteração importante no seu caso)

Você pode utilizar uma ferramenta como **pgAdmin**, **DBeaver**, ou qualquer outro cliente PostgreSQL para conectar.

### 7. Parar o Backend e Banco de Dados

Para parar os contêineres do Docker, execute:

```bash
docker-compose down
```

Este comando irá:
- Parar os contêineres.
- Remover os contêineres criados pelo `docker-compose up`.

## Estrutura do Projeto

- **docker-compose.yml**: Arquivo de configuração para orquestrar os contêineres (backend + banco de dados).
- **Dockerfile**: Arquivo para construir a imagem Docker do backend.
- **balcao-uff/src/**: Código-fonte do backend em Java (Spring Boot).
- **front-balcao-uff/**: Código-fonte do frontend em React.
- **target/**: Pasta onde o arquivo JAR gerado pelo Maven será armazenado.
- **uploads/**: (Não existe no projeto atualmente, pois o Docker gerencia o armazenamento de uploads).

## Personalizações

### Configuração do Banco de Dados

Se você desejar alterar as configurações do banco de dados (usuário, senha, nome do banco), edite o arquivo `docker-compose.yml`:

```yaml
  db:
    image: postgres:15-alpine
    environment:
      POSTGRES_USER: novo_usuario
      POSTGRES_PASSWORD: nova_senha
      POSTGRES_DB: novo_banco
    ports:
      - "5433:5432"  # Porta alterada para 5433
```

### Configuração de Uploads

Atualmente, o gerenciamento dos uploads de imagens e arquivos está sendo feito dentro do contêiner do Docker, o que significa que qualquer arquivo enviado via a API será salvo dentro do contêiner. Se você quiser persistir esses arquivos fora do contêiner, pode adicionar um volume ao `docker-compose.yml`:

```yaml
  app:
    volumes:
      - ./uploads:/app/src/main/resources/uploads/images/
```

Isso montará a pasta `./uploads` da sua máquina local dentro do contêiner, permitindo que os arquivos sejam persistidos na máquina.

## Controle de Versão e Contribuições

Este projeto está sendo gerido com Git. O código fonte está hospedado no GitHub. Se você quiser contribuir:

1. Faça um fork do repositório.
2. Crie uma branch para suas alterações (`git checkout -b feature/nova-funcionalidade`).
3. Faça commit das suas alterações (`git commit -am 'Adiciona nova funcionalidade'`).
4. Envie suas alterações para o seu repositório remoto (`git push origin feature/nova-funcionalidade`).
5. Abra um pull request.

