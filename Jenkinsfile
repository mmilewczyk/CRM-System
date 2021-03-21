pipeline {
     agent any
     environment {
             DATABASE_URL= 'jdbc:postgresql://localhost:5432/crm-test',
             DATABASE_USERNAME= 'postgres',
             DATABASE_PASSWORD= 'root'
         }

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
            steps {
                sh "mvn test"
            }
         }
     }
 }