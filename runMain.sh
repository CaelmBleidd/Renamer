#!/usr/bin/env bash
mkdir -p out
find . -name "*.java" > .sources.txt
javac -d out -cp .:lib/* @.sources.txt
rm .sources.txt
java -cp out:lib/* Main $1
