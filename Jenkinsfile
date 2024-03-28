pipeline {
    agent { label 'agent1' }
    tools { maven 'Maven' }
    
    stages {
        stage('Feature Branch') {
            when { not { anyOf { branch 'master'; branch 'staging' } } }
            steps {
                // Run unit tests
                sh 'mvn clean package -f backend/pom.xml'
                
                // Run integration tests
                sh 'mvn clean verify -f backend/pom.xml'
            }
        }
        
        stage('Staging') {
            when { branch : 'staging' }
            steps {
                // Build all modules
                sh './build-all.sh'
                
                // Run end to end tests
                sh 'docker compose up -d --scale tcf-cli=0'
                sh 'cd cli && docker compose run tcf-cli script demo.txt && exit'
                sh 'cd cli && docker compose run tcf-cli script demo.txt2 && exit'
                sh 'docker compose down -v --remove-orphans'

                // Build artifacts
                sh './artifacts.sh'
            }
        }
        
        stage('Master') {
            when { branch 'master' }
            steps {
                // Pull artifacts of modules from Artifactory
                
                // Deploy to prod with docker compose up
                sh 'docker compose up -d'
                
                // Docker tag and push to Docker Hub
            }
        }


    }

    post {
        always {
            sh 'docker compose down -v --remove-orphans'
        }
    }
}