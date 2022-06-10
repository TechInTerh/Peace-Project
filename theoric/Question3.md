### Quelles sont les erreurs de Land pouvant expliquer leur échec ?

On peut expliquer l'échec de Land par le fait que les Garde-paix
voulaient stocker tous les rapports de chaque Watcher
sans vraiment savoir ce qu'ils allaient en faire.

Le probléme avec cela est que la somme des rapports de tous les Watchers représentent 200Gb
de données par jour, ce qui est colossal. Qui plus est, seulement 1% des rapports
sauvegardés contiennent des alertes. Or pour faire faire leurs statistiques, il serait peut-être
intéressant de ne garder que ces derniers pour comprendre les causes des alertes.

### Solution

Afin de dimininuer drastiquement la quantité de données à sauvegarder, on pourrait:
- Sauvegarder uniquement les rapports contenant des erreurs
- Demander aux Watcher de déterminer les informations utiles et garder uniquement ces dernières.
- Embaucher des data-engineer plutôt que des data-scientists pour gérer l'architecture et les données.
