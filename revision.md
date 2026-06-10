Bien sûr. Bien que je ne puisse pas générer un fichier téléchargeable directement, je peux vous fournir le contenu complet de la fiche de révision, formaté de manière claire et structurée.

Vous pourrez ainsi facilement le copier-coller dans l'éditeur de texte ou le document de votre choix (Word, Google Docs, etc.).

---
### **Fiche Technique de Révision – Développeur Back-End Java / Spring Boot**

Cette fiche synthétise les concepts évalués dans le test HackerRank, en mettant l'accent sur les notions à renforcer (marquées d'un ⚠️).

### **1. Spring Framework & Spring Boot**

#### **a. Injection de Dépendances (DI)**
*   **Concepts Clés :** Spring gère la création et la liaison des objets (beans).
*   **Types d'Injection :**
    *   **Par Constructeur (⭐ Recommandé) :** Les dépendances sont des arguments du constructeur, garantissant que les objets sont créés avec tout ce dont ils ont besoin. C'est plus propre et facilite les tests.
        ```java
        @Service
        public class MyService {
            private final MyRepository repository;

            @Autowired // Optionnel si un seul constructeur
            public MyService(MyRepository repository) {
                this.repository = repository;
            }
        }
        ```
    *   **Par Setter :** Utile pour les dépendances optionnelles.
    *   **Par Champ (Field Injection) :** Déconseillé car il rend les tests plus difficiles et peut masquer les dépendances.

#### **b. ⚠️ Gestion des Transactions (`@Transactional`)**
*   **Concepts Clés :** Garantit que des opérations sur la base de données sont "tout ou rien" (atomiques).
*   **Propagation :**
    *   `REQUIRED` (défaut) : Utilise la transaction en cours ou en crée une nouvelle.
    *   `REQUIRES_NEW` : Crée **toujours** une nouvelle transaction, suspendant la transaction externe.
*   **Piège à connaître (Question 9) :** Dans votre code, la méthode externe (`saveProduct`) intercepte l'exception (`try-catch`) de l'appel interne. Si `saveProductToLogService` (`REQUIRES_NEW`) échoue, sa transaction est annulée (rollback). **Cependant, comme l'exception est attrapée, la transaction externe de `saveProduct` n'est pas informée de l'échec et validera ses propres changements (commit).**

#### **c. ⚠️ Tests et Mocks avec Spring Boot**
*   **Concepts Clés :** Isoler le code à tester de ses dépendances externes (BDD, API...).
*   **Annotations à Maîtriser :**
    *   `@MockBean` : Remplace un bean dans le contexte Spring par un mock Mockito. C'est l'outil parfait pour tester un service sans appeler son vrai repository.
    *   `@SpyBean` : Espionne un vrai bean. Les appels passent par le vrai code, sauf si une méthode est spécifiquement mockée.
*   **Différence `@Mock` vs `@MockBean` :** `@Mock` (Mockito) crée un mock simple, mais n'est pas géré par Spring. `@MockBean` (Spring) intègre le mock dans le contexte d'injection de Spring, ce qui est nécessaire pour les tests `@SpringBootTest`.
*   **Transactions en Test (`@Transactional` sur une méthode `@Test`) :** Par défaut, la transaction est **automatiquement annulée (rollback)** à la fin du test. `@Commit` ou `@Rollback(false)` forcent la validation (commit) des données en base. [1]

#### **d. ⚠️ Dépendances Circulaires**
*   **Problème :** Bean A a besoin de Bean B, et Bean B a besoin de Bean A. Spring ne peut pas les construire.
*   **Solution :** `@Lazy` retarde l'injection. Au lieu d'injecter le vrai bean, Spring injecte un proxy. Le vrai bean sera créé uniquement au premier appel.
*   **⚠️ Piège (Question 3) :** Utiliser `@Lazy` résout le problème à l'injection. Mais si vous appelez une méthode de cette dépendance "lazy" dans une méthode `@PostConstruct`, vous forcez sa création immédiate, ce qui recrée la dépendance circulaire et lève une `BeanCurrentlyInCreationException`. [1]

#### **e. ⚠️ Programmation Orientée Aspect (AOP)**
*   **Concepts Clés :** Gérer des problématiques transverses (logs, sécurité, métriques) sans polluer le code métier.
*   **Annotations Essentielles :**
    *   `@Aspect` : Déclare la classe comme un aspect.
    *   `@Around` : "Conseil" le plus puissant. S'exécute "autour" de la méthode ciblée. Prend un `ProceedingJoinPoint` en argument, et `joinPoint.proceed()` exécute la méthode cible. Idéal pour mesurer un temps d'exécution. [1]
    *   `@Before` / `@After` : S'exécutent avant / après.

#### **f. Spring Data JPA**
*   `@Query` : Pour écrire une requête JPQL ou SQL native directement dans un repository.
*   `JpaSpecificationExecutor` : Interface à ajouter à votre repository pour permettre des requêtes dynamiques basées sur des `Specification`. Fournit la méthode `findAll(Specification<T> spec, Pageable pageable)`.
*   `@EntityGraph` : Pour résoudre les problèmes de N+1 requêtes en forçant le chargement `EAGER` de relations `LAZY` pour une requête spécifique, optimisant les accès BDD.

---
### **2. Langage Java**

#### **a. ⚠️ POO : Polymorphisme & Héritage**
*   **Règle Clé (Question 14) :** Le type de la **référence** détermine quelles méthodes peuvent être appelées, pas le type de l'objet. Si `Parent p = new Child();`, vous ne pouvez appeler sur `p` que les méthodes définies dans la classe `Parent`. Pour appeler une méthode de `Child` non présente dans `Parent`, il faut caster : `((Child) p).methodeDeChild();`.
*   **Héritage :** `class C extends A implements X, Y` (une classe parente, plusieurs interfaces).
*   **Constructeur et `super()` (Question 13) :** L'appel au constructeur de la classe parente (`super()`) doit **obligatoirement** être la **première instruction** dans le constructeur de la classe fille.

#### **b. ⚠️ Java Streams & Lambda**
Un point clé à renforcer.
*   **Opérations Intermédiaires (chaînables) :** `filter`, `map`, `sorted`, `distinct`, `skip`, `limit`.
*   **Opérations Terminales (déclenchent le traitement) :** `forEach`, `collect`, `sum`, `anyMatch`, `noneMatch`.
*   **Logique de la Question 19 :**
    1.  `Arrays.asList(1, 2, 2)`
    2.  `.map(i -> i * 2)` -> `[2, 4, 4]`
    3.  `.distinct()` -> `[2, 4]`
    4.  `.sum()` -> `6`
    5.  `function.andThen(function2)` -> `6 * 10 = 60`
    6.  `andThen(function3)` -> `60 * 100 = 6000`

#### **c. Collections**
*   **`ArrayList` vs `Vector` (Question 17) :** `Vector` est synchronisé (thread-safe) mais plus lent. `ArrayList` n'est pas synchronisé. On préfère `ArrayList` et on gère la synchronisation manuellement si nécessaire.
*   **`HashMap` (Question 21) :** Pour vider complètement une map, on utilise la méthode `clear()`.

#### **d. Gestion des Exceptions**
*   **Checked vs Unchecked :**
    *   **Checked** (`Exception` sauf `RuntimeException`) : Doivent être déclarées (`throws`) ou rattrapées (`try-catch`). Ex: `IOException`, `FileNotFoundException`.
    *   **Unchecked** (`RuntimeException`) : Erreurs de programmation, gestion non obligatoire. Ex: `NullPointerException`, `NumberFormatException`.
*   **Bloc `finally` (Question 25) :** Ce bloc est **toujours exécuté**, qu'une exception soit levée ou non. C'est pourquoi le programme affiche "An exception occurred." puis "Ending the program.".

#### **e. Modificateurs d'Accès**
*   `public` : Accessible de partout.
*   `protected` : Accessible dans le même package et par les sous-classes.
*   `default` (aucun mot-clé) : Accessible uniquement depuis le même package.
*   `private` : Accessible uniquement dans la même classe.
*   **Règle Clé (Question 26) :** Pour qu'une classe du package `A` soit visible depuis le package `B`, elle doit être déclarée `public`.

---

J'espère que cette fiche structurée vous sera utile pour vos révisions.