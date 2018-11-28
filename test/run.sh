#!/usr/bin/env bash

# This script
# - compiles the source
# - runs the code against every test suite file
# - outputs the result to the corresponding solution file
# - gets a diff between the output and the orginal solution
# - measures the number of lines changed
# - optionally restores the solution files
# Usage
# ./test/run.sh           # restore solution files
# ./test/run.sh argument  # leave output in place to compare individual diffs

set -e

# Set dirs.
TEST_DIR=$( cd $( dirname ${BASH_SOURCE[0]} ) >/dev/null && pwd )
ROOT_DIR=$( cd $TEST_DIR/..; pwd )
SRC_DIR=$ROOT_DIR/src
SUITE_DIR=$TEST_DIR/Project_Testing_export
SOLUTIONS_DIR=$SUITE_DIR/Solutions
cd $SRC_DIR

# Compile.
javac *.java

# Run the test suite.
for filepath in $SUITE_DIR/*.txt; do
  filename=$(basename $filepath)
  trimmed=$(echo ${filename} | cut -d"_" -f1)
  new=$trimmed\_solution.txt
  newpath=$SOLUTIONS_DIR/$new
  java Main $filepath > $newpath
  printf "."
done
echo ""

# Clean up.
rm $SOLUTIONS_DIR/test_solution.txt
rm $SRC_DIR/*.class

# Print the number of lines changed.
git diff $SOLUTIONS_DIR | grep '+' | grep -v ',' | grep -v 'txt' | wc -l

# Remove change unless an argument was provided.
if [ $# -eq 0 ]; then
  git checkout -- $SOLUTIONS_DIR/*
fi
