pipeline {
    agent {label "agent1"}
    tools {
        maven 'Maven'
    }
    stages {

        stage('Checkout') {
            steps{
                checkout scm
            }
        }

        stage('Build') {
            steps {
                //sh 'chmod -R 777 scheduler'
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
                //tests unitaires
                sh 'mvn clean package -f backend/pom.xml'
                //test d'intégration
                sh 'mvn clean verify -f backend/pom.xml'
                //tests e2e
                sh 'cd cli && docker compose run tcf-cli script demo.txt && exit'
                //sh 'docker compose stop tcf-cli'
                sh 'cd cli && docker compose run tcf-cli script demo2.txt && exit'

            }
        }
        stage('Cleanup') {
            steps {
                // Cleanup any resources if needed
                sh 'docker compose down -v'
            }
        }
    }
    post {
        always {
            // Clean up Docker resources in case of pipeline failure
            sh 'docker compose down -v'
        }
    }
}
