#!/bin/bash

docker build -t jonnyman9/selenium-hub -f ./Dockerfile.selenium-hub .
docker build -t jonnyman9/selenium-node-chrome -f ./Dockerfile.selenium-node-chrome .
