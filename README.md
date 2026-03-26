# Plateforme de Gestion GraphQL

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-green) ![GraphQL](https://img.shields.io/badge/GraphQL-Latest-purple) ![Java](https://img.shields.io/badge/Java-21-orange) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)


---

## 🎯 Objectif

**KH-ABDEV** est une **API GraphQL** permettant de gérer des **utilisateurs** et des **posts**. Elle offre une interface moderne complète (CRUD) avec relations bidirectionnelles.

**Fonctionnalités principales:**
- ✅ Gestion complète des utilisateurs (créer, lire, mettre à jour, supprimer)
- ✅ Gestion des posts avec relation à l'auteur (utilisateur)
- ✅ Requêtes et mutations GraphQL
- ✅ Gestion d'exceptions personnalisées
- ✅ Validation des données
- ✅ Support PostgreSQL avec Hibernate
- ✅ Interface GraphiQL sur `/graphiql`

---

## 🛠️ Stack Technologique

| Composant | Version | Rôle |
|-----------|---------|------|
| **Java** | 21 | Langage de programmation |
| **Spring Boot** | 4.0.4 | Framework principal |
| **Spring GraphQL** | Latest | API query language |
| **Spring Data JPA** | Latest | Accès et persistence des données |
| **PostgreSQL** | Latest | Base de données relationnelle |
| **Lombok** | Latest | Réduction du boilerplate |
| **Hibernate** | Latest | ORM (Object-Relational Mapping) |
| **Maven** | Latest | Gestion des dépendances |
| **Docker Compose** | Latest | Orchestration des services |

---

## 🏗️ Architecture

```
KH-ABDEV/
├── src/main/java/com/kholty/KH_ABDEV/
│   ├── Application.java                 # Point d'entrée Spring Boot
│   ├── controller/
│   │   ├── UserController.java          # Mutations & Queries pour Users
│   │   └── PostController.java          # Mutations & Queries pour Posts
│   ├── entity/
│   │   ├── user/
│   │   │   └── User.java                # Entité JPA - Utilisateur
│   │   └── post/
│   │       └── Post.java                # Entité JPA - Post
│   ├── repository/
│   │   ├── UserRepository.java          # Requêtes custom pour User
│   │   └── PostRepository.java          # Requêtes custom pour Post
│   └── exception/
│       ├── UserException.java           # Exception personnalisée User
│       └── PostException.java           # Exception personnalisée Post
│                          
│
├── src/main/resources/
│   ├── application.properties            # Configuration app
│   └── graphql/
│       └── schema.graphqls               # Schéma GraphQL
│
├── compose.yaml                          # Configuration Docker Compose
├── pom.xml                               # Dépendances Maven
└── README.md                             # Ce fichier
```

---

## 📊 Entités
### Entité User
- **id**: Long (Auto-généré)
- **name**: String (Obligatoire, unique)
- **email**: String (Obligatoire, unique)
- **posts**: List<Post> (Relation One-to-Many)

### Entité Post
- **id**: Long (Auto-généré)
- **title**: String (Obligatoire)
- **content**: String (Obligatoire)
- **author**: User (Relation Many-to-One)

### Relation
Un **User** peut avoir plusieurs **Posts**.
Un **Post** appartient à exactement un **User** (auteur).

---

## 🔌 API GraphQL

### URL d'accès
- **Endpoint GraphQL**: `http://localhost:8080/graphql`
- **Interface GraphiQL**: `http://localhost:8080/graphiql`

### Requêtes (Queries)

#### Récupérer tous les utilisateurs
```graphql
query {
    users {
        id
        name
        email
        posts { id title }
    }
}
```

#### Récupérer un utilisateur par ID
```graphql
query {
    user(id: "1") {
        id
        name
        email
    }
}
```

#### Récupérer tous les posts
```graphql
query {
    posts {
        id
        title
        content
        author { name email }
    }
}
```

#### Récupérer un post par ID
```graphql
query {
    post(id: "1") {
        id
        title
        content
        author { name }
    }
}
```

#### Récupérer les posts d'un utilisateur
```graphql
query {
    postByUser(userId: "1") {
        id
        title
        content
    }
}
```

### Mutations (Modifications)

#### Créer un utilisateur
```graphql
mutation {
    createUser(name: "Alice Martin", email: "alice@example.com") {
        id
        name
        email
    }
}
```

#### Mettre à jour un utilisateur
```graphql
mutation {
    updateUser(id: "1", name: "Bob Smith", email: "bob@example.com") {
        id
        name
        email
    }
}
```

#### Supprimer un utilisateur
```graphql
mutation {
    deleteUser(id: "1")
}
```

#### Créer un post
```graphql
mutation {
    createPost(title: "Mon Post", content: "Contenu...", authorId: "1") {
        id
        title
        content
        author { name }
    }
}
```

#### Mettre à jour un post
```graphql
mutation {
    updatePost(id: "1", title: "Nouveau titre", content: "Nouveau contenu") {
        id
        title
        content
    }
}
```

#### Supprimer un post
```graphql
mutation {
    deletePost(id: "1")
}
```

---

## ⚙️ Configuration

### Fichier: `application.properties`

```properties
spring.application.name=KH-ABDEV
server.port=8080

# GraphQL Configuration
spring.graphql.graphiql.enabled=true
spring.graphql.graphiql.path=/graphiql

# PostgreSQL Datasource
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 🚀 Lancer l'Application

### Prérequis
- **Java 21** (ou version supérieure)
- **Maven 3.8+**
- **PostgreSQL 12+** (ou utiliser Docker Compose)

### Étape 1: Configurer la base de données

#### Option A - Utiliser Docker Compose
```bash
docker-compose up -d
```

#### Option B - PostgreSQL local
```bash
# Créer la base de données
createdb mydatabase

# Configurer application.properties avec vos identifiants
```

### Étape 2: Construire et démarrer

```bash
# Compiler le projet
mvn clean install

# Démarrer l'application
mvn spring-boot:run

# L'application démarre sur http://localhost:8080
```

---

## 🛠️ Dépendances Principales

```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-graphql</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- Database Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok (réduction du boilerplate) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 🔧 Gestion des Exceptions

### UserException

Levée lorsqu'une opération User échoue:
- Email déjà existant lors de la création
- Utilisateur non trouvé lors de la mise à jour

### PostException

Levée lorsqu'une opération Post échoue:
- Auteur (User) non trouvé lors de la création
- Post non trouvé lors de la mise à jour

---

## 📝 Brève Description du Code

### Controllers
- **UserController**: Gère les Queries et Mutations pour les utilisateurs
- **PostController**: Gère les Queries et Mutations pour les posts

Tous deux utilisent les annotations GraphQL:
- `@QueryMapping`: pour les requêtes de lecture
- `@MutationMapping`: pour les opérations d'écriture

### Repositories
- **UserRepository**: Méthodes personnalisées `existsByEmail()`, `findByEmail()`
- **PostRepository**: Méthode personnalisée `findByAuthorId()`

### Entités JPA
- Utilisation de Lombok pour réduire le boilerplate
- Relations correctement configurées avec `@OneToMany` et `@ManyToOne`
- Lazy loading pour optimiser les requêtes

---

**Dernière mise à jour:** Mars 2026
