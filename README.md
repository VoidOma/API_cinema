# TP Noté – Cinéma (Spring Boot)

Application REST pour la gestion d’un cinéma: films, séances, réservations, salles et utilisateurs (avec carte de fidélité).

Fonctionnalités principales
- Consulter la liste des films
- Réserver des places de cinéma pour une séance
- Enregistrer des utilisateurs et gérer la carte de fidélité (valable 1 an) donnant droit à des réductions
- Producteurs/Distributeurs: ajouter de nouveaux films
- Administrateur: ajouter des salles de projection

Stack technique
- Java 17, Spring Boot 3
- Spring Web, Spring Data JPA, Validation
- Base de données H2 en mémoire (par défaut) – MySQL possible
- OpenAPI/Swagger UI pour la documentation
- Tests unitaires JUnit 5, AssertJ

Prérequis
- Java 17+
- Maven 3.9+

Démarrage rapide
1) Cloner le projet
2) Lancer l’application
- Via Maven: mvn spring-boot:run
- Ou via IDE: lancer la classe org.example.tp_noteqd_cinema.TpNoteQdCinemaApplication
3) Consulter la doc API (Swagger)
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
4) Console H2 (BD en mémoire)
- http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:productsdb
- User: sa, Password: (vide)

Configuration
Le fichier src/main/resources/application.properties contient la configuration par défaut:
- H2 en mémoire
- DDL auto: update
- Affichage des requêtes SQL
- Activation Swagger UI
Pour MySQL, remplacer les propriétés de datasource par celles de votre instance puis ajouter la dépendance du driver MySQL.

Modèle de données (entités principales)
- Film: id, titre, genre, durée, dateSortie, prix, réalisateur
- Utilisateur: id, nom, email, motDePasse, carteFidelite (booléen ou entité dédiée)
- Salle: id, nom, capacité
- Seance: id, date, heure, salle (ref), film (ref)
- Reservation: id, utilisateur, seance, nombrePlaces

DTOs
- FilmDto, FilmDtoMapper
- ReservationRequestDto, ReservationResponseDto
- SalleCreateDto
- SeanceCreateDto
- UserRegistrationDto

Endpoints REST (exemples complets)
- Films
  - GET /api/films: lister les films
    - curl: curl -X GET http://localhost:8080/api/films
  - POST /api/films (JSON): ajouter un film (producteur/distributeur)
    - Body (ex.): {"titre":"Inception","genre":"Sci-Fi","duree":148,"dateSortie":"2010-07-16","prix":12.5,"realisateur":"Christopher Nolan"}
    - curl: curl -X POST http://localhost:8080/api/films -H "Content-Type: application/json" -d '{"titre":"Inception","genre":"Sci-Fi","duree":148,"dateSortie":"2010-07-16","prix":12.5,"realisateur":"Christopher Nolan"}'
  - POST /api/films (form-data/x-www-form-urlencoded):
    - Champs: titre (obligatoire), genre, duree, dateSortie (yyyy-MM-dd), prix (obligatoire), realisateur
    - curl (x-www-form-urlencoded): curl -X POST http://localhost:8080/api/films -H "Content-Type: application/x-www-form-urlencoded" -d "titre=Inception&genre=Sci-Fi&duree=148&dateSortie=2010-07-16&prix=12.5&realisateur=Christopher+Nolan"

- Salles (admin)
  - POST /api/salles (JSON): créer une salle
    - Body (ex.): {"nom":"Salle 1","capacite":120}
    - curl: curl -X POST http://localhost:8080/api/salles -H "Content-Type: application/json" -d '{"nom":"Salle 1","capacite":120}'
  - POST /api/salles (form-data/x-www-form-urlencoded):
    - Champs: nom (obligatoire), capacite (>=1)
    - curl: curl -X POST http://localhost:8080/api/salles -H "Content-Type: application/x-www-form-urlencoded" -d "nom=Salle%201&capacite=120"

- Séances
  - POST /api/seances (JSON): créer une séance
    - Body (ex.): {"filmId":1,"salleId":1,"date":"2025-10-01","heure":"20:00"}
    - Contraintes: date au format yyyy-MM-dd et aujourd'hui/futur; heure au format HH:mm
    - curl: curl -X POST http://localhost:8080/api/seances -H "Content-Type: application/json" -d '{"filmId":1,"salleId":1,"date":"2025-10-01","heure":"20:00"}'
  - POST /api/seances (form-data/x-www-form-urlencoded):
    - Champs: filmId, salleId, date (yyyy-MM-dd), heure (HH:mm)
    - curl: curl -X POST http://localhost:8080/api/seances -H "Content-Type: application/x-www-form-urlencoded" -d "filmId=1&salleId=1&date=2025-10-01&heure=20:00"

- Réservations
  - POST /api/reservations (JSON): réserver des places
    - Body (ex.): {"utilisateurId":1,"seanceId":2,"nombrePlaces":3}
    - curl: curl -X POST http://localhost:8080/api/reservations -H "Content-Type: application/json" -d '{"utilisateurId":1,"seanceId":2,"nombrePlaces":3}'
  - POST /api/reservations (form-data/x-www-form-urlencoded):
    - Champs: utilisateurId, seanceId, nombrePlaces (>=1)
    - curl: curl -X POST http://localhost:8080/api/reservations -H "Content-Type: application/x-www-form-urlencoded" -d "utilisateurId=1&seanceId=2&nombrePlaces=3"
  - Réponse (ex.): {"reservationId":10,"utilisateurId":1,"seanceId":2,"nombrePlaces":3,"prixTotal":33.75,"reductionAppliquee":true}

- Utilisateurs
  - POST /api/utilisateurs: enregistrer un utilisateur
    - Body (ex.): {"nom":"Alice","email":"alice@example.com","motDePasse":"secret"}
    - curl: curl -X POST http://localhost:8080/api/utilisateurs -H "Content-Type: application/json" -d '{"nom":"Alice","email":"alice@example.com","motDePasse":"secret"}'
  - POST /api/utilisateurs/{id}/carte: acheter une carte de fidélité (valable 1 an)
    - curl: curl -X POST http://localhost:8080/api/utilisateurs/1/carte
    - Effet: active la carte et définit carteExpiration = date du jour + 1 an

Règles métier (extraits)
- Capacité de salle: une réservation ne peut pas dépasser la capacité restante de la salle (exception CapacityExceededException)
- Carte de fidélité: valable 1 an; peut donner droit à des réductions lors des réservations (voir ReservationService)
- Gestion d’erreurs: exceptions custom (BadRequestException, NotFoundException) avec un GlobalExceptionHandler

Tests
- Exemple de test repository: FilmRepositoryTest (save & find)
- Pour lancer tous les tests: mvn test

Structure du projet (extraits)
- controller/: FilmsController, ReservationsController, SallesController, SeancesController, UsersController
- domain/: Film, Salle, Seance, Reservation, Utilisateur
- dto/: DTOs et mappers
- repository/: Spring Data JPA repositories
- service/: règles métier (FilmService, ReservationService, etc.)
- exception/: exceptions custom + GlobalExceptionHandler

Déploiement
- Construction du JAR: mvn clean package
- Lancement: java -jar target/TP_noteQD_cinema-0.0.1-SNAPSHOT.jar

Liens utiles
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Console H2: http://localhost:8080/h2-console

Aide de Junie pour le projet
