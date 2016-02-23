# CPS
## Cours 2
### Langage de spécification
**Objectif:** Décrire les fonctionnalités d'un logiciel d'un point de vue métier.

Dans CPS: un langage "semi-formel" basé sur les types algébriques (~ méthode Event-B)

##### Avantages

	* Précis
	* Adapté à la méthodologie

##### Inconvénients

	* Expressivité (premier ordre)

##### Fondements 
	
	* Logique typée du premier ordre
	* Ensembles (typés)
	* Fonctions, relations...
	
### Format des spécifications:

* **Service:** <Nom service>
* **Observators:** fonctions d'observation de l'état
	* Signatures (types)
	* Préconditions (contraintes sur le domaine)
* **Constructors:** fonctions de construction de l'état initial
* **Operators:** fonctions de transition
* **Observations:**
	* Invariants: propriétés valides dans **tous** les états.
	* Invariants de minimisation: pour éviter les redondances.
	* Post-conditions: propriétés des constructeurs/opérateurs ~ sémantique du service.
	
#### Exemple: les cuves

* **Service:** Cuve
* **Observators:** fonctions d'observation de l'état
	* Signatures (types)
	* Préconditions (contraintes sur le domaine)
* **Constructors:** fonctions de construction de l'état initial
* **Operators:** fonctions de transition
* **Observations:**
	* Invariants: propriétés valides dans **tous** les états.
	* Invariants de minimisation: pour éviter les redondances.
	* Post-conditions: propriétés des constructeurs/opérateurs ~ sémantique du service.
