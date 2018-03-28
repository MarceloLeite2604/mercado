#!/bin/bash

source "$(dirname ${BASH_SOURCE})/variables.sh";

pid=$(cat ${pid_file});
if [ -z "${pid}" ];
then
	kill -SIGKILL ${pid};
	rm ${pid_file};
fi;
