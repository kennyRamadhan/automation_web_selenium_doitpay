pipeline {
    agent any

    tools {
        jdk 'jdk-22'
        maven 'maven-latest'
    }

    environment {
        PATH = "/opt/homebrew/bin:${PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Checking out repository..."
                git branch: 'main', url: 'https://github.com/your-username/your-selenium-project.git'
            }
        }

        stage('Build & Run Tests') {
            steps {
                echo " Running Selenium TestNG tests..."
                sh 'mvn clean test'
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo " Generating Allure Report..."
                sh 'allure generate target/allure-results --clean -o target/allure-report'
            }
        }

        stage('Publish Allure Report') {
            steps {
                echo "Publishing Allure Report..."
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            echo "🧹 Cleaning workspace..."
            cleanWs()
        }
        success {
            echo " Build succeeded! Allure report generated successfully."
        }
        failure {
            echo " Build failed! Check console logs and allure-results for details."
        }
    }
}
