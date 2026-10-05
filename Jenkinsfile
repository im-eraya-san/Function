@Library('lambda') _

def imageName = "serverless/lambda:${env.BUILD_ID}"

pipeline{
    agent any
    
    stages{
        stage("Login to aws"){
            steps{
                script{
                    uatSecret.awsLogin()
                }
            }
        }
        stage("Creating build"){
            steps {
                script{
		    sh "echo '$imageName'"
                    uatSecret.makeBuild(imageName)
                }
            }
        }
        
    }
}
