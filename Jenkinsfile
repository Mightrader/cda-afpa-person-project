pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build API') {
            steps {
                dir('person-api') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build WEB') {
            steps {
                dir('person-web') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker build -t person-api:1.0 ./person-api'
                sh 'docker build -t person-web:1.0 -f person-web/Dockerfile .'
            }
        }
    }
}
