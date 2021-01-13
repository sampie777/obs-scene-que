#!/bin/bash

# Script to run on my computer to collect all .jar files from the obs-scene-queue plugin repositories

VERSION="$1"
VERSION="2.7.0"

CURRENT_DIR=$(dirname $0)
echo "Collecting all plugins with tag *-${VERSION}-*dependencies.jar"
ls -1 "${CURRENT_DIR}"/../../osq-plugins/*/target/*-${VERSION}-*dependencies.jar
cp "${CURRENT_DIR}"/../../osq-plugins/*/target/*-${VERSION}-*dependencies.jar .