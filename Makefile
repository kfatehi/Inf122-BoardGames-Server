mainClassName="team5.Main"

deps:
	mvn dependency:copy-dependencies -DoutputDirectory=lib

run:
	mvn package
	mvn exec:java -Dexec.mainClass="$(mainClassName)"
