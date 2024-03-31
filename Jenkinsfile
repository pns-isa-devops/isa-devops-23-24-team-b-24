pipeline {
    agent { label 'agent1' }
    tools { maven 'Maven' }
    
    stages {
        stage('Feature Branch') {
            // For all branches except master and staging (i.e. feature branches)
            when { not { anyOf { branch 'master'; branch 'staging' } } }
            steps {
                // Run unit tests
                sh 'mvn clean package -f backend/pom.xml'
                
                // Run integration tests
                sh 'mvn clean verify -f backend/pom.xml'
            }
        }
        
        stage('Staging') {
            when { branch 'staging' }
            steps {
                // Build all modules
                sh './build-all.sh'
                
                // Run end to end tests
                sh 'docker compose up -d --scale tcf-cli=0'
                sh 'cd cli && docker compose run tcf-cli script populate.txt && exit'
                sh 'cd cli && docker compose run tcf-cli script script.txt && exit'

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
            script {
                if (env.BRANCH_NAME == 'staging') {
                    sh 'docker compose down -v --remove-orphans'
                }
            }
        }
    }
}