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
                sh 'chmod -R 777 scheduler'
                sh './build-all.sh'
            }
        }

        stage('Up') {
            steps {
                //sh "ls -ali scheduler"
                sh 'docker compose up -d'
                //sh 'docker compose up --abort-on-container-exit --exit-code-from tcf-cli'
                sh 'mvn clean package -f backend/pom.xml'
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
