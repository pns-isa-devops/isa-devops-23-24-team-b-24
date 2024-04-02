# DevOps Team B Deliverable

## Workflow

We have set up a workflow that allows us to:

- Always have a deliverable and functional master branch
- Work on feature branches that allow for quick integration of changes
- Have a Staging branch to test changes before deploying them to the master branch

To do this, we use a Jenkins multi-branching pipeline that allows us to:
- Run unit and integration tests on feature branches on each push to ensure that changes do not break the code
- Once the tests have passed, if we want to integrate the changes into the Staging branch, the same pipeline will run a full build of our project and the E2E tests to ensure that the changes do not break macro-level functionalities
- If the E2E tests pass, we generate artifacts that we store in JFrog Artifactory
- Finally, when it's time to move the changes from Staging to master, we upload image artifacts to Dockerhub

## Justification

- We initially started with a Trunk Based Development branching strategy, but we decided to abandon it because it did not allow us to keep the master branch always functional and to test the changes before putting them into production. So we opted to work with an intermediate Staging branch that allows us to ensure that the changes do not break the application before putting them into production.

- Our project is quite large, indeed the full builds can take several minutes (4-6 minutes), so we cannot run them on every commit to our project, as this would slow down the integration of our changes. That's why, thanks to the Multi-Branching pipeline, we have decided to only run the unit and integration tests on the feature branches, and not run the full build of our project
.
## Tools implemented

To carry out this DevOps part, we have set up the following tools for our project:

- **Jenkins**
- **JFrog Artifactory**

All these tools are deployed through docker-compose on our virtual machine **vmpx02.polytech.unice.fr** and stored in a directory in the /opt/project directory.

### Jenkins

Jenkins is accessible via the following URL: http://vmpx02.polytech.unice.fr:8000/. To connect, you need to use the following credentials:

- **Login**: jenkins_teamb
- **Password**: C*Wb2)4_c$Xzr^"

We have configured a Jenkins user with sudo rights on our virtual machine so that it can play the role of a Jenkins agent.

To connect to it, you need to run the following SSH command:

```bash
ssh jenkins@vmpx02.polytech.unice.fr
Password: 123456
```

### JFrog Artifactory

JFrog Artifactory is accessible via the following URL: http://vmpx02.polytech.unice.fr:8001/. To connect, you need to use the following credentials:

- **Login**: admin
- **Password**: Teambjfrog1*

On JFrog Artifactory, we have set up a repository to store the artifacts of our project, it's called **TeamB**
JFrog uses ports 8001 for the web interface and 8081 for communication and to push artifacts.
To add artifacts, we don't use the Maven plugin but the JfrogCLI. For this, we have installed the JfrogCLI on our virtual machine with the following command:

```bash
sudo apt-get install jfrog-cli-v2-jf
```

And we have configured the configuration file so that it can connect to our JFrog Artifactory and we can add the artifacts to the TeamB repository.