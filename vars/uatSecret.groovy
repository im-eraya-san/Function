def infraRegion(){
   
//    This function takes aws region from Jenkins cred store
//    it is for security purpose.
   
    withCredentials([
        string(credentialsId: 'REGION', variable: 'Region')
    ]){ return Region } 
}

def awsLogin(){

//  Take region from infraRegion file
    def REGION = infraRegion()

// Apply aws cred according to region
    withAWS(credentials: 'AWS', region: REGION){
            withCredentials([
                usernamePassword(
                    // Get userName and Password of ECR
                    credentialsId: 'ECR-CRED',
                    usernameVariable: 'uname',
                    passwordVariable: 'passwd'
                )
            ]){
                // Login to ECR
                sh 'aws ecr get-login-password --region "$REGION"| docker login --username "$uname" --password-stdin "$passwd"'
            }
    }
}
