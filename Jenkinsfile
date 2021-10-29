pipeline {

  agent any
  
  environment {
        appianCredentials = credentials('<Credentials ID where you stored Appian Credentials in Jenkins>')
    }
  
  stages {
		
		//This command is needed to give Jenkins permission to access the workspace when working with a Linux OS
		stage('Allow workspace access') {	
			steps{			

				sh "chmod +x -R ${env.WORKSPACE}"	
			}      
		}
		
		stage('Inspect Package') {	
			steps{							
				
				script {
				
					def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
				  
				    if ("${packagesToDeploy}" == "Multiple Packages") {
						
						jenkinsUtils.inspectMultiplePackage("${firstPackage}", "${lastPackage}", "Test", "$appianCredentials_USR", "$appianCredentials_PSW", "<Appian environmentURL>")
					
					} else {
					
						jenkinsUtils.inspectPackage("Test", "${firstPackage}", "$appianCredentials_USR", "$appianCredentials_PSW", "<Appian environmentURL>")
					}		
				}      
			}
		}
  
		stage('Deploy Package') {	
			steps{							
				
				script {
				
					def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
				  
				    if ("${packagesToDeploy}" == "Multiple Packages") {
						
						jenkinsUtils.importMultiplePackage("${firstPackage}", "${lastPackage}", "<Environment Name>", "$appianCredentials_USR", "$appianCredentials_PSW", "h<Appian environmentURL>")
					
					} else {
					
						jenkinsUtils.importPackage("<Environment Name>", "${firstPackage}", "$appianCredentials_USR", "$appianCredentials_PSW", "<Appian environmentURL>")
					}		
				}      
			}
		}
		
		stage('Run Unit Tests') {	
			steps{		
				script {
				  def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
				  
				  def unitResult = jenkinsUtils.buildJob('<Name of the Job to Run>')
				  env.unitResult = unitResult
				}		
			}      
		}
		
		stage('Run FitNesse Tests') {	
			steps{		
				script {
				  def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
				  
				  def fitnesseTest = jenkinsUtils.buildJob('<Name of the Job to Run>')
				  env.fitnesseTest = fitnesseTest
				}		
			}      
		}
		
		stage('Deploy to last environment') {	
			when {
                // Only deploy to next environment if both tests build successfully
                expression { "${unitResult}" == 'SUCCESS' && "${fitnesseTest}" == 'SUCCESS' }
            }
            steps {
                script {
				
					def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
				  
				    if ("${packagesToDeploy}" == "Multiple Packages") {
						
						jenkinsUtils.importMultiplePackage("${firstPackage}", "${lastPackage}", "<Environment Name>", "$appianCredentials_USR", "$appianCredentials_PSW", "<Appian environmentURL>")
					
					} else {
					
						jenkinsUtils.importPackage("<Environment Name>", "${firstPackage}", "$appianCredentials_USR", "$appianCredentials_PSW", "<Appian environmentURL>")
					}

				}				
            }     
		}
	}
}