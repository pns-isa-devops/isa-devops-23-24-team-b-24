pipeline {
    agent {label "agent1"}

    stages {
        stage('List Files') {
            steps {
                script {
                    def output = sh(returnStdout: true, script: 'ls').trim()
                    echo "Files in the current directory:"
                    echo output
                }
            }
        }
    }
}