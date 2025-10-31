pipeline {
    agent any

    tools {
        jdk 'jdk22'                // Sesuai label di Jenkins Global Tools
        maven 'maven3'             // Sesuai label di Jenkins Global Tools
    }

    parameters {
        choice(
            name: 'SUITE',
            choices: ['WebSuite.xml', 'APISuite.xml'],
            description: 'Pilih TestNG suite yang akan dijalankan'
        )
    }

    environment {
        ALLURE_HOME = '/opt/homebrew/bin/allure'   // Path ke allure CLI (Homebrew)
        PATH = "/opt/homebrew/bin:${env.PATH}"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo '📦 Cloning repository from GitHub...'
                git branch: 'main', url: 'https://github.com/kennyRamadhan/automation_web_selenium_doitpay.git'
            }
        }

        stage('Build & Run TestNG Suite') {
            steps {
                echo "🧪 Running selected suite: ${params.SUITE}"
                sh "mvn clean test -DsuiteXmlFile=${params.SUITE}"
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo '📊 Generating Allure report...'
                sh 'allure generate allure-results --clean -o allure-report'
            }
        }

        stage('Publish Allure Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'allure-results']]
            }
        }

        stage('Archive Report Artifact') {
            steps {
                echo '🗂️ Archiving Allure HTML report...'
                archiveArtifacts artifacts: 'allure-report/**', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Build and tests completed successfully!'
        }
        failure {
            echo '❌ Build failed! Check logs and allure-results for details.'
        }
    }
}
