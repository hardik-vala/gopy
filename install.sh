#!/bin/bash

#######################
# Installation script #
#######################


SABLECC_NAME=sablecc-3.7

CLI_FNAME=commons-cli-1.3.1.jar
JUNIT_FNAME=junit-4.12.jar
HAMCREST_FNAME=hamcrest-core-1.3.jar
ASSERTJ_FNAME=assertj-core-3.3.0.jar

# Colour control commands.
RED=`tput setaf 1`
GREEN=`tput setaf 2`
YELLOW=`tput setaf 3`
RESET=`tput sgr 0`

# Flag for checking if at least one item needs installing.
installed_nothing=true

# Make sure the script is run from the project source directory.
if [[ `basename "$PWD"` != "src" ]]
then
	echo -e "${RED}ERROR: Run me from comp520-2016-05/src${RESET}"
fi

# Install SableCC if not already done so.
if [ ! -d $SABLECC_NAME ]
then
	echo -e "${YELLOW}Installing SableCC...${RESET}\n\n"

	curl -L https://sourceforge.net/projects/sablecc/files/SableCC/3.7/sablecc-3.7.zip/download?use_mirror=iweb > $SABLECC_NAME.zip
	unzip $SABLECC_NAME.zip
	rm -f $SABLECC_NAME.zip

	installed_nothing=false
fi

# Install Apache Commons CLI if not already done so.
if [ ! -d $CLI_FNAME ]
then
	echo -e "${YELLOW}Installing Apache Commons CLI...${RESET}\n\n"

	curl -L http://mirror.csclub.uwaterloo.ca/apache//commons/cli/binaries/commons-cli-1.3.1-bin.zip > commons-cli-1.3.1-bin.zip
	unzip commons-cli-1.3.1-bin.zip
	mv commons-cli-1.3.1/commons-cli-1.3.1.jar lib/$CLI_FNAME

	rm -rf commons-cli-1.3.1 commons-cli-1.3.1-bin.zip

	installed_nothing=false
fi

# Install JUnit if not already done so.
if [ ! -f "lib/$JUNIT_FNAME" ]
then
	echo -e "${YELLOW}Installing JUnit...${RESET}\n\n"

	curl -L http://search.maven.org/remotecontent\?filepath\=junit/junit/4.12/junit-4.12.jar > lib/$JUNIT_FNAME

	installed_nothing=false
fi

# Install Hamcrest Core if not already done so.
if [ ! -f "lib/$HAMCREST_FNAME" ]
then
	echo -e "${YELLOW}Installing Hamcrest Core...${RESET}\n\n"

	curl -L http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar > lib/$HAMCREST_FNAME

	installed_nothing=false
fi

# Install AssertJ if not already done so.
if [ ! -f "lib/$ASSERTJ_FNAME" ]
then
	echo -e "${YELLOW}Installing AssertJ...${RESET}\n\n"

	curl -L http://search.maven.org/remotecontent?filepath=org/assertj/assertj-core/3.3.0/assertj-core-3.3.0.jar > lib/$ASSERTJ_FNAME

	installed_nothing=false
fi

if $installed_nothing
then
	echo -e "${YELLOW}(Nothing to install, you're good)${RESET}"
else
	echo -e "${GREEN}Installation successful!${RESET}"
fi
