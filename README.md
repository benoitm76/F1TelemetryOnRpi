F1TelemetryOnRpi
================

Ce projet permet le contrôle d'un écran **LCD** (équipé d'un contrôleur hd44780) ainsi que de plusieurs séries de **LED** en fonction des données renvoyées pas un des jeux de la série **F1 20XX** développée par Codemasters.

Afin de pouvoir compiler ce projet, vous aurez besoin de la librairie **Pi4J** (elle permet de faire le lien entre l'application Java et les librairies natives permenttant le contrôle des ports GPIO). Elle est disponible à cette adresse : [http://pi4j.com/](http://pi4j.com/).

Les fichiers .jar de la librairie Pi4J devront être placés dans un dossier **lib** à la racine de votre projet.

Afin d'obtenir un fichier .jar à exécuter sur votre Raspberry Pi, il vous suffit de compiler le projet avec ant.