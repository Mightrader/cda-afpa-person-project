pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean install -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker build -t person-api:1.0 ./person-api'
                sh 'docker build -t person-web:1.0 ./person-web'
            }
        }
    }
}
