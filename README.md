#  achat-en-ligne-ms

Microservice Spring Boot de gestion des achats en ligne — développé.

---

##  Stack technique

| Technologie | Version |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Security + OAuth2 | Keycloak 22 (JWT) |
| Spring Data JPA | Hibernate |
| Spring Cache | ConcurrentMapCache |
| MapStruct | 1.5.5 |
| Lombok | Latest |
| Springdoc OpenAPI | 2.5.0 (Swagger UI) |
| Base de données | MySQL 8+ |
| Build | Maven |

---

##  Structure du projet

```
src/main/java/com/groupeisi/company/
├── AchatEnLigneMsApplication.java
├── config/
│   ├── CacheConfig.java          # Configuration du cache Spring
│   ├── DataInitializer.java      # Données préchargées au 1er démarrage
│   ├── SecurityConfig.java       # OAuth2 Resource Server (Keycloak)
│   └── SwaggerConfig.java        # OpenAPI / Swagger UI
├── controller/
│   ├── UserAccountController.java
│   ├── ProduitsController.java
│   ├── AchatsController.java
│   └── VentesController.java
├── dto/
│   ├── UserAccountDto.java
│   ├── ProduitsDto.java          # ref = UUID (clé primaire, auto-généré)
│   ├── AchatsDto.java
│   └── VentesDto.java
├── entities/
│   ├── UserAccount.java          # id (Long, auto-incrémenté)
│   ├── Produits.java             # ref (String UUID = clé primaire)
│   ├── Achats.java
│   └── Ventes.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   └── GlobalExceptionHandler.java
├── mapper/
│   ├── UserAccountMapper.java
│   ├── ProduitsMapper.java
│   ├── AchatsMapper.java
│   └── VentesMapper.java
├── repository/
│   ├── UserAccountRepository.java
│   ├── ProduitsRepository.java
│   ├── AchatsRepository.java
│   └── VentesRepository.java
└── service/
    ├── UserAccountService.java
    ├── ProduitsService.java
    ├── AchatsService.java
    └── VentesService.java
```

---

##  Configuration

### `src/main/resources/application.properties`

```properties
# Application — port 9090 (Keycloak occupe le 8080)
server.port=9090

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/achat_en_ligne_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root

# Keycloak (port 8080)
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/achat-en-ligne
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/achat-en-ligne/protocol/openid-connect/certs
keycloak.auth-server-url=http://localhost:8080
keycloak.realm=achat-en-ligne
keycloak.resource=achat-en-ligne-client
```

> Modifier `username` / `password` MySQL selon votre environnement local.

---

##  Configuration Keycloak (local port 8080)

> Keycloak doit être démarré **avant** l'application.

### 1. Créer le Realm
- Nom : `achat-en-ligne`

### 2. Créer le Client
- Client ID : `achat-en-ligne-client`
- Type : `OpenID Connect`
- Activer :  **Direct access grants** +  **Standard flow**
- Valid redirect URIs : `http://localhost:9090/*`
- Web origins : `http://localhost:9090`

### 3. Créer un utilisateur de test
- Username / Email au choix
- Onglet **Credentials** → Set password → Temporary : **Off**

### 4. Obtenir un token JWT
```bash
curl -X POST http://localhost:8080/realms/achat-en-ligne/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=achat-en-ligne-client" \
  -d "username=VOTRE_USER" \
  -d "password=VOTRE_MDP" \
  -d "grant_type=password"
```

---

##  Lancer l'application

### Prérequis
- Java 17+
- Maven 3.8+
- MySQL démarré sur le port 3306
- Keycloak démarré sur le port 8080

### Commande
```bash
mvn spring-boot:run
```

L'application démarre sur **http://localhost:9090**

> Au **premier démarrage**, des données de test sont automatiquement insérées (2 utilisateurs, 3 produits, 2 achats, 1 vente).  
> Aux démarrages suivants, l'insertion est ignorée si des données existent déjà.

---

##  Swagger UI

```
http://localhost:9090/swagger-ui.html
```

**Pour s'authentifier dans Swagger :**
1. Générer un token (voir commande curl ci-dessus)
2. Cliquer sur **Authorize **
3. Saisir : `Bearer <votre_access_token>`
4. Cliquer **Authorize** → tous les endpoints sont déverrouillés

---

##  Endpoints disponibles

###  UserAccount — `/api/v1/users`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/v1/users` | Liste tous les utilisateurs |
| GET | `/api/v1/users/{id}` | Détail d'un utilisateur |
| GET | `/api/v1/users/email/{email}` | Recherche par email |
| POST | `/api/v1/users` | Créer un utilisateur |
| PUT | `/api/v1/users/{id}` | Modifier un utilisateur |
| DELETE | `/api/v1/users/{id}` | Supprimer un utilisateur |

###  Produits — `/api/v1/produits`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/v1/produits` | Liste tous les produits |
| GET | `/api/v1/produits/{ref}` | Détail par référence UUID |
| GET | `/api/v1/produits/user/{userId}` | Produits d'un utilisateur |
| POST | `/api/v1/produits` | Créer un produit (ref UUID auto-généré) |
| PUT | `/api/v1/produits/{ref}` | Modifier un produit |
| DELETE | `/api/v1/produits/{ref}` | Supprimer (cascade sur achats/ventes) |

###  Achats — `/api/v1/achats`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/v1/achats` | Liste tous les achats |
| GET | `/api/v1/achats/{id}` | Détail d'un achat |
| GET | `/api/v1/achats/user/{userId}` | Achats d'un utilisateur |
| GET | `/api/v1/achats/produit/{productRef}` | Achats d'un produit |
| POST | `/api/v1/achats` | Créer un achat (décrémente le stock) |
| PUT | `/api/v1/achats/{id}` | Modifier un achat |
| DELETE | `/api/v1/achats/{id}` | Supprimer un achat |

###  Ventes — `/api/v1/ventes`
| Méthode | URL | Description |
|---|---|---|
| GET | `/api/v1/ventes` | Liste toutes les ventes |
| GET | `/api/v1/ventes/{id}` | Détail d'une vente |
| GET | `/api/v1/ventes/user/{userId}` | Ventes d'un utilisateur |
| GET | `/api/v1/ventes/produit/{productRef}` | Ventes d'un produit |
| POST | `/api/v1/ventes` | Créer une vente (décrémente le stock) |
| PUT | `/api/v1/ventes/{id}` | Modifier une vente |
| DELETE | `/api/v1/ventes/{id}` | Supprimer une vente |

---

##  Modèle de données

### UserAccount
```json
{ "id": 1, "email": "user@example.com", "password": "secret" }
```

### Produits
```json
{ "ref": "550e8400-e29b-41d4-a716-446655440000", "name": "Laptop Dell XPS", "stock": 50.0, "userId": 1 }
```
> `ref` est la **clé primaire UUID** — ne pas le fournir à la création, il est auto-généré.

### Achats / Ventes
```json
{ "dateP": "2025-01-15T10:00:00.000+00:00", "quantity": 2.0, "productRef": "550e8400-...", "userId": 1 }
```

---

##  Comportements métier

- **Création d'un achat ou d'une vente** → le stock du produit est **décrémenté automatiquement**
- **Stock insuffisant** → erreur `400 Bad Request` avec message explicite
- **Suppression d'un produit** → tous les achats et ventes liés sont **supprimés en cascade**
- **Email dupliqué** → erreur `409 Conflict`
- **Ressource introuvable** → erreur `404 Not Found`

---

##  Build

```bash
# Compiler et packager
mvn clean package -DskipTests

# Lancer le JAR
java -jar target/achat-en-ligne-ms-1.0.0.jar
```



##  Captures de quelques tests fait avec postman 
### 1. 401
<img width="960" height="540" alt="401" src="https://github.com/user-attachments/assets/3faaa31c-1cf4-4b17-965f-f7395e549a7b" />

### 2. Créer un user
<img width="960" height="540" alt="postman_users_create" src="https://github.com/user-attachments/assets/84326293-8bf5-4c7b-a4d4-c4648ece5d78" />

### 3. Editer un produit
<img width="960" height="540" alt="postman_put_produits" src="https://github.com/user-attachments/assets/2660194d-ae42-4579-94b1-f74d1595cd56" />

### 4. Lister les ventes
<img width="960" height="540" alt="postman_get_ventes" src="https://github.com/user-attachments/assets/43173879-2e25-4316-a689-5eaa9dd44bcd" />

### 5. Supprimer un achat
<img width="960" height="540" alt="postman_delete_achats" src="https://github.com/user-attachments/assets/afe12ee9-ccd3-46d8-a0e8-65bb8ec5ade4" />

### 6. 409 CONFLICT EXCEPTION
<img width="960" height="540" alt="postman_409_users" src="https://github.com/user-attachments/assets/566e3d7d-2fb1-40f6-a01a-62afed1ca4ad" />

### 7. 404 EXCEPTION
<img width="960" height="540" alt="404" src="https://github.com/user-attachments/assets/eed1027d-4efc-4ca1-9ca7-96a2351715ed" />

