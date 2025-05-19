# CSL Librabry Manager API

Rozwiązanie zostało przygotowane na potrzeby rekrutacji na pozycję Junior Java Developer w firmie Cyber Security Lab.

CSL Library Manager API wykorzystuje framework Spring Boot, wraz z PostgreSQL, Lombok, Flyway itd. które będą
opisane poniżej.

Rozwiązanie to miało oferować funkcjonalności takie jak:
* Rejestracja użytkowników
* Logowanie
* Pobranie listy wszystkich użytkowników (tylko ADMIN)
* Dodawanie książek do bazy danych
* Edycja istniejących książek w bazie danych
* Usuwanie książek z bazy danych
* Pobieranie listy książek z bazy danych

Aplikacja uwzględnia aspekty związane z bezpieczeństwem:
* Role dostępowa (USER, ADMIN)
* Autoryzacja użytkowników przy użyciu JWT
* Haszowanie haseł użytkowników przed zapisaniem ich w bazie danych
* Zabezpieczenie endpointów aplikacji przed atakami typu SQL Injection


## Struktura
Dla tego rozwiązania przyjęta została struktura feature-based.

### Auth
Zawiera komponenty związane z uwierzytelnianiem i autoryzacją.

| Nazwa | Opis                                                                                                                     |
|-------|--------------------------------------------------------------------------------------------------------------------------|
| AuthController | Kontroler odpowiedzialny za obsługę requestów HTTP związanych z logowaniem i rejestracją oraz delegowanie ich do serwisu |
| AuthResponse | Bardzo uproszczony obiekt zwracany po poprawnym logowaniu (zawiera token)                                                |
| AuthService | Serwis odpowiedzialny za wykonanie logiki związanej z logowaniem i rejestracją                                           | 
| JwtService | Serwis odpowiedzialny za generowanie oraz sprawdzanie tokenów JWT                                                        |
| dto   | Obiekty pozwalające na mapowanie json -> obiekt lub obiekt -> json                                                       |

### User
Zawiera komponenty związane z użytkownikami.

| Nazwa          | Opis                                                                                         |
|----------------|----------------------------------------------------------------------------------------------|
| UserController | Kontroler odpowiedzialny za obsługę requestów HTTP związanych pobieraniem listy użytkowników |
| UserRepository | Repozytorium odpowiedzialne za dostęp do tabeli użytkowników w bazie danych                  |
| UserService    | Serwis odpowiedzialny za wykonanie logiki związanej z użytkownikami                          | 
| UserRole       | Enum zawierający role używane w aplikacji (USER, ADMIN)                                      |
| dto            | Obiekty pozwalające na mapowanie json -> obiekt lub obiekt -> json                           |
| User           | Obiekt, encja, reprezentująca rekord w bazie danych, w tabeli users                          |

### Book
Zawiera komponenty związane z książkami.

| Nazwa          | Opis                                                                                                      |
|----------------|-----------------------------------------------------------------------------------------------------------|
| BookController | Kontroler odpowiedzialny za obsługę requestów HTTP związanych z dodawaniem, edycją oraz usuwaniem książek |
| BookRepository | Repozytorium odpowiedzialne za dostęp do tabeli książek w bazie danych                                    |
| BookService    | Serwis odpowiedzialny za wykonanie logiki związanej z dodawaniem, edycją oraz usuwaniem książek           | 
| Book           | Obiekt, encja, reprezentująca rekord w bazie danych, w tabeli books                                       |
| dto            | Obiekty pozwalające na mapowanie json -> obiekt lub obiekt -> json                                        |

### Config
Zawiera konfiguracje.

| Nazwa                          | Opis                                                                                             |
|--------------------------------|--------------------------------------------------------------------------------------------------|
| security/SecurityConfiguration | Konfiguracja odpowiedzialna za zabezpieczenia aplikacji, uwierzytelnianie i dostęp do endpointów |
| ApplicationConfiguration       | Konfiguracja kluczowych komponentów aplikacji, np. UserDetailsService itd.                       |
| JwtAuthenticationFilter        | Filtr, który przechwytuje żądania i sprawdza poprawność tokenu JWT                               | 

### Utils
Klasa DtoUtils zawiera statyczne metody bookToDto oraz userToDto, dla ułatwienia zwracania użytkownikowi końcowemu obiekt DTO, a nie obiekt encji. 

## Baza danych
Ze względu na problemy przy wystartowaniu dockera z obrazem MS SQL na moim systemie operacyjnym,
postanowiłem skorzystać z bazy danych PostgreSQL.

## Additional libs used:

### Flyway
Postanowiłem skorzystać z biblioteki Flyway, ponieważ bardzo lubię podejście , które ona oferuje.

Plusy tego rozwiązania:
* migracja przy starcie aplikacji
* wersjonowanie zmian (pliki .sql), co daje kontrolę nad zmianami w bazie
* powtarzalność i przewidywalność - możliwość otrzymania identycznego schematu na środowiskach dev, test oraz produkcyjnym
* Rollback - Flyway nie wykona migracji, jeśli napotka konflikt lub błąd

### Lombok
Bibliotekę Lombok użyłem w celu wyeliminowania powtarzającego się boilerplate kodu.

### Mockito

### Junit
Framework testowy

### Testcontainers
Biblioteka umożliwiająca testowanie aplikacji na prawdziwej bazie danych.