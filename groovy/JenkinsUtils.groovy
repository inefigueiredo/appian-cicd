#!/usr/bin/env groovy

void inspectPackage(environmentName, packageName, username, password, environmentUrl) {

	echo "Inspecting package " + packageName + " in " + environmentName + " environment..."
	
	sh "<path to deploy-application.sh> -application_path <path to package>" + packageName + " -inspect_only -username " + username + " -password " + password + " -url " + environmentUrl
	
	echo packageName + " inspected."
}

void inspectMultiplePackage(firstPackage, lastPackage, environmentName, username, password, environmentUrl) {

	String[] first = firstPackage.split("\\.")
	String[] last = lastPackage.split("\\.")
	
	for (i = first[0]; i <= last[0]; i++) {
	
		inspectPackage(environmentName, i + ".zip", username, password, environmentUrl)
	}
}

void importPackage(environmentName, packageName, username, password, environmentUrl) {

	echo "Deploying package " + packageName + " to " + environmentName + " environment..."
	
	sh "<path to deploy-application.sh> -application_path <path to package>" + packageName + " -username " + username + " -password " + password + " -url " + environmentUrl
	
	echo packageName + " deployed to: " + environmentName
}

void importMultiplePackage(firstPackage, lastPackage, environmentName, username, password, environmentUrl) {

	String[] first = firstPackage.split("\\.")
	String[] last = lastPackage.split("\\.")
	
	for (i = first[0]; i <= last[0]; i++) {
	
		importPackage(environmentName, i + ".zip", username, password, environmentUrl)
	}
}						

void buildJob(jobName) {

	echo "Starting job: " + jobName

	build job: jobName, propagate: true, wait: true	
	
	def job = jenkins.model.Jenkins.instance.getItemByFullName(jobName)
	def jobResult = job.getLastBuild().getResult().toString()
	env.jobResult = jobResult
	
	echo jobName + " ended. Job status: ${jobResult}"
	
	return "${jobResult}"
	
}

return this
