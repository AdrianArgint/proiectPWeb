# proiectPWeb

In acest repo se afla 2 proiecte. 

In primul proiect se afla proiectul principal cu toate fisierele docker de configurare, un fisier docker-compose si aplicatiile
de backend si frontend. Frontend-ul este realizat in Angular 13, iar backend-ul in Spring boot. Pentru a putea rula backendul intr-un container trebuia date
urmatoarele comenzi:
1. mvn clean install -Dmaven.test.skip=true -> iti compileaza backendul si iti impacheteaza intr-un jar frontnedul si backend-ul. Am utilizat un plugin care este
adaugat in pom.xml si care in momentul compilarii backendul va instala si node_modules 
3. ./mvnw verify -DskipTests -Pprod jib:dockerBuild -> verifica jar-ul si creaza o imagine e baza acestuia
4. docker-compose -f .\docker_compose.yml up -d --build

In al doilea proiect se afla un proiect spring care primeste mesaje de la coada si va trimite emailuri catre persoane. Pentru a putea rula trebuia date aceleasi
comenzi ca mai sus.
