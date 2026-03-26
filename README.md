# KH-ABDEV: Plateforme de Gestion GraphQL

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green) ![GraphQL](https://img.shields.io/badge/GraphQL-Latest-purple) ![Java](https://img.shields.io/badge/Java-17+-orange)

## 📋 Table des Matières

- [Objectif](#objectif)
- [Configuration](#configuration)
- [Architecture](#architecture)
- [Schema GraphQL](#schema-graphql)
- [Accès aux Données](#accès-aux-données)
- [Résolveurs GraphQL](#résolveurs-graphql)
- [Tests](#tests)
- [Installation et Utilisation](#installation-et-utilisation)

---

## 🎯 Objectif

Développer une **API GraphQL** complète permettant de gérer des **utilisateurs** et des **posts**. Cette plateforme offre une interface moderne pour créer, lire, mettre à jour et supprimer des ressources avec des relations bidirectionnelles entre utilisateurs et posts.

**Fonctionnalités principales:**
- Gestion complète des utilisateurs (CRUD)
- Gestion des posts avec relation à l'utilisateur
- Requêtes et mutations GraphQL optimisées
- Gestion d'exceptions personnalisée

---

## ⚙️ Configuration

### Stack Technologique

```
Spring Boot 3.x       → Framework principal
GraphQL               → API query language
Spring Data JPA       → Accès aux données
Base de Données       → Support MySQL/PostgreSQL
Docker Compose        → Orchestration des services
Maven                 → Gestion des dépendances
```

### Dépendances Principales

```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- GraphQL -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-graphql</artifactId>
</dependency>

<!-- JPA & Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Database Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

### Configuration Application (application.properties)

```properties
spring.application.name=KH-ABDEV
spring.datasource.url=jdbc:mysql://localhost:3306/kh_abdev
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# GraphQL Configuration
graphql.servlet.enabled=true
```

---

## 🏗️ Architecture

```
KH-ABDEV/
├── src/main/java/com/kholty/KH_ABDEV/
│   ├── Application.java                 # Point d'entrée
│   ├── config/                          # Configurations
│   ├── controller/
│   │   ├── UserController.java
│   │   └── PostController.java
│   ├── entity/
│   │   ├── user/
│   │   │   └── User.java               # Entité JPA User
│   │   └── post/
│   │       └── Post.java               # Entité JPA Post
│   ├── repository/
│   │   ├── UserRepository.java         # Repository User
│   │   └── PostRepository.java         # Repository Post
│   └── exception/
│       ├── UserException.java
│       └── PostException.java
├── src/main/resources/
│   ├── application.properties
│   ├── graphql/
│   │   └── schema.graphqls            # Définition du schéma GraphQL
│   └── static/
├── compose.yaml                         # Configuration Docker Compose
└── pom.xml                              # Configuration Maven
```

---

## 📡 Schema GraphQL

Le schéma GraphQL définit tous les types, requêtes et mutations de l'API.

### Fichier: `schema.graphqls`

```graphql
# Types
type User {
    id: ID!
    name: String!
    email: String!
    posts: [Post!]!
}

type Post {
    id: ID!
    title: String!
    content: String!
    author: User!
}

# Requêtes (Lectures)
type Query {
    # Récupérer tous les utilisateurs
    getAllUsers: [User!]!
    
    # Récupérer un utilisateur par ID
    getUser(id: ID!): User
    
    # Récupérer tous les posts
    getAllPosts: [Post!]!
    
    # Récupérer un post par ID
    getPost(id: ID!): Post
}

# Mutations (Écritures)
type Mutation {
    # Créer un nouvel utilisateur
    createUser(name: String!, email: String!): User!
    
    # Créer un nouveau post
    createPost(title: String!, content: String!, authorId: ID!): Post!
    
    # Mettre à jour un utilisateur
    updateUser(id: ID!, name: String, email: String): User
    
    # Supprimer un utilisateur
    deleteUser(id: ID!): Boolean!
    
    # Supprimer un post
    deletePost(id: ID!): Boolean!
}
```

---

## 💾 Accès aux Données

### Entité User

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
    
    // Getters et Setters
}
```

### Entité Post

```java
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    // Getters et Setters
}
```

### Repositories JPA

#### UserRepository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAll();
}
```

#### PostRepository

```java
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(Long authorId);
    List<Post> findAll();
}
```

---

## 🔍 Résolveurs GraphQL

### User Query Resolver

```java
@Component
public class UserQueryResolver implements GraphQLQueryResolver {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
```

### Post Query Resolver

```java
@Component
public class PostQueryResolver implements GraphQLQueryResolver {
    
    @Autowired
    private PostRepository postRepository;
    
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    public Post getPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
```

### Mutation Resolvers

```java
@Component
public class MutationResolver implements GraphQLMutationResolver {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    // Mutations pour User
    public User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, String name, String email) {
        return userRepository.findById(id)
            .map(user -> {
                if (name != null) user.setName(name);
                if (email != null) user.setEmail(email);
                return userRepository.save(user);
            })
            .orElseThrow(() -> new UserException("User not found"));
    }
    
    public boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return true;
    }
    
    // Mutations pour Post
    public Post createPost(String title, String content, Long authorId) {
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new UserException("Author not found"));
        
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }
    
    public boolean deletePost(Long id) {
        postRepository.deleteById(id);
        return true;
    }
}
```

---

## 🧪 Tests

### Tests GraphQL

Tests des mutations et requêtes principales:

```java
@SpringBootTest
@AutoConfigureWebTestClient
public class GraphQLIntegrationTest {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    public void testCreateUser() {
        String query = """
            mutation {
                createUser(name: "John Doe", email: "john@example.com") {
                    id
                    name
                    email
                }
            }
        """;
        
        webTestClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"query\": \"" + query + "\"}")
            .exchange()
            .expectStatus().isOk();
    }
    
    @Test
    public void testGetAllUsers() {
        String query = """
            query {
                getAllUsers {
                    id
                    name
                    email
                }
            }
        """;
        
        webTestClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"query\": \"" + query + "\"}")
            .exchange()
            .expectStatus().isOk();
    }
    
    @Test
    public void testCreatePost() {
        String mutation = """
            mutation {
                createPost(title: "Mon Post", content: "Contenu...", authorId: 1) {
                    id
                    title
                    content
                    author {
                        name
                    }
                }
            }
        """;
        
        webTestClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"query\": \"" + mutation + "\"}")
            .exchange()
            .expectStatus().isOk();
    }
}
```

---

## 🚀 Installation et Utilisation

### Prérequis

- Java 17+
- Maven 3.8+
- MySQL/PostgreSQL
- Docker & Docker Compose (optionnel)

### Installation Locale

```bash
# 1. Cloner le projet
git clone <repository-url>
cd KH-ABDEV

# 2. Configurer la base de données dans application.properties
# Modifier les paramètres de connexion DB

# 3. Construire le projet
mvn clean install

# 4. Démarrer l'application
mvn spring-boot:run
```

### Utilisation avec Docker Compose

```bash
# Démarrer les services
docker-compose up -d

# Arrêter les services
docker-compose down
```

### Tester l'API GraphQL

**URL:** `http://localhost:8080/graphql`

#### Exemple 1: Créer un utilisateur

```graphql
mutation {
    createUser(name: "Alice Martin", email: "alice@example.com") {
        id
        name
        email
    }
}
```

#### Exemple 2: Récupérer tous les utilisateurs

```graphql
query {
    getAllUsers {
        id
        name
        email
        posts {
            title
        }
    }
}
```

#### Exemple 3: Créer un post

```graphql
mutation {
    createPost(title: "Mon Premier Post", content: "Contenu intéressant", authorId: 1) {
        id
        title
        content
        author {
            id
            name
        }
    }
}
```

---

## 🔧 Gestion des Exceptions

### UserException

```java
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
```

### PostException

```java
public class PostException extends RuntimeException {
    public PostException(String message) {
        super(message);
    }
}
```

---

## 📝 Notes de Développement

- Les IDs sont auto-générés par la base de données (IDENTITY)
- Les relations User ↔ Post sont bidirectionnelles
- La gestion des exceptions est centralisée
- Les mutations modifient l'état de la base de données
- Les requêtes sont en lecture seule

---

## 📦 Structure des Dépôts

| Repository | Responsabilité |
|-----------|-----------------|
| `UserRepository` | Opérations CRUD sur les utilisateurs |
| `PostRepository` | Opérations CRUD sur les posts |

---

## 🤝 Contribution

Pour contribuer au projet, veuillez:
1. Créer une branche pour votre feature
2. Effectuer vos modifications
3. Ajouter des tests
4. Soumettre une pull request

---

## 📄 Licence

Ce projet est fourni à des fins éducatives.

---

## 📞 Support

Pour toute question ou problème, veuillez contacter l'équipe de développement.

**Dernière mise à jour:** Mars 2026
