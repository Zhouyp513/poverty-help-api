#!/usr/bin/env bash

        Project_HOME=$(echo `pwd` | sed 's/\/sbin//')

        echo "$Project_HOME"

        mvn -f $Project_HOME/pom.xml clean assembly:assembly -DskipTests