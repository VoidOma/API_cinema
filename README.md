# TP Noté – Cinéma (Spring Boot)

Application REST pour la gestion d’un cinéma: films, salles, séances, réservations et utilisateurs (avec carte de fidélité). Le projet fournit des endpoints documentés (Swagger), une persistance H2 par défaut, des validations et une gestion d’erreurs claire.

## Fonctionnalités
- Consulter la liste des films
- Ajouter des films (producteur ou distributeur)
- Ajouter des salles (administrateur)
- Créer des séances (film + salle + date + heure)
- Enregistrer des utilisateurs et acheter une carte de fidélité (valable 1 an)
- Réserver des places pour une séance (contrôle de capacité, réduction fidélité)

## Stack technique
- Java 17, Spring Boot 3.x
- Spring Web, Spring Data JPA, Bean Validation
- Base de données H2 en mémoire (par défaut) – MySQL possible
- OpenAPI/Swagger UI (springdoc 2.6.0)
- Tests unitaires JUnit 5, AssertJ

## Prérequis
- Java 17+
- Maven 3.9+

## Démarrage rapide
1) Cloner le projet
2) Lancer l’application
   - Via Maven: mvn spring-boot:run
   - Ou via l’IDE: exécuter la classe org.example.tp_noteqd_cinema.TpNoteQdCinemaApplication
3) Consulter la documentation API
   - Swagger UI: http://localhost:8080/swagger-ui/index.html
   - OpenAPI JSON: http://localhost:8080/v3/api-docs
4) Console H2 (base en mémoire)
   - URL: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:productsdb
   - User: sa, Password: (vide)

## Configuration
Le fichier src/main/resources/application.properties contient la configuration par défaut:
- H2 en mémoire (jdbc:h2:mem:productsdb; DB_CLOSE_DELAY=-1)
- DDL auto: update
- Affichage des requêtes SQL
- Swagger UI activé


## Modèle de données (entités principales)
- Film: id, titre, genre, duree, dateSortie (String ISO yyyy-MM-dd), prix (BigDecimal), realisateur
- Salle: id, nom (unique), capacite
- Seance: id, date (String ISO yyyy-MM-dd), heure (String HH:mm), salle (ref), film (ref)
- Utilisateur: id, nom, email, motDePasse, carteFideliteActive (booléen), carteExpiration (String ISO, optionnel)
- Reservation: id, utilisateur, seance, nombrePlaces

Note: les champs de date et d’heure sont stockés en String pour faciliter la saisie multi-formats; la validation est appliquée côté DTO/service.

## DTOs & mappers
- FilmDto, FilmDtoMapper
- SalleCreateDto
- SeanceCreateDto
- ReservationRequestDto, ReservationResponseDto
- UserRegistrationDto

## Rôles et autorisation (léger)
Certaines opérations nécessitent un rôle fourni via l’en-tête HTTP X-ROLE:
- ADMIN: requis pour POST /api/salles (création de salle)
- PRODUCTEUR ou DISTRIBUTEUR: requis pour POST /api/films (création de film)

Exemples rapides:
- Création de salle (ADMIN):
  curl -X POST http://localhost:8080/api/salles -H "X-ROLE: ADMIN" -H "Content-Type: application/json" -d '{"nom":"Salle 1","capacite":120}'
- Création de film (PRODUCTEUR):
  curl -X POST http://localhost:8080/api/films -H "X-ROLE: PRODUCTEUR" -H "Content-Type: application/json" -d '{"titre":"Inception","genre":"Sci-Fi","duree":148,"dateSortie":"2010-07-16","prix":12.5,"realisateur":"Christopher Nolan"}'

Note: il ne s’agit pas d’une authentification complète; c’est un garde-fou minimal par en-tête. Une intégration Spring Security/JWT est possible ultérieurement.

## Endpoints REST (exemples)

### Films
- GET /api/films — lister les films
  - curl: curl -X GET http://localhost:8080/api/films
- POST /api/films (JSON) — ajouter un film (rôle: PRODUCTEUR ou DISTRIBUTEUR)
  - Body (ex.): {"titre":"Inception","genre":"Sci-Fi","duree":148,"dateSortie":"2010-07-16","prix":12.5,"realisateur":"Christopher Nolan"}
  - curl: curl -X POST http://localhost:8080/api/films -H "X-ROLE: PRODUCTEUR" -H "Content-Type: application/json" -d '{"titre":"Inception","genre":"Sci-Fi","duree":148,"dateSortie":"2010-07-16","prix":12.5,"realisateur":"Christopher Nolan"}'
- POST /api/films (form-data/x-www-form-urlencoded) — même contrainte de rôle
  - Champs: titre (obligatoire), genre, duree, dateSortie (yyyy-MM-dd), prix (obligatoire), realisateur
  - curl: curl -X POST http://localhost:8080/api/films -H "X-ROLE: DISTRIBUTEUR" -H "Content-Type: application/x-www-form-urlencoded" -d "titre=Inception&genre=Sci-Fi&duree=148&dateSortie=2010-07-16&prix=12.5&realisateur=Christopher+Nolan"

### Salles (admin)
- POST /api/salles (JSON) — créer une salle (rôle: ADMIN)
  - Body (ex.): {"nom":"Salle 1","capacite":120}
  - curl: curl -X POST http://localhost:8080/api/salles -H "X-ROLE: ADMIN" -H "Content-Type: application/json" -d '{"nom":"Salle 1","capacite":120}'
- POST /api/salles (form-data/x-www-form-urlencoded) — même contrainte de rôle
  - Champs: nom (obligatoire), capacite (>=1)
  - curl: curl -X POST http://localhost:8080/api/salles -H "X-ROLE: ADMIN" -H "Content-Type: application/x-www-form-urlencoded" -d "nom=Salle%201&capacite=120"

### Séances
- POST /api/seances (JSON) — créer une séance
  - Body (ex.): {"filmId":1,"salleId":1,"date":"2025-10-01","heure":"20:00"}
  - Contraintes: date au format yyyy-MM-dd et aujourd’hui/futur; heure au format HH:mm
  - curl: curl -X POST http://localhost:8080/api/seances -H "Content-Type: application/json" -d '{"filmId":1,"salleId":1,"date":"2025-10-01","heure":"20:00"}'
- POST /api/seances (form-data/x-www-form-urlencoded)
  - Champs: filmId, salleId, date (yyyy-MM-dd), heure (HH:mm)
  - curl: curl -X POST http://localhost:8080/api/seances -H "Content-Type: application/x-www-form-urlencoded" -d "filmId=1&salleId=1&date=2025-10-01&heure=20:00"

### Réservations
- POST /api/reservations (JSON) — réserver des places
  - Body (ex.): {"utilisateurId":1,"seanceId":2,"nombrePlaces":3}
  - curl: curl -X POST http://localhost:8080/api/reservations -H "Content-Type: application/json" -d '{"utilisateurId":1,"seanceId":2,"nombrePlaces":3}'
- POST /api/reservations (form-data/x-www-form-urlencoded)
  - Champs: utilisateurId, seanceId, nombrePlaces (>=1)
  - curl: curl -X POST http://localhost:8080/api/reservations -H "Content-Type: application/x-www-form-urlencoded" -d "utilisateurId=1&seanceId=2&nombrePlaces=3"
- Réponse (ex.): {"reservationId":10,"utilisateurId":1,"seanceId":2,"nombrePlaces":3,"prixTotal":33.75,"reductionAppliquee":true}

### Utilisateurs
- POST /api/utilisateurs — enregistrer un utilisateur (JSON ou formulaire)
  - Body (ex.): {"nom":"Alice","email":"alice@example.com","motDePasse":"secret"}
  - curl: curl -X POST http://localhost:8080/api/utilisateurs -H "Content-Type: application/json" -d '{"nom":"Alice","email":"alice@example.com","motDePasse":"secret"}'
- POST /api/utilisateurs/{id}/carte — acheter une carte de fidélité (valable 1 an)
  - curl: curl -X POST http://localhost:8080/api/utilisateurs/1/carte
  - Effet: active la carte et définit carteExpiration = date du jour + 1 an

## Règles métier & gestion des erreurs
- Capacité de salle: une réservation ne peut pas dépasser la capacité restante (CapacityExceededException)
- Carte de fidélité: réduction 10% si carte active et non expirée
- Séances: date au format ISO et aujourd’hui/futur; heure au format HH:mm
- Gestion d’erreurs: exceptions custom (BadRequestException, NotFoundException, ForbiddenException) + GlobalExceptionHandler (400/403/404/500)

## Tests
- Exemple de test repository: FilmRepositoryTest (save & find)
- Lancer tous les tests: mvn test

## Structure du projet (extraits)
- controller/: FilmsController, ReservationsController, SallesController, SeancesController, UsersController
- domain/: Film, Salle, Seance, Reservation, Utilisateur
- dto/: DTOs et mappers
- repository/: Spring Data JPA repositories
- service/: logique métier (FilmService, ReservationService, etc.)
- exception/: exceptions custom + GlobalExceptionHandler
- config/: BindingConfig (sanitisation des champs String pour formulaires)

## Déploiement
- Construction du JAR: mvn clean package
- Lancement: java -jar target/TP_noteQD_cinema-0.0.1-SNAPSHOT.jar

## Liens utiles
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Console H2: http://localhost:8080/h2-console
