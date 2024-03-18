pipeline {
    agent any

    stages {
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Verify') {
            steps {
                sh 'mvn verify'
            }
        }
    }
}
