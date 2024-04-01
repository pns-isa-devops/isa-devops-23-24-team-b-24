pipeline {
    agent { label 'agent1' }
    tools { maven 'Maven' }

    environment {
        AF_ACCESS_TOKEN = 'eyJ2ZXIiOiIyIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYiLCJraWQiOiJ1UHNyUmtpVVk1U2dPSnh2OEs2Mk1HNDhsNy1vVzhYRi1nWlViVUt0TzlNIn0.eyJleHQiOiJ7XCJyZXZvY2FibGVcIjpcInRydWVcIn0iLCJzdWIiOiJqZmFjQDAxaHNnNzNobmZuam5qMDI0c3Fxd2Mxa3Y5XC91c2Vyc1wvYWRtaW4iLCJzY3AiOiJhcHBsaWVkLXBlcm1pc3Npb25zXC9hZG1pbiIsImF1ZCI6IipAKiIsImlzcyI6ImpmZmVAMDAwIiwiaWF0IjoxNzExNzEyMDI2LCJqdGkiOiI2Nzg1YjliNC1kMjI5LTRlYTMtYWYzYy0zNjM0MmVmOTBmZDkifQ.u3ZyTnN19g4ohEGNVT4OAYi7XHcRq_Qze-DXzmhi2nFofZzS8vWRr4Z0S5YDJy42e025YLXndEEP1ahG2odSmNryeDdCvOf3-Esi6vm7gDp3xQuatH8xSZMefY8hzXPNMwRtgpicvEgzFXCIQEoea-xzmJJl50aRm7oWHAlwPIvYnj6wjG1RJmg5vAPmy5TGX-wnqpL-NP-_vxbIv8bNsEWM3GVtMga4Y5i5hiQXRC9LwFNVJaqTg9JUGdyJMLsULznA4wzFwZwL5-bXDHM6pbxUX9qIYiiOHtA_n-7V2yvpZj2ZZtvWRpBQao_CS3FvtJuWWd4F5MU_Y_h4h7tkpg'
    }
    
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

                // Push artifacts to Artifactory
                sh 'jf rt upload \
                        --url=http://localhost:8081/artifactory \
                        --access-token=$AF_ACCESS_TOKEN \
                        --archive=zip \
                    "artifacts-temp/*" \
                    TeamB/w4e.zip'
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