#!/bin/bash

java=$(which java)
cat=$(which cat)

working_directory=/opt/marcelo/git/mercado/controller/
jar_name=controller-0.0.1-SNAPSHOT-jar-with-dependencies.jar
target_directory=${working_directory}target/
output_directory=${working_directory}output/
logs_directory=${output_directory}logs/
jar_path=${target_directory}${jar_name}
output_file=${logs_directory}output.log
pid_file=${output_directory}pid
