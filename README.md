F1TelemetryOnRpi
================

Ce projet permet le contrôle d'un écran **LCD** (équipé d'un contrôleur hd44780) ainsi que de plusieurs séries de **LED** en fonction des données renvoyées pas un des jeux de la série **F1 20XX** développée par Codemasters.

Afin de pouvoir compiler ce projet, vous aurez besoin de la librairie **Pi4J** (elle permet de faire le lien entre l'application Java et les librairies natives permenttant le contrôle des ports GPIO). Elle est disponible à cette adresse : [http://pi4j.com/](http://pi4j.com/).

Les fichiers .jar de la librairie Pi4J devront être placés dans un dossier **lib** à la racine de votre projet.

Afin d'obtenir un fichier .jar à exécuter sur votre Raspberry Pi, il vous suffit de compiler le projet avec ant. Le .jar sera alors disponible au sein du dossier dist.

Vous pouvez retrouver le tutoriel complet sur notre site à l'adresse suivante : [http://code4pi.fr/2013/11/systeme-de-led-et-ecran-lcd-relie-au-jeu-f1-2013/](http://code4pi.fr/2013/11/systeme-de-led-et-ecran-lcd-relie-au-jeu-f1-2013/)

Fichier de configuration
------------------------

Vous la liste des différents paramètres disponible dans le fichier de configuration :

* led\_on : Active ou désactive le système de LED (false par défaut, mettez a true si vous souhaitez l'activer)
* lcd\_on  : Active ou désactive l'affichage sur le LCD (false par défaut, mettez a true si vous souhaitez l'activer)
* led\_first\_leds : le port GPIO pour la première série de LED (7 par défaut)
* led\_second\_leds : le port GPIO pour la seconde série de LED (9 par défaut)
* led\_third\_leds  : le port GPIO pour la troisième série de LED (8 par défaut)
* lcd\_rs : le port GPIO relié au port RS de l'écran (11 par défaut)
* lcd\_scrobe : le port GPIO relié au port scrobe de l'écran (10 par défaut)
* lcd\_bit\_1 : le port GPIO relié au port bit 1 dé l'écran (6 par défaut)
* lcd\_bit\_2 : le port GPIO relié au port bit 2 dé l'écran (5 par défaut)
* lcd\_bit\_3 : le port GPIO relié au port bit 3 de l'écran (4 par défaut)
* lcd\_bit\_4 : le port GPIO relié au port bit 4 de l'écran (1 par défaut)
* rpm\_for\_pin\_1 : le régime minimale a partir duquel la première série de LED s'allumera (1785 par défaut)
* rpm\_for\_pin\_2 : le régime minimale a partir duquel la seconde série de LED s'allumera (1825 par défaut)
* rpm\_for\_pin\_3 : le régime minimale a partir duquel la troisième série de LED s'allumera (1865 par défaut)
* rpm\_for\_blinking : le régime minimale a partir duquel les LED se mettront a clignoter (1880 par défaut)
* lcd\_refresh\_time : temps de rafraîchissement des informations de l'écran LCD en milliseconde (50 par défaut)

Concernant les valeurs de régime moteur, il faut les multiplier par 10 afin d'obtenir la vrai valeur. Néanmoins j'ai décider de conserver la même unité que celle renvoyé par le jeux.

**Attention** : La nomenclature des ports GPIO n'est pas la même que celle qu'on utilise habituellement lorsque l'on développe en python. Il faut utilisé la numérotation de WiringPi.