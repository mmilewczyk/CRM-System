pipeline {
     agent any

     tools {
         maven "M3"
     }

     stages {
         stage('Build') {
             steps {
                 sh "mvn clean compile"
             }
         }
         stage('Test') {
            when {
                expression {
                    BRANCH_NAME == 'master'
                }
            }
            steps {
                sh "mvn test"
            }
         }
     }
 }