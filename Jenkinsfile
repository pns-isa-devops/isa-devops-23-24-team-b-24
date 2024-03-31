pipeline {
    agent { label "agent1" }
    tools { maven 'Maven' }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh './build-all.sh'
            }
        }
        stage('Up') {
            steps {
                sh 'docker compose up -d --scale tcf-cli=0'
            }
        }
        stage('Tests') {
            steps {
                script {
                    def branchName = env.BRANCH_NAME
                    if (branchName.contains('feat')) { // execution of unit/it tests
                        sh 'mvn clean package -f backend/pom.xml'
                        sh 'mvn clean verify -f backend/pom.xml'
                    } else if (branchName.contains('staging')) { // execution of e2e tests
                        sh 'cd cli && docker compose run tcf-cli script demo.txt && exit'
                        sh 'cd cli && docker compose run tcf-cli script demo2.txt && exit'
                    } else if (branchName == 'master') { //execution of all tests
                        sh 'mvn clean package -f backend/pom.xml'
                        sh 'mvn clean verify -f backend/pom.xml'
                        sh 'cd cli && docker compose run tcf-cli script demo.txt && exit'
                        sh 'cd cli && docker compose run tcf-cli script demo2.txt && exit'
                    }
                }
            }
        }
        stage('Cleanup') {
            steps {
                sh 'docker compose down -v'
            }
        }
    }
    post {
        always {
            sh 'docker compose down -v'
        }
    }
}
