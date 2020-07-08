#!/bin/bash

MODULE=stats

JAR_FILE=./${MODULE}/build/libs/${MODULE}-1.0-SNAPSHOT-all.jar
DATASET_DIR=../dataset
EPSILON=$1
GS=$2
TRIALS=100
AFTER=$3
CSVDIR=$4

MAIN_CLASS=presto.MainStats

./gradlew :${MODULE}:shadowJar

APP=barometer
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=bible
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=dpm
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=drumpads
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=equibase
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=localtv
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=loctracker
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=mitula
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=moonphases
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=parking
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=parrot
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/parrot ${DATASET_DIR}/parrot.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=post
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/post ${DATASET_DIR}/post.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=quicknews
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/quicknews ${DATASET_DIR}/quicknews.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=speedlogic
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/speedlogic ${DATASET_DIR}/speedlogic.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"

APP=vidanta
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/vidanta ${DATASET_DIR}/vidanta.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}" "${AFTER}"
