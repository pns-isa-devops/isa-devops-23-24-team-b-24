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

- Our project is quite large, indeed the full builds can take several minutes (4-6 minutes), so we cannot run them on every commit to our project, as this would slow down the integration of our changes. That's why, thanks to the Multi-Branching pipeline, we have decided to only run the unit and integration tests on the feature branches, and not run the full build of our project.

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

## Further Improvements

Our system isn't perfect. Here are some improvements we could make given more time:

### Base pushed Docker images off of Artifactory artifacts

Currently, images we're sending off to Dockerhub are built using the `./build-all.sh` script which is executed in the Staging branch before the execution of the E2E tests. In order to properly assiociate artifact builds with the corresponding Docker, we could pull the built artifacts from Artifactory, build Docker images from them and push them to Dockerhub.

### Proper versioning of pushed Docker images and Artifactory artifacts

Currently, for both pushed Docker images and Artifactory artifacts, we overwrite the 'latest' tag on each release. This isn't how artifacts and images should be versioned. We should use a versioning scheme that allows us to distinguish between different versions of the same artifact or image. We could potentially look into incorporating semantic versioning (Semver) into our build process. This would provide clients of our system with more control over what they're pulling.

### Implement commit ignoring for CI

Certain commits, such as updates to documentation, do not merit a CI build. We could implement a commit ignoring mechanism to prevent these commits from triggering a build. This would probably require a change to the Jenkins pipeline.

### SonarQube integration

In order to better guage the quality of our code and areas of improvement, we could integrate SonarQube into our build process. We would have rolling statistics on code smell, code coverage, and code quality.

## Business Continuity Plan

### Potential Issues

#### VM

- **Issue**: Lack of memory space on the VM
- **Solution**: Delete old Docker images and volumes to free up space
- **Issue**: No access to the VM
- **Solution**: Ask the professor to restart the VM

#### Jenkins

- **Issue**: Pipelines are not running even though Smee is configured and functional
- **Solution**: Reindex the pipeline branches

#### Smee

- **Issue**: Smee is not working
- **Solution**: 
    - Restart the Smee Docker container by running `docker compose up -d` in the `/opt/project/smee` directory
    - If the problem persists, check that the webhook is still configured correctly on GitHub
    - Otherwise, check if the Smee website is working, and if not, recreate a Smee instance

#### JFrog Artifactory

- **Issue**: Artifactory is not starting
- **Solution**: Restart the Artifactory Docker container by running `docker compose up -d` in the `/opt/project/artifactory` directory

### Restart/Recovery Procedure

#### General Setup

If the VM is fresh, you need to:
- Reinstall Docker and Docker-compose following the instructions available at https://docs.docker.com/engine/install/ubuntu/ or run the following commands:

```bash
# Add the GPG key
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
# Add the Docker repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
# Verify the installation
sudo docker run hello-world
```

- Configure user permissions to run Docker commands without being root

```bash
sudo usermod -aG docker $USER
```

- Configure a shared directory for VM users

```bash
sudo mkdir /opt/project
sudo chgrp admin /opt/project
sudo chmod g+w /opt/project
sudo chmod g+s /opt/project
```

- Install Java

```bash
sudo apt update
java -version
sudo apt install default-jre
sudo apt install default-jdk
```

#### Smee

- Install Smee by creating a Smee directory in `/opt/project` and adding a docker-compose file like the one in `doc/smee`, then start it.
```bash
sudo mkdir /opt/project/smee
docker compose up -d
```
- Create a payload URL on Smee and configure it on GitHub so that webhooks are sent to Smee
    - [Smee.io](https://smee.io/) -> Start new Channel -> Copy Webhook Proxy URL
    - github.com/pns-isa-devops/isa-devops-23-24-team-b-24/ -> Settings -> Webhooks -> Add Webhook

#### Jenkins

- Install Jenkins by creating a Jenkins directory in `/opt/project` and adding a docker-compose file like the one in `doc/jenkins`, then start it. The Jenkins interface will be available on port 8000 of the VM.

```bash
sudo mkdir /opt/project/jenkins
docker compose up -d
docker compose logs # to retrieve the password
```

- Create a Jenkins user to be able to run Jenkins pipelines, then generate SSH keys for this user

```bash
sudo adduser jenkins
sudo chown jenkins /opt/project/jenkins
ssh-keygen -t ed25519
ssh-copy-id -i ~/.ssh/key_ssh jenkins@vmpx02.polytech.unice.fr
```

- Once Jenkins is running, you need to configure a Jenkins agent and its credentials to be able to run Jenkins pipelines, following these instructions https://www.jenkins.io/doc/book/using/using-agents/#generating-an-ssh-key-pair, the host of our machine is 172.17.0.1
- Once the agent is configured, you will need to create the Job and configure it for a multibranching pipeline, following these instructions https://www.jenkins.io/doc/book/pipeline/multibranch/, you will also need the **multibranch scan webhooks trigger** plugin so that Jenkins can receive the webhooks from GitHub

#### JFrog Artifactory

- Install JFrog Artifactory by creating an Artifactory directory in `/opt/project` and adding a docker-compose file like the one in `doc/jfrog`, then start it. The JFrog Artifactory interface will be available on port 8001 of the VM.

```bash
sudo mkdir /opt/project/artifactory
docker compose up -d
```

- Configure a **TeamB** repository on JFrog Artifactory to store the artifacts of our project
- Configure an API token on JFrog Artifactory to be able to add artifacts to the TeamB repository and add it
- Install the JFrogCLI tool to be able to add artifacts to JFrog Artifactory

```bash
sudo apt-get install jfrog-cli-v2-jf
```