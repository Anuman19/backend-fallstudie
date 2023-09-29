# Webentwicklung Backend 

## Wichtige Ordner

|Ordner         | Beschreibung                                                  |
|---            |---                                                            |
|**database**:  |SQL-Files für Datenimport und ERM                |
|**src/main**:  |Java-Source-Code                                               |
|**src/test**:  |Junit5/Jupiter Testklassen                                              |

## ERM
![ERM der vollständigen Datenbank](database/Database_Diagram.png)

Im Auftrag 1 sind nur die beiden Tabellen "application_user" und "item" von Bedeutung.

## Applikation starten

1. Lokal eine MySQL Instanz installieren
2. /database/CreateDatabase-with-data.sql ausführen (erzeugt Datenbank inkl. User und Testdaten)
3. Im Terminal mit ```gradlew bootRun``` oder über das Gradle-Tab in der IDE die App starten
4. Applikation kann über ```http://localhost:8080``` angesprochen werden
5. Wurde DB mit Daten geladen, existiert der Benutzer "user" mit dem Passwort "test"
6. Zum Einloggen POST-Request an /login schicken mit {"username": "user", "password": "test"} im Body}
7. Im Header der Response nach dem Login ist der Token enthalten (Authorization: "Bearer eyJ0eXAiOiJKV1QiLC...", wobei das Wort "
   Bearer" auch Teil des Tokens ist). Dieser Token muss bei jedem künftigen Request im Header als value zum Attribut "
   Authorization" mitgeschickt werden.

### Nützliche urls

* API-Endpunkte
    * [http://localhost:8080/users](http://localhost:8080/users)
    * [http://localhost:8080/items](http://localhost:8080/items)
