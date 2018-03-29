#!/bin/bash

source "$(dirname ${BASH_SOURCE})/variables.sh";

${java} -jar ${jar_path} 1>${output_file} 2>&1 &
#${java} -jar ${jar_path};
command_result=${?};
pid=${!};
if [ ${command_result} -eq 0 ];
then
	echo ${pid} > ${pid_file};
fi;
