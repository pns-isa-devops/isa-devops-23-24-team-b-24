# Livrable DevOps Team B

## Workflow

Nous avons mis en place un workflow qui nous permet :

- De toujours avoir la branche master livrable et fonctionnelle
- De travailler sur des branches de features qui permettent de rapidement integrer des changements
- Et d'avoir une branche de Staging qui permet de tester les changements avant de les mettre en production sur la branche master

Pour ce faire nous utilisons une pipeline miltibranching Jenkins qui permet de :

- Lancer des tests unitaires et d'intégration sur les branches de features sur chaque push afin de s'assurer que les changements ne cassent pas le code
- Une fois les tests passés, si l'on souhaite intégrer les changements sur la branche de Staging, la meme pipeline va lancé un build-all de notre projet et les test E2E pour s'assurer que les changements ne cassent pas l'application
- Si les tests E2E passent, nous generons des artefacts que nous stockons sur JFrog Artifactory
- Enfin au moment de mettre les changements de staging sur master, on transforme les artefacts en Docker images et on les push sur DockerHub

## Justification

- Nous etions dans un premier temps partie sur une strategie de branching Trunk Based Development, mais nous avons décidé de l'abandonner, pour cause elle ne permettait pas de garder une branche master toujours fonctionnelle et de tester les changements avant de les mettre en production. Ainsi nous avons opté de travailler avec une branche intermediaire de Staging qui permet de s'assurer que les changements ne cassent pas l'application avant de les mettre en production. 

- Notre projet est assez imposant, en effet les builds complets peuvent prendre plusieurs minutes (4-6 minutes), de ce fait nous ne pouvons pas les lancer a chaque commit sur notre projet, car cela ralentirai l'intégration de nos changement. C'est pour cela que, grace au pipeline MultiBranching, nous avons décidé de ne lancer que les tests unitaires et d'intégrations sur les branches de features, et donc ne lancent pas de construction de notre projet. 

## Outils mis en place

Pour réaliser cette partie Devops nous avons mis en place pour notre projet les outils suivants :

- **Jenkins**  
- **JFrog Artifactory**

Tout ces outils sont déployés a travers des docker-compose sur notre mahcine virtuelle **vmpx02.polytech.unice.fr** et stockées dans dossier au niveau du repertoire /opt/project.

### Jenkins

Le Jenkins est accessible via l'url suivante : http://vmpx02.polytech.unice.fr:8000/. Pour se connecter il faut utiliser les identifiants suivants : 
- **Login** : jenkins_teamb
- **Password** : C*Wb2)4_c$Xzr^"

Nous avons configurer un utilisateur Jenkins avec droit sudo sur notre machine virtuelle pour qu'il puisse jouer le role d'agent Jenkins. 
Pour s'y connecter il faut effectuer la commande ssh suivante : 
```bash
ssh jenkins@vmpx02.polytech.unice.fr
Password : 123456
```

### JFrog Artifactory

Le JFrog Artifactory est accessible via l'url suivante : http://vmpx02.polytech.unice.fr:8001/. Pour se connecter il faut utiliser les identifiants suivants :
- **Login** : admin
- **Password** : Teambjfrog1*

Sur Jfrog Artifactory nous avons mis en place un repository pour stocker les artefacts de notre projet, il s'appelle **TeamB**

Jfrog utilise les ports 8001 pour l'interface web et 8081 pour la communication et pour push les artefacts. 

Pour ajouter des artefacts nous ne passons pas par le plugin maven mais par la JfrogCLI. Pour cela nous avons installer la JfrogCLI sur notre machine virtuelle avec la commande suivante :
```bash
sudo apt-get install jfrog-cli-v2-jf
```
Et nous avons configurer le fichier de configuration pour qu'il puisse se connecter a notre Jfrog Artifactory et que nous puissions ajouter les artefacts sur le repository TeamB.