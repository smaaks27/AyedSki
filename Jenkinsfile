pipeline {
    agent any


    stages {
        stage('Checkout') {
            steps {
                // Checkout code from the repository
                git branch: 'master', url: 'https://github.com/smaaks27/AyedSki.git'
            }
        }

        stage('Start MySQL') {
            steps {
                script {
                    // Start MySQL Docker container
                    sh 'docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=user -e MYSQL_PASSWORD=password -p 3306:3306 -d mysql:8.0'
                }
            }
        }

        stage('Set Permissions') {
            steps {
                dir('gestion-station-ski') {
                    // Grant execute permission to the mvnw file
                    sh 'chmod +x mvnw'
                }
            }
        }

        stage('Build') {
            steps {
                // Clean and build the project with Maven
                dir('gestion-station-ski') {
                    sh './mvnw clean install'
                }
            }
        }

        stage('Test') {
            steps {
                // Run unit tests
                dir('gestion-station-ski') {
                    sh './mvnw test'
                }
            }
        }

        stage('Package') {
            steps {
                // Package the application (e.g., create a JAR file)
                dir('gestion-station-ski') {
                    sh './mvnw package'
                }
            }
        }

        stage('Deploy') {
            steps {
                // Deploy the application, adjust as per your deploy method (SSH, Docker, Kubernetes, etc.)
                echo 'Deploying application...'
                // Example for copying JAR file to remote server:
                // sh 'scp target/gestion-station-ski.jar user@remote.server:/path/to/deploy'
            }
        }
    }

    post {
        success {
            echo 'Build and Deployment completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
