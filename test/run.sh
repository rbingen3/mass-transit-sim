#!/usr/bin/env bash

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

# rm Project_Testing_export/Solutions/test_solution.txt
# 
# git diff > diff.txt
# diffsize=$(grep -Rl "+" . | wc -l)
# echo "diffsize: $diffsize"
# rm diff.txt
# 
# rm Project_Testing_export/Solutions/test_solution.txt
# 
# git diff Project_Testing_export/Solutions/ | grep '+' | grep -v ',' | grep -v 'txt' | wc -l
# 
# if [ $# -eq 0 ]; then
#   /bin/bash reset.sh
# fi
