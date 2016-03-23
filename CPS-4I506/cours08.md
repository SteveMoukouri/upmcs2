# Cours 8 - Modélisation de la concurrence

## Concurrence:

* Concepts anciens $\rightarrow$ connu, bien étudié
* Besoins récents - multi-coeur, GPU..
* Difficile :o
	
## Problèmes:

En _séquentiel_:

* On peut maintenir un lien entre modélisation et implémentation (contrats, MBT, etc)
* On peut maintenir un lien __formel__ - logique de Hoare, méthodes formelles (B, Z, cf. SVP en M2)

En _concurrence_:

* Difficile (voire impossible) de maintenir un lien ($\Rightarrow$ domaine de recherche)
* Comment tester? (interférences entre le test et l'implémentation
* Comment débugger?

## Comment modéliser?

__Formalismes:__

* Réseaux de Pétri (~ automates concurrents)
* State Charts (UML), tiré des Message Sequencing Charts
* Automates temporisés (UPPAAL)
* Algèbres/Calculs de processus - langages de modélisation
	* CCS, CSP, Pi-calcul (théorique)
	* LOTOS, µ-crl2, PROMELA (pratique)

## Outils d'analyse

__Model-checking:__

* Analyse l'espace d'état (~ test exhaustif)
* Vérification de propriétés
	* sur les états
	* sur les suites de transitions (chemins)

__Vérification au runtime:__ (~ contrats)

__Preuve de programme:__

Dans ce cours: 

```
	            propriétés LTL                                      OK propriétés vérifiées
                    |                                                          |
Modèle PROMELA -> SPIN -> analyseur par model checking (code C) -> gcc -> Model checker
	                |                                                          |
					|-> 
```
