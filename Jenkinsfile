pipeline {
    agent any

    environment {
        // SonarQube environment variables
        SCANNER_HOME = tool 'sonar' // Name of SonarQube installation
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.50.4:8081"
        NEXUS_REPOSITORY = "AyedSki"
        NEXUS_CREDENTIAL_ID = "nexus-cred"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from the repository
                git branch: 'Ayed', url: 'https://github.com/smaaks27/AyedSki.git'
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

         stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-cred') {
                    sh "$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=AyedSki -Dsonar.projectName='AyedSki' -Dsonar.java.binaries=gestion-station-ski/target/classes"
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

        stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
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
