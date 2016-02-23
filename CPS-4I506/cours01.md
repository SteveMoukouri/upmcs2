# CPS

## Cours 1

### Plan

1. Rappels (design patterns etc)
2. Spécification (métier)
   * Langage de spécification: types de données algébriques (ADT), langage semi-formel (ensembles, relations, logique)
3. Conception par contrat
4. Contrat et héritage
5. Tests basés sur les modèles (MBT)
   * Préparer les tests à partir de la spécification pour des critères de couverture
6. Logique de Hoare 1
7. Logique de Hoare 2
8. Modélisation de la concurrence 1
9. Modélisation de la concurrence 1
10. Ouverture

### Notion de composants logiciels

C. Szyperski

```
Une unité de composition avec des interfaces spécifiées contractuellement [..]
Un composant doit pouvoir être déployé.
```

B. Meyer

```
Un composant logiciel [..] (unité de modularité) qui satisfait:
	1. [..] utilisé par des clients
	2. [..] possède un mode d'emploi suffisant
	3. [..] le client n'est pas unique
```

### Design pattern require/provide

#### Motivation

Un composant explicite ses dépendances:
1. Ce dont il a besoin pour fonctionner
2. Ce qu'il fournit à ses clients

cf. Feuille

#### Exemple de composition

```java
MyClient c = new MyClient();       // Configuration
MyProvider p = new MyProvider();   // Configuration

c.bindMyService(p);                // Composition
c.work();                          // Utilisation
```

#### Diagramme de composant

cf. Feuille

#### Diagramme de composition

cf. Feuille
