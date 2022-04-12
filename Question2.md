### A quelles contraintes business l'architecture devrait-elle se soumettre pour satisfaire les conditions décrites dans le paragraphe "Alerte" ?
### Quel composant choisir ?

Le paragraphe "Alerte" met l'accent sur la rapidité à laquelle les données doivent être fournies, pour résoudre les problèmes des citoyens rapidement.
Dès qu'un citoyen reçoit un mauvais "Paciscore", une alerte doit être déclenchée au plus vite du coté des Garde-Paix pour lui venir en aide avant qu'il ne perturbe les autres citoyens.
C'est pourquoi la base de données doit impérativement avoir le composant "Available" afin de pouvoir obtenir les données à n'importe quel moment et en vitesse.

Pour le composant à choisir, le mieux est certainement d'utiliser une stream afin de pouvoir transmettre rapidement des données.
