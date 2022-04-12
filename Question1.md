### Quelles contraintes buisness/techniques le composant de stockage de données de l'architecture doit-il remplir pour satisfaire les conditions décrites par le client dans le paragraphe «Statistics» ?

On doit créer une base de données distribuée qui peut contenir une charge conséquente de données (200Gb/jour), ce qui requiert d'être tolérante à la partition. De plus elle doit être consistante pour pouvoir faire des statistiques plus tard. On choisira pour cela un **Data Lake CP** qui stocke toutes les données telles quelles.
