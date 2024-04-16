pipeline {
    agent any

    stages {
        stage ('Build Jar') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage ('Build Docker Image') {
            steps {
                sh "docker build -t blitzstriker/selenium-java ."
            }
        }

        stage ('Push Image') {
            steps {
                sh "docker push blitzstriker/selenium-java"
            }
        }
    }
}