# achat-en-ligne-ms

Microservice Spring Boot pour la gestion des achats en ligne.

## Technologies

- Java 17
- Spring Boot 3.2.5
- Spring Security + OAuth2 Resource Server (Keycloak)
- Spring Data JPA
- Spring Cache
- MapStruct
- Lombok
- Springdoc OpenAPI (Swagger UI)
- H2 (dev) / MySQL (prod)

---

## Structure du projet

```
src/main/java/com/groupeisi/company/
├── AchatEnLigneMsApplication.java
├── config/
│   ├── CacheConfig.java
│   ├── DataInitializer.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── controller/
│   ├── AchatsController.java
│   ├── ProduitsController.java
│   ├── UserAccountController.java
│   └── VentesController.java
├── dto/
│   ├── AchatsDto.java
│   ├── ProduitsDto.java
│   ├── UserAccountDto.java
│   └── VentesDto.java
├── entities/
│   ├── Achats.java
│   ├── Produits.java
│   ├── UserAccount.java
│   └── Ventes.java
├── exception/
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── mapper/
│   ├── AchatsMapper.java
│   ├── ProduitsMapper.java
│   ├── UserAccountMapper.java
│   └── VentesMapper.java
├── repository/
│   ├── AchatsRepository.java
│   ├── ProduitsRepository.java
│   ├── UserAccountRepository.java
│   └── VentesRepository.java
└── service/
    ├── AchatsService.java
    ├── ProduitsService.java
    ├── UserAccountService.java
    └── VentesService.java
```

---

## Lancer le projet

```bash
mvn spring-boot:run
```

L'application sera disponible sur : http://localhost:8080

### Swagger UI
http://localhost:9090/swagger-ui.html

### H2 Console (dev)
http://localhost:9090/h2-console  
JDBC URL : `jdbc:h2:mem:achatdb`

---

## Configuration Keycloak

### Pré-requis
Keycloak local démarré sur le port **8080**.

### Étapes de configuration

1. **Créer un Realm** nommé : `achat-en-ligne`

2. **Créer un Client** :
   - Client ID : `achat-en-ligne-client`
   - Client type : `OpenID Connect`
   - Authentication flow : activez `Standard flow` et `Direct access grants`
   - Valid redirect URIs : `http://localhost:8080/*`
   - Web origins : `http://localhost:8080`

3. **Créer les Rôles du Realm** :
   - `ADMIN`
   - `USER`

4. **Créer des utilisateurs** et leur assigner les rôles.

5. **Obtenir un token** :
```bash
curl -X POST http://localhost:8080/realms/achat-en-ligne/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=achat-en-ligne-client" \
  -d "username=admin@groupeisi.com" \
  -d "password=admin123" \
  -d "grant_type=password"
```

6. **Utiliser le token** dans les requêtes :
```bash
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:9090/api/v1/users
```

---

## Endpoints disponibles

| Méthode | URL                           | Rôle requis   | Description                    |
|---------|-------------------------------|---------------|--------------------------------|
| GET     | /api/v1/users                 | ADMIN         | Tous les utilisateurs          |
| GET     | /api/v1/users/{id}            | ADMIN, USER   | Un utilisateur par ID          |
| POST    | /api/v1/users                 | ADMIN         | Créer un utilisateur           |
| PUT     | /api/v1/users/{id}            | ADMIN, USER   | Modifier un utilisateur        |
| DELETE  | /api/v1/users/{id}            | ADMIN         | Supprimer un utilisateur       |
| GET     | /api/v1/produits              | ADMIN, USER   | Tous les produits              |
| GET     | /api/v1/produits/{id}         | ADMIN, USER   | Un produit par ID              |
| POST    | /api/v1/produits              | ADMIN         | Créer un produit               |
| PUT     | /api/v1/produits/{id}         | ADMIN         | Modifier un produit            |
| DELETE  | /api/v1/produits/{id}         | ADMIN         | Supprimer un produit           |
| GET     | /api/v1/achats                | ADMIN         | Tous les achats                |
| GET     | /api/v1/achats/{id}           | ADMIN, USER   | Un achat par ID                |
| POST    | /api/v1/achats                | ADMIN, USER   | Créer un achat (déstock auto)  |
| PUT     | /api/v1/achats/{id}           | ADMIN         | Modifier un achat              |
| DELETE  | /api/v1/achats/{id}           | ADMIN         | Supprimer un achat             |
| GET     | /api/v1/ventes                | ADMIN         | Toutes les ventes              |
| GET     | /api/v1/ventes/{id}           | ADMIN, USER   | Une vente par ID               |
| POST    | /api/v1/ventes                | ADMIN, USER   | Créer une vente (déstock auto) |
| PUT     | /api/v1/ventes/{id}           | ADMIN         | Modifier une vente             |
| DELETE  | /api/v1/ventes/{id}           | ADMIN         | Supprimer une vente            |

---

## Profil MySQL (production)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Configurer les paramètres dans `application.yml` section `spring.datasource` (profil mysql).
