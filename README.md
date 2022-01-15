# NBP-projekat-1-i-2

Za kreiranje baze potrebno je kreirati keyspace-ove u Cassandri. Komande su sledece:
```
CREATE  KEYSPACE mykeyspace 
   WITH REPLICATION = { 
      'class' : 'SimpleStrategy', 'replication_factor' : 1 };
CREATE  KEYSPACE timelineservice 
   WITH REPLICATION = { 
      'class' : 'SimpleStrategy', 'replication_factor' : 1 };
CREATE  KEYSPACE profileservice 
   WITH REPLICATION = { 
      'class' : 'SimpleStrategy', 'replication_factor' : 1 };
 ```

Za pokretanje frontend-a aplikacije:
1. Neophodno je instalirati npm (Node Package Manager) i Node.js. 
2. Pozicionirati se u folderu FE u okviru projekta i povuci sve zavisnosti komandom `npm install`. 
3. Instalirati angular cli komandom `npm install -g @angular/cli@6.14.15`.
4. Potom je moguce aplikaciju pokrenuti komandom `ng serve`.

Za pokretanje beckend-a aplikacije trebalo bi koristiti IntelliJ. 
1. Potrebna je instalacija Jave 1.8.
2. Otvoriti svaki od 4 servisa u posebnom window-u. Sledeca tri koraka izvrsiti za svaki servis posebno.
3. U gornjem desnom uglu pod maven opcijama konkretno "m" tj. execute maven goal izvrsiti komandu `mvn clean install`.
4. Omoguciti procesiranje [lombok anotacija](https://stackoverflow.com/questions/24006937/lombok-annotations-do-not-compile-under-intellij-idea?fbclid=IwAR0J7oG8SBGBWEGBXDKVlmNiNiZJEMtJrtjXmlHWfUz9UhPqK21xCya_0qw).
5. Potom pokrenuti svaki servis.


